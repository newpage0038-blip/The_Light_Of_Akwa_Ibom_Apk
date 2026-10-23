package com.example.data

import com.example.data.model.BusinessListing
import com.example.data.model.CommunityAnnouncement
import com.example.data.model.CommunityEvent
import com.example.data.model.CommunityPoll
import com.example.data.model.CultureItem
import com.example.data.model.DiscussionComment
import com.example.data.model.DiscussionPost
import com.example.data.model.NewsArticle
import com.example.data.model.OpportunityItem
import com.example.data.model.UserProfile

object InitialData {
  val LGAS = listOf(
    "Abak", "Eastern Obolo", "Eket", "Esit Eket", "Essien Udim", "Etim Ekpo", "Etinan",
    "Ibeno", "Ibesikpo Asutan", "Ibiono Ibom", "Ika", "Ikono", "Ikot Abasi", "Ikot Ekpene",
    "Ini", "Itu", "Mbo", "Mkpat Enin", "Nsit Atai", "Nsit Ibom", "Nsit Ubium", "Obot Akara",
    "Okobo", "Onna", "Oron", "Oruk Anam", "Udung Uko", "Ukanafun", "Uruan", "Urue-Offong/Oruko", "Uyo"
  )

  val ANNOUNCEMENTS = listOf(
    CommunityAnnouncement(
      title = "Welcome to The Light of Akwa Ibom!",
      content = "We are united by hope and driven by purpose. Welcome to our official community platform. Brighter we shine!",
      date = "October 2026",
      tag = "Priority"
    ),
    CommunityAnnouncement(
      title = "Akwa Ibom Youth Innovation Summit 2026",
      content = "Registration is now open for all creatives, tech builders, and entrepreneurs across all 31 LGAs.",
      date = "November 2026",
      tag = "Event"
    ),
    CommunityAnnouncement(
      title = "Community Clean & Green Initiative",
      content = "Join your neighborhood ward this Saturday for community beautification and tree planting across Uyo, Eket, and Ikot Ekpene.",
      date = "This Weekend",
      tag = "Update"
    )
  )

  val NEWS = listOf(
    NewsArticle(
      title = "Ibom Air Expands Regional Routes with New Aircraft Fleet",
      category = "Development",
      date = "Oct 22, 2026",
      summary = "Akwa Ibom's premier airline adds state-of-the-art Airbus jets, cementing its reputation as Nigeria's most reliable carrier.",
      fullContent = "Ibom Air continues to set the benchmark in Nigerian and West African aviation. Operating from its modern hub at Victor Attah International Airport in Uyo, the airline has celebrated high on-time flight dispatch reliability and announced direct flights connecting regional hubs. The airline represents the visionary spirit of Akwa Ibom State, showing the world what purposeful governance and committed people can achieve together.",
      readTime = "3 min read",
      isFeatured = true
    ),
    NewsArticle(
      title = "Victor Attah International Airport Smart Terminal & MRO Complete",
      category = "Infrastructure",
      date = "Oct 19, 2026",
      summary = "The smart international terminal and Maintenance, Repair & Overhaul facility open for global commercial operations.",
      fullContent = "The newly completed terminal features automated biometric check-in gates, digitised luggage handling, and expansive retail and cultural showcases. The world-class MRO facility is poised to attract airlines from across Africa for certified maintenance, creating over 2,500 direct technical and engineering jobs for local youth.",
      readTime = "4 min read",
      isFeatured = false
    ),
    NewsArticle(
      title = "Dakkada Enterprise Fund Empowers 10,000 Micro-Entrepreneurs",
      category = "Education & Tech",
      date = "Oct 15, 2026",
      summary = "Young farmers, fashion designers, and digital artisans receive non-repayable empowerment seed grants.",
      fullContent = "In alignment with the Dakkada philosophy ('Rise to Greatness'), beneficiaries from all 31 Local Government Areas participated in intensive business development mentorship before receiving seed capital. The beneficiaries showcased products ranging from refined palm oil to automated software systems.",
      readTime = "3 min read",
      isFeatured = true
    ),
    NewsArticle(
      title = "Akwa United Celebrates Historic Victory at the Nest of Champions",
      category = "Culture & Sports",
      date = "Oct 10, 2026",
      summary = "Over 28,000 passionate fans turn out in orange and green as Akwa United seals decisive league triumph.",
      fullContent = "The Godswill Akpabio International Stadium was a spectacle of roaring chants, vibrant banners, and unity. The 'Promise Keepers' displayed high tactical discipline and athletic prowess, inspiring thousands of young aspiring athletes across the state.",
      readTime = "2 min read",
      isFeatured = false
    )
  )

