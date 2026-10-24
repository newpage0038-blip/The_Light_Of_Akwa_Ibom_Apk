package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.data.model.BroadcasterOption
import com.example.data.model.FanZonePost
import com.example.data.model.FootballLeague
import com.example.data.model.FootballMatch
import com.example.data.model.LeagueStanding
import com.example.data.model.MatchStatus
import com.example.data.repository.FootballRepository
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

enum class FootballSection(val title: String) {
  LIVE_CENTRE("Live Centre"),
  PREMIER_LEAGUE("Premier League"),
  CHAMPIONS_LEAGUE("Champions League"),
  STANDINGS("Table & Standings"),
  HIGHLIGHTS("Match Highlights"),
  FAN_ZONE("Fan Zone & Banter"),
  FAVOURITE_TEAM("My Club")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FootballScreen(
  repository: FootballRepository,
  currentUserName: String,
  onShowSnackbar: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedSection by remember { mutableStateOf(FootballSection.LIVE_CENTRE) }

  // Observables
  val matches by repository.matches.collectAsStateWithLifecycle()
  val standings by repository.standings.collectAsStateWithLifecycle()
  val fanZonePosts by repository.fanZonePosts.collectAsStateWithLifecycle()
  val favoriteTeam by repository.favoriteTeam.collectAsStateWithLifecycle()
  val isRefreshing by repository.isRefreshing.collectAsStateWithLifecycle()
  val lastUpdatedTime by repository.lastUpdatedTime.collectAsStateWithLifecycle()
  val isOfflineMode by repository.isOfflineMode.collectAsStateWithLifecycle()

  // Dialog states
  var selectedMatchForDetails by remember { mutableStateOf<FootballMatch?>(null) }
  var showNewFanPostDialog by remember { mutableStateOf(false) }
  var activeFanPostForComments by remember { mutableStateOf<FanZonePost?>(null) }
  var reportingFanPostId by remember { mutableStateOf<String?>(null) }

  val openExternalLink: (String) -> Unit = { url ->
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Unable to open link. Please check network.", Toast.LENGTH_SHORT).show()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF9FAFB))
  ) {
    // Top Sports Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B)))
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
              imageVector = Icons.Default.SportsSoccer,
              contentDescription = null,
              tint = RadiantSunAmber,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "FOOTBALL CENTRE",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              ),
              color = Color.White
            )
          }
          Text(
            text = "EPL • UEFA Champions League • WAT Match Times",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = {
              repository.refreshFootballData()
              onShowSnackbar("Football scores and fixtures refreshed ($lastUpdatedTime).")
            },
            modifier = Modifier.testTag("refresh_football_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Refresh",
              tint = RadiantSunAmber
            )
          }
        }
      }
    }

    // Secondary sub-bar showing West Africa Time and Favorite Team badge
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF1F5F9))
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF22C55E))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Updated: $lastUpdatedTime",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF475569)
        )
      }

      favoriteTeam?.let { team ->
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AkwaGreenLight)
            .padding(horizontal = 6.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = AkwaGreenDark,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "My Club: $team",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark,
            fontSize = 10.sp
          )
        }
      }
    }

    // Scrollable Tab Row
    ScrollableTabRow(
      selectedTabIndex = selectedSection.ordinal,
      containerColor = Color.White,
      contentColor = Color(0xFF0F172A),
      edgePadding = 12.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection.ordinal]),
          height = 3.dp,
          color = Color(0xFF0F172A)
        )
      }
    ) {
      FootballSection.values().forEach { section ->
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
              color = if (selected) Color(0xFF0F172A) else Color(0xFF64748B)
            )
          },
          modifier = Modifier.testTag("football_tab_${section.name.lowercase()}")
        )
      }
    }

    // Content Switch
    when (selectedSection) {
      FootballSection.LIVE_CENTRE -> {
        LiveCentreContent(
          matches = matches,
          onSelectMatch = { selectedMatchForDetails = it },
          onOpenHighlights = openExternalLink,
          broadcasters = repository.officialBroadcasters,
          onOpenBroadcaster = openExternalLink
        )
      }
      FootballSection.PREMIER_LEAGUE -> {
        LeagueMatchesContent(
          league = FootballLeague.EPL,
          matches = matches.filter { it.league == FootballLeague.EPL },
          onSelectMatch = { selectedMatchForDetails = it },
          onOpenHighlights = openExternalLink
        )
      }
      FootballSection.CHAMPIONS_LEAGUE -> {
        LeagueMatchesContent(
          league = FootballLeague.UCL,
          matches = matches.filter { it.league == FootballLeague.UCL },
          onSelectMatch = { selectedMatchForDetails = it },
          onOpenHighlights = openExternalLink
        )
      }
      FootballSection.STANDINGS -> {
        StandingsContent(standings = standings)
      }
      FootballSection.HIGHLIGHTS -> {
        HighlightsContent(
          matches = matches.filter { it.officialHighlightsUrl != null },
          onOpenHighlights = openExternalLink
        )
      }
      FootballSection.FAN_ZONE -> {
        FanZoneContent(
          posts = fanZonePosts,
          currentUserName = currentUserName,
          onToggleLike = { repository.toggleFanPostLike(it) },
          onOpenComments = { activeFanPostForComments = it },
          onReport = { reportingFanPostId = it.id },
          onNewPost = { showNewFanPostDialog = true }
        )
      }
      FootballSection.FAVOURITE_TEAM -> {
        FavoriteTeamContent(
          availableTeams = repository.availableTeams,
          currentFavorite = favoriteTeam,
          onSelectTeam = { team ->
            repository.setFavoriteTeam(team)
            onShowSnackbar("Selected $team as your favorite club.")
          },
          matches = matches.filter {
            favoriteTeam == null || it.homeTeam.contains(favoriteTeam!!) || it.awayTeam.contains(favoriteTeam!!)
          },
          onSelectMatch = { selectedMatchForDetails = it },
          onOpenHighlights = openExternalLink
        )
      }
    }
  }

  // Match Details Dialog
  selectedMatchForDetails?.let { match ->
    MatchDetailsDialog(
      match = match,
      onDismiss = { selectedMatchForDetails = null },
      onOpenHighlights = openExternalLink,
      onOpenBroadcaster = openExternalLink
    )
  }

  // New Fan Zone Post Dialog
  if (showNewFanPostDialog) {
    NewFanPostDialog(
      currentUserName = currentUserName,
      favoriteTeam = favoriteTeam ?: "Football Fan",
      onDismiss = { showNewFanPostDialog = false },
      onSubmit = { teamTag, text ->
        repository.addFanPost(currentUserName, teamTag, text)
        showNewFanPostDialog = false
        onShowSnackbar("Your thought was posted to the Fan Zone!")
      }
    )
  }

  // Fan Zone Comments Dialog
  activeFanPostForComments?.let { post ->
    FanCommentsDialog(
      post = post,
      currentUserName = currentUserName,
      onDismiss = { activeFanPostForComments = null },
      onAddComment = { text ->
        repository.addFanComment(post.id, currentUserName, text)
        val updated = post.copy(
          comments = post.comments + com.example.data.model.FanZoneComment(
            id = "fzc_${System.currentTimeMillis()}",
            authorName = currentUserName,
            text = text,
            timestamp = "Just now"
          )
        )
        activeFanPostForComments = updated
      }
    )
  }

  // Report dialog
  reportingFanPostId?.let {
    ReportContentDialog(
      itemTitle = "Fan Zone Post #$it",
      onDismiss = { reportingFanPostId = null },
      onReportConfirmed = {
        reportingFanPostId = null
        onShowSnackbar("Thank you. Fan post reported for moderation.")
      }
    )
  }
}

