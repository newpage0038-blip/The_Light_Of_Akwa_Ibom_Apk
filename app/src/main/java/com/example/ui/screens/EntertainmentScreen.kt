package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ComedyItem
import com.example.data.model.CultureQuizQuestion
import com.example.data.model.DanceEntry
import com.example.data.model.LightTvVideo
import com.example.data.model.MusicArtist
import com.example.data.model.TalentCategory
import com.example.data.model.TalentProfile
import com.example.data.repository.EntertainmentRepository
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

enum class EntertainmentSection(val title: String) {
  MUSIC("Music Zone"),
  COMEDY("Comedy Corner"),
  DANCE("Dance Challenge"),
  TALENT("Light of Talent"),
  QUIZ("Culture & Quiz"),
  LIGHT_TV("Light TV")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntertainmentScreen(
  repository: EntertainmentRepository,
  currentUserName: String,
  onShowSnackbar: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedSection by remember { mutableStateOf(EntertainmentSection.MUSIC) }

  // Observables from repository
  val featuredArtist by repository.featuredArtist.collectAsStateWithLifecycle()
  val artistsList by repository.artistsList.collectAsStateWithLifecycle()
  val comedySkits by repository.comedySkits.collectAsStateWithLifecycle()
  val danceChallenge by repository.danceChallenge.collectAsStateWithLifecycle()
  val talentOfTheWeek by repository.talentOfTheWeek.collectAsStateWithLifecycle()
  val talentsList by repository.talentsList.collectAsStateWithLifecycle()
  val leaderboard by repository.leaderboard.collectAsStateWithLifecycle()
  val lightTvVideos by repository.lightTvVideos.collectAsStateWithLifecycle()

  // Dialog states
  var showMusicSubmitDialog by remember { mutableStateOf(false) }
  var showComedySubmitDialog by remember { mutableStateOf(false) }
  var showDanceSubmitDialog by remember { mutableStateOf(false) }
  var showTalentNominateDialog by remember { mutableStateOf(false) }
  var activeComedyForComments by remember { mutableStateOf<ComedyItem?>(null) }
  var reportingItemTitle by remember { mutableStateOf<String?>(null) }

  // Helper to open links safely
  val openExternalLink: (String) -> Unit = { url ->
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Unable to open link. Please verify internet connection.", Toast.LENGTH_SHORT).show()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF9FAFB))
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            listOf(AkwaGreenDark, AkwaGreenPrimary)
          )
        )
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.MusicNote,
              contentDescription = null,
              tint = RadiantSunAmber,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ENTERTAINMENT CENTRE",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              ),
              color = Color.White
            )
          }
          Text(
            text = "Music • Comedy • Dance • Talents • Quiz • Light TV",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f)
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(RadiantSunOrange)
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "BRIGHTER WE SHINE!",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = Color.White
          )
        }
      }
    }

    // Scrollable Tab Bar
    ScrollableTabRow(
      selectedTabIndex = selectedSection.ordinal,
      containerColor = Color.White,
      contentColor = AkwaGreenPrimary,
      edgePadding = 12.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection.ordinal]),
          height = 3.dp,
          color = AkwaGreenPrimary
        )
      }
    ) {
      EntertainmentSection.values().forEach { section ->
        val selected = selectedSection == section
        Tab(
          selected = selected,
          onClick = { selectedSection = section },
          text = {
            Text(
              text = section.title,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
              ),
              color = if (selected) AkwaGreenPrimary else Color(0xFF6B7280)
            )
          },
          modifier = Modifier.testTag("tab_${section.name.lowercase()}")
        )
      }
    }

    // Content based on selected section
    when (selectedSection) {
      EntertainmentSection.MUSIC -> {
        MusicZoneContent(
          featuredArtist = featuredArtist,
          artists = artistsList,
          onOpenLink = openExternalLink,
          onSubmitMusic = { showMusicSubmitDialog = true }
        )
      }
      EntertainmentSection.COMEDY -> {
        ComedyCornerContent(
          comedySkits = comedySkits,
          onToggleLike = { repository.toggleComedyLike(it) },
          onOpenComments = { activeComedyForComments = it },
          onReport = { reportingItemTitle = it.title },
          onOpenLink = openExternalLink,
          onSubmitComedy = { showComedySubmitDialog = true }
        )
      }
      EntertainmentSection.DANCE -> {
        DanceChallengeContent(
          challenge = danceChallenge,
          onVote = { repository.voteDanceEntry(it) },
          onOpenLink = openExternalLink,
          onSubmitEntry = { showDanceSubmitDialog = true }
        )
      }
      EntertainmentSection.TALENT -> {
        LightOfTalentContent(
          talentOfTheWeek = talentOfTheWeek,
          talents = talentsList,
          onOpenLink = openExternalLink,
          onNominate = { showTalentNominateDialog = true }
        )
      }
      EntertainmentSection.QUIZ -> {
        CultureAndQuizContent(
          questions = repository.quizQuestions,
          leaderboard = leaderboard,
          currentUserName = currentUserName,
          onRecordScore = { name, score, lga ->
            repository.recordQuizScore(name, score, lga)
            onShowSnackbar("Congratulations! Quiz score recorded.")
          }
        )
      }
      EntertainmentSection.LIGHT_TV -> {
        LightTvContent(
          videos = lightTvVideos,
          onOpenLink = openExternalLink
        )
      }
    }
  }

  // Dialogs
  if (showMusicSubmitDialog) {
    SubmitMusicDialog(
      onDismiss = { showMusicSubmitDialog = false },
      onSubmit = { name, title, genre, link, bio, contact ->
        repository.submitSong(name, title, genre, link, bio, contact)
        showMusicSubmitDialog = false
        onShowSnackbar("Song submitted for review! Our cultural curators will evaluate it.")
      }
    )
  }

  if (showComedySubmitDialog) {
    SubmitComedyDialog(
      onDismiss = { showComedySubmitDialog = false },
      onSubmit = { title, creator, url, desc ->
        repository.submitComedy(title, creator, url, desc)
        showComedySubmitDialog = false
        onShowSnackbar("Comedy skit submitted for review!")
      }
    )
  }

  if (showDanceSubmitDialog) {
    SubmitDanceDialog(
      onDismiss = { showDanceSubmitDialog = false },
      onSubmit = { dancer, title, lga, url, desc ->
        repository.submitDanceEntry(dancer, title, lga, url, desc)
        showDanceSubmitDialog = false
        onShowSnackbar("Dance entry submitted for Brighter We Shine Challenge!")
      }
    )
  }

  if (showTalentNominateDialog) {
    NominateTalentDialog(
      nominatorName = currentUserName,
      onDismiss = { showTalentNominateDialog = false },
      onSubmit = { nominee, cat, lga, reason ->
        repository.nominateTalent(nominee, cat, lga, reason, currentUserName)
        showTalentNominateDialog = false
        onShowSnackbar("Talent nominated! Thank you for shining the light on our people.")
      }
    )
  }

  activeComedyForComments?.let { comedy ->
    ComedyCommentsDialog(
      comedy = comedy,
      currentUserName = currentUserName,
      onDismiss = { activeComedyForComments = null },
      onAddComment = { text ->
        repository.addComedyComment(comedy.id, text, currentUserName)
        // update local active item view
        val updated = comedy.copy(
          comments = comedy.comments + com.example.data.model.ComedyComment(
            id = "cc_${System.currentTimeMillis()}",
            authorName = currentUserName,
            text = text,
            timeAgo = "Just now"
          )
        )
        activeComedyForComments = updated
      }
    )
  }

  reportingItemTitle?.let { title ->
    ReportContentDialog(
      itemTitle = title,
      onDismiss = { reportingItemTitle = null },
      onReportConfirmed = {
        reportingItemTitle = null
        onShowSnackbar("Thank you. Report received for community moderation review.")
      }
    )
  }
}

