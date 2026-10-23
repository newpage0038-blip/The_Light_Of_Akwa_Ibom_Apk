package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.InitialData
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
import kotlinx.coroutines.launch

@Database(
  entities = [
    DiscussionPost::class,
    DiscussionComment::class,
    CommunityPoll::class,
    BusinessListing::class,
    OpportunityItem::class,
    CommunityEvent::class,
    UserProfile::class,
    SupportMessage::class,
    NewsArticle::class,
    CultureItem::class,
    CommunityAnnouncement::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun appDao(): AppDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "the_light_of_akwa_ibom.db"
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }
  }

  private class DatabaseCallback(
    private val scope: CoroutineScope
  ) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      INSTANCE?.let { database ->
        scope.launch(Dispatchers.IO) {
          populateInitialDatabase(database.appDao())
        }
      }
    }
  }
}

suspend fun populateInitialDatabase(dao: AppDao) {
  // Announcements
  InitialData.ANNOUNCEMENTS.forEach { dao.insertAnnouncement(it) }

  // News
  InitialData.NEWS.forEach { dao.insertNewsArticle(it) }

  // Culture
  InitialData.CULTURE.forEach { dao.insertCultureItem(it) }

  // Opportunities
  InitialData.OPPORTUNITIES.forEach { dao.insertOpportunity(it) }

  // Businesses
  InitialData.BUSINESSES.forEach { dao.insertBusiness(it) }

  // Events
  InitialData.EVENTS.forEach { dao.insertEvent(it) }

  // Discussions
  InitialData.DISCUSSIONS.forEach { dao.insertDiscussion(it) }

  // Polls
  InitialData.POLLS.forEach { dao.insertPoll(it) }

  // User Profile
  dao.saveUserProfile(InitialData.DEFAULT_PROFILE)
}