// -------------------------------------------------------------
// LIVE CENTRE COMPOSABLE
// -------------------------------------------------------------
@Composable
fun LiveCentreContent(
  matches: List<FootballMatch>,
  onSelectMatch: (FootballMatch) -> Unit,
  onOpenHighlights: (String) -> Unit,
  broadcasters: List<BroadcasterOption>,
  onOpenBroadcaster: (String) -> Unit
) {
  val liveMatches = remember(matches) {
    matches.filter { it.status == MatchStatus.LIVE || it.status == MatchStatus.HALF_TIME }
  }

  val otherMatches = remember(matches) {
    matches.filter { it.status != MatchStatus.LIVE && it.status != MatchStatus.HALF_TIME }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Official Viewing & Legitimate Broadcaster Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
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
                imageVector = Icons.Default.LiveTv,
                contentDescription = null,
                tint = RadiantSunAmber,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "AUTHORIZED LIVE BROADCASTERS",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
            Text(
              text = "Nigeria & WAT",
              style = MaterialTheme.typography.labelSmall,
              color = RadiantSunAmber
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Watch official matches legally via authorized rights holders. Stream via SuperSport on DStv Stream, GOtv, Showmax Mobile, and UEFA.tv.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCBD5E1)
          )

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            broadcasters.take(2).forEach { bc ->
              OutlinedButton(
                onClick = { onOpenBroadcaster(bc.officialUrl) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text(bc.name.split(" ").first(), fontSize = 11.sp, color = Color.White)
              }
            }
          }
        }
      }
    }

    // Live Matches Section
    if (liveMatches.isNotEmpty()) {
      item {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(Color.Red)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "LIVE NOW (WEST AFRICA TIME)",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            ),
            color = Color.Red
          )
        }
      }

      items(liveMatches) { match ->
        MatchScoreCard(
          match = match,
          onSelect = { onSelectMatch(match) },
          onOpenHighlights = onOpenHighlights
        )
      }
    }

    // Upcoming & Recent Matches
    item {
      Text(
        text = "UPCOMING & RECENT MATCHES",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = Color(0xFF0F172A)
      )
    }

    items(otherMatches) { match ->
      MatchScoreCard(
        match = match,
        onSelect = { onSelectMatch(match) },
        onOpenHighlights = onOpenHighlights
      )
    }

    // Offline / Data provider guidance
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFF1F5F9))
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFF475569),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Scores and kick-off times are synchronized in West Africa Time (WAT). If network access is interrupted, the app automatically preserves and displays the cached schedule.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF475569),
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// LEAGUE MATCHES COMPOSABLE (EPL & UCL)
// -------------------------------------------------------------
@Composable
fun LeagueMatchesContent(
  league: FootballLeague,
  matches: List<FootballMatch>,
  onSelectMatch: (FootballMatch) -> Unit,
  onOpenHighlights: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = if (league == FootballLeague.EPL) Color(0xFF38003C) else Color(0xFF0A1B44)
        ),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = league.displayName.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (league == FootballLeague.EPL)
              "The most watched football league in the world. Full coverage in West Africa Time."
            else
              "European nights: The premier continental club tournament featuring the finest teams.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }
    }

    items(matches) { match ->
      MatchScoreCard(
        match = match,
        onSelect = { onSelectMatch(match) },
        onOpenHighlights = onOpenHighlights
      )
    }
  }
}

