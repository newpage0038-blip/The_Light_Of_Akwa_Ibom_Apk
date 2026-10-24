package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallSession
import com.example.data.model.ChatUser
import com.example.data.model.CommunityPoll
import com.example.data.model.DirectMessage
import com.example.data.model.DiscussionComment
import com.example.data.model.DiscussionPost
import com.example.data.model.GroupMessage
import com.example.ui.components.ActiveCallScreen
import com.example.ui.components.IncomingCallBanner
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
  // Existing features preserved
  discussions: List<DiscussionPost>,
  polls: List<CommunityPoll>,
  activePostForComments: DiscussionPost?,
  comments: List<DiscussionComment>,
  onToggleLike: (Long) -> Unit,
  onOpenComments: (DiscussionPost) -> Unit,
  onCloseComments: () -> Unit,
  onAddComment: (Long, String) -> Unit,
  onVotePoll: (Long, Int) -> Unit,
  onCreateDiscussion: (String, String, String) -> Unit,
  // New communication system
  currentUserId: String?,
  currentUserName: String,
  usersList: List<ChatUser>,
  groupMessages: List<GroupMessage>,
  directMessages: List<DirectMessage>,
  incomingCall: CallSession?,
  activeCall: CallSession?,
  activeChatUser: ChatUser?,
  onSelectChatUser: (ChatUser?) -> Unit,
  onSendGroupMessage: (String, String, String?) -> Unit,
  onSendDirectMessage: (String, String, String, String, String?) -> Unit,
  onInitiateCall: (ChatUser, String) -> Unit, // callType: "audio" or "video"
  onAnswerCall: (CallSession) -> Unit,
  onRejectCall: (CallSession) -> Unit,
  onEndCall: (CallSession) -> Unit,
  onOpenSignIn: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Community Hall", "Direct Chats", "Members", "Forums & Polls")

  // Permissions launcher for audio and video calls
  var pendingCallType by remember { mutableStateOf<String?>(null) }
  var pendingCallUser by remember { mutableStateOf<ChatUser?>(null) }

  val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val recordAudioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
    val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false

    val targetUser = pendingCallUser
    val targetType = pendingCallType

    if (targetUser != null && targetType != null) {
      if (targetType == "video" && cameraGranted) {
        onInitiateCall(targetUser, "video")
      } else if (recordAudioGranted || targetType == "audio") {
        onInitiateCall(targetUser, "audio")
      }
    }
    pendingCallUser = null
    pendingCallType = null
  }

  fun requestCall(user: ChatUser, type: String) {
    if (currentUserId == null) {
      onOpenSignIn()
      return
    }
    pendingCallUser = user
    pendingCallType = type
    if (type == "video") {
      permissionLauncher.launch(
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
      )
    } else {
      permissionLauncher.launch(
        arrayOf(Manifest.permission.RECORD_AUDIO)
      )
    }
  }

  Box(modifier = modifier.fillMaxSize().background(Color.White)) {
    // If inside a direct 1-on-1 chat
    if (activeChatUser != null) {
      DirectChatView(
        otherUser = activeChatUser,
        currentUserId = currentUserId ?: "",
        messages = directMessages,
        onBack = { onSelectChatUser(null) },
        onSendMessage = { text, type, media ->
          onSendDirectMessage(activeChatUser.userId, activeChatUser.displayName, text, type, media)
        },
        onAudioCall = { requestCall(activeChatUser, "audio") },
        onVideoCall = { requestCall(activeChatUser, "video") }
      )
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        // Sign In Prompt Banner if unauthenticated
        if (currentUserId == null) {
          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Sign In with Google to Message & Call",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF78350F)
                )
                Text(
                  text = "Chat with Akwa Ibomites across 31 LGAs",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFF92400E)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = onOpenSignIn,
                colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("community_sign_in_banner_button")
              ) {
                Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }

        // WhatsApp / Messenger inspired Primary Tabs
        PrimaryTabRow(
          selectedTabIndex = selectedTab,
          containerColor = AkwaGreenDark,
          contentColor = Color.White
        ) {
          tabTitles.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = title,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                  ),
                  color = if (selectedTab == index) RadiantSunAmber else Color(0xFFE2E8F0)
                )
              },
              modifier = Modifier.testTag("tab_${index}")
            )
          }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
          when (selectedTab) {
            0 -> GroupChatView(
              currentUserId = currentUserId,
              messages = groupMessages,
              onSendMessage = onSendGroupMessage,
              onOpenSignIn = onOpenSignIn
            )

            1 -> DirectConversationsList(
              currentUserId = currentUserId,
              users = usersList,
              onSelectUser = onSelectChatUser,
              onOpenSignIn = onOpenSignIn
            )

            2 -> MembersDirectoryView(
              currentUserId = currentUserId,
              users = usersList,
              onSelectChat = onSelectChatUser,
              onAudioCall = { user -> requestCall(user, "audio") },
              onVideoCall = { user -> requestCall(user, "video") },
              onOpenSignIn = onOpenSignIn
            )

            3 -> ForumsAndPollsView(
              discussions = discussions,
              polls = polls,
              onToggleLike = onToggleLike,
              onOpenComments = onOpenComments,
              onVotePoll = onVotePoll,
              onCreateDiscussion = onCreateDiscussion
            )
          }
        }
      }
    }

    // Floating Incoming Call Banner
    if (incomingCall != null) {
      IncomingCallBanner(
        call = incomingCall,
        onAnswer = { onAnswerCall(incomingCall) },
        onReject = { onRejectCall(incomingCall) },
        modifier = Modifier.align(Alignment.TopCenter)
      )
    }

    // Full Active Voice / Video Call Screen
    if (activeCall != null) {
      ActiveCallScreen(
        call = activeCall,
        isMyCall = activeCall.callerId == currentUserId,
        onEndCall = { onEndCall(activeCall) }
      )
    }

    // Comments dialog for offline discussions
    if (activePostForComments != null) {
      CommentsDialog(
        post = activePostForComments,
        comments = comments,
        onDismiss = onCloseComments,
        onAddComment = { text -> onAddComment(activePostForComments.id, text) }
      )
    }
  }
}

