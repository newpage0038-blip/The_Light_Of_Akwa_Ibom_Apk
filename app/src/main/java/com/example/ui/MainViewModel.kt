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
import com.example.data.repository.AkwaIbomRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
  private val repository: AkwaIbomRepository
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
    val current = _navigationStack.value.toMutableList()
    current.add(screen)
    _navigationStack.value = current
    _currentScreen.value = screen
  }

  fun navigateBack(): Boolean {
    val current = _navigationStack.value.toMutableList()
    if (current.size > 1) {
      current.removeAt(current.size - 1)
      _navigationStack.value = current
      _currentScreen.value = current.last()
      return true
    } else if (_currentScreen.value != Screen.HOME && _currentScreen.value != Screen.WELCOME) {
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
}

class MainViewModelFactory(
  private val repository: AkwaIbomRepository
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      return MainViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
