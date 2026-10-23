package com.example.data.repository

import com.example.data.InitialData
import com.example.data.local.AppDao
import com.example.data.local.populateInitialDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AkwaIbomRepository(
  private val dao: AppDao,
  scope: CoroutineScope
) {
  init {
    // Ensure initial data is populated even if DB file existed without initial rows
    scope.launch(Dispatchers.IO) {
      val existingProfile = dao.getUserProfile().firstOrNull()
      if (existingProfile == null) {
        populateInitialDatabase(dao)
      }
    }
  }

  // Announcements
  val announcements: Flow<List<CommunityAnnouncement>> = dao.getActiveAnnouncements()
  suspend fun addAnnouncement(announcement: CommunityAnnouncement) = dao.insertAnnouncement(announcement)
  suspend fun deleteAnnouncement(id: Long) = dao.deleteAnnouncement(id)

  // Discussions
  val approvedDiscussions: Flow<List<DiscussionPost>> = dao.getApprovedDiscussions()
  val allDiscussions: Flow<List<DiscussionPost>> = dao.getAllDiscussions()
  suspend fun createDiscussion(post: DiscussionPost) = dao.insertDiscussion(post)
  suspend fun toggleLike(postId: Long) = dao.togglePostLike(postId)
  suspend fun setDiscussionApproved(id: Long, approved: Boolean) = dao.setDiscussionApproved(id, approved)
  suspend fun deleteDiscussion(id: Long) = dao.deleteDiscussion(id)

  // Comments
  fun getComments(postId: Long): Flow<List<DiscussionComment>> = dao.getCommentsForPost(postId)
  suspend fun addComment(comment: DiscussionComment) {
    dao.insertComment(comment)
    dao.incrementCommentsCount(comment.postId)
  }

  // Polls
  val polls: Flow<List<CommunityPoll>> = dao.getPolls()
  suspend fun votePoll(pollId: Long, option: Int) = dao.votePoll(pollId, option)

  // Businesses
  val approvedBusinesses: Flow<List<BusinessListing>> = dao.getApprovedBusinesses()
  val allBusinesses: Flow<List<BusinessListing>> = dao.getAllBusinesses()
  suspend fun registerBusiness(business: BusinessListing) = dao.insertBusiness(business)
  suspend fun setBusinessApproved(id: Long, approved: Boolean) = dao.setBusinessApproved(id, approved)
  suspend fun deleteBusiness(id: Long) = dao.deleteBusiness(id)

  // Opportunities
  val approvedOpportunities: Flow<List<OpportunityItem>> = dao.getApprovedOpportunities()
  val allOpportunities: Flow<List<OpportunityItem>> = dao.getAllOpportunities()
  suspend fun submitOpportunity(item: OpportunityItem) = dao.insertOpportunity(item)
  suspend fun setOpportunityApproved(id: Long, approved: Boolean) = dao.setOpportunityApproved(id, approved)
  suspend fun deleteOpportunity(id: Long) = dao.deleteOpportunity(id)

  // Events
  val events: Flow<List<CommunityEvent>> = dao.getAllEvents()
  suspend fun toggleEventRegistration(id: Long, currentRegistered: Boolean) =
    dao.setEventRegistration(id, !currentRegistered)
  suspend fun toggleEventBookmark(id: Long, currentBookmarked: Boolean) =
    dao.setEventBookmark(id, !currentBookmarked)
  suspend fun addEvent(event: CommunityEvent) = dao.insertEvent(event)

  // News
  val news: Flow<List<NewsArticle>> = dao.getAllNews()

  // Culture
  val culture: Flow<List<CultureItem>> = dao.getAllCultureItems()

  // Profile & Chants
  val userProfile: Flow<UserProfile?> = dao.getUserProfile()
  suspend fun saveProfile(profile: UserProfile) = dao.saveUserProfile(profile)
  suspend fun incrementChant() = dao.incrementChantsCount()

  // Support
  val supportMessages: Flow<List<SupportMessage>> = dao.getAllSupportMessages()
  suspend fun sendSupportMessage(msg: SupportMessage) = dao.insertSupportMessage(msg)

  // Reset to default data
  suspend fun resetDemoData() {
    populateInitialDatabase(dao)
  }
}