// -------------------------------------------------------------
// STANDINGS TABLE COMPOSABLE
// -------------------------------------------------------------
@Composable
fun StandingsContent(standings: List<LeagueStanding>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "PREMIER LEAGUE STANDINGS",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = Color(0xFF0F172A)
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC))
              .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("#", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(24.dp))
            Text("Club", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("P", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
            Text("W", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
            Text("D", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
            Text("L", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
            Text("GD", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
            Text("Pts", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
          }

          HorizontalDivider(thickness = 1.dp, color = Color(0xFFE2E8F0))

          standings.forEach { row ->
            val isChampionsLeagueZone = row.position <= 4
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .width(24.dp),
                contentAlignment = Alignment.CenterStart
              ) {
                Text(
                  text = "${row.position}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = if (isChampionsLeagueZone) Color(0xFF0284C7) else Color(0xFF64748B)
                )
              }
              Text(
                text = row.teamName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFF1E293B),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text("${row.played}", fontSize = 11.sp, modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
              Text("${row.won}", fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
              Text("${row.drawn}", fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
              Text("${row.lost}", fontSize = 11.sp, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
              Text(
                text = if (row.goalDifference > 0) "+${row.goalDifference}" else "${row.goalDifference}",
                fontSize = 11.sp,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
              )
              Text(
                text = "${row.points}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = Color(0xFF0F172A),
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
              )
            }
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF0284C7))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Positions 1–4 qualify for UEFA Champions League group stage.",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF64748B)
        )
      }
    }
  }
}

// -------------------------------------------------------------
// HIGHLIGHTS COMPOSABLE
// -------------------------------------------------------------
@Composable
fun HighlightsContent(
  matches: List<FootballMatch>,
  onOpenHighlights: (String) -> Unit
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
          Text(
            text = "OFFICIAL MATCH HIGHLIGHTS",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Watch official goals, red cards, key saves, and match moments streamed directly from official league and broadcaster channels.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCBD5E1)
          )
        }
      }
    }

    items(matches) { match ->
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
            Text(
              text = match.league.displayName,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
            Text(
              text = match.matchDate,
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF64748B)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "${match.homeTeam} vs ${match.awayTeam}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )

          if (match.homeScore != null && match.awayScore != null) {
            Text(
              text = "Final Score: ${match.homeScore} - ${match.awayScore}",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
              color = RadiantSunOrange
            )
          }

          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = { match.officialHighlightsUrl?.let { onOpenHighlights(it) } },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Watch Highlights on Official Channel")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FAN ZONE & BANTER COMPOSABLE
// -------------------------------------------------------------
@Composable
fun FanZoneContent(
  posts: List<FanZonePost>,
  currentUserName: String,
  onToggleLike: (String) -> Unit,
  onOpenComments: (FanZonePost) -> Unit,
  onReport: (FanZonePost) -> Unit,
  onNewPost: () -> Unit
) {
  var selectedClubFilter by remember { mutableStateOf<String?>(null) }
  val clubs = listOf("All", "Arsenal", "Chelsea", "Manchester United", "Liverpool", "Manchester City", "Akwa United FC", "Real Madrid")

  val filteredPosts = remember(selectedClubFilter, posts) {
    if (selectedClubFilter == null || selectedClubFilter == "All") posts
    else posts.filter { it.teamTag.equals(selectedClubFilter, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Banter Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "FOOTBALL FAN ZONE & BANTER",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
              color = Color(0xFF78350F)
            )
            Text(
              text = "Share predictions, debate matches, and cheer with football lovers across Akwa Ibom State.",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF92400E)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = onNewPost,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("new_fan_post_btn")
          ) {
            Text("Post")
          }
        }
      }
    }

    // Filter Chips
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(clubs) { club ->
          FilterChip(
            selected = (selectedClubFilter == null && club == "All") || selectedClubFilter == club,
            onClick = { selectedClubFilter = if (club == "All") null else club },
            label = { Text(club, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AkwaGreenLight,
              selectedLabelColor = AkwaGreenDark
            )
          )
        }
      }
    }

    items(filteredPosts) { post ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("fan_post_${post.id}"),
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
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = post.authorName.take(1).uppercase(),
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E293B)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = post.authorName,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF0F172A)
                )
                Text(
                  text = "${post.teamTag} • ${post.timestamp}",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFF64748B)
                )
              }
            }

            IconButton(
              onClick = { onReport(post) },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.Flag, contentDescription = "Report", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = post.text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF334155)
          )

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier
                .clickable { onToggleLike(post.id) }
                .padding(4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (post.isLikedByMe) Color.Red else Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("${post.likesCount} Cheers", fontSize = 12.sp, color = Color(0xFF475569))
            }

            Row(
              modifier = Modifier
                .clickable { onOpenComments(post) }
                .padding(4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("${post.comments.size} Replies", fontSize = 12.sp, color = Color(0xFF475569))
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FAVORITE TEAM COMPOSABLE
// -------------------------------------------------------------
@Composable
fun FavoriteTeamContent(
  availableTeams: List<com.example.data.model.TeamOption>,
  currentFavorite: String?,
  onSelectTeam: (String) -> Unit,
  matches: List<FootballMatch>,
  onSelectMatch: (FootballMatch) -> Unit,
  onOpenHighlights: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Text(
        text = "SELECT YOUR FAVOURITE CLUB",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = Color(0xFF0F172A)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Select your club to highlight fixtures and filter matches. Includes Nigeria Premier Football League heroes Akwa United FC!",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF64748B)
      )
    }

    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(availableTeams) { team ->
          val isSelected = currentFavorite == team.name
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isSelected) {
                  if (team.isLocalPride) AkwaGreenDark else Color(0xFF0F172A)
                } else Color.White
              )
              .border(
                1.dp,
                if (isSelected) Color.Transparent else Color(0xFFCBD5E1),
                RoundedCornerShape(10.dp)
              )
              .clickable { onSelectTeam(team.name) }
              .padding(horizontal = 14.dp, vertical = 10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (team.isLocalPride) {
                Text(
                  text = "⭐ ",
                  fontSize = 12.sp
                )
              }
              Text(
                text = team.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF1E293B),
                fontSize = 12.sp
              )
            }
          }
        }
      }
    }

    item {
      Text(
        text = "FIXTURES & RESULTS FOR ${currentFavorite?.uppercase() ?: "ALL CLUBS"}",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        ),
        color = Color(0xFF0F172A)
      )
    }

    if (matches.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Text(
            text = "No matches scheduled for this club in the current round.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center
          )
        }
      }
    } else {
      items(matches) { match ->
        MatchScoreCard(
          match = match,
          onSelect = { onSelectMatch(match) },
          onOpenHighlights = onOpenHighlights
        )
      }
    }
  }
}

