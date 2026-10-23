package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
  // Announcements
  @Query("SELECT * FROM community_announcements WHERE isActive = 1 ORDER BY id DESC")
  fun getActiveAnnouncements(): Flow<List<CommunityAnnouncement>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnnouncement(announcement: CommunityAnnouncement)

  @Query("DELETE FROM community_announcements WHERE id = :id")
  suspend fun deleteAnnouncement(id: Long)

  // Discussions
  @Query("SELECT * FROM discussion_posts WHERE isApproved = 1 ORDER BY timestamp DESC")
  fun getApprovedDiscussions(): Flow<List<DiscussionPost>>

  @Query("SELECT * FROM discussion_posts ORDER BY timestamp DESC")
  fun getAllDiscussions(): Flow<List<DiscussionPost>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDiscussion(post: DiscussionPost): Long

  @Update
  suspend fun updateDiscussion(post: DiscussionPost)

  @Query("DELETE FROM discussion_posts WHERE id = :id")
  suspend fun deleteDiscussion(id: Long)

  @Query("UPDATE discussion_posts SET isApproved = :approved WHERE id = :id")
  suspend fun setDiscussionApproved(id: Long, approved: Boolean)

  @Query("UPDATE discussion_posts SET likesCount = likesCount + (CASE WHEN isLikedByMe = 1 THEN -1 ELSE 1 END), isLikedByMe = (CASE WHEN isLikedByMe = 1 THEN 0 ELSE 1 END) WHERE id = :id")
  suspend fun togglePostLike(id: Long)

  // Comments
  @Query("SELECT * FROM discussion_comments WHERE postId = :postId ORDER BY timestamp ASC")
  fun getCommentsForPost(postId: Long): Flow<List<DiscussionComment>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComment(comment: DiscussionComment): Long

  @Query("UPDATE discussion_posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
  suspend fun incrementCommentsCount(postId: Long)

  // Polls
  @Query("SELECT * FROM community_polls ORDER BY id DESC")
  fun getPolls(): Flow<List<CommunityPoll>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPoll(poll: CommunityPoll)

  @Query("UPDATE community_polls SET votes1 = votes1 + (CASE WHEN :option = 1 THEN 1 ELSE 0 END), votes2 = votes2 + (CASE WHEN :option = 2 THEN 1 ELSE 0 END), votes3 = votes3 + (CASE WHEN :option = 3 THEN 1 ELSE 0 END), userVotedOption = :option WHERE id = :pollId")
  suspend fun votePoll(pollId: Long, option: Int)

  // Businesses
  @Query("SELECT * FROM business_listings WHERE isApproved = 1 ORDER BY id DESC")
  fun getApprovedBusinesses(): Flow<List<BusinessListing>>

  @Query("SELECT * FROM business_listings ORDER BY id DESC")
  fun getAllBusinesses(): Flow<List<BusinessListing>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBusiness(business: BusinessListing)

  @Query("UPDATE business_listings SET isApproved = :approved WHERE id = :id")
  suspend fun setBusinessApproved(id: Long, approved: Boolean)

  @Query("DELETE FROM business_listings WHERE id = :id")
  suspend fun deleteBusiness(id: Long)

  // Opportunities
  @Query("SELECT * FROM opportunities WHERE isApproved = 1 ORDER BY id DESC")
  fun getApprovedOpportunities(): Flow<List<OpportunityItem>>

  @Query("SELECT * FROM opportunities ORDER BY id DESC")
  fun getAllOpportunities(): Flow<List<OpportunityItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOpportunity(opportunity: OpportunityItem)

  @Query("UPDATE opportunities SET isApproved = :approved WHERE id = :id")
  suspend fun setOpportunityApproved(id: Long, approved: Boolean)

  @Query("DELETE FROM opportunities WHERE id = :id")
  suspend fun deleteOpportunity(id: Long)

  // Events
  @Query("SELECT * FROM community_events ORDER BY id ASC")
  fun getAllEvents(): Flow<List<CommunityEvent>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEvent(event: CommunityEvent)

  @Query("UPDATE community_events SET isRegistered = :registered, attendeesCount = attendeesCount + (CASE WHEN :registered = 1 THEN 1 ELSE -1 END) WHERE id = :id")
  suspend fun setEventRegistration(id: Long, registered: Boolean)

  @Query("UPDATE community_events SET isBookmarked = :bookmarked WHERE id = :id")
  suspend fun setEventBookmark(id: Long, bookmarked: Boolean)

  // User Profile
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveUserProfile(profile: UserProfile)

  @Query("UPDATE user_profile SET chantsCount = chantsCount + 1 WHERE id = 1")
  suspend fun incrementChantsCount()

  // Support & Reports
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSupportMessage(msg: SupportMessage)

  @Query("SELECT * FROM support_messages ORDER BY timestamp DESC")
  fun getAllSupportMessages(): Flow<List<SupportMessage>>

  // News
  @Query("SELECT * FROM news_articles ORDER BY id ASC")
  fun getAllNews(): Flow<List<NewsArticle>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNewsArticle(article: NewsArticle)

  // Culture
  @Query("SELECT * FROM culture_items ORDER BY id ASC")
  fun getAllCultureItems(): Flow<List<CultureItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCultureItem(item: CultureItem)
}