// 1. Group Chat View (Community Hall)
@Composable
private fun GroupChatView(
  currentUserId: String?,
  messages: List<GroupMessage>,
  onSendMessage: (String, String, String?) -> Unit,
  onOpenSignIn: () -> Unit
) {
  var inputText by remember { mutableStateOf("") }
  var showAttachmentMenu by remember { mutableStateOf(false) }

  Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAF9))) {
    // Channel description
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFE8F5E9))
        .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
      Text(
        text = "🌟 The Light of Akwa Ibom Official Hall • Shared with all registered members",
        style = MaterialTheme.typography.labelSmall,
        color = AkwaGreenDark
      )
    }

    if (currentUserId == null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = RadiantSunLight.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = RadiantSunOrange, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text("Community Hall Chat", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = AkwaGreenDark)
            Text("Sign in with Google to view live messages and participate in real-time discussion.", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = onOpenSignIn,
            colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text("Sign In", fontSize = 12.sp, color = Color.White)
          }
        }
      }
    }

    // Message list
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      reverseLayout = false,
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      if (messages.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Welcome to the Community Hall!\nBe the first to post a greeting.",
              style = MaterialTheme.typography.bodyMedium,
              color = Color.Gray,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      } else {
        items(messages, key = { it.id }) { msg ->
          val isMe = msg.senderId == currentUserId
          GroupMessageBubble(msg = msg, isMe = isMe)
        }
      }
    }

    // Message Input Bar
    ChatInputBar(
      inputText = inputText,
      onTextChanged = { inputText = it },
      onSend = {
        if (currentUserId == null) {
          onOpenSignIn()
        } else if (inputText.isNotBlank()) {
          onSendMessage(inputText.trim(), "text", null)
          inputText = ""
        }
      },
      onSendPhoto = {
        if (currentUserId == null) {
          onOpenSignIn()
        } else {
          onSendMessage("📷 Shared a photo from Akwa Ibom", "image", "photo_data_placeholder")
        }
      },
      onSendVoice = {
        if (currentUserId == null) {
          onOpenSignIn()
        } else {
          onSendMessage("🎤 Voice Note (0:14) • \"Brighter we shine!\"", "voice", "voice_note_placeholder")
        }
      }
    )
  }
}

