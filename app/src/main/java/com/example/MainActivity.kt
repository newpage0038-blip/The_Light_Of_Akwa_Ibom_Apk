package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.repository.AkwaIbomRepository
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.Screen
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppTopBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.BusinessScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CultureScreen
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MembershipScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.OpportunitiesScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RadiantSunOrange

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize Database & Repository
    val database = AppDatabase.getDatabase(applicationContext, kotlinx.coroutines.GlobalScope)
    val repository = AkwaIbomRepository(database.appDao(), kotlinx.coroutines.GlobalScope)
    val factory = MainViewModelFactory(repository)
    val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

    setContent {
      MyApplicationTheme {
        AkwaIbomApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun AkwaIbomApp(viewModel: MainViewModel) {
  val currentScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
  val announcements by viewModel.announcements.collectAsStateWithLifecycle()
  val approvedDiscussions by viewModel.approvedDiscussions.collectAsStateWithLifecycle()
  val allDiscussions by viewModel.allDiscussions.collectAsStateWithLifecycle()
  val polls by viewModel.polls.collectAsStateWithLifecycle()
  val approvedBusinesses by viewModel.approvedBusinesses.collectAsStateWithLifecycle()
  val allBusinesses by viewModel.allBusinesses.collectAsStateWithLifecycle()
  val approvedOpportunities by viewModel.approvedOpportunities.collectAsStateWithLifecycle()
  val allOpportunities by viewModel.allOpportunities.collectAsStateWithLifecycle()
  val events by viewModel.events.collectAsStateWithLifecycle()
  val news by viewModel.news.collectAsStateWithLifecycle()
  val culture by viewModel.culture.collectAsStateWithLifecycle()
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val supportMessages by viewModel.supportMessages.collectAsStateWithLifecycle()

  val isChanting by viewModel.isChanting.collectAsStateWithLifecycle()
  val greetingMessage by viewModel.greetingMessage.collectAsStateWithLifecycle()
  val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()
  val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()

  val activePostForComments by viewModel.activePostForComments.collectAsStateWithLifecycle()
  val commentsForActivePost by viewModel.commentsForActivePost.collectAsStateWithLifecycle()
  val selectedNewsArticle by viewModel.selectedNewsArticle.collectAsStateWithLifecycle()
  val selectedCultureItem by viewModel.selectedCultureItem.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(feedbackMessage) {
    feedbackMessage?.let {
      snackbarHostState.showSnackbar(it)
    }
  }

  // Handle system back navigation
  BackHandler(enabled = currentScreen != Screen.WELCOME && currentScreen != Screen.HOME) {
    viewModel.navigateBack()
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (currentScreen != Screen.WELCOME) {
        AppTopBar(
          currentScreen = currentScreen,
          isAdminMode = isAdminMode,
          onNavigateBack = { viewModel.navigateBack() },
          onToggleAdmin = {
            viewModel.toggleAdminMode()
            if (!isAdminMode) {
              viewModel.navigateTo(Screen.ADMIN)
            } else {
              viewModel.navigateTo(Screen.HOME)
            }
          },
          onChantClicked = { viewModel.chantGreeting() }
        )
      }
    },
    bottomBar = {
      if (currentScreen != Screen.WELCOME) {
        AppBottomBar(
          currentScreen = currentScreen,
          onNavigate = { screen -> viewModel.navigateTo(screen) }
        )
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        Screen.WELCOME -> {
          WelcomeScreen(
            onGetStarted = { viewModel.navigateTo(Screen.HOME) },
            onChantClicked = { viewModel.chantGreeting() },
            chantsCount = userProfile?.chantsCount ?: 12,
            isChanting = isChanting
          )
        }

        Screen.HOME -> {
          HomeScreen(
            announcements = announcements,
            news = news,
            events = events,
            chantsCount = userProfile?.chantsCount ?: 12,
            isChanting = isChanting,
            onChantClicked = { viewModel.chantGreeting() },
            onNavigate = { screen -> viewModel.navigateTo(screen) },
            onSelectNews = { article ->
              viewModel.selectNewsArticle(article)
              viewModel.navigateTo(Screen.NEWS)
            }
          )
        }

        Screen.ABOUT -> {
          AboutScreen()
        }

        Screen.COMMUNITY -> {
          CommunityScreen(
            discussions = if (isAdminMode) allDiscussions else approvedDiscussions,
            polls = polls,
            activePostForComments = activePostForComments,
            comments = commentsForActivePost,
            onToggleLike = { postId -> viewModel.togglePostLike(postId) },
            onOpenComments = { post -> viewModel.openComments(post) },
            onCloseComments = { viewModel.closeComments() },
            onAddComment = { postId, text -> viewModel.addComment(postId, text) },
            onVotePoll = { pollId, opt -> viewModel.votePoll(pollId, opt) },
            onCreateDiscussion = { title, content, cat ->
              viewModel.createDiscussion(title, content, cat)
            }
          )
        }

        Screen.NEWS -> {
          NewsScreen(
            news = news,
            selectedArticle = selectedNewsArticle,
            onSelectArticle = { viewModel.selectNewsArticle(it) }
          )
        }

        Screen.CULTURE -> {
          CultureScreen(
            cultureItems = culture,
            selectedItem = selectedCultureItem,
            onSelectItem = { viewModel.selectCultureItem(it) },
            onShowFeedback = { viewModel.showFeedback(it) }
          )
        }

        Screen.OPPORTUNITIES -> {
          OpportunitiesScreen(
            opportunities = if (isAdminMode) allOpportunities else approvedOpportunities,
            onSubmitOpportunity = { title, cat, org, loc, ddl, desc, contact ->
              viewModel.submitOpportunity(title, cat, org, loc, ddl, desc, contact)
            },
            onShowFeedback = { viewModel.showFeedback(it) }
          )
        }

        Screen.BUSINESS -> {
          BusinessScreen(
            businesses = if (isAdminMode) allBusinesses else approvedBusinesses,
            onRegisterBusiness = { name, cat, desc, lga, addr, phone, wa, owner ->
              viewModel.registerBusiness(name, cat, desc, lga, addr, phone, wa, owner)
            },
            onShowFeedback = { viewModel.showFeedback(it) }
          )
        }

        Screen.EVENTS -> {
          EventsScreen(
            events = events,
            onToggleRegister = { viewModel.toggleEventRegistration(it) },
            onToggleBookmark = { viewModel.toggleEventBookmark(it) },
            onSubmitEvent = { title, cat, date, time, venue, lga, desc ->
              viewModel.submitEvent(title, cat, date, time, venue, lga, desc)
            }
          )
        }

        Screen.MEMBERSHIP -> {
          MembershipScreen(
            profile = userProfile,
            onSaveProfile = { name, lga, occ, bio ->
              viewModel.saveProfile(name, lga, occ, bio)
            }
          )
        }

        Screen.CONTACT -> {
          ContactScreen(
            onSendMessage = { name, contact, subj, msg, type ->
              viewModel.sendSupportMessage(name, contact, subj, msg, type)
            }
          )
        }

        Screen.ADMIN -> {
          AdminScreen(
            announcements = announcements,
            discussions = allDiscussions,
            businesses = allBusinesses,
            opportunities = allOpportunities,
            supportMessages = supportMessages,
            onAddAnnouncement = { t, c, d, tg -> viewModel.addAnnouncement(t, c, d, tg) },
            onDeleteAnnouncement = { viewModel.deleteAnnouncement(it) },
            onSetDiscussionApproved = { id, app -> viewModel.setDiscussionApproved(id, app) },
            onDeleteDiscussion = { viewModel.deleteDiscussion(it) },
            onSetBusinessApproved = { id, app -> viewModel.setBusinessApproved(id, app) },
            onDeleteBusiness = { viewModel.deleteBusiness(it) },
            onSetOpportunityApproved = { id, app -> viewModel.setOpportunityApproved(id, app) },
            onDeleteOpportunity = { viewModel.deleteOpportunity(it) },
            onResetData = { viewModel.resetData() }
          )
        }
      }

      // Celebratory Chant Banner Overlay
      AnimatedVisibility(
        visible = greetingMessage != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 16.dp, start = 20.dp, end = 20.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadiantSunOrange)
            .padding(16.dp)
        ) {
          Text(
            text = greetingMessage ?: "",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              fontSize = 16.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}
