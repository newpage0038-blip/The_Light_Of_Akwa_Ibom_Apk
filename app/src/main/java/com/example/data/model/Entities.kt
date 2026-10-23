package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discussion_posts")
data class DiscussionPost(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val authorName: String,
  val authorLga: String,
  val title: String,
  val content: String,
  val category: String, // e.g. "Unity", "Youth Empowerment", "Cultural Heritage", "Tech & Innovation", "General"
  val timestamp: Long = System.currentTimeMillis(),
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val isApproved: Boolean = true,
  val isLikedByMe: Boolean = false
)

@Entity(tableName = "discussion_comments")
data class DiscussionComment(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val postId: Long,
  val authorName: String,
  val content: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "community_polls")
data class CommunityPoll(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val question: String,
  val option1: String,
  val votes1: Int = 0,
  val option2: String,
  val votes2: Int = 0,
  val option3: String,
  val votes3: Int = 0,
  val userVotedOption: Int = 0 // 0 = not voted, 1, 2, 3
)

@Entity(tableName = "business_listings")
data class BusinessListing(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val businessName: String,
  val category: String, // "Catering & Delicacies", "Fashion & Crafts", "Tech & Digital", "Agriculture & Trade", "Services"
  val description: String,
  val lga: String,
  val address: String,
  val phoneNumber: String,
  val whatsapp: String,
  val ownerName: String,
  val rating: Float = 4.8f,
  val isApproved: Boolean = true
)

@Entity(tableName = "opportunities")
data class OpportunityItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "Job Vacancy", "Scholarship", "Training & Skills", "Business Grant", "Auditions"
  val organization: String,
  val location: String,
  val deadline: String,
  val description: String,
  val contactOrUrl: String,
  val isApproved: Boolean = true
)

@Entity(tableName = "community_events")
data class CommunityEvent(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "Cultural Celebration", "Youth Summit", "Town Hall", "Networking", "Festival"
  val date: String,
  val time: String,
  val venue: String,
  val lga: String,
  val description: String,
  val attendeesCount: Int = 24,
  val isRegistered: Boolean = false,
  val isBookmarked: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: Int = 1,
  val fullName: String = "Anietie Udoh",
  val lga: String = "Uyo",
  val occupation: String = "Digital Creator & Community Volunteer",
  val bio: String = "Proud Akwa Ibomite passionate about community empowerment, unity, and tech innovation. Brighter we shine!",
  val memberId: String = "AKWA-LIGHT-2026-4821",
  val isRegistered: Boolean = true,
  val joinedDate: String = "October 2026",
  val chantsCount: Int = 12
)

@Entity(tableName = "support_messages")
data class SupportMessage(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val emailOrPhone: String,
  val subject: String,
  val message: String,
  val type: String, // "Inquiry", "Suggestion", "Report Inappropriate Content"
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "news_articles")
data class NewsArticle(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "Development", "Education & Tech", "Culture & Sports", "State Milestones"
  val date: String,
  val summary: String,
  val fullContent: String,
  val readTime: String = "3 min read",
  val isFeatured: Boolean = false
)

@Entity(tableName = "culture_items")
data class CultureItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val type: String, // "Language", "Delicacy", "Dance", "Festival", "Landmark"
  val subtitle: String,
  val description: String,
  val details: String,
  val localPhrase: String = "",
  val meaning: String = ""
)

@Entity(tableName = "community_announcements")
data class CommunityAnnouncement(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val content: String,
  val date: String,
  val tag: String, // "Priority", "Event", "Update"
  val isActive: Boolean = true
)