@Composable
private fun GroupMessageBubble(msg: GroupMessage, isMe: Boolean) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
  ) {
    if (!isMe) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(AkwaGreenPrimary),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = msg.senderName.take(1).uppercase(),
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
      Spacer(modifier = Modifier.width(6.dp))
    }

    Column(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 14.dp,
            topEnd = 14.dp,
            bottomStart = if (isMe) 14.dp else 2.dp,
            bottomEnd = if (isMe) 2.dp else 14.dp
          )
        )
        .background(if (isMe) Color(0xFFDCF8C6) else Color.White)
        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .widthIn(max = 280.dp)
    ) {
      if (!isMe) {
        Text(
          text = msg.senderName,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = RadiantSunOrange
        )
        Spacer(modifier = Modifier.height(2.dp))
      }

      if (msg.type == "image") {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE8F5E9)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = AkwaGreenPrimary, modifier = Modifier.size(36.dp))
            Text("Community Photo Asset", fontSize = 11.sp, color = AkwaGreenDark)
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
      } else if (msg.type == "voice") {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(6.dp)
        ) {
          Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = RadiantSunOrange, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "▶ 0:14 Voice Note", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      Text(
        text = msg.text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF1E293B)
      )
    }
  }
}

// 2. Direct Conversations List
@Composable
private fun DirectConversationsList(
  currentUserId: String?,
  users: List<ChatUser>,
  onSelectUser: (ChatUser) -> Unit,
  onOpenSignIn: () -> Unit
) {
  if (currentUserId == null) {
    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Person, contentDescription = null, tint = AkwaGreenPrimary, modifier = Modifier.size(52.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Sign In to View Chats",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Sign in with Google to start private messages and calls with other registered Akwa Ibom members.",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.Gray,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onOpenSignIn,
          colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange)
        ) {
          Text("Sign in with Google", color = Color.White)
        }
      }
    }
    return
  }

  val otherUsers = users.filter { it.userId != currentUserId }

  if (otherUsers.isEmpty()) {
    Box(modifier = Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "No private chats yet.\nSelect any member from the 'Members' tab to start a conversation!",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.Gray,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
    }
  } else {
    LazyColumn(
      modifier = Modifier.fillMaxSize().background(Color.White),
      contentPadding = PaddingValues(vertical = 8.dp)
    ) {
      items(otherUsers, key = { it.userId }) { user ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectUser(user) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(AkwaGreenPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.displayName.take(1).uppercase(),
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = user.displayName,
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF1E293B)
            )
            Text(
              text = "${user.lga} LGA • ${user.occupation}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF64748B)
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = "Chat",
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

// 3. Members Directory View (Searchable list with Chat, Audio Call, Video Call actions)
@Composable
private fun MembersDirectoryView(
  currentUserId: String?,
  users: List<ChatUser>,
  onSelectChat: (ChatUser) -> Unit,
  onAudioCall: (ChatUser) -> Unit,
  onVideoCall: (ChatUser) -> Unit,
  onOpenSignIn: () -> Unit
) {
  if (currentUserId == null) {
    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.People, contentDescription = null, tint = AkwaGreenPrimary, modifier = Modifier.size(52.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Registered Members Directory",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Sign in with Google to explore verified members across all 31 LGAs, chat directly, and network.",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.Gray,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onOpenSignIn,
          colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange)
        ) {
          Text("Sign in with Google", color = Color.White)
        }
      }
    }
    return
  }

  var searchQuery by remember { mutableStateOf("") }

  val filteredUsers = users.filter {
    searchQuery.isBlank() ||
      it.displayName.contains(searchQuery, ignoreCase = true) ||
      it.lga.contains(searchQuery, ignoreCase = true) ||
      it.occupation.contains(searchQuery, ignoreCase = true)
  }

  Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Search members by name, LGA, or skill...", fontSize = 13.sp) },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Close, contentDescription = "Clear")
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)
        .testTag("search_members_input")
    )

    LazyColumn(
      modifier = Modifier.weight(1f).fillMaxWidth(),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(filteredUsers, key = { it.userId.ifEmpty { it.id } }) { user ->
        val isMe = user.userId == currentUserId
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth().testTag("member_card_${user.userId}")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(AkwaGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.displayName.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = user.displayName,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF1E293B)
                )
                if (isMe) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "(You)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = RadiantSunOrange
                  )
                }
              }

              Text(
                text = "${user.lga} LGA • ${user.occupation}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B)
              )
            }

            if (!isMe) {
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Chat button
                IconButton(
                  onClick = { onSelectChat(user) },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AkwaGreenLight)
                    .testTag("chat_user_${user.userId}")
                ) {
                  Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "Message", tint = AkwaGreenPrimary, modifier = Modifier.size(18.dp))
                }

                // Audio Call button
                IconButton(
                  onClick = { onAudioCall(user) },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7))
                    .testTag("audio_call_${user.userId}")
                ) {
                  Icon(imageVector = Icons.Default.Call, contentDescription = "Voice Call", tint = RadiantSunOrange, modifier = Modifier.size(18.dp))
                }

                // Video Call button
                IconButton(
                  onClick = { onVideoCall(user) },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0F2FE))
                    .testTag("video_call_${user.userId}")
                ) {
                  Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video Call", tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                }
              }
            }
          }
        }
      }
    }
  }
}

