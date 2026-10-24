package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.BusinessListing
import com.example.data.model.CommunityAnnouncement
import com.example.data.model.CommunityEvent
import com.example.data.model.CommunityPoll
import com.example.data.model.CultureItem
import com.example.data.model.DiscussionComment
import com.example.data.model.DiscussionPost
import com.example.data.model.NewsArticle
import com.example.data.model.OpportunityItem
import com.example.data.model.SupportMessage
import com.example.data.model.UserProfile
import com.example.data.model.CallSession
import com.example.data.model.ChatUser
import com.example.data.model.DirectMessage
import com.example.data.model.GroupMessage
import com.example.data.repository.AkwaIbomRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.EntertainmentRepository
import com.example.data.repository.FootballRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
  private val repository: AkwaIbomRepository,
  private val chatRepository: ChatRepository? = null,
  val entertainmentRepository: EntertainmentRepository = EntertainmentRepository(),
  val footballRepository: FootballRepository = FootballRepository()
) : ViewModel() {

  // Navigation Stack
  private val _navigationStack = MutableStateFlow<List<Screen>>(listOf(Screen.WELCOME))
  val currentScreen: StateFlow<Screen> = MutableStateFlow(Screen.WELCOME)
  private val _currentScreen = MutableStateFlow(Screen.WELCOME)
  val activeScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // Chanting State
  private val _isChanting = MutableStateFlow(false)
  val isChanting: StateFlow<Boolean> = _isChanting.asStateFlow()

  private val _greetingMessage = MutableStateFlow<String?>(null)
  val greetingMessage: StateFlow<String?> = _greetingMessage.asStateFlow()

  // User feedback snackbar/toast
  private val _feedbackMessage = MutableStateFlow<String?>(null)
  val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

  // Admin Mode Toggle
  private val _isAdminMode = MutableStateFlow(false)
  val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

  // Auth Dialog & Current User
  private val _isAuthDialogOpen = MutableStateFlow(false)
  val isAuthDialogOpen: StateFlow<Boolean> = _isAuthDialogOpen.asStateFlow()

  private val _currentUserId = MutableStateFlow<String?>(FirebaseAuth.getInstance().currentUser?.uid)
  val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

  private val _currentUserName = MutableStateFlow(FirebaseAuth.getInstance().currentUser?.displayName ?: "Akwa Ibom Citizen")
  val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

  // Communication: Members, Group Messages, Direct Messages, Calls
  private val _chatUsersList = MutableStateFlow<List<ChatUser>>(emptyList())
  val chatUsersList: StateFlow<List<ChatUser>> = _chatUsersList.asStateFlow()

  private val _groupMessages = MutableStateFlow<List<GroupMessage>>(emptyList())
  val groupMessages: StateFlow<List<GroupMessage>> = _groupMessages.asStateFlow()

  private val _directMessages = MutableStateFlow<List<DirectMessage>>(emptyList())
  val directMessages: StateFlow<List<DirectMessage>> = _directMessages.asStateFlow()

  private val _selectedChatUser = MutableStateFlow<ChatUser?>(null)
  val selectedChatUser: StateFlow<ChatUser?> = _selectedChatUser.asStateFlow()

  private val _incomingCall = MutableStateFlow<CallSession?>(null)
  val incomingCall: StateFlow<CallSession?> = _incomingCall.asStateFlow()

  private val _activeCall = MutableStateFlow<CallSession?>(null)
  val activeCall: StateFlow<CallSession?> = _activeCall.asStateFlow()

  private var groupMessagesJob: Job? = null
  private var usersJob: Job? = null
  private var directMessagesJob: Job? = null
  private var activeCallJob: Job? = null
  private var incomingCallsJob: Job? = null

  private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
    val user = auth.currentUser
    val uid = user?.uid
    _currentUserId.value = uid
    _currentUserName.value = user?.displayName ?: "Akwa Ibom Citizen"
    if (uid != null) {
      startChatObservation(uid)
    } else {
      stopChatObservation()
    }
  }

  init {
    FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
      startChatObservation(currentUser.uid)
    }
  }

  fun initChatListeners() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    _currentUserId.value = uid
    _currentUserName.value = FirebaseAuth.getInstance().currentUser?.displayName ?: "Akwa Ibom Citizen"

    if (uid != null) {
      startChatObservation(uid)
    } else {
      stopChatObservation()
    }
  }

  private fun startChatObservation(uid: String) {
    if (chatRepository == null) return

    groupMessagesJob?.cancel()
    groupMessagesJob = viewModelScope.launch {
      chatRepository.observeGroupMessages().collectLatest { msgs ->
        _groupMessages.value = msgs
      }
    }

    usersJob?.cancel()
    usersJob = viewModelScope.launch {
      chatRepository.observeUsers().collectLatest { users ->
        _chatUsersList.value = users
      }
    }

    startIncomingCallsObservation(uid)
  }

  private fun stopChatObservation() {
    groupMessagesJob?.cancel()
    groupMessagesJob = null
    usersJob?.cancel()
    usersJob = null
    incomingCallsJob?.cancel()
    incomingCallsJob = null
    directMessagesJob?.cancel()
    directMessagesJob = null
    activeCallJob?.cancel()
    activeCallJob = null

    _groupMessages.value = emptyList()
    _chatUsersList.value = emptyList()
    _directMessages.value = emptyList()
    _incomingCall.value = null
    _activeCall.value = null
  }

  private fun startIncomingCallsObservation(uid: String) {
    incomingCallsJob?.cancel()
    incomingCallsJob = viewModelScope.launch {
      chatRepository?.observeIncomingCalls(uid)?.collectLatest { calls ->
        _incomingCall.value = calls.firstOrNull()
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    stopChatObservation()
  }

  // Active Discussion Post Comments Dialog
  private val _activePostForComments = MutableStateFlow<DiscussionPost?>(null)
  val activePostForComments: StateFlow<DiscussionPost?> = _activePostForComments.asStateFlow()

  private val _commentsForActivePost = MutableStateFlow<List<DiscussionComment>>(emptyList())
  val commentsForActivePost: StateFlow<List<DiscussionComment>> = _commentsForActivePost.asStateFlow()

  // Selected Article for Reading View
  private val _selectedNewsArticle = MutableStateFlow<NewsArticle?>(null)
  val selectedNewsArticle: StateFlow<NewsArticle?> = _selectedNewsArticle.asStateFlow()

  // Selected Culture Item for Details View
  private val _selectedCultureItem = MutableStateFlow<CultureItem?>(null)
  val selectedCultureItem: StateFlow<CultureItem?> = _selectedCultureItem.asStateFlow()

  // Data Flows
  val announcements: StateFlow<List<CommunityAnnouncement>> = repository.announcements
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val approvedDiscussions: StateFlow<List<DiscussionPost>> = repository.approvedDiscussions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allDiscussions: StateFlow<List<DiscussionPost>> = repository.allDiscussions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val polls: StateFlow<List<CommunityPoll>> = repository.polls
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val approvedBusinesses: StateFlow<List<BusinessListing>> = repository.approvedBusinesses
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allBusinesses: StateFlow<List<BusinessListing>> = repository.allBusinesses
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val approvedOpportunities: StateFlow<List<OpportunityItem>> = repository.approvedOpportunities
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allOpportunities: StateFlow<List<OpportunityItem>> = repository.allOpportunities
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val events: StateFlow<List<CommunityEvent>> = repository.events
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val news: StateFlow<List<NewsArticle>> = repository.news
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val culture: StateFlow<List<CultureItem>> = repository.culture
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val userProfile: StateFlow<UserProfile?> = repository.userProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val supportMessages: StateFlow<List<SupportMessage>> = repository.supportMessages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Navigation Methods
  fun navigateTo(screen: Screen) {
    if (_currentScreen.value == screen) return

    // Dismiss open dialogs/sub-states when navigating to another screen
    _selectedChatUser.value = null
    _activePostForComments.value = null
    _selectedNewsArticle.value = null
    _selectedCultureItem.value = null
    _isAuthDialogOpen.value = false

    if (screen == Screen.HOME) {
      _navigationStack.value = listOf(Screen.HOME)
      _currentScreen.value = Screen.HOME
      return
    }

    val current = _navigationStack.value.toMutableList()
    // When transitioning from Welcome or empty, establish HOME as root
    if (current.isEmpty() || current.firstOrNull() == Screen.WELCOME) {
      current.clear()
      current.add(Screen.HOME)
    } else if (current.firstOrNull() != Screen.HOME) {
      current.add(0, Screen.HOME)
    }

    // Maintain proper history and prevent duplicate pages from repeatedly stacking up (Requirement 5)
    current.removeAll { it == screen }
    current.add(screen)

    _navigationStack.value = current
    _currentScreen.value = screen
  }

  fun navigateHome() {
    // Return directly to the main dashboard without exiting the app (Requirement 3)
    selectChatUser(null)
    _activePostForComments.value = null
    _commentsForActivePost.value = emptyList()
    _selectedNewsArticle.value = null
    _selectedCultureItem.value = null
    _isAuthDialogOpen.value = false
    _navigationStack.value = listOf(Screen.HOME)
    _currentScreen.value = Screen.HOME
  }

  fun canNavigateBack(): Boolean {
    // Returns true if there is an active sub-state (direct chat, comments, dialogs)
    // or if the user is on any secondary screen
    return _selectedChatUser.value != null ||
      _activePostForComments.value != null ||
      _selectedNewsArticle.value != null ||
      _selectedCultureItem.value != null ||
      _isAuthDialogOpen.value ||
      (_currentScreen.value != Screen.HOME && _currentScreen.value != Screen.WELCOME) ||
      _navigationStack.value.size > 1
  }

  fun navigateBack(): Boolean {
    // 1. If inside a direct 1-on-1 chat, return to community member list / tabs
    if (_selectedChatUser.value != null) {
      selectChatUser(null)
      return true
    }

    // 2. If comments dialog is active, dismiss comments
    if (_activePostForComments.value != null) {
      _activePostForComments.value = null
      _commentsForActivePost.value = emptyList()
      return true
    }

    // 3. If news article dialog is open, close it
    if (_selectedNewsArticle.value != null) {
      _selectedNewsArticle.value = null
      return true
    }

    // 4. If culture item dialog is open, close it
    if (_selectedCultureItem.value != null) {
      _selectedCultureItem.value = null
      return true
    }

    // 5. If auth dialog is open, close it
    if (_isAuthDialogOpen.value) {
      _isAuthDialogOpen.value = false
      return true
    }

    // 6. Navigate back in screen history stack (Requirement 2 & 5)
    val current = _navigationStack.value.toMutableList()
    if (current.size > 1) {
      current.removeAt(current.size - 1)
      _navigationStack.value = current
      _currentScreen.value = current.last()
      return true
    } else if (_currentScreen.value != Screen.HOME && _currentScreen.value != Screen.WELCOME) {
      // Return to main dashboard if on any secondary screen without further stack
      _currentScreen.value = Screen.HOME
      _navigationStack.value = listOf(Screen.HOME)
      return true
    }
    return false
  }

  fun toggleAdminMode() {
    _isAdminMode.value = !_isAdminMode.value
    showFeedback(if (_isAdminMode.value) "Administrator Mode Enabled" else "Standard Member Mode")
  }

  fun showFeedback(message: String) {
    _feedbackMessage.value = message
    viewModelScope.launch {
      delay(3000)
      if (_feedbackMessage.value == message) {
        _feedbackMessage.value = null
      }
    }
  }

  // Chanting Greeting Action
  fun chantGreeting() {
    viewModelScope.launch {
      _isChanting.value = true
      _greetingMessage.value = "Leader: THE LIGHT! 🌟\nMembers: BRIGHTER WE SHINE!"
      repository.incrementChant()
      delay(2500)
      _isChanting.value = false
    }
  }

  fun dismissGreeting() {
    _greetingMessage.value = null
  }

  // Discussions & Comments
  fun openComments(post: DiscussionPost) {
    _activePostForComments.value = post
    viewModelScope.launch {
      repository.getComments(post.id).collect { list ->
        _commentsForActivePost.value = list
      }
    }
  }

  fun closeComments() {
    _activePostForComments.value = null
    _commentsForActivePost.value = emptyList()
  }

  fun addComment(postId: Long, content: String) {
    if (content.isBlank()) return
    val currentProfile = userProfile.value
    val author = currentProfile?.fullName ?: "Proud Akwa Ibomite"
    viewModelScope.launch {
      repository.addComment(
        DiscussionComment(
          postId = postId,
          authorName = author,
          content = content.trim()
        )
      )
      showFeedback("Comment posted successfully!")
    }
  }

  fun createDiscussion(title: String, content: String, category: String) {
    if (title.isBlank() || content.isBlank()) return
    val currentProfile = userProfile.value
    val author = currentProfile?.fullName ?: "Akwa Ibom Member"
    val lga = currentProfile?.lga ?: "Uyo"
    viewModelScope.launch {
      repository.createDiscussion(
        DiscussionPost(
          authorName = author,
          authorLga = lga,
          title = title.trim(),
          content = content.trim(),
          category = category,
          isApproved = true // Auto-approved for community participation, moderator can manage
        )
      )
      showFeedback("Discussion topic shared with community!")
    }
  }

  fun togglePostLike(postId: Long) {
    viewModelScope.launch {
      repository.toggleLike(postId)
    }
  }

  // Polls
  fun votePoll(pollId: Long, option: Int) {
    viewModelScope.launch {
      repository.votePoll(pollId, option)
      showFeedback("Vote recorded! Thank you for participating.")
    }
  }

  // Business
  fun registerBusiness(
    name: String,
    category: String,
    description: String,
    lga: String,
    address: String,
    phone: String,
    whatsapp: String,
    owner: String
  ) {
    viewModelScope.launch {
      repository.registerBusiness(
        BusinessListing(
          businessName = name.trim(),
          category = category,
          description = description.trim(),
          lga = lga,
          address = address.trim(),
          phoneNumber = phone.trim(),
          whatsapp = whatsapp.trim(),
          ownerName = owner.trim(),
          isApproved = true
        )
      )
      showFeedback("Business successfully listed on Akwa Ibom Directory!")
    }
  }

  // Opportunities
  fun submitOpportunity(
    title: String,
    category: String,
    organization: String,
    location: String,
    deadline: String,
    description: String,
    contact: String
  ) {
    viewModelScope.launch {
      repository.submitOpportunity(
        OpportunityItem(
          title = title.trim(),
          category = category,
          organization = organization.trim(),
          location = location.trim(),
          deadline = deadline.trim(),
          description = description.trim(),
          contactOrUrl = contact.trim(),
          isApproved = true
        )
      )
      showFeedback("Opportunity shared with community members!")
    }
  }

  // Events
  fun toggleEventRegistration(event: CommunityEvent) {
    viewModelScope.launch {
      repository.toggleEventRegistration(event.id, event.isRegistered)
      showFeedback(if (!event.isRegistered) "Registered for ${event.title}!" else "Registration cancelled")
    }
  }

  fun toggleEventBookmark(event: CommunityEvent) {
    viewModelScope.launch {
      repository.toggleEventBookmark(event.id, event.isBookmarked)
      showFeedback(if (!event.isBookmarked) "Event saved to bookmarks" else "Removed from bookmarks")
    }
  }

  fun submitEvent(
    title: String,
    category: String,
    date: String,
    time: String,
    venue: String,
    lga: String,
    description: String
  ) {
    viewModelScope.launch {
      repository.addEvent(
        CommunityEvent(
          title = title.trim(),
          category = category,
          date = date.trim(),
          time = time.trim(),
          venue = venue.trim(),
          lga = lga,
          description = description.trim(),
          attendeesCount = 1,
          isRegistered = true
        )
      )
      showFeedback("Community event scheduled successfully!")
    }
  }

  // Profile
  fun saveProfile(fullName: String, lga: String, occupation: String, bio: String) {
    val existing = userProfile.value
    val updated = UserProfile(
      id = 1,
      fullName = fullName.trim().ifEmpty { existing?.fullName ?: "Akwa Ibomite" },
      lga = lga.ifEmpty { existing?.lga ?: "Uyo" },
      occupation = occupation.trim().ifEmpty { existing?.occupation ?: "Member" },
      bio = bio.trim().ifEmpty { existing?.bio ?: "Proud member of The Light of Akwa Ibom" },
      memberId = existing?.memberId ?: "AKWA-LIGHT-2026-${(1000..9999).random()}",
      isRegistered = true,
      joinedDate = existing?.joinedDate ?: "October 2026",
      chantsCount = existing?.chantsCount ?: 0
    )
    viewModelScope.launch {
      repository.saveProfile(updated)
      showFeedback("Membership Profile updated successfully!")
    }
  }

  // Support / Reports
  fun sendSupportMessage(name: String, emailOrPhone: String, subject: String, message: String, type: String) {
    viewModelScope.launch {
      repository.sendSupportMessage(
        SupportMessage(
          name = name.trim(),
          emailOrPhone = emailOrPhone.trim(),
          subject = subject.trim(),
          message = message.trim(),
          type = type
        )
      )
      showFeedback(if (type.contains("Report", ignoreCase = true)) "Report submitted to moderation board" else "Message sent to Administration!")
    }
  }

  // Admin moderation operations
  fun setDiscussionApproved(id: Long, approved: Boolean) {
    viewModelScope.launch {
      repository.setDiscussionApproved(id, approved)
      showFeedback("Discussion status updated")
    }
  }

  fun deleteDiscussion(id: Long) {
    viewModelScope.launch {
      repository.deleteDiscussion(id)
      showFeedback("Discussion removed")
    }
  }

  fun setBusinessApproved(id: Long, approved: Boolean) {
    viewModelScope.launch {
      repository.setBusinessApproved(id, approved)
      showFeedback("Business status updated")
    }
  }

  fun deleteBusiness(id: Long) {
    viewModelScope.launch {
      repository.deleteBusiness(id)
      showFeedback("Business listing removed")
    }
  }

  fun setOpportunityApproved(id: Long, approved: Boolean) {
    viewModelScope.launch {
      repository.setOpportunityApproved(id, approved)
      showFeedback("Opportunity status updated")
    }
  }

  fun deleteOpportunity(id: Long) {
    viewModelScope.launch {
      repository.deleteOpportunity(id)
      showFeedback("Opportunity removed")
    }
  }

  fun addAnnouncement(title: String, content: String, date: String, tag: String) {
    viewModelScope.launch {
      repository.addAnnouncement(
        CommunityAnnouncement(
          title = title.trim(),
          content = content.trim(),
          date = date.trim(),
          tag = tag
        )
      )
      showFeedback("Announcement published to all members!")
    }
  }

  fun deleteAnnouncement(id: Long) {
    viewModelScope.launch {
      repository.deleteAnnouncement(id)
      showFeedback("Announcement deleted")
    }
  }

  fun resetData() {
    viewModelScope.launch {
      repository.resetDemoData()
      showFeedback("Sample data restored successfully!")
    }
  }

  fun selectNewsArticle(article: NewsArticle?) {
    _selectedNewsArticle.value = article
  }

  fun selectCultureItem(item: CultureItem?) {
    _selectedCultureItem.value = item
  }

  // Auth Dialog Controls
  fun openSignIn() {
    _isAuthDialogOpen.value = true
  }

  fun closeSignIn() {
    _isAuthDialogOpen.value = false
  }

  fun handleAuthSuccess() {
    _isAuthDialogOpen.value = false
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val uid = firebaseUser?.uid
    _currentUserId.value = uid
    _currentUserName.value = firebaseUser?.displayName ?: "Akwa Ibom Citizen"

    showFeedback("Welcome, ${_currentUserName.value}!")

    if (uid != null && chatRepository != null) {
      val localProfile = userProfile.value
      val chatUser = ChatUser(
        id = uid,
        userId = uid,
        displayName = firebaseUser.displayName ?: localProfile?.fullName ?: "Proud Akwa Ibomite",
        email = firebaseUser.email ?: "",
        photoUrl = firebaseUser.photoUrl?.toString(),
        lga = localProfile?.lga ?: "Uyo",
        occupation = localProfile?.occupation ?: "Akwa Ibom Citizen",
        bio = localProfile?.bio ?: "Brighter We Shine!",
        status = "online"
      )
      viewModelScope.launch {
        try {
          chatRepository.saveUserProfile(chatUser)
        } catch (e: Exception) {
          // Handled in repository
        }
      }
      initChatListeners()
    }
  }

  // 1-on-1 Chat Selection & Direct Message Observation
  fun selectChatUser(user: ChatUser?) {
    _selectedChatUser.value = user
    directMessagesJob?.cancel()
    if (user != null && chatRepository != null) {
      directMessagesJob = viewModelScope.launch {
        chatRepository.observeDirectMessages(user.userId).collectLatest { msgs ->
          _directMessages.value = msgs
        }
      }
    } else {
      _directMessages.value = emptyList()
    }
  }

  // Send Group Message
  fun sendGroupMessage(text: String, type: String = "text", mediaData: String? = null) {
    if (chatRepository == null) return
    viewModelScope.launch {
      try {
        chatRepository.sendGroupMessage(text, type, mediaData)
      } catch (e: Exception) {
        showFeedback("Failed to send message: ${e.message}")
      }
    }
  }

  // Send Direct Message
  fun sendDirectMessage(receiverId: String, receiverName: String, text: String, type: String = "text", mediaData: String? = null) {
    if (chatRepository == null) return
    viewModelScope.launch {
      try {
        chatRepository.sendDirectMessage(receiverId, receiverName, text, type, mediaData)
      } catch (e: Exception) {
        showFeedback("Failed to send message: ${e.message}")
      }
    }
  }

  // Calls: Initiate, Answer, Reject, End
  fun initiateCall(user: ChatUser, callType: String) {
    if (chatRepository == null) return
    viewModelScope.launch {
      try {
        val callId = chatRepository.initiateCall(user.userId, user.displayName, callType)
        observeActiveCall(callId)
        showFeedback("Calling ${user.displayName}...")
      } catch (e: Exception) {
        showFeedback("Failed to start call: ${e.message}")
      }
    }
  }

  fun answerCall(call: CallSession) {
    if (chatRepository == null) return
    viewModelScope.launch {
      chatRepository.updateCallStatus(call.id, "accepted")
      _incomingCall.value = null
      observeActiveCall(call.id)
    }
  }

  fun rejectCall(call: CallSession) {
    if (chatRepository == null) return
    viewModelScope.launch {
      chatRepository.updateCallStatus(call.id, "rejected")
      _incomingCall.value = null
    }
  }

  fun endCall(call: CallSession) {
    if (chatRepository == null) return
    viewModelScope.launch {
      chatRepository.updateCallStatus(call.id, "ended")
      _activeCall.value = null
      activeCallJob?.cancel()
    }
  }

  private fun observeActiveCall(callId: String) {
    activeCallJob?.cancel()
    activeCallJob = viewModelScope.launch {
      chatRepository?.observeCallSession(callId)?.collectLatest { session ->
        _activeCall.value = session
        if (session == null || session.status == "ended" || session.status == "rejected") {
          delay(1200)
          _activeCall.value = null
          activeCallJob?.cancel()
        }
      }
    }
  }
}

class MainViewModelFactory(
  private val repository: AkwaIbomRepository,
  private val chatRepository: ChatRepository? = null,
  private val entertainmentRepository: EntertainmentRepository = EntertainmentRepository(),
  private val footballRepository: FootballRepository = FootballRepository()
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      return MainViewModel(repository, chatRepository, entertainmentRepository, footballRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