// -------------------------------------------------------------
// MUSIC ZONE COMPOSABLE
// -------------------------------------------------------------
@Composable
fun MusicZoneContent(
  featuredArtist: MusicArtist,
  artists: List<MusicArtist>,
  onOpenLink: (String) -> Unit,
  onSubmitMusic: () -> Unit
) {
  var selectedGenreFilter by remember { mutableStateOf("All") }
  val genres = listOf("All", "Afrobeats", "Gospel", "Highlife", "Traditional")

  val filteredArtists = remember(selectedGenreFilter, artists) {
    if (selectedGenreFilter == "All") artists
    else artists.filter { it.genre.contains(selectedGenreFilter, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Featured Artist of the Week Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .testTag("featured_artist_card"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(RadiantSunOrange)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "FEATURED ARTIST OF THE WEEK",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
            Text(
              text = featuredArtist.originLga,
              style = MaterialTheme.typography.labelSmall,
              color = AkwaGreenDark
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(listOf(AkwaGreenPrimary, RadiantSunOrange))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = featuredArtist.stageName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = AkwaGreenDark
              )
              Text(
                text = featuredArtist.genre,
                style = MaterialTheme.typography.bodyMedium,
                color = RadiantSunOrange,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = featuredArtist.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF374151)
          )

          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Signature Tracks: " + featuredArtist.topTracks.joinToString(", "),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF4B5563)
          )

          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = { onOpenLink(featuredArtist.officialLink) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("play_featured_music_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Listen on Official Channels (YouTube / Stream)")
          }
        }
      }
    }

    // Submission banner for artists
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Are You an Akwa Ibom Musician?",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF7C2D12)
            )
            Text(
              text = "Submit your music profile and official streaming links for community spotlight.",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF9A3412)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = onSubmitMusic,
            colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("submit_music_button")
          ) {
            Text("Submit Music")
          }
        }
      }
    }

    // Genre Filter Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        genres.forEach { genre ->
          FilterChip(
            selected = selectedGenreFilter == genre,
            onClick = { selectedGenreFilter = genre },
            label = { Text(genre, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AkwaGreenLight,
              selectedLabelColor = AkwaGreenDark
            )
          )
        }
      }
    }

    // Musicians Directory
    item {
      Text(
        text = "DISCOVER AKWA IBOM ARTISTS",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = AkwaGreenDark
      )
    }

    items(filteredArtists) { artist ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("artist_card_${artist.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(AkwaGreenLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Headphones,
                  contentDescription = null,
                  tint = AkwaGreenDark,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = artist.stageName,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF111827)
                )
                Text(
                  text = "${artist.genre} • ${artist.originLga}",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFF6B7280)
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFFEF3C7))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = artist.badgeText,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                color = Color(0xFFB45309)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = artist.bio,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4B5563)
          )

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Tracks: ${artist.topTracks.take(2).joinToString(", ")}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF6B7280),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
              onClick = { onOpenLink(artist.officialLink) },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.testTag("listen_btn_${artist.id}")
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AkwaGreenPrimary
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Listen", color = AkwaGreenPrimary, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Disclaimer
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF3F4F6))
          .padding(12.dp)
      ) {
        Text(
          text = "Notice: All musical content and links point exclusively to official artist channels and authorized streaming services. No unauthorized copyrighted mp3 files are hosted directly.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6B7280),
          fontSize = 11.sp
        )
      }
    }
  }
}