// 4. Direct 1-on-1 Chat View
@Composable
private fun DirectChatView(
  otherUser: ChatUser,
  currentUserId: String,
  messages: List<DirectMessage>,
  onBack: () -> Unit,
  onSendMessage: (String, String, String?) -> Unit,
  onAudioCall: () -> Unit,
  onVideoCall: () -> Unit
) {
  var text by remember { mutableStateOf("") }

  // Intercept back button when inside 1-on-1 direct chat
  BackHandler {
    onBack()
  }

  Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF1F5F9))) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(AkwaGreenDark)
        .padding(horizontal = 8.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("direct_chat_back_button")
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Members", tint = Color.White)
      }

      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(RadiantSunOrange),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = otherUser.displayName.take(1).uppercase(),
          color = Color.White,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = otherUser.displayName,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )
        Text(
          text = "${otherUser.lga} LGA • Online",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFFA7F3D0)
        )
      }

      // Audio Call
      IconButton(onClick = onAudioCall, modifier = Modifier.testTag("direct_chat_audio_call")) {
        Icon(Icons.Default.Call, contentDescription = "Audio Call", tint = Color.White)
      }

      // Video Call
      IconButton(onClick = onVideoCall, modifier = Modifier.testTag("direct_chat_video_call")) {
        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White)
      }
    }

    // Messages
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        val isMe = msg.senderId == currentUserId
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
        ) {
          Column(
            modifier = Modifier
              .clip(
                RoundedCornerShape(
                  topStart = 14.dp,
                  topEnd = 14.dp,
                  bottomStart = if (isMe) 14.dp else 2.dp,
                  bottomEnd = if (isMe) 2.dp else 14.dp
                )
              )
              .background(if (isMe) Color(0xFFDCF8C6) else Color.White)
              .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .widthIn(max = 280.dp)
          ) {
            if (msg.type == "image") {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(130.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
              ) {
                Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = AkwaGreenPrimary, modifier = Modifier.size(36.dp))
              }
              Spacer(modifier = Modifier.height(4.dp))
            } else if (msg.type == "voice") {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = RadiantSunOrange, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "▶ 0:12 Voice Message", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
              }
              Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
              text = msg.text,
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF1E293B)
            )

            if (isMe) {
              Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (msg.isRead) Icons.Default.DoneAll else Icons.Default.Check,
                  contentDescription = null,
                  tint = if (msg.isRead) Color(0xFF0284C7) else Color.Gray,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    }

    // Input Bar
    ChatInputBar(
      inputText = text,
      onTextChanged = { text = it },
      onSend = {
        if (text.isNotBlank()) {
          onSendMessage(text.trim(), "text", null)
          text = ""
        }
      },
      onSendPhoto = {
        onSendMessage("📷 Shared a photo", "image", "photo_data_placeholder")
      },
      onSendVoice = {
        onSendMessage("🎤 Voice Note (0:10)", "voice", "voice_note_placeholder")
      }
    )
  }
}