  val CULTURE = listOf(
    CultureItem(
      title = "Amesiere",
      type = "Language",
      subtitle = "Ibibio Greeting",
      description = "Traditional morning greeting wishing peace, vitality, and dawn upon your home.",
      details = "Spoken throughout Akwa Ibom. The standard reply is 'Amesiere kaba' or 'Moyo'. It conveys warmth, neighborly affection, and the wish for a bright day.",
      localPhrase = "Amesiere",
      meaning = "Good morning / May you see the light of dawn in peace"
    ),
    CultureItem(
      title = "Sosongo",
      type = "Language",
      subtitle = "Expression of Gratitude",
      description = "Deep heartfelt appreciation in Ibibio, Annang, and related state dialects.",
      details = "To say 'Thank you very much', one says 'Sosongo etieti'. Gratitude and mutual respect are foundational pillars of Akwa Ibom hospitality.",
      localPhrase = "Sosongo etieti",
      meaning = "Thank you very much"
    ),
    CultureItem(
      title = "Abadie",
      type = "Language",
      subtitle = "Casual & Caring Inquiry",
      description = "How are you? How are things with you and your family?",
      details = "The traditional warm reply is 'Idem mfo owo?' (How is your body?) or 'Idem sọñ' (I am strong and well!).",
      localPhrase = "Abadie? Idem sọñ?",
      meaning = "How are you? I am strong and healthy!"
    ),
    CultureItem(
      title = "Afang Soup",
      type = "Delicacy",
      subtitle = "Crowned Queen of Akwa Ibom Soups",
      description = "Rich, aromatic soup prepared with fresh shredded Afang leaves, tender waterleaf, periwinkles, and choice meats.",
      details = "Afang is universally celebrated across Nigeria and the world. Cooked with seasoned stockfish, dried catfish, periwinkles with shells intact for suction flavor, and pure virgin palm oil. Best enjoyed with pounded yam, fufu, or garri.",
      localPhrase = "Udia Afang",
      meaning = "Royal Afang Delicacy"
    ),
    CultureItem(
      title = "Edikang Ikong",
      type = "Delicacy",
      subtitle = "Nutrient-Dense Heritage Stew",
      description = "Prepared with pumpkin leaves (Ugwu), tender waterleaf, assorted meats, cow leg, and dried seafood.",
      details = "Traditionally reserved for royalty and celebrations, this soup uses almost zero water, relying on the natural juices of the vegetables and thick palm oil broth. It represents vitality and strength.",
      localPhrase = "Edikang Ikong",
      meaning = "Traditional Vegetable Pottage"
    ),
    CultureItem(
      title = "Ekpang Nkukwo",
      type = "Delicacy",
      subtitle = "Cocoyam Wrapped Masterpiece",
      description = "Grated water yam and cocoyam wrapped in tender cocoyam leaves, simmered in palm oil with periwinkles.",
      details = "A labor of love often cooked collectively during family reunions and cultural ceremonies. Garnished with scent leaves and red pepper.",
      localPhrase = "Ekpang Nkukwo",
      meaning = "Ceremonial Cocoyam Wrap"
    ),
    CultureItem(
      title = "Ekombi Dance",
      type = "Dance",
      subtitle = "The Dance of Grace and Modesty",
      description = "A graceful rhythmic dance by maidens dressed in colorful beads, imitating the gentle movements of ocean waves.",
      details = "Accompanied by rhythmic brass gongs and drums, dancers exhibit elegance, poise, and cultural pride. It is performed during major cultural festivals and state welcomes.",
      localPhrase = "Unok Ekombi",
      meaning = "Ocean Wave Maiden Dance"
    ),
    CultureItem(
      title = "Ibom Christmas Village",
      type = "Festival",
      subtitle = "Africa's Longest Festive Gathering",
      description = "A 32-day extravaganza featuring lights, continuous live music, culinary delights, and creative exhibitions.",
      details = "Hosted at the Unity Park and cultural grounds in Uyo, attracting hundreds of thousands of tourists annually. A celebration of joy, community, and economic vitality.",
      localPhrase = "Usoro Usen Christmas",
      meaning = "Celebration of Joy and Light"
    ),
    CultureItem(
      title = "Godswill Akpabio International Stadium",
      type = "Landmark",
      subtitle = "Nest of Champions, Uyo",
      description = "A 30,000-seat ultra-modern stadium with world-class facilities modeled after Allianz Arena.",
      details = "Home of the Nigerian Super Eagles and Akwa United. A shining symbol of Akwa Ibom's infrastructure excellence and sports culture.",
      localPhrase = "Nest of Champions",
      meaning = "Stadium of Excellence"
    )
  )