// -------------------------------------------------------------
// COMEDY CORNER COMPOSABLE
// -------------------------------------------------------------
@Composable
fun ComedyCornerContent(
  comedySkits: List<ComedyItem>,
  onToggleLike: (String) -> Unit,
  onOpenComments: (ComedyItem) -> Unit,
  onReport: (ComedyItem) -> Unit,
  onOpenLink: (String) -> Unit,
  onSubmitComedy: () -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "COMEDY & SKITS CORNER",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
              color = Color(0xFF991B1B)
            )
            Text(
              text = "Laugh out loud with top comedy skits celebrating Akwa Ibom humor and Nigerian creativity.",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF7F1D1D)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = onSubmitComedy,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("submit_comedy_btn")
          ) {
            Text("Submit Skit")
          }
        }
      }
    }

    items(comedySkits) { skit ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("comedy_card_${skit.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = skit.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827)
              )
              Text(
                text = "${skit.creator} • ${skit.duration}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280)
              )
            }

            IconButton(
              onClick = { onReport(skit) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = "Report content",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = skit.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF374151)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Video Play button
          Button(
            onClick = { onOpenLink(skit.videoUrl) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F2937)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("watch_skit_${skit.id}")
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Watch Skit on Official Source")
          }

          Spacer(modifier = Modifier.height(8.dp))
          HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.5.dp)
          Spacer(modifier = Modifier.height(8.dp))

          // Social interactions: Likes, Comments, Share
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier
                .clickable { onToggleLike(skit.id) }
                .padding(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (skit.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like",
                tint = if (skit.isLikedByMe) Color.Red else Color(0xFF6B7280),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${skit.likesCount} Likes",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF4B5563)
              )
            }

            Row(
              modifier = Modifier
                .clickable { onOpenComments(skit) }
                .padding(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = "Comments",
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${skit.comments.size} Comments",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF4B5563)
              )
            }

            Row(
              modifier = Modifier
                .clickable { onOpenLink(skit.videoUrl) }
                .padding(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Share",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF4B5563)
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// DANCE CHALLENGE COMPOSABLE
// -------------------------------------------------------------
@Composable
fun DanceChallengeContent(
  challenge: com.example.data.model.DanceChallenge,
  onVote: (String) -> Unit,
  onOpenLink: (String) -> Unit,
  onSubmitEntry: () -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Challenge Spotlight Hero Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .testTag("dance_challenge_card"),
        colors = CardDefaults.cardColors(containerColor = RadiantSunOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "OFFICIAL DANCE CHALLENGE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
            Text(
              text = challenge.hashtag,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = challenge.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = Color.White
          )
          Text(
            text = challenge.theme,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White.copy(alpha = 0.9f)
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = challenge.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.95f)
          )

          Spacer(modifier = Modifier.height(12.dp))
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.Black.copy(alpha = 0.15f))
              .padding(10.dp)
          ) {
            Text(
              text = "Guidelines to Join:",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            challenge.guidelines.forEach { rule ->
              Text(
                text = "• $rule",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = onSubmitEntry,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("participate_dance_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = "Participate in Dance Challenge",
              color = RadiantSunOrange,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Community Entries & Voting
    item {
      Text(
        text = "VOTE FOR COMMUNITY DANCE ENTRIES",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = AkwaGreenDark
      )
    }

    items(challenge.entries) { entry ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("dance_entry_${entry.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827)
              )
              Text(
                text = "${entry.dancerOrCrew} • ${entry.lga}",
                style = MaterialTheme.typography.labelSmall,
                color = AkwaGreenPrimary,
                fontWeight = FontWeight.SemiBold
              )
            }

            // Vote button
            Button(
              onClick = { onVote(entry.id) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (entry.isVotedByMe) AkwaGreenPrimary else RadiantSunOrange
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("vote_btn_${entry.id}")
            ) {
              Icon(
                imageVector = if (entry.isVotedByMe) Icons.Default.CheckCircle else Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.White
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (entry.isVotedByMe) "Voted (${entry.votesCount})" else "Vote (${entry.votesCount})",
                fontSize = 12.sp,
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          OutlinedButton(
            onClick = { onOpenLink(entry.videoUrl) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = AkwaGreenPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Watch Dance Video", color = AkwaGreenPrimary)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// LIGHT OF TALENT COMPOSABLE
// -------------------------------------------------------------
@Composable
fun LightOfTalentContent(
  talentOfTheWeek: TalentProfile,
  talents: List<TalentProfile>,
  onOpenLink: (String) -> Unit,
  onNominate: () -> Unit
) {
  var selectedCategoryFilter by remember { mutableStateOf<TalentCategory?>(null) }

  val filteredTalents = remember(selectedCategoryFilter, talents) {
    if (selectedCategoryFilter == null) talents
    else talents.filter { it.category == selectedCategoryFilter }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Talent of the Week Spotlight
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .testTag("talent_of_the_week_card"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AkwaGreenPrimary)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "TALENT OF THE WEEK",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
            Text(
              text = talentOfTheWeek.originLga,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = AkwaGreenDark
            )
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "${talentOfTheWeek.fullName} (\"${talentOfTheWeek.stageName}\")",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = AkwaGreenDark
          )
          Text(
            text = talentOfTheWeek.category.displayName,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = RadiantSunOrange
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = talentOfTheWeek.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF374151)
          )

          Spacer(modifier = Modifier.height(10.dp))
          talentOfTheWeek.highlights.forEach { hl ->
            Text(
              text = "⭐ $hl",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF4B5563)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = { onOpenLink(talentOfTheWeek.portfolioUrl) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("View Portfolio & Performances")
          }
        }
      }
    }

    // Nomination banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Know an Akwa Ibom Talent?",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
            Text(
              text = "Nominate singers, actors, poets, and creatives for our weekly showcase.",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF065F46)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = onNominate,
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenDark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("nominate_talent_btn")
          ) {
            Text("Nominate")
          }
        }
      }
    }

    // Category Filter
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedCategoryFilter == null,
            onClick = { selectedCategoryFilter = null },
            label = { Text("All Talents") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AkwaGreenLight,
              selectedLabelColor = AkwaGreenDark
            )
          )
        }
        items(TalentCategory.values()) { cat ->
          FilterChip(
            selected = selectedCategoryFilter == cat,
            onClick = { selectedCategoryFilter = cat },
            label = { Text(cat.displayName) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AkwaGreenLight,
              selectedLabelColor = AkwaGreenDark
            )
          )
        }
      }
    }

    items(filteredTalents) { talent ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "${talent.fullName} (${talent.stageName})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827)
              )
              Text(
                text = "${talent.category.displayName} • ${talent.originLga}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = talent.bio,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF374151)
          )

          Spacer(modifier = Modifier.height(10.dp))
          OutlinedButton(
            onClick = { onOpenLink(talent.portfolioUrl) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("View Work & Clips", color = AkwaGreenPrimary)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// CULTURE AND QUIZ COMPOSABLE
// -------------------------------------------------------------
@Composable
fun CultureAndQuizContent(
  questions: List<CultureQuizQuestion>,
  leaderboard: List<com.example.data.model.QuizLeaderboardEntry>,
  currentUserName: String,
  onRecordScore: (name: String, score: Int, lga: String) -> Unit
) {
  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
  var isAnswerSubmitted by remember { mutableStateOf(false) }
  var totalScore by remember { mutableIntStateOf(0) }
  var isQuizCompleted by remember { mutableStateOf(false) }
  var showLeaderboard by remember { mutableStateOf(false) }

  val question = questions.getOrNull(currentQuestionIndex)

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Mode switch: Active Quiz vs Leaderboard
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (showLeaderboard) "AKWA IBOM QUIZ LEADERBOARD" else "CULTURE & KNOWLEDGE CHALLENGE",
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          ),
          color = AkwaGreenDark
        )
        TextButton(
          onClick = { showLeaderboard = !showLeaderboard }
        ) {
          Text(
            text = if (showLeaderboard) "Play Quiz" else "View Leaderboard",
            color = RadiantSunOrange,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    if (showLeaderboard) {
      items(leaderboard) { entry ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = entry.playerName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827)
              )
              Text(
                text = "${entry.badge} • ${entry.lga}",
                style = MaterialTheme.typography.labelSmall,
                color = AkwaGreenPrimary
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AkwaGreenLight)
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "${entry.score} / ${entry.totalQuestions} Pts",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AkwaGreenDark
              )
            }
          }
        }
      }
    } else {
      if (!isQuizCompleted && question != null) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("quiz_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFEF3C7))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = question.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFB45309)
                  )
                }
                Text(
                  text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFF6B7280)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  lineHeight = 22.sp
                ),
                color = Color(0xFF1F2937)
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Options
              question.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrect = index == question.correctOptionIndex

                val backgroundColor = when {
                  isAnswerSubmitted && isCorrect -> Color(0xFFDCFCE7)
                  isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                  isSelected -> AkwaGreenLight
                  else -> Color(0xFFF9FAFB)
                }

                val borderColor = when {
                  isAnswerSubmitted && isCorrect -> AkwaGreenPrimary
                  isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444)
                  isSelected -> AkwaGreenPrimary
                  else -> Color(0xFFE5E7EB)
                }

                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .background(backgroundColor)
                    .clickable(enabled = !isAnswerSubmitted) {
                      selectedOptionIndex = index
                    }
                    .padding(14.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "${('A'.code + index).toChar()}.",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      color = AkwaGreenDark
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = optionText,
                      style = MaterialTheme.typography.bodyMedium,
                      color = Color(0xFF1F2937)
                    )
                  }
                }
              }

              // Answer explanation
              if (isAnswerSubmitted) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF6FF))
                    .padding(12.dp)
                ) {
                  Column {
                    Text(
                      text = "Historical / Cultural Note:",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = Color(0xFF1E40AF)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = question.explanation,
                      style = MaterialTheme.typography.bodySmall,
                      color = Color(0xFF1E3A8A)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              if (!isAnswerSubmitted) {
                Button(
                  onClick = {
                    if (selectedOptionIndex != null) {
                      isAnswerSubmitted = true
                      if (selectedOptionIndex == question.correctOptionIndex) {
                        totalScore += 1
                      }
                    }
                  },
                  enabled = selectedOptionIndex != null,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("submit_quiz_answer_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Text("Confirm Answer")
                }
              } else {
                Button(
                  onClick = {
                    if (currentQuestionIndex < questions.size - 1) {
                      currentQuestionIndex += 1
                      selectedOptionIndex = null
                      isAnswerSubmitted = false
                    } else {
                      isQuizCompleted = true
                      onRecordScore(currentUserName, totalScore, "Uyo")
                    }
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("next_quiz_question_btn"),
                  colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Text(if (currentQuestionIndex < questions.size - 1) "Next Question" else "See Final Results")
                }
              }
            }
          }
        }
      } else {
        // Quiz Completed Screen
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .clip(CircleShape)
                  .background(RadiantSunOrange),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Quiz,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(40.dp)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "Challenge Completed!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = AkwaGreenDark
              )
              Text(
                text = "You scored $totalScore out of ${questions.size}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = RadiantSunOrange
              )

              Spacer(modifier = Modifier.height(8.dp))
              val badge = when {
                totalScore >= 7 -> "🏆 Akwa Ibom Scholar"
                totalScore >= 5 -> "🌟 Dakkada Master"
                else -> "🌱 Heritage Learner"
              }
              Text(
                text = "Your Award: $badge",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF374151)
              )

              Spacer(modifier = Modifier.height(18.dp))
              Button(
                onClick = {
                  currentQuestionIndex = 0
                  selectedOptionIndex = null
                  isAnswerSubmitted = false
                  totalScore = 0
                  isQuizCompleted = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Play Again")
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// LIGHT TV COMPOSABLE
// -------------------------------------------------------------
@Composable
fun LightTvContent(
  videos: List<LightTvVideo>,
  onOpenLink: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = RadiantSunAmber,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "LIGHT TV BROADCASTS",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.White
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFDC2626))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "AUTHORIZED STREAMS",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Watch official documentaries, interviews, cultural performances, and development features showcasing Akwa Ibom State.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCBD5E1)
          )
        }
      }
    }

    items(videos) { video ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("tv_card_${video.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFE0E7FF))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = video.category,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF3730A3)
              )
            }
            Text(
              text = video.duration,
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF6B7280)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = video.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = video.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4B5563)
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Producer: ${video.presenterOrProducer}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280)
          )

          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = { onOpenLink(video.officialUrl) },
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenDark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("play_video_${video.id}")
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Watch Video Stream")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// DIALOGS & FORMS
// -------------------------------------------------------------
@Composable
fun SubmitMusicDialog(
  onDismiss: () -> Unit,
  onSubmit: (name: String, title: String, genre: String, link: String, bio: String, contact: String) -> Unit
) {
  var artistName by remember { mutableStateOf("") }
  var songTitle by remember { mutableStateOf("") }
  var genre by remember { mutableStateOf("Afrobeats") }
  var link by remember { mutableStateOf("") }
  var bio by remember { mutableStateOf("") }
  var contact by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth()
      ) {
        Text(
          text = "Submit Music for Review",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Text(
          text = "Submit your song details for community spotlight and featured artist considerations.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = artistName,
          onValueChange = { artistName = it },
          label = { Text("Artist / Stage Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = songTitle,
          onValueChange = { songTitle = it },
          label = { Text("Song Title") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = genre,
          onValueChange = { genre = it },
          label = { Text("Genre (Afrobeats, Gospel, Highlife, Traditional)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = link,
          onValueChange = { link = it },
          label = { Text("Official Link (YouTube, Audiomack, Spotify)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = contact,
          onValueChange = { contact = it },
          label = { Text("Phone / Email") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Cancel")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (artistName.isNotBlank() && songTitle.isNotBlank()) {
                onSubmit(artistName, songTitle, genre, link, bio, contact)
              }
            },
            enabled = artistName.isNotBlank() && songTitle.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
          ) {
            Text("Submit")
          }
        }
      }
    }
  }
}

@Composable
fun SubmitComedyDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, creator: String, url: String, desc: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var creator by remember { mutableStateOf("") }
  var url by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Submit Comedy Skit",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFFDC2626)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Skit Title") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = creator,
          onValueChange = { creator = it },
          label = { Text("Creator / Comedian Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = url,
          onValueChange = { url = it },
          label = { Text("Public Video Link (YouTube / Facebook)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Brief Description") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) { Text("Cancel") }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (title.isNotBlank() && creator.isNotBlank()) {
                onSubmit(title, creator, url, desc)
              }
            },
            enabled = title.isNotBlank() && creator.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
          ) {
            Text("Submit Skit")
          }
        }
      }
    }
  }
}

