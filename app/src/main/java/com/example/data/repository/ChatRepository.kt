package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CallSession
import com.example.data.model.ChatUser
import com.example.data.model.DirectMessage
import com.example.data.model.GroupMessage
import com.example.data.util.OperationType
import com.example.data.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flowOf

class ChatRepository(private val db: FirebaseFirestore) {

  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth = Firebase.auth

  fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google to access community communication.")
  }

  // 1. Members Directory
  fun observeUsers(): Flow<List<ChatUser>> {
    if (auth.currentUser == null) {
      return flowOf(emptyList())
    }
    val path = "users"
    return db.collection(path)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(ChatUser::class.java)
      }
      .catch { error ->
        if (error !is CancellationException && error is Exception) {
          handleFirestoreError(error, OperationType.LIST, path)
        }
        emit(emptyList())
      }
  }

  suspend fun saveUserProfile(user: ChatUser) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
    val data = mutableMapOf<String, Any>(
      "userId" to uid,
      "displayName" to user.displayName,
      "email" to user.email,
      "lga" to user.lga,
      "occupation" to user.occupation,
      "bio" to user.bio,
      "status" to user.status,
      "updatedAt" to FieldValue.serverTimestamp()
    )
    if (user.photoUrl != null) {
      data["photoUrl"] = user.photoUrl
    }
    if (user.createdAt == null) {
      data["createdAt"] = FieldValue.serverTimestamp()
    }
    try {
      docRef.set(data, com.google.firebase.firestore.SetOptions.merge()).await()
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  // 2. Community Group Chat
  fun observeGroupMessages(): Flow<List<GroupMessage>> {
    if (auth.currentUser == null) {
      return flowOf(emptyList())
    }
    val path = "group_messages"
    return db.collection(path)
      .orderBy("createdAt", Query.Direction.ASCENDING)
      .limitToLast(150)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(GroupMessage::class.java)
      }
      .catch { error ->
        if (error !is CancellationException && error is Exception) {
          handleFirestoreError(error, OperationType.LIST, path)
        }
        emit(emptyList())
      }
  }

  suspend fun sendGroupMessage(
    text: String,
    type: String = "text",
    mediaData: String? = null
  ) {
    val uid = requireUserId()
    val user = auth.currentUser
    val name = user?.displayName ?: "Akwa Ibom Member"
    val photo = user?.photoUrl?.toString()

    val path = "group_messages"
    val docRef = db.collection(path).document()
    val payload = mutableMapOf<String, Any>(
      "id" to docRef.id,
      "senderId" to uid,
      "senderName" to name,
      "text" to text,
      "type" to type,
      "createdAt" to FieldValue.serverTimestamp()
    )
    if (photo != null) payload["senderPhoto"] = photo
    if (mediaData != null) payload["mediaData"] = mediaData

    try {
      docRef.set(payload).await()
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  // 3. Private 1-on-1 Chat
  fun getConversationId(userId1: String, userId2: String): String {
    return if (userId1 < userId2) "${userId1}_${userId2}" else "${userId2}_${userId1}"
  }

  fun observeDirectMessages(otherUserId: String): Flow<List<DirectMessage>> {
    val myUid = auth.currentUser?.uid ?: return flowOf(emptyList())
    val conversationId = getConversationId(myUid, otherUserId)
    val path = "direct_messages"

    return db.collection(path)
      .whereEqualTo("conversationId", conversationId)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(DirectMessage::class.java)
          .sortedBy { it.createdAt?.seconds ?: 0L }
      }
      .catch { error ->
        if (error !is CancellationException && error is Exception) {
          handleFirestoreError(error, OperationType.LIST, path)
        }
        emit(emptyList())
      }
  }

  suspend fun sendDirectMessage(
    receiverId: String,
    receiverName: String,
    text: String,
    type: String = "text",
    mediaData: String? = null
  ) {
    val myUid = requireUserId()
    val senderName = auth.currentUser?.displayName ?: "Akwa Ibom Member"
    val conversationId = getConversationId(myUid, receiverId)

    val path = "direct_messages"
    val docRef = db.collection(path).document()
    val payload = mutableMapOf<String, Any>(
      "id" to docRef.id,
      "conversationId" to conversationId,
      "senderId" to myUid,
      "senderName" to senderName,
      "receiverId" to receiverId,
      "receiverName" to receiverName,
      "text" to text,
      "type" to type,
      "isRead" to false,
      "createdAt" to FieldValue.serverTimestamp()
    )
    if (mediaData != null) payload["mediaData"] = mediaData

    try {
      docRef.set(payload).await()
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun markMessageRead(messageId: String) {
    val path = "direct_messages/$messageId"
    try {
      db.document(path).update("isRead", true).await()
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
    }
  }

  // 4. Audio & Video Call Signaling
  fun observeIncomingCalls(myUserId: String): Flow<List<CallSession>> {
    if (auth.currentUser == null || myUserId.isBlank()) {
      return flowOf(emptyList())
    }
    val path = "calls"
    return db.collection(path)
      .whereEqualTo("receiverId", myUserId)
      .whereEqualTo("status", "ringing")
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(CallSession::class.java)
      }
      .catch { error ->
        if (error !is CancellationException && error is Exception) {
          handleFirestoreError(error, OperationType.LIST, path)
        }
        emit(emptyList())
      }
  }

  fun observeCallSession(callId: String): Flow<CallSession?> {
    if (auth.currentUser == null || callId.isBlank()) {
      return flowOf(null)
    }
    val path = "calls/$callId"
    return db.document(path)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) snapshot.toObject(CallSession::class.java) else null
      }
      .catch { error ->
        if (error !is CancellationException && error is Exception) {
          handleFirestoreError(error, OperationType.GET, path)
        }
        emit(null)
      }
  }

  suspend fun initiateCall(
    receiverId: String,
    receiverName: String,
    callType: String
  ): String {
    val myUid = requireUserId()
    val myName = auth.currentUser?.displayName ?: "Akwa Ibom Member"

    val path = "calls"
    val docRef = db.collection(path).document()
    val payload = mapOf(
      "id" to docRef.id,
      "callerId" to myUid,
      "callerName" to myName,
      "receiverId" to receiverId,
      "receiverName" to receiverName,
      "callType" to callType,
      "status" to "ringing",
      "createdAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )
    try {
      docRef.set(payload).await()
      return docRef.id
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun updateCallStatus(callId: String, status: String) {
    val path = "calls/$callId"
    try {
      db.document(path).update(
        "status", status,
        "updatedAt", FieldValue.serverTimestamp()
      ).await()
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, path)
    }
  }
}