// -------------------------------------------------------------
// REUSABLE MATCH SCORE CARD
// -------------------------------------------------------------
@Composable
fun MatchScoreCard(
  match: FootballMatch,
  onSelect: () -> Unit,
  onOpenHighlights: (String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelect() }
      .testTag("match_card_${match.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: League & Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${match.league.displayName} • ${match.matchday}",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF475569)
        )

        val statusColor = when (match.status) {
          MatchStatus.LIVE -> Color(0xFFDC2626)
          MatchStatus.HALF_TIME -> Color(0xFFD97706)
          MatchStatus.FULL_TIME -> Color(0xFF059669)
          MatchStatus.UPCOMING -> Color(0xFF0284C7)
          MatchStatus.POSTPONED -> Color(0xFF6B7280)
        }

        val statusText = when (match.status) {
          MatchStatus.LIVE -> "● LIVE ${match.elapsedMinutes?.let { "$it'" } ?: ""}"
          MatchStatus.HALF_TIME -> "HALF-TIME"
          MatchStatus.FULL_TIME -> "FULL-TIME"
          MatchStatus.UPCOMING -> match.kickOffTimeWAT
          MatchStatus.POSTPONED -> "POSTPONED"
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(statusColor.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = statusColor,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Teams & Scores
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Home Team
        Column(
          modifier = Modifier.weight(1f),
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = match.homeTeam,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A),
            textAlign = TextAlign.End
          )
        }

        // Score or VS
        Box(
          modifier = Modifier
            .padding(horizontal = 14.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 12.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          if (match.homeScore != null && match.awayScore != null) {
            Text(
              text = "${match.homeScore} - ${match.awayScore}",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
              color = Color(0xFF0F172A)
            )
          } else {
            Text(
              text = "VS",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF64748B)
            )
          }
        }

        // Away Team
        Column(
          modifier = Modifier.weight(1f),
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = match.awayTeam,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Start
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Venue & Kick-off Info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = match.venue,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF64748B),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
        Text(
          text = match.kickOffTimeWAT,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
          color = AkwaGreenDark
        )
      }
    }
  }
}

