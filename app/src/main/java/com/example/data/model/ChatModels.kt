package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class ChatUser(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val email: String = "",
  val photoUrl: String? = null,
  val lga: String = "Uyo",
  val occupation: String = "Akwa Ibom Citizen",
  val bio: String = "Brighter We Shine!",
  val status: String = "online",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class GroupMessage(
  @DocumentId val id: String = "",
  val senderId: String = "",
  val senderName: String = "",
  val senderPhoto: String? = null,
  val text: String = "",
  val type: String = "text", // "text", "image", "voice"
  val mediaData: String? = null, // base64 payload for photo or voice note
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class DirectMessage(
  @DocumentId val id: String = "",
  val conversationId: String = "",
  val senderId: String = "",
  val senderName: String = "",
  val receiverId: String = "",
  val receiverName: String = "",
  val text: String = "",
  val type: String = "text", // "text", "image", "voice"
  val mediaData: String? = null,
  val isRead: Boolean = false,
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CallSession(
  @DocumentId val id: String = "",
  val callerId: String = "",
  val callerName: String = "",
  val receiverId: String = "",
  val receiverName: String = "",
  val callType: String = "audio", // "audio", "video"
  val status: String = "ringing", // "ringing", "accepted", "rejected", "ended"
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)