@Composable
private fun ChatInputBar(
  inputText: String,
  onTextChanged: (String) -> Unit,
  onSend: () -> Unit,
  onSendPhoto: () -> Unit,
  onSendVoice: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(horizontal = 8.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = onSendPhoto, modifier = Modifier.size(38.dp)) {
      Icon(Icons.Default.PhotoCamera, contentDescription = "Send Photo", tint = AkwaGreenPrimary)
    }

    IconButton(onClick = onSendVoice, modifier = Modifier.size(38.dp)) {
      Icon(Icons.Default.Mic, contentDescription = "Voice Note", tint = RadiantSunOrange)
    }

    OutlinedTextField(
      value = inputText,
      onValueChange = onTextChanged,
      placeholder = { Text("Type a message...", fontSize = 13.sp) },
      modifier = Modifier
        .weight(1f)
        .testTag("chat_input_text_field"),
      shape = RoundedCornerShape(20.dp),
      maxLines = 4
    )

    Spacer(modifier = Modifier.width(6.dp))

    IconButton(
      onClick = onSend,
      modifier = Modifier
        .size(42.dp)
        .clip(CircleShape)
        .background(AkwaGreenPrimary)
        .testTag("chat_send_button")
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.Send,
        contentDescription = "Send",
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

// 5. Forums & Polls View (Preserved original community discussions & polls)
@Composable
private fun ForumsAndPollsView(
  discussions: List<DiscussionPost>,
  polls: List<CommunityPoll>,
  onToggleLike: (Long) -> Unit,
  onOpenComments: (DiscussionPost) -> Unit,
  onVotePoll: (Long, Int) -> Unit,
  onCreateDiscussion: (String, String, String) -> Unit
) {
  var showNewPostDialog by remember { mutableStateOf(false) }
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Unity", "Youth Empowerment", "Cultural Heritage", "Economy & Innovation", "General")

  val filteredPosts = if (selectedCategory == "All") {
    discussions
  } else {
    discussions.filter { it.category.contains(selectedCategory, ignoreCase = true) }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showNewPostDialog = true },
        containerColor = AkwaGreenPrimary,
        contentColor = Color.White
      ) {
        Icon(Icons.Default.Add, contentDescription = "Post Topic")
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (polls.isNotEmpty()) {
        item {
          Text(
            text = "COMMUNITY POLLS",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = RadiantSunOrange
          )
        }
        items(polls, key = { it.id }) { poll ->
          PollCard(poll = poll, onVote = { opt -> onVotePoll(poll.id, opt) })
        }
      }

      item {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "COMMUNITY DISCUSSIONS",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(cat, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AkwaGreenPrimary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }

      items(filteredPosts, key = { it.id }) { post ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            Text(text = "${post.authorName} (${post.authorLga}) • ${post.category}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.content, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Row(modifier = Modifier.clickable { onToggleLike(post.id) }, verticalAlignment = Alignment.CenterVertically) {
                Icon(if (post.isLikedByMe) Icons.Default.WbSunny else Icons.Default.ThumbUp, contentDescription = null, tint = if (post.isLikedByMe) RadiantSunOrange else Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.likesCount} Shined", fontSize = 12.sp)
              }
              Row(modifier = Modifier.clickable { onOpenComments(post) }, verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.commentsCount} Comments", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }
  }

  if (showNewPostDialog) {
    CreatePostDialog(
      categories = categories.filter { it != "All" },
      onDismiss = { showNewPostDialog = false },
      onSubmit = { title, content, cat ->
        onCreateDiscussion(title, content, cat)
        showNewPostDialog = false
      }
    )
  }
}

@Composable
private fun PollCard(poll: CommunityPoll, onVote: (Int) -> Unit) {
  val total = (poll.votes1 + poll.votes2 + poll.votes3).coerceAtLeast(1)
  val pct1 = (poll.votes1.toFloat() / total * 100).toInt()
  val pct2 = (poll.votes2.toFloat() / total * 100).toInt()
  val pct3 = (poll.votes3.toFloat() / total * 100).toInt()

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(text = poll.question, fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
      Spacer(modifier = Modifier.height(8.dp))
      PollOption(poll.option1, pct1, poll.userVotedOption == 1) { onVote(1) }
      Spacer(modifier = Modifier.height(4.dp))
      PollOption(poll.option2, pct2, poll.userVotedOption == 2) { onVote(2) }
      Spacer(modifier = Modifier.height(4.dp))
      PollOption(poll.option3, pct3, poll.userVotedOption == 3) { onVote(3) }
    }
  }
}

@Composable
private fun PollOption(text: String, pct: Int, selected: Boolean, onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(if (selected) Color(0xFFFFE0B2) else Color.White)
      .clickable { onClick() }
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    Text(text = "$pct%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = RadiantSunOrange)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePostDialog(
  categories: List<String>,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(categories.first()) }
  var expanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Share Topic with Community", fontWeight = FontWeight.Bold, color = AkwaGreenDark) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Topic Title") }, modifier = Modifier.fillMaxWidth())
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
          OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
          )
          ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { cat ->
              DropdownMenuItem(text = { Text(cat) }, onClick = { selectedCategory = cat; expanded = false })
            }
          }
        }
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Content") }, modifier = Modifier.fillMaxWidth().height(100.dp), maxLines = 4)
      }
    },
    confirmButton = {
      Button(onClick = { if (title.isNotBlank() && content.isNotBlank()) onSubmit(title, content, selectedCategory) }, colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)) {
        Text("Post")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

@Composable
private fun CommentsDialog(
  post: DiscussionPost,
  comments: List<DiscussionComment>,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var newCommentText by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Comments (${comments.size})", fontWeight = FontWeight.Bold, color = AkwaGreenDark)
        IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth().height(320.dp)) {
        Text(text = post.title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), maxLines = 2)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          items(comments) { c ->
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)), modifier = Modifier.fillMaxWidth()) {
              Column(modifier = Modifier.padding(8.dp)) {
                Text(text = c.authorName, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = AkwaGreenPrimary))
                Text(text = c.content, style = MaterialTheme.typography.bodySmall)
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
          OutlinedTextField(value = newCommentText, onValueChange = { newCommentText = it }, placeholder = { Text("Comment...", fontSize = 12.sp) }, modifier = Modifier.weight(1f), singleLine = true)
          Spacer(modifier = Modifier.width(6.dp))
          IconButton(onClick = { if (newCommentText.isNotBlank()) { onAddComment(newCommentText); newCommentText = "" } }, modifier = Modifier.clip(CircleShape).background(AkwaGreenPrimary)) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(16.dp))
          }
        }
      }
    },
    confirmButton = {}
  )
}