// -------------------------------------------------------------
// MATCH DETAILS DIALOG
// -------------------------------------------------------------
@Composable
fun MatchDetailsDialog(
  match: FootballMatch,
  onDismiss: () -> Unit,
  onOpenHighlights: (String) -> Unit,
  onOpenBroadcaster: (String) -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = match.league.displayName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = "${match.matchDate} • ${match.kickOffTimeWAT}",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Matchup Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = match.homeTeam,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF0F172A),
              modifier = Modifier.weight(1f),
              textAlign = TextAlign.End
            )

            Text(
              text = if (match.homeScore != null && match.awayScore != null)
                "  ${match.homeScore} - ${match.awayScore}  "
              else "  VS  ",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
              color = RadiantSunOrange
            )

            Text(
              text = match.awayTeam,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF0F172A),
              modifier = Modifier.weight(1f),
              textAlign = TextAlign.Start
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "Venue: ${match.venue}",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Official Broadcaster Guide (Nigeria & WAT):",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Text(
          text = match.broadcasterGuide,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF475569)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (match.officialHighlightsUrl != null) {
          Button(
            onClick = { onOpenHighlights(match.officialHighlightsUrl) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Watch Official Highlights")
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
          onClick = { onOpenBroadcaster("https://www.dstv.com/en-ng") },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.LiveTv, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Official Broadcaster (DStv / Showmax)")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// NEW FAN POST DIALOG
// -------------------------------------------------------------
@Composable
fun NewFanPostDialog(
  currentUserName: String,
  favoriteTeam: String,
  onDismiss: () -> Unit,
  onSubmit: (teamTag: String, text: String) -> Unit
) {
  var teamTag by remember { mutableStateOf(favoriteTeam) }
  var postText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Share Football Banter",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = teamTag,
          onValueChange = { teamTag = it },
          label = { Text("Club Tag (e.g. Arsenal, Chelsea, Akwa United)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = postText,
          onValueChange = { postText = it },
          label = { Text("Your thoughts / match prediction") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
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
              if (postText.isNotBlank()) {
                onSubmit(teamTag.ifBlank { "Football" }, postText)
              }
            },
            enabled = postText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
          ) {
            Text("Publish")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FAN COMMENTS DIALOG
// -------------------------------------------------------------
@Composable
fun FanCommentsDialog(
  post: FanZonePost,
  currentUserName: String,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var commentText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(460.dp)
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
            text = "Replies (${post.comments.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = "\"${post.text}\"",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF64748B),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (post.comments.isEmpty()) {
            item {
              Text(
                text = "No replies yet. Join the banter!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(vertical = 16.dp)
              )
            }
          } else {
            items(post.comments) { comment ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFF8FAFC))
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
                      color = Color(0xFF0F172A)
                    )
                    Text(
                      text = comment.timestamp,
                      style = MaterialTheme.typography.labelSmall,
                      color = Color(0xFF94A3B8),
                      fontSize = 10.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF334155)
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
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Write a reply...") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (commentText.isNotBlank()) {
                onAddComment(commentText)
                commentText = ""
              }
            },
            enabled = commentText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
          ) {
            Text("Send")
          }
        }
      }
    }
  }
}
