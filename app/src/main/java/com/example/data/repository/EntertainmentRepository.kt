package com.example.data.repository

import com.example.data.model.ComedyComment
import com.example.data.model.ComedyItem
import com.example.data.model.ComedySubmission
import com.example.data.model.CultureQuizQuestion
import com.example.data.model.DanceChallenge
import com.example.data.model.DanceEntry
import com.example.data.model.DanceSubmission
import com.example.data.model.LightTvVideo
import com.example.data.model.MusicArtist
import com.example.data.model.QuizLeaderboardEntry
import com.example.data.model.SongSubmission
import com.example.data.model.TalentCategory
import com.example.data.model.TalentNomination
import com.example.data.model.TalentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntertainmentRepository {

  // 1. Music Zone State
  private val _featuredArtist = MutableStateFlow(
    MusicArtist(
      id = "m_featured_1",
      name = "Progress Effiong",
      stageName = "Progress Effiong",
      genre = "Gospel & Inspirational Afrobeats",
      bio = "Award-winning Akwa Ibom gospel minister, producer, and composer. Celebrated for uplifting hearts worldwide with songs celebrating divine grace and cultural heritage.",
      originLga = "Uyo LGA",
      topTracks = listOf("Amanam", "Miracle Worker", "Iban Uyo", "Gbejem"),
      officialLink = "https://www.youtube.com/results?search_query=Progress+Effiong+Amanam",
      isFeatured = true,
      badgeText = "Featured Artist of the Week"
    )
  )
  val featuredArtist: StateFlow<MusicArtist> = _featuredArtist.asStateFlow()

  private val _artistsList = MutableStateFlow(
    listOf(
      MusicArtist(
        id = "m1",
        name = "Michael Mish",
        stageName = "Mish",
        genre = "Highlife / Afro-Pop",
        bio = "Legendary singer whose smash hit 'Akwa Ibom Ayaya' made the beauty, culture, and progress of Akwa Ibom famous across Nigeria and the diaspora.",
        originLga = "Ikot Ekpene LGA",
        topTracks = listOf("Akwa Ibom Ayaya", "Every Woman is Beautiful", "Uyo Boy"),
        officialLink = "https://www.youtube.com/results?search_query=Mish+Akwa+Ibom+Ayaya",
        badgeText = "Akwa Ibom Icon"
      ),
      MusicArtist(
        id = "m2",
        name = "Innocent Udeme Udofot",
        stageName = "MC Galaxy",
        genre = "Afrobeats & Dancehall",
        bio = "Internationally acclaimed dancer, singer, and performer who originated the viral 'Sekem' dance trend, showcasing the energetic rhythm of Akwa Ibom.",
        originLga = "Uyo LGA",
        topTracks = listOf("Sekem", "Hello", "Snapchat", "Oyaa"),
        officialLink = "https://www.youtube.com/results?search_query=MC+Galaxy+Sekem",
        badgeText = "King of New Dance"
      ),
      MusicArtist(
        id = "m3",
        name = "Freke Umoh",
        stageName = "Freke Umoh",
        genre = "Contemporary Gospel",
        bio = "Internationally renowned worship singer and vocal powerhouse who has represented Akwa Ibom on global gospel stages with passion and spiritual depth.",
        originLga = "Etinan LGA",
        topTracks = listOf("Deserve", "A Dondo", "Hallelujah No Go Finish", "More Than Enough"),
        officialLink = "https://www.youtube.com/results?search_query=Freke+Umoh+A+Dondo",
        badgeText = "Gospel Ambassador"
      ),
      MusicArtist(
        id = "m4",
        name = "Moses Bliss Uyoh Enang",
        stageName = "Moses Bliss",
        genre = "Gospel Praise & Worship",
        bio = "Chart-topping gospel artist whose faith-filled music has garnered tens of millions of streams globally, inspiring youth across the continent.",
        originLga = "Akwa Ibom State",
        topTracks = listOf("Too Faithful", "Bigger Everyday", "Miracle No Dey Tire Jesus", "Daddy Wey Dey Pamper"),
        officialLink = "https://www.youtube.com/results?search_query=Moses+Bliss+Too+Faithful",
        badgeText = "Global Chart Topper"
      ),
      MusicArtist(
        id = "m5",
        name = "Sir James & Cultural Troupe",
        stageName = "Sir James Cultural Ensemble",
        genre = "Traditional Folk & Uta Rhythms",
        bio = "Preservers of ancient Ibibio brass horns, xylophones, and royal Ekombi rhythms, keeping our authentic ancestry alive for current and future generations.",
        originLga = "Abak LGA",
        topTracks = listOf("Uta Royal Salute", "Edidem Rhythm", "Ayaya Mfon", "Ibong Dance"),
        officialLink = "https://www.youtube.com/results?search_query=Akwa+Ibom+Traditional+Uta+Music",
        badgeText = "Cultural Heritage"
      )
    )
  )
  val artistsList: StateFlow<List<MusicArtist>> = _artistsList.asStateFlow()

  private val _songSubmissions = MutableStateFlow<List<SongSubmission>>(emptyList())
  val songSubmissions: StateFlow<List<SongSubmission>> = _songSubmissions.asStateFlow()

  fun submitSong(artistName: String, songTitle: String, genre: String, link: String, bio: String, contact: String) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val submission = SongSubmission(
      artistName = artistName,
      songTitle = songTitle,
      genre = genre,
      officialLink = link,
      bio = bio,
      contact = contact,
      dateSubmitted = dateStr
    )
    _songSubmissions.value = listOf(submission) + _songSubmissions.value
  }

  // 2. Comedy Corner State
  private val _comedySkits = MutableStateFlow(
    listOf(
      ComedyItem(
        id = "c1",
        title = "The Uyo Suya Special Investigation",
        creator = "Sabinus & Akwa Humour",
        duration = "3:45",
        videoUrl = "https://www.youtube.com/results?search_query=Sabinus+Comedy+Skits",
        description = "Investor Sabinus attempts to purchase all the suya in Itam Market with funny promises and hilarious confusion.",
        likesCount = 342,
        comments = listOf(
          ComedyComment("cc1", "Etim Bassey", "This made my entire weekend! Sabinus never fails.", "2 hours ago"),
          ComedyComment("cc2", "Blessing Udoh", "The Akwa Ibom accent was spot on!", "5 hours ago")
        )
      ),
      ComedyItem(
        id = "c2",
        title = "Akwa Ibom Village Meeting Resolutions",
        creator = "Sir James & Friends",
        duration = "4:20",
        videoUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+Comedy+Skits",
        description = "A comical portrayal of a community youth meeting deciding who will organize the festive football match.",
        likesCount = 289,
        comments = listOf(
          ComedyComment("cc3", "Idongesit", "Pure authentic humor from the grassroots!", "1 day ago")
        )
      ),
      ComedyItem(
        id = "c3",
        title = "When You Eat Afang Soup in Lagos",
        creator = "Mark Angel & Akwa Crew",
        duration = "2:50",
        videoUrl = "https://www.youtube.com/results?search_query=Mark+Angel+Comedy",
        description = "Hilarious encounter when a Lagos cook claims to prepare genuine Akwa Ibom Afang with waterleaf substitute.",
        likesCount = 412,
        comments = listOf(
          ComedyComment("cc4", "Aniefiok", "Nothing can replace authentic Afang! Fact!", "1 day ago")
        )
      )
    )
  )
  val comedySkits: StateFlow<List<ComedyItem>> = _comedySkits.asStateFlow()

  fun toggleComedyLike(id: String) {
    _comedySkits.value = _comedySkits.value.map { item ->
      if (item.id == id) {
        val newLiked = !item.isLikedByMe
        val newCount = if (newLiked) item.likesCount + 1 else (item.likesCount - 1).coerceAtLeast(0)
        item.copy(isLikedByMe = newLiked, likesCount = newCount)
      } else item
    }
  }

  fun addComedyComment(comedyId: String, text: String, author: String) {
    _comedySkits.value = _comedySkits.value.map { item ->
      if (item.id == comedyId) {
        val newComment = ComedyComment(
          id = "cc_${System.currentTimeMillis()}",
          authorName = author,
          text = text,
          timeAgo = "Just now"
        )
        item.copy(comments = item.comments + newComment)
      } else item
    }
  }

  private val _comedySubmissions = MutableStateFlow<List<ComedySubmission>>(emptyList())
  val comedySubmissions: StateFlow<List<ComedySubmission>> = _comedySubmissions.asStateFlow()

  fun submitComedy(title: String, creator: String, videoUrl: String, desc: String) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val item = ComedySubmission(
      title = title,
      creatorName = creator,
      videoUrl = videoUrl,
      description = desc,
      dateSubmitted = dateStr
    )
    _comedySubmissions.value = listOf(item) + _comedySubmissions.value
  }

  // 3. Dance Challenge State
  private val _danceChallenge = MutableStateFlow(
    DanceChallenge(
      id = "dc_main",
      title = "Brighter We Shine Dance Challenge",
      theme = "Celebrating Unity, Joy & Dakkada Spirit",
      hashtag = "#BrighterWeShineDance #AkwaIbomShine",
      description = "Showcase your energy, creativity, and Akwa Ibom pride! Incorporate our signature celebratory hand-wave and authentic moves into your routine.",
      guidelines = listOf(
        "Use positive Akwa Ibom or Afrobeats music",
        "Include the signature 'Brighter We Shine' gesture at the climax",
        "Keep performances family-friendly and respectful of community heritage",
        "Solo, duo, and group entries are warmly welcome!"
      ),
      prizeInfo = "Top 3 most-voted community entries will be spotlighted at the State Gala & Light of Akwa Ibom Festival!",
      entries = listOf(
        DanceEntry(
          id = "de1",
          dancerOrCrew = "Dakkada Youth Dance Troupe",
          title = "Ekombi Fusion: Modern Meets Heritage",
          lga = "Uyo LGA",
          videoUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+Ekombi+Dance",
          votesCount = 184
        ),
        DanceEntry(
          id = "de2",
          dancerOrCrew = "Raffia City Krumpers",
          title = "Energy in Ikot Ekpene",
          lga = "Ikot Ekpene LGA",
          videoUrl = "https://www.youtube.com/results?search_query=Nigerian+Afrobeats+Dance+Battle",
          votesCount = 142
        ),
        DanceEntry(
          id = "de3",
          dancerOrCrew = "Eket Coastal Dancers",
          title = "Fisherman & Palm Wave Groove",
          lga = "Eket LGA",
          videoUrl = "https://www.youtube.com/results?search_query=African+Traditional+Dance+Celebration",
          votesCount = 129
        )
      )
    )
  )
  val danceChallenge: StateFlow<DanceChallenge> = _danceChallenge.asStateFlow()

  fun voteDanceEntry(entryId: String) {
    val challenge = _danceChallenge.value
    val updatedEntries = challenge.entries.map { entry ->
      if (entry.id == entryId) {
        val newVoted = !entry.isVotedByMe
        val count = if (newVoted) entry.votesCount + 1 else (entry.votesCount - 1).coerceAtLeast(0)
        entry.copy(isVotedByMe = newVoted, votesCount = count)
      } else entry
    }
    _danceChallenge.value = challenge.copy(entries = updatedEntries)
  }

  private val _danceSubmissions = MutableStateFlow<List<DanceSubmission>>(emptyList())
  val danceSubmissions: StateFlow<List<DanceSubmission>> = _danceSubmissions.asStateFlow()

  fun submitDanceEntry(dancer: String, title: String, lga: String, url: String, desc: String) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val sub = DanceSubmission(
      dancerOrCrew = dancer,
      title = title,
      lga = lga,
      videoUrl = url,
      description = desc,
      dateSubmitted = dateStr
    )
    _danceSubmissions.value = listOf(sub) + _danceSubmissions.value
  }

  // 4. Light of Talent State
  private val _talentOfTheWeek = MutableStateFlow(
    TalentProfile(
      id = "totw_1",
      fullName = "Aniekeme Ekong",
      stageName = "Akwa Bard",
      category = TalentCategory.SPOKEN_WORD,
      originLga = "Oron LGA",
      bio = "Passionate poet and cultural chronicler whose powerful spoken-word performances celebrate our mangrove roots, language preservation, and youth resilience.",
      highlights = listOf(
        "Winner, South-South Spoken Word Slam 2024",
        "Author of 'Rivers of Gold and Oil'",
        "Featured orator at Akwa Ibom Youth Summit"
      ),
      portfolioUrl = "https://www.youtube.com/results?search_query=Spoken+Word+Poetry+Nigeria",
      isTalentOfWeek = true
    )
  )
  val talentOfTheWeek: StateFlow<TalentProfile> = _talentOfTheWeek.asStateFlow()

  private val _talentsList = MutableStateFlow(
    listOf(
      TalentProfile(
        id = "tp1",
        fullName = "Emem Bassey",
        stageName = "Melody Queen",
        category = TalentCategory.SINGER,
        originLga = "Itu LGA",
        bio = "Soulful soprano vocalist blending classical choir training with contemporary Afrosoul melodies.",
        highlights = listOf("Lead soloist at State Carol Festival", "Debut EP 'Light of Dawn'"),
        portfolioUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+Gospel+Voices"
      ),
      TalentProfile(
        id = "tp2",
        fullName = "Kufre Okon",
        stageName = "K-Laughs",
        category = TalentCategory.COMEDIAN,
        originLga = "Abak LGA",
        bio = "Clean family comedian skilled in bilingual English/Ibibio punchlines that unite audiences of all ages.",
        highlights = listOf("Resident MC at Uyo Laugh Fest", "Over 50 community shows hosted"),
        portfolioUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+Standup+Comedy"
      ),
      TalentProfile(
        id = "tp3",
        fullName = "Victoria Udoh",
        stageName = "Vic Canvas",
        category = TalentCategory.VISUAL_ARTIST,
        originLga = "Ikot Ekpene LGA",
        bio = "Contemporary painter and raffia weaver revitalizing ancestral Akwa Ibom motifs with acrylic on canvas.",
        highlights = listOf("Exhibited at National Museum Uyo", "Craft educator for secondary school girls"),
        portfolioUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+Raffia+Art+and+Craft"
      )
    )
  )
  val talentsList: StateFlow<List<TalentProfile>> = _talentsList.asStateFlow()

  private val _talentNominations = MutableStateFlow<List<TalentNomination>>(emptyList())
  val talentNominations: StateFlow<List<TalentNomination>> = _talentNominations.asStateFlow()

  fun nominateTalent(nominee: String, category: String, lga: String, reason: String, nominator: String) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val nom = TalentNomination(
      nomineeName = nominee,
      category = category,
      nomineeLga = lga,
      reason = reason,
      nominatorName = nominator,
      dateNominated = dateStr
    )
    _talentNominations.value = listOf(nom) + _talentNominations.value
  }

  // 5. Culture and Quiz State
  val quizQuestions: List<CultureQuizQuestion> = listOf(
    CultureQuizQuestion(
      id = 1,
      question = "What is the greeting meaning 'How are you?' in the Ibibio and Annang languages?",
      options = listOf("Sosongo", "Abadie", "Idara", "Mfoniso"),
      correctOptionIndex = 1,
      explanation = "'Abadie' means 'How are you?' or 'What is happening?'. 'Sosongo' means 'Thank you'.",
      category = "Language"
    ),
    CultureQuizQuestion(
      id = 2,
      question = "Which famous Akwa Ibom delicacy is prepared using Afang (Gnetum africanum) leaves and waterleaf?",
      options = listOf("Edikang Ikong", "Afang Soup", "Ekpang Nkukwo", "Atama Soup"),
      correctOptionIndex = 1,
      explanation = "Afang soup is world-famous, rich in nutrients, and made by pounding dried Afang leaves with fresh waterleaf.",
      category = "Cuisine"
    ),
    CultureQuizQuestion(
      id = 3,
      question = "Which historic town in Akwa Ibom is globally celebrated as 'The Raffia City' for its supreme cane and wood carvings?",
      options = listOf("Oron", "Eket", "Ikot Ekpene", "Abak"),
      correctOptionIndex = 2,
      explanation = "Ikot Ekpene was the first premier local government authority in West Africa and is internationally known as 'The Raffia City'.",
      category = "Heritage"
    ),
    CultureQuizQuestion(
      id = 4,
      question = "In which Local Government Area did Scottish missionary Mary Slessor live and carry out her historic work ending the killing of twins?",
      options = listOf("Itu LGA", "Ikot Abasi LGA", "Ibiono Ibom LGA", "Nsit Ubium LGA"),
      correctOptionIndex = 0,
      explanation = "Mary Slessor lived and worked in Itu LGA, where her cairn, monument, and missionary house remain cherished historical landmarks.",
      category = "History"
    ),
    CultureQuizQuestion(
      id = 5,
      question = "What is the state slogan and inspiring motto of 'The Light of Akwa Ibom'?",
      options = listOf("Forward Ever!", "Brighter We Shine!", "Rising Together!", "Peace and Progress!"),
      correctOptionIndex = 1,
      explanation = "'BRIGHTER WE SHINE!' is the vibrant call and response representing unity, hope, and excellence across our people.",
      category = "State Identity"
    ),
    CultureQuizQuestion(
      id = 6,
      question = "What is the name of the ultra-modern 30,000-seater stadium in Uyo, nicknamed 'The Nest of Champions'?",
      options = listOf("Akwa Arena", "Godswill Akpabio International Stadium", "Uyo Township Grounds", "Dakkada Sports Complex"),
      correctOptionIndex = 1,
      explanation = "The Godswill Akpabio International Stadium is an elite FIFA-standard multi-sports complex in Uyo.",
      category = "Sports & Landmarks"
    ),
    CultureQuizQuestion(
      id = 7,
      question = "Which dish made with grated cocoyam or water yam wrapped in cocoyam leaves is a beloved traditional comfort food?",
      options = listOf("Ekpang Nkukwo", "Otong Soup", "Asa Iwa", "Fisherman Soup"),
      correctOptionIndex = 0,
      explanation = "Ekpang Nkukwo is a cherished delicacy requiring skilled preparation, garnished with dried fish, periwinkles, and red palm oil.",
      category = "Cuisine"
    ),
    CultureQuizQuestion(
      id = 8,
      question = "The historic 1914 building where Lord Lugard proclaimed the Amalgamation of Northern and Southern Nigeria is located in which LGA?",
      options = listOf("Ikot Abasi LGA", "Oron LGA", "Eket LGA", "Uruan LGA"),
      correctOptionIndex = 0,
      explanation = "The Amalgamation House and the Bridge of No Return are located in historic Ikot Abasi (formerly Opobo Division).",
      category = "History"
    )
  )

  private val _leaderboard = MutableStateFlow(
    listOf(
      QuizLeaderboardEntry("lb1", "Anietie Udoh", 8, 8, "Akwa Ibom Scholar", "Uyo", "Today"),
      QuizLeaderboardEntry("lb2", "Idara Bassey", 7, 8, "Dakkada Master", "Ikot Ekpene", "Today"),
      QuizLeaderboardEntry("lb3", "Ndifreke Etim", 7, 8, "Dakkada Master", "Eket", "Yesterday"),
      QuizLeaderboardEntry("lb4", "Kufreabasi Okon", 6, 8, "Light Pioneer", "Oron", "Yesterday")
    )
  )
  val leaderboard: StateFlow<List<QuizLeaderboardEntry>> = _leaderboard.asStateFlow()

  fun recordQuizScore(playerName: String, score: Int, lga: String) {
    val badge = when {
      score >= 8 -> "Akwa Ibom Scholar"
      score >= 6 -> "Dakkada Master"
      score >= 4 -> "Light Pioneer"
      else -> "Heritage Learner"
    }
    val entry = QuizLeaderboardEntry(
      id = "lb_${System.currentTimeMillis()}",
      playerName = playerName,
      score = score,
      totalQuestions = quizQuestions.size,
      badge = badge,
      lga = lga.ifBlank { "Akwa Ibom" },
      date = "Today"
    )
    _leaderboard.value = listOf(entry) + _leaderboard.value
  }

  // 6. Light TV Videos
  private val _lightTvVideos = MutableStateFlow(
    listOf(
      LightTvVideo(
        id = "ltv1",
        title = "Uyo Christmas Village Festival Highlights",
        category = "Festivals & Tourism",
        duration = "8:25",
        description = "Experience the music, lighting, culinary stalls, and vibrant community celebrations at the annual Uyo Christmas Village.",
        officialUrl = "https://www.youtube.com/results?search_query=Uyo+Christmas+Village+Festival",
        presenterOrProducer = "Akwa Ibom Tourism Directorate"
      ),
      LightTvVideo(
        id = "ltv2",
        title = "Discovering Ikot Ekpene: The Raffia City Heritage",
        category = "Cultural Documentary",
        duration = "12:40",
        description = "A deep documentary into the ancient woodcarvers, masquerade masks, and hand-woven raffia craftsmanship passed down generations.",
        officialUrl = "https://www.youtube.com/results?search_query=Ikot+Ekpene+Raffia+Documentary",
        presenterOrProducer = "Heritage Media Group"
      ),
      LightTvVideo(
        id = "ltv3",
        title = "Akwa Ibom State Anthem & Dakkada Choral Symphony",
        category = "Performances",
        duration = "4:50",
        description = "Stirring orchestral rendition of our state anthem, reminding all Akwa Ibomites to rise and shine brightly in character and achievement.",
        officialUrl = "https://www.youtube.com/results?search_query=Akwa+Ibom+State+Anthem+Choir",
        presenterOrProducer = "State Symphony Choir"
      ),
      LightTvVideo(
        id = "ltv4",
        title = "The Nest of Champions: Inside Godswill Akpabio Stadium",
        category = "Sports & Modern Uyo",
        duration = "6:15",
        description = "A visual tour of the iconic sports architecture that has hosted Nigeria's Super Eagles and continental championship finals.",
        officialUrl = "https://www.youtube.com/results?search_query=Godswill+Akpabio+Stadium+Uyo+Tour",
        presenterOrProducer = "Sports Focus Africa"
      )
    )
  )
  val lightTvVideos: StateFlow<List<LightTvVideo>> = _lightTvVideos.asStateFlow()
}
