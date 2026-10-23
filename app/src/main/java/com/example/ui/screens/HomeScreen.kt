package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommunityAnnouncement
import com.example.data.model.CommunityEvent
import com.example.data.model.NewsArticle
import com.example.ui.Screen
import com.example.ui.components.LightGreetingBanner
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun HomeScreen(
  announcements: List<CommunityAnnouncement>,
  news: List<NewsArticle>,
  events: List<CommunityEvent>,
  chantsCount: Int,
  isChanting: Boolean,
  onChantClicked: () -> Unit,
  onNavigate: (Screen) -> Unit,
  onSelectNews: (NewsArticle) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White),
    contentPadding = PaddingValues(bottom = 28.dp)
  ) {
    // Header Greeting Banner
    item {
      Column(modifier = Modifier.padding(16.dp)) {
        LightGreetingBanner(
          chantsCount = chantsCount,
          isChanting = isChanting,
          onChantClicked = onChantClicked
        )
      }
    }

    // Daily Dakkada Inspiration Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RadiantSunLight)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(RadiantSunOrange),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.FormatQuote,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "DAKKADA PHILOSOPHY (RISE TO GREATNESS)",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              ),
              color = RadiantSunOrange
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "\"Akwa Ibomite, arise to greatness! You are created for impact, unity, and excellence. Brighter we shine!\"",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFF422006)
              )
            )
          }
        }
      }
    }

    // Community Announcements Carousel
    if (announcements.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "COMMUNITY ANNOUNCEMENTS",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            ),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(announcements) { item ->
            AnnouncementCard(item)
          }
        }
      }
    }

    // 10 Main Navigation Sections Grid
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "EXPLORE THE LIGHT PLATFORM",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        ),
        color = AkwaGreenDark,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
      Spacer(modifier = Modifier.height(12.dp))

      Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MenuTile(
            screen = Screen.ABOUT,
            subtitle = "Vision & Values",
            color = AkwaGreenPrimary,
            bgColor = AkwaGreenLight,
            onClick = { onNavigate(Screen.ABOUT) },
            modifier = Modifier.weight(1f)
          )
          MenuTile(
            screen = Screen.COMMUNITY,
            subtitle = "Discussions & Polls",
            color = RadiantSunOrange,
            bgColor = RadiantSunLight,
            onClick = { onNavigate(Screen.COMMUNITY) },
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MenuTile(
            screen = Screen.NEWS,
            subtitle = "State Developments",
            color = Color(0xFF0284C7),
            bgColor = Color(0xFFE0F2FE),
            onClick = { onNavigate(Screen.NEWS) },
            modifier = Modifier.weight(1f)
          )
          MenuTile(
            screen = Screen.CULTURE,
            subtitle = "Food, Dance, Language",
            color = Color(0xFFD97706),
            bgColor = Color(0xFFFEF3C7),
            onClick = { onNavigate(Screen.CULTURE) },
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MenuTile(
            screen = Screen.OPPORTUNITIES,
            subtitle = "Jobs, Grants, Skills",
            color = Color(0xFF16A34A),
            bgColor = Color(0xFFDCFCE7),
            onClick = { onNavigate(Screen.OPPORTUNITIES) },
            modifier = Modifier.weight(1f)
          )
          MenuTile(
            screen = Screen.BUSINESS,
            subtitle = "MSME Directory",
            color = Color(0xFFEA580C),
            bgColor = Color(0xFFFFEDD5),
            onClick = { onNavigate(Screen.BUSINESS) },
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MenuTile(
            screen = Screen.EVENTS,
            subtitle = "Festivals & Meetings",
            color = Color(0xFF8B5CF6),
            bgColor = Color(0xFFEDE9FE),
            onClick = { onNavigate(Screen.EVENTS) },
            modifier = Modifier.weight(1f)
          )
          MenuTile(
            screen = Screen.MEMBERSHIP,
            subtitle = "Digital ID Badge",
            color = Color(0xFF059669),
            bgColor = Color(0xFFD1FAE5),
            onClick = { onNavigate(Screen.MEMBERSHIP) },
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MenuTile(
            screen = Screen.CONTACT,
            subtitle = "Support & Feedback",
            color = Color(0xFF4B5563),
            bgColor = Color(0xFFF3F4F6),
            onClick = { onNavigate(Screen.CONTACT) },
            modifier = Modifier.weight(1f)
          )
          MenuTile(
            screen = Screen.ADMIN,
            subtitle = "Moderation Panel",
            color = Color(0xFFDC2626),
            bgColor = Color(0xFFFEE2E2),
            onClick = { onNavigate(Screen.ADMIN) },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Featured Positive Story
    if (news.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "FEATURED STORY",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            ),
            color = AkwaGreenDark
          )
          Text(
            text = "See All",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = AkwaGreenPrimary
            ),
            modifier = Modifier.clickable { onNavigate(Screen.NEWS) }
          )
        }

        val featured = news.firstOrNull { it.isFeatured } ?: news.first()
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onSelectNews(featured) }
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
                  .background(AkwaGreenLight)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = featured.category,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = AkwaGreenPrimary
                )
              }
              Text(
                text = featured.readTime,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = featured.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = featured.summary,
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF4B5563),
              maxLines = 3,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Read Full Story",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = RadiantSunOrange
                )
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = RadiantSunOrange,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }
    }

    // Upcoming Event Sneak Peek
    if (events.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "UPCOMING EVENT",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            ),
            color = AkwaGreenDark
          )
          Text(
            text = "View Calendar",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = AkwaGreenPrimary
            ),
            modifier = Modifier.clickable { onNavigate(Screen.EVENTS) }
          )
        }
        val nextEvent = events.first()
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onNavigate(Screen.EVENTS) }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AkwaGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "NOV\n14",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  fontSize = 11.sp,
                  lineHeight = 12.sp
                ),
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = nextEvent.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "${nextEvent.venue} • ${nextEvent.lga}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AnnouncementCard(announcement: CommunityAnnouncement) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
    modifier = Modifier.width(280.dp)
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
            .background(AkwaGreenPrimary)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = announcement.tag.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = Color.White
          )
        }
        Text(
          text = announcement.date,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF6B7280)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = announcement.title,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        color = AkwaGreenDark,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = announcement.content,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF374151),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
private fun MenuTile(
  screen: Screen,
  subtitle: String,
  color: Color,
  bgColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = bgColor),
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag("menu_tile_${screen.name.lowercase()}")
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = screen.icon,
          contentDescription = screen.title,
          tint = color,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = screen.shortLabel,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = Color(0xFF1F2937),
          maxLines = 1
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
          color = Color(0xFF6B7280),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