@Composable
fun SubmitDanceDialog(
  onDismiss: () -> Unit,
  onSubmit: (dancer: String, title: String, lga: String, url: String, desc: String) -> Unit
) {
  var dancer by remember { mutableStateOf("") }
  var title by remember { mutableStateOf("") }
  var lga by remember { mutableStateOf("Uyo LGA") }
  var url by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Join Dance Challenge",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = RadiantSunOrange
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = dancer,
          onValueChange = { dancer = it },
          label = { Text("Dancer or Crew Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Dance Routine Title") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = lga,
          onValueChange = { lga = it },
          label = { Text("Your LGA in Akwa Ibom") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = url,
          onValueChange = { url = it },
          label = { Text("Video Link (YouTube/Instagram/TikTok)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) { Text("Cancel") }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (dancer.isNotBlank() && title.isNotBlank()) {
                onSubmit(dancer, title, lga, url, desc)
              }
            },
            enabled = dancer.isNotBlank() && title.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange)
          ) {
            Text("Submit Entry")
          }
        }
      }
    }
  }
}

@Composable
fun NominateTalentDialog(
  nominatorName: String,
  onDismiss: () -> Unit,
  onSubmit: (nominee: String, category: String, lga: String, reason: String) -> Unit
) {
  var nominee by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Vocalist / Musician") }
  var lga by remember { mutableStateOf("Uyo LGA") }
  var reason by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Nominate a Talent",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = nominee,
          onValueChange = { nominee = it },
          label = { Text("Talented Person's Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category (Singer, Dancer, Actor, Poet, etc.)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = lga,
          onValueChange = { lga = it },
          label = { Text("Hometown / LGA") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = reason,
          onValueChange = { reason = it },
          label = { Text("Why should they be featured?") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) { Text("Cancel") }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (nominee.isNotBlank()) {
                onSubmit(nominee, category, lga, reason)
              }
            },
            enabled = nominee.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenDark)
          ) {
            Text("Nominate")
          }
        }
      }
    }
  }
}

