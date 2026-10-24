package com.example

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BusinessScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CultureScreen
import com.example.ui.screens.EntertainmentScreen
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.FootballScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MembershipScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.OpportunitiesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RadiantSunOrange

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize Database & Repositories
    val database = AppDatabase.getDatabase(applicationContext, kotlinx.coroutines.GlobalScope)
    val repository = AkwaIbomRepository(database.appDao(), kotlinx.coroutines.GlobalScope)
    val chatRepository = com.example.data.repository.ChatRepository(applicationContext)
    val factory = MainViewModelFactory(repository, chatRepository)
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

  // Real-time Communication State
  val isAuthDialogOpen by viewModel.isAuthDialogOpen.collectAsStateWithLifecycle()
  val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()
  val currentUserName by viewModel.currentUserName.collectAsStateWithLifecycle()
  val chatUsersList by viewModel.chatUsersList.collectAsStateWithLifecycle()
  val groupMessages by viewModel.groupMessages.collectAsStateWithLifecycle()
  val directMessages by viewModel.directMessages.collectAsStateWithLifecycle()
  val selectedChatUser by viewModel.selectedChatUser.collectAsStateWithLifecycle()
  val incomingCall by viewModel.incomingCall.collectAsStateWithLifecycle()
  val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  val context = LocalContext.current
  var lastBackPressTime by remember { mutableLongStateOf(0L) }

  LaunchedEffect(feedbackMessage) {
    feedbackMessage?.let {
      snackbarHostState.showSnackbar(it)
    }
  }

  // Handle system back navigation (Requirements 4, 5, 6, 7)
  BackHandler(enabled = true) {
    when {
      // 1. Initial Welcome screen: close app if back is pressed
      currentScreen == Screen.WELCOME -> {
        (context as? Activity)?.finish()
      }
      // 2. Can navigate back (sub-states such as direct chat, comments dialog, or secondary screens)
      viewModel.canNavigateBack() -> {
        viewModel.navigateBack()
      }
      // 3. User is on Main Dashboard (Screen.HOME) - double-back to exit pattern
      currentScreen == Screen.HOME -> {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < 2000L) {
          (context as? Activity)?.finish()
        } else {
          lastBackPressTime = currentTime
          Toast.makeText(context, "Press Back again to exit", Toast.LENGTH_SHORT).show()
        }
      }
      // 4. Fallback: navigate home to dashboard
      else -> {
        viewModel.navigateHome()
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (currentScreen != Screen.WELCOME) {
        AppTopBar(
          currentScreen = currentScreen,
          isAdminMode = isAdminMode,
          onNavigateBack = { viewModel.navigateBack() },
          onNavigateHome = { viewModel.navigateHome() },
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

        Screen.ENTERTAINMENT -> {
          EntertainmentScreen(
            repository = viewModel.entertainmentRepository,
            currentUserName = currentUserName,
            onShowSnackbar = { viewModel.showFeedback(it) }
          )
        }

        Screen.FOOTBALL -> {
          FootballScreen(
            repository = viewModel.footballRepository,
            currentUserName = currentUserName,
            onShowSnackbar = { viewModel.showFeedback(it) }
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
            },
            currentUserId = currentUserId,
            currentUserName = currentUserName,
            usersList = chatUsersList,
            groupMessages = groupMessages,
            directMessages = directMessages,
            incomingCall = incomingCall,
            activeCall = activeCall,
            activeChatUser = selectedChatUser,
            onSelectChatUser = { viewModel.selectChatUser(it) },
            onSendGroupMessage = { text, type, media -> viewModel.sendGroupMessage(text, type, media) },
            onSendDirectMessage = { rId, rName, text, type, media -> viewModel.sendDirectMessage(rId, rName, text, type, media) },
            onInitiateCall = { user, type -> viewModel.initiateCall(user, type) },
            onAnswerCall = { call -> viewModel.answerCall(call) },
            onRejectCall = { call -> viewModel.rejectCall(call) },
            onEndCall = { call -> viewModel.endCall(call) },
            onOpenSignIn = { viewModel.openSignIn() }
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

        Screen.SETTINGS -> {
          SettingsScreen(
            userProfile = userProfile,
            currentUserId = currentUserId,
            currentUserName = currentUserName,
            onNavigate = { screen -> viewModel.navigateTo(screen) },
            onOpenSignIn = { viewModel.openSignIn() },
            onSignOut = { viewModel.signOut() },
            onShowFeedback = { viewModel.showFeedback(it) }
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

      // Global Auth Dialog
      if (isAuthDialogOpen) {
        Dialog(
          onDismissRequest = { viewModel.closeSignIn() },
          properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
          AuthScreen(
            onAuthSuccess = { viewModel.handleAuthSuccess() },
            onDismiss = { viewModel.closeSignIn() }
          )
        }
      }
    }
  }
}