  val OPPORTUNITIES = listOf(
    OpportunityItem(
      title = "Ibom Innovation Hub Software Engineering Fellowship",
      category = "Training & Skills",
      organization = "Ibom Innovation Network",
      location = "Uyo / Hybrid",
      deadline = "Nov 15, 2026",
      description = "6-month intensive full-stack mobile and cloud development bootcamp with monthly stipends and internship placement.",
      contactOrUrl = "apply@ibominnovation.ng"
    ),
    OpportunityItem(
      title = "Akwa Ibom State Postgraduate STEM Scholarship",
      category = "Scholarship",
      organization = "State Scholarship Board",
      location = "Uyo",
      deadline = "Dec 01, 2026",
      description = "Full tuition and research grant for indigenous students pursuing Master's and PhD degrees in renewable energy, AI, and aviation.",
      contactOrUrl = "scholarships@akwaibomstate.gov.ng"
    ),
    OpportunityItem(
      title = "Aviation Maintenance Technician Apprenticeship",
      category = "Job Vacancy",
      organization = "Victor Attah International Airport MRO",
      location = "Uyo Airport Complex",
      deadline = "Nov 20, 2026",
      description = "Hands-on apprenticeship for graduates of mechanical and electrical engineering to train under certified aeronautical engineers.",
      contactOrUrl = "careers@ibomairport.ng"
    ),
    OpportunityItem(
      title = "Dakkada Agri-Business Grant for Cassava & Palm Oil",
      category = "Business Grant",
      organization = "Ministry of Agriculture & Rural Development",
      location = "All 31 LGAs",
      deadline = "Dec 10, 2026",
      description = "Up to 1,500,000 NGN in equipment and zero-interest working capital for verified cooperative farming groups.",
      contactOrUrl = "agri-empowerment@akwaibom.gov.ng"
    ),
    OpportunityItem(
      title = "Ibom Cultural Troupe & Creative Auditions",
      category = "Auditions",
      organization = "Akwa Ibom Arts & Culture Council",
      location = "Ibom Hall, Uyo",
      deadline = "Nov 08, 2026",
      description = "Auditions for traditional vocalists, drummers, and Ekombi dancers for the upcoming State Heritage World Tour.",
      contactOrUrl = "culture@akwaibom.org"
    )
  )

  val BUSINESSES = listOf(
    BusinessListing(
      businessName = "Uyo Heritage Culinary Kitchen",
      category = "Catering & Delicacies",
      description = "Authentic Afang, Edikang Ikong, Ekpang Nkukwo, and Fishermen Soup made with fresh local ingredients.",
      lga = "Uyo",
      address = "42 Oron Road, Uyo",
      phoneNumber = "+234 803 123 4567",
      whatsapp = "2348031234567",
      ownerName = "Inemesit Akpan",
      rating = 4.9f
    ),
    BusinessListing(
      businessName = "Raffia City Royal Crafts & Wears",
      category = "Fashion & Crafts",
      description = "Handwoven raffia footwear, ceremonial hats, custom George wrappers, and indigenous beaded accessories.",
      lga = "Ikot Ekpene",
      address = "12 Raffia Market Road, Ikot Ekpene",
      phoneNumber = "+234 802 987 6543",
      whatsapp = "2348029876543",
      ownerName = "Bassey Okon",
      rating = 4.8f
    ),
    BusinessListing(
      businessName = "Ibom Code & Cloud Studio",
      category = "Tech & Digital",
      description = "Mobile apps, enterprise software, UI/UX design, and IT training for local businesses and organizations.",
      lga = "Uyo",
      address = "8 Banking Layout, Udo Udoma Avenue, Uyo",
      phoneNumber = "+234 814 555 0199",
      whatsapp = "2348145550199",
      ownerName = "Edidiong Umoh",
      rating = 5.0f
    ),
    BusinessListing(
      businessName = "Oil Palm Haven & Agro Processors",
      category = "Agriculture & Trade",
      description = "Unadulterated virgin red palm oil, high-grade cassava flour, and organic crayfish supply.",
      lga = "Abak",
      address = "Plot 5 Industrial Layout, Abak",
      phoneNumber = "+234 805 444 3210",
      whatsapp = "2348054443210",
      ownerName = "Ekaette Udom",
      rating = 4.7f
    ),
    BusinessListing(
      businessName = "Eket Coastal Maritime Logistics",
      category = "Services",
      description = "Marine transport logistics, equipment leasing, and safety consultancy for oil and gas and fishing fleets.",
      lga = "Eket",
      address = "18 Marina Road, Eket",
      phoneNumber = "+234 806 777 8899",
      whatsapp = "2348067778899",
      ownerName = "Effiong Asuquo",
      rating = 4.8f
    )
  )