@Composable
fun ComedyCommentsDialog(
  comedy: ComedyItem,
  currentUserName: String,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var newCommentText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(480.dp)
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Comments (${comedy.comments.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = comedy.title,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (comedy.comments.isEmpty()) {
            item {
              Text(
                text = "No comments yet. Be the first to share your reaction!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(vertical = 20.dp)
              )
            }
          } else {
            items(comedy.comments) { comment ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFF3F4F6))
                  .padding(10.dp)
              ) {
                Column {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = comment.authorName,
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = AkwaGreenDark
                    )
                    Text(
                      text = comment.timeAgo,
                      style = MaterialTheme.typography.labelSmall,
                      color = Color(0xFF9CA3AF),
                      fontSize = 10.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF374151)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = newCommentText,
            onValueChange = { newCommentText = it },
            placeholder = { Text("Add a comment...") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (newCommentText.isNotBlank()) {
                onAddComment(newCommentText)
                newCommentText = ""
              }
            },
            enabled = newCommentText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
          ) {
            Text("Send")
          }
        }
      }
    }
  }
}

@Composable
fun ReportContentDialog(
  itemTitle: String,
  onDismiss: () -> Unit,
  onReportConfirmed: () -> Unit
) {
  var reason by remember { mutableStateOf("Inappropriate Content") }
  val reasons = listOf("Inappropriate Content", "Copyright Issue", "Spam / Misleading", "Offensive Language")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Report Content",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFFDC2626)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Reporting: \"$itemTitle\"",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF4B5563)
        )
        Spacer(modifier = Modifier.height(12.dp))

        reasons.forEach { r ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { reason = r }
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (reason == r) Icons.Default.CheckCircle else Icons.Default.Close,
              contentDescription = null,
              tint = if (reason == r) Color(0xFFDC2626) else Color(0xFF9CA3AF),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(r, style = MaterialTheme.typography.bodyMedium)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) { Text("Cancel") }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = onReportConfirmed,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
          ) {
            Text("Submit Report")
          }
        }
      }
    }
  }
}
