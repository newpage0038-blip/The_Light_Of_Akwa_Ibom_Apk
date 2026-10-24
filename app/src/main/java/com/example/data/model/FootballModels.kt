package com.example.data.model

enum class FootballLeague(val code: String, val displayName: String) {
  EPL("PL", "Premier League"),
  UCL("CL", "UEFA Champions League")
}

enum class MatchStatus(val label: String) {
  LIVE("LIVE"),
  UPCOMING("UPCOMING"),
  HALF_TIME("HT"),
  FULL_TIME("FT"),
  POSTPONED("POSTPONED")
}

data class FootballMatch(
  val id: String,
  val league: FootballLeague,
  val homeTeam: String,
  val awayTeam: String,
  val homeScore: Int?,
  val awayScore: Int?,
  val matchDate: String,
  val kickOffTimeWAT: String,
  val status: MatchStatus,
  val elapsedMinutes: Int? = null,
  val venue: String,
  val matchday: String,
  val broadcasterGuide: String,
  val officialHighlightsUrl: String? = null
)

data class LeagueStanding(
  val position: Int,
  val teamName: String,
  val played: Int,
  val won: Int,
  val drawn: Int,
  val lost: Int,
  val goalsFor: Int,
  val goalsAgainst: Int,
  val goalDifference: Int,
  val points: Int,
  val form: List<String>
)

data class FanZonePost(
  val id: String,
  val authorName: String,
  val teamTag: String,
  val text: String,
  val timestamp: String,
  val likesCount: Int,
  val isLikedByMe: Boolean = false,
  val comments: List<FanZoneComment> = emptyList()
)

data class FanZoneComment(
  val id: String,
  val authorName: String,
  val text: String,
  val timestamp: String
)

data class BroadcasterOption(
  val name: String,
  val serviceType: String,
  val coverage: String,
  val officialUrl: String,
  val regionNote: String = "Authorised in Nigeria (West Africa)"
)

data class TeamOption(
  val name: String,
  val shortCode: String,
  val league: FootballLeague,
  val isLocalPride: Boolean = false
)