  val EVENTS = listOf(
    CommunityEvent(
      title = "Akwa Ibom Grand Cultural Carnival",
      category = "Cultural Celebration",
      date = "Saturday, Nov 14, 2026",
      time = "10:00 AM",
      venue = "Ibom Hall Grounds, IBB Way",
      lga = "Uyo",
      description = "Parade of all 31 LGAs in traditional attire, featuring Ekombi dancers, raffia displays, and live musical troupes.",
      attendeesCount = 340,
      isRegistered = true,
      isBookmarked = true
    ),
    CommunityEvent(
      title = "Tech Builders & Creative Founders Meetup",
      category = "Networking",
      date = "Thursday, Nov 19, 2026",
      time = "4:00 PM",
      venue = "Ibom Innovation Hub",
      lga = "Uyo",
      description = "Connect with local software developers, product designers, and investors building technology from Akwa Ibom.",
      attendeesCount = 85,
      isRegistered = false,
      isBookmarked = false
    ),
    CommunityEvent(
      title = "Statewide Town Hall on Agriculture & Food Security",
      category = "Town Hall",
      date = "Saturday, Nov 28, 2026",
      time = "11:00 AM",
      venue = "Eket Township Stadium Pavilion",
      lga = "Eket",
      description = "Open community discussion on expanding cooperative palm oil mills and youth involvement in agro-exports.",
      attendeesCount = 120,
      isRegistered = false,
      isBookmarked = true
    )
  )

  val DISCUSSIONS = listOf(
    DiscussionPost(
      authorName = "Idorenyin Essien",
      authorLga = "Uyo",
      title = "How can we create a digital marketplace for our Ikot Ekpene raffia artisans?",
      content = "Our weavers in Ikot Ekpene produce world-class raffia bags and shoes, but many lack direct access to national and international buyers. If we organize digital cataloging and cooperative shipping through The Light of Akwa Ibom, we can multiply their earnings. What are your ideas?",
      category = "Economy & Innovation",
      timestamp = System.currentTimeMillis() - 3600000 * 5,
      likesCount = 38,
      commentsCount = 7,
      isApproved = true,
      isLikedByMe = true
    ),
    DiscussionPost(
      authorName = "Aniekan Bassey",
      authorLga = "Oron",
      title = "Preserving the Oron and Annang dialects alongside Ibibio",
      content = "It is beautiful that our state has rich sister languages. Let us encourage our youth to speak and write their native mother tongues with pride. What community initiatives can we run in schools to celebrate our linguistic heritage?",
      category = "Cultural Heritage",
      timestamp = System.currentTimeMillis() - 3600000 * 18,
      likesCount = 52,
      commentsCount = 12,
      isApproved = true,
      isLikedByMe = false
    ),
    DiscussionPost(
      authorName = "Mfonobong Udoh",
      authorLga = "Eket",
      title = "Mentoring our secondary school students in STEM and Software",
      content = "We have many talented engineers and IT specialists here. Let us volunteer our weekends to run free robotics and coding sessions for young students in public schools across our local government areas.",
      category = "Youth Empowerment",
      timestamp = System.currentTimeMillis() - 3600000 * 32,
      likesCount = 44,
      commentsCount = 9,
      isApproved = true,
      isLikedByMe = true
    )
  )

  val POLLS = listOf(
    CommunityPoll(
      question = "Which community project should The Light of Akwa Ibom organize next?",
      option1 = "Youth Digital & Coding Skills Bootcamp",
      votes1 = 142,
      option2 = "Local Food & Cultural Heritage Fair",
      votes2 = 98,
      option3 = "Artisan Micro-Grant Pitch Competition",
      votes3 = 115,
      userVotedOption = 1
    ),
    CommunityPoll(
      question = "What is your favorite iconic Akwa Ibom delicacy?",
      option1 = "Afang Soup with Periwinkles",
      votes1 = 265,
      option2 = "Edikang Ikong with Assorted Seafood",
      votes2 = 180,
      option3 = "Ekpang Nkukwo Hot Pottage",
      votes3 = 145,
      userVotedOption = 0
    )
  )

  val DEFAULT_PROFILE = UserProfile(
    id = 1,
    fullName = "Anietie Udoh",
    lga = "Uyo",
    occupation = "Digital Creator & Community Volunteer",
    bio = "Proud Akwa Ibomite passionate about community empowerment, unity, and youth tech. Brighter we shine!",
    memberId = "AKWA-LIGHT-2026-4821",
    isRegistered = true,
    joinedDate = "October 2026",
    chantsCount = 18
  )
}
