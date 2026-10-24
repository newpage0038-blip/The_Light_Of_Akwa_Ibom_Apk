package com.example.data.model

data class MusicArtist(
  val id: String,
  val name: String,
  val stageName: String,
  val genre: String,
  val bio: String,
  val originLga: String,
  val topTracks: List<String>,
  val officialLink: String,
  val isFeatured: Boolean = false,
  val badgeText: String = "Akwa Ibom Star"
)

data class SongSubmission(
  val id: String = System.currentTimeMillis().toString(),
  val artistName: String,
  val songTitle: String,
  val genre: String,
  val officialLink: String,
  val bio: String,
  val contact: String,
  val dateSubmitted: String,
  val status: String = "Under Review"
)

data class ComedyItem(
  val id: String,
  val title: String,
  val creator: String,
  val duration: String,
  val videoUrl: String,
  val description: String,
  val likesCount: Int,
  val isLikedByMe: Boolean = false,
  val comments: List<ComedyComment> = emptyList()
)

data class ComedyComment(
  val id: String,
  val authorName: String,
  val text: String,
  val timeAgo: String
)

data class ComedySubmission(
  val id: String = System.currentTimeMillis().toString(),
  val title: String,
  val creatorName: String,
  val videoUrl: String,
  val description: String,
  val dateSubmitted: String
)

data class DanceChallenge(
  val id: String,
  val title: String,
  val theme: String,
  val hashtag: String,
  val description: String,
  val guidelines: List<String>,
  val prizeInfo: String,
  val entries: List<DanceEntry>
)

data class DanceEntry(
  val id: String,
  val dancerOrCrew: String,
  val title: String,
  val lga: String,
  val videoUrl: String,
  val votesCount: Int,
  val isVotedByMe: Boolean = false
)

data class DanceSubmission(
  val id: String = System.currentTimeMillis().toString(),
  val dancerOrCrew: String,
  val title: String,
  val lga: String,
  val videoUrl: String,
  val description: String,
  val dateSubmitted: String
)

enum class TalentCategory(val displayName: String) {
  SINGER("Vocalist & Musician"),
  DANCER("Dancer & Choreographer"),
  COMEDIAN("Comedian & Humorist"),
  ACTOR("Actor & Dramatist"),
  SPOKEN_WORD("Spoken Word & Poet"),
  VISUAL_ARTIST("Visual Artist & Sculptor")
}

data class TalentProfile(
  val id: String,
  val fullName: String,
  val stageName: String,
  val category: TalentCategory,
  val originLga: String,
  val bio: String,
  val highlights: List<String>,
  val portfolioUrl: String,
  val isTalentOfWeek: Boolean = false
)

data class TalentNomination(
  val id: String = System.currentTimeMillis().toString(),
  val nomineeName: String,
  val category: String,
  val nomineeLga: String,
  val reason: String,
  val nominatorName: String,
  val dateNominated: String
)

data class CultureQuizQuestion(
  val id: Int,
  val question: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanation: String,
  val category: String
)

data class QuizLeaderboardEntry(
  val id: String,
  val playerName: String,
  val score: Int,
  val totalQuestions: Int,
  val badge: String,
  val lga: String,
  val date: String
)

data class LightTvVideo(
  val id: String,
  val title: String,
  val category: String,
  val duration: String,
  val description: String,
  val officialUrl: String,
  val presenterOrProducer: String
)
