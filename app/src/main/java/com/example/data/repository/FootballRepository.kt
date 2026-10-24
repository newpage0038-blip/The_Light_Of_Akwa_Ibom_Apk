package com.example.data.repository

import com.example.data.model.BroadcasterOption
import com.example.data.model.FanZoneComment
import com.example.data.model.FanZonePost
import com.example.data.model.FootballLeague
import com.example.data.model.FootballMatch
import com.example.data.model.LeagueStanding
import com.example.data.model.MatchStatus
import com.example.data.model.TeamOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FootballRepository {

  // Current selected favorite team
  private val _favoriteTeam = MutableStateFlow<String?>("Arsenal")
  val favoriteTeam: StateFlow<String?> = _favoriteTeam.asStateFlow()

  fun setFavoriteTeam(teamName: String?) {
    _favoriteTeam.value = teamName
  }

  // Network/Refresh state
  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

  private val _lastUpdatedTime = MutableStateFlow(
    SimpleDateFormat("HH:mm 'WAT'", Locale.getDefault()).format(Date())
  )
  val lastUpdatedTime: StateFlow<String> = _lastUpdatedTime.asStateFlow()

  private val _isOfflineMode = MutableStateFlow(false)
  val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

  // Authorized Broadcasters in Nigeria / West Africa
  val officialBroadcasters: List<BroadcasterOption> = listOf(
    BroadcasterOption(
      name = "SuperSport (DStv / GOtv)",
      serviceType = "Satellite & Stream",
      coverage = "Full Live Premier League & UEFA Champions League broadcast with commentary in English",
      officialUrl = "https://www.dstv.com/en-ng",
      regionNote = "SuperSport Football Plus & Premier League channels (Ch 202/203/205)"
    ),
    BroadcasterOption(
      name = "Showmax Premier League",
      serviceType = "Mobile OTT Streaming",
      coverage = "All 380 Premier League matches streamed live to mobile phones across Nigeria",
      officialUrl = "https://www.showmax.com",
      regionNote = "Mobile streaming plan optimized for Nigerian smartphone subscribers"
    ),
    BroadcasterOption(
      name = "UEFA.tv",
      serviceType = "Official UEFA Digital App",
      coverage = "Match highlights, magazine shows, classic UCL moments, and youth tournament live feeds",
      officialUrl = "https://www.uefa.tv",
      regionNote = "Official UEFA streaming service"
    ),
    BroadcasterOption(
      name = "Canal+ Afrique",
      serviceType = "Francophone Broadcaster",
      coverage = "Official broadcast coverage for regional and continental tournaments",
      officialUrl = "https://www.canalplus-afrique.com",
      regionNote = "Authorized West African broadcast partner"
    )
  )

  // Teams list for selection
  val availableTeams: List<TeamOption> = listOf(
    TeamOption("Akwa United FC", "AKW", FootballLeague.EPL, isLocalPride = true),
    TeamOption("Arsenal", "ARS", FootballLeague.EPL),
    TeamOption("Chelsea", "CHE", FootballLeague.EPL),
    TeamOption("Manchester United", "MUN", FootballLeague.EPL),
    TeamOption("Liverpool", "LIV", FootballLeague.EPL),
    TeamOption("Manchester City", "MCI", FootballLeague.EPL),
    TeamOption("Tottenham Hotspur", "TOT", FootballLeague.EPL),
    TeamOption("Aston Villa", "AVL", FootballLeague.EPL),
    TeamOption("Newcastle United", "NEW", FootballLeague.EPL),
    TeamOption("Real Madrid", "RMA", FootballLeague.UCL),
    TeamOption("Barcelona", "BAR", FootballLeague.UCL),
    TeamOption("Bayern Munich", "BAY", FootballLeague.UCL),
    TeamOption("Paris Saint-Germain", "PSG", FootballLeague.UCL),
    TeamOption("Inter Milan", "INT", FootballLeague.UCL)
  )

  // Fixtures & Live Matches
  private val _matches = MutableStateFlow(
    listOf(
      // EPL Matches
      FootballMatch(
        id = "pl_1",
        league = FootballLeague.EPL,
        homeTeam = "Arsenal",
        awayTeam = "Chelsea",
        homeScore = 2,
        awayScore = 1,
        matchDate = "Saturday, Matchday 28",
        kickOffTimeWAT = "17:30 WAT",
        status = MatchStatus.LIVE,
        elapsedMinutes = 68,
        venue = "Emirates Stadium, London",
        matchday = "Matchday 28",
        broadcasterGuide = "SuperSport Premier League (DStv Ch 203) & Showmax Live",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=Premier+League+Highlights+Arsenal+vs+Chelsea"
      ),
      FootballMatch(
        id = "pl_2",
        league = FootballLeague.EPL,
        homeTeam = "Manchester City",
        awayTeam = "Liverpool",
        homeScore = null,
        awayScore = null,
        matchDate = "Sunday, Matchday 28",
        kickOffTimeWAT = "16:30 WAT",
        status = MatchStatus.UPCOMING,
        venue = "Etihad Stadium, Manchester",
        matchday = "Matchday 28",
        broadcasterGuide = "SuperSport Premier League & Showmax Mobile",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=Premier+League+Official+Highlights"
      ),
      FootballMatch(
        id = "pl_3",
        league = FootballLeague.EPL,
        homeTeam = "Manchester United",
        awayTeam = "Tottenham Hotspur",
        homeScore = 1,
        awayScore = 1,
        matchDate = "Saturday, Matchday 28",
        kickOffTimeWAT = "15:00 WAT",
        status = MatchStatus.FULL_TIME,
        venue = "Old Trafford, Manchester",
        matchday = "Matchday 28",
        broadcasterGuide = "SuperSport Football (DStv Ch 205)",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=Premier+League+Official+Highlights+Man+Utd"
      ),
      FootballMatch(
        id = "pl_4",
        league = FootballLeague.EPL,
        homeTeam = "Newcastle United",
        awayTeam = "Aston Villa",
        homeScore = 0,
        awayScore = 0,
        matchDate = "Saturday, Matchday 28",
        kickOffTimeWAT = "12:30 WAT",
        status = MatchStatus.HALF_TIME,
        elapsedMinutes = 45,
        venue = "St. James' Park, Newcastle",
        matchday = "Matchday 28",
        broadcasterGuide = "SuperSport Premier League",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=Premier+League+Official+Highlights"
      ),

      // UCL Matches
      FootballMatch(
        id = "ucl_1",
        league = FootballLeague.UCL,
        homeTeam = "Real Madrid",
        awayTeam = "Manchester City",
        homeScore = 3,
        awayScore = 3,
        matchDate = "Tuesday, Quarter-Final 1st Leg",
        kickOffTimeWAT = "20:00 WAT",
        status = MatchStatus.FULL_TIME,
        venue = "Santiago Bernabéu, Madrid",
        matchday = "Quarter-Final",
        broadcasterGuide = "SuperSport Grandstand & UEFA.tv Highlights",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=UEFA+Champions+League+Real+Madrid+vs+Man+City+Highlights"
      ),
      FootballMatch(
        id = "ucl_2",
        league = FootballLeague.UCL,
        homeTeam = "Arsenal",
        awayTeam = "Bayern Munich",
        homeScore = 2,
        awayScore = 2,
        matchDate = "Tuesday, Quarter-Final 1st Leg",
        kickOffTimeWAT = "20:00 WAT",
        status = MatchStatus.FULL_TIME,
        venue = "Emirates Stadium, London",
        matchday = "Quarter-Final",
        broadcasterGuide = "SuperSport Football Plus & DStv Stream",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=UEFA+Champions+League+Arsenal+vs+Bayern+Highlights"
      ),
      FootballMatch(
        id = "ucl_3",
        league = FootballLeague.UCL,
        homeTeam = "Paris Saint-Germain",
        awayTeam = "Barcelona",
        homeScore = null,
        awayScore = null,
        matchDate = "Wednesday, Quarter-Final 1st Leg",
        kickOffTimeWAT = "20:00 WAT",
        status = MatchStatus.UPCOMING,
        venue = "Parc des Princes, Paris",
        matchday = "Quarter-Final",
        broadcasterGuide = "SuperSport Premier League (DStv Ch 203)",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=UEFA+Champions+League+Official+Highlights"
      ),

      // Local Hero Match: Akwa United FC in NPFL
      FootballMatch(
        id = "npfl_1",
        league = FootballLeague.EPL,
        homeTeam = "Akwa United FC",
        awayTeam = "Enyimba FC",
        homeScore = 2,
        awayScore = 0,
        matchDate = "Sunday, NPFL Matchday 24",
        kickOffTimeWAT = "16:00 WAT",
        status = MatchStatus.FULL_TIME,
        venue = "Godswill Akpabio International Stadium (Nest of Champions), Uyo",
        matchday = "NPFL Clásico",
        broadcasterGuide = "StarTimes Nigeria & NPFL Live Stream",
        officialHighlightsUrl = "https://www.youtube.com/results?search_query=Akwa+United+FC+Highlights"
      )
    )
  )
  val matches: StateFlow<List<FootballMatch>> = _matches.asStateFlow()

  // Premier League Standings
  private val _standings = MutableStateFlow(
    listOf(
      LeagueStanding(1, "Liverpool", 28, 20, 6, 2, 65, 25, 40, 66, listOf("W", "W", "W", "D", "W")),
      LeagueStanding(2, "Arsenal", 28, 19, 7, 2, 62, 23, 39, 64, listOf("W", "W", "D", "W", "W")),
      LeagueStanding(3, "Manchester City", 28, 19, 5, 4, 63, 28, 35, 62, listOf("D", "W", "W", "W", "L")),
      LeagueStanding(4, "Chelsea", 28, 15, 6, 7, 52, 34, 18, 51, listOf("W", "L", "W", "W", "D")),
      LeagueStanding(5, "Newcastle United", 28, 14, 6, 8, 48, 36, 12, 48, listOf("L", "W", "W", "D", "W")),
      LeagueStanding(6, "Aston Villa", 28, 13, 7, 8, 45, 37, 8, 46, listOf("W", "D", "L", "W", "D")),
      LeagueStanding(7, "Tottenham Hotspur", 28, 12, 5, 11, 55, 42, 13, 41, listOf("L", "W", "L", "W", "L")),
      LeagueStanding(8, "Manchester United", 28, 11, 6, 11, 40, 41, -1, 39, listOf("D", "L", "W", "L", "D")),
      LeagueStanding(9, "Brighton & Hove Albion", 28, 10, 8, 10, 42, 44, -2, 38, listOf("D", "D", "W", "L", "W")),
      LeagueStanding(10, "West Ham United", 28, 10, 6, 12, 38, 49, -11, 36, listOf("L", "W", "L", "D", "W"))
    )
  )
  val standings: StateFlow<List<LeagueStanding>> = _standings.asStateFlow()

  // Fan Zone Discussions
  private val _fanZonePosts = MutableStateFlow(
    listOf(
      FanZonePost(
        id = "fzp1",
        authorName = "Bassey Udofia (Uyo Gunner)",
        teamTag = "Arsenal",
        text = "Massive clash this weekend! Arsenal's midfield with Rice and Odegaard will control the tempo. What are your score predictions from Uyo?",
        timestamp = "20 mins ago",
        likesCount = 47,
        comments = listOf(
          FanZoneComment("fzc1", "Emeka Chelsea", "Don't count the Blues out! We always show up for London derbies.", "15 mins ago"),
          FanZoneComment("fzc2", "Kufre ManUtd", "Enjoy your derby, but remember champions are made in May!", "8 mins ago")
        )
      ),
      FanZonePost(
        id = "fzp2",
        authorName = "Anietie Akpan (Nest of Champions)",
        teamTag = "Akwa United FC",
        text = "Akwa United (The Promise Keepers) showing true character at the Nest of Champions in Uyo! Let's pack the stadium this Sunday for the boys!",
        timestamp = "2 hours ago",
        likesCount = 92,
        comments = listOf(
          FanZoneComment("fzc3", "Edidiong", "Promise Keepers all the way! Akwa Ibom to the world!", "1 hour ago")
        )
      ),
      FanZonePost(
        id = "fzp3",
        authorName = "Ndifreke (Madridista)",
        teamTag = "Real Madrid",
        text = "Champions League nights are simply magical! Santiago Bernabéu is ready for the knockout clash.",
        timestamp = "4 hours ago",
        likesCount = 38,
        comments = listOf(
          FanZoneComment("fzc4", "Ime Bassey", "City vs Madrid is the real final before the final!", "3 hours ago")
        )
      )
    )
  )
  val fanZonePosts: StateFlow<List<FanZonePost>> = _fanZonePosts.asStateFlow()

  fun toggleFanPostLike(postId: String) {
    _fanZonePosts.value = _fanZonePosts.value.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLikedByMe
        val count = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        post.copy(isLikedByMe = newLiked, likesCount = count)
      } else post
    }
  }

  fun addFanPost(authorName: String, teamTag: String, text: String) {
    val post = FanZonePost(
      id = "fzp_${System.currentTimeMillis()}",
      authorName = authorName,
      teamTag = teamTag,
      text = text,
      timestamp = "Just now",
      likesCount = 0,
      comments = emptyList()
    )
    _fanZonePosts.value = listOf(post) + _fanZonePosts.value
  }

  fun addFanComment(postId: String, authorName: String, text: String) {
    _fanZonePosts.value = _fanZonePosts.value.map { post ->
      if (post.id == postId) {
        val newComment = FanZoneComment(
          id = "fzc_${System.currentTimeMillis()}",
          authorName = authorName,
          text = text,
          timestamp = "Just now"
        )
        post.copy(comments = post.comments + newComment)
      } else post
    }
  }

  fun refreshFootballData() {
    _isRefreshing.value = true
    _lastUpdatedTime.value = SimpleDateFormat("HH:mm 'WAT'", Locale.getDefault()).format(Date())
    // Simulate instantaneous live refresh check
    _isRefreshing.value = false
  }
}
