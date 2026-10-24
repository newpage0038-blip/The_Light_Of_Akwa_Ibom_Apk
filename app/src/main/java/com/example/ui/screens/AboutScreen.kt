package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun AboutScreen(
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Header
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                AkwaGreenDark,
                AkwaGreenPrimary
              )
            )
          )
          .padding(20.dp)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(RadiantSunOrange),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.WbSunny,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "THE LIGHT OF AKWA IBOM",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            ),
            color = Color.White
          )
          Text(
            text = "BRIGHTER WE SHINE!",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            ),
            color = RadiantSunAmber
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "A people united by faith, pride, and shared prosperity.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD1FAE5)
          )
        }
      }
    }

    // Vision Section
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "OUR VISION",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "To bring the people of Akwa Ibom State, Nigeria, together through unity, culture, community engagement, education, empowerment, and economic opportunities. We celebrate our identity, creativity, talents, achievements, and potential while encouraging members to support one another and contribute positively to state development.",
          style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
          color = Color(0xFF1F2937)
        )
      }
    }

    // Mission & Objectives
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Flag,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "MISSION & OBJECTIVES",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = RadiantSunOrange
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ObjectiveBullet("Promote unbroken unity and peaceful brotherhood across all 31 LGAs.")
        ObjectiveBullet("Provide equal access to jobs, scholarships, grants, and training programs.")
        ObjectiveBullet("Showcase Akwa Ibom businesses, local foods, arts, and creative talents.")
        ObjectiveBullet("Foster collective, accountable leadership where every voice matters.")
      }
    }

    // Meaning of the State Colors & Identity
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "STATE IDENTITY & SYMBOLISM",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.height(12.dp))

        SymbolRow(
          color = AkwaGreenPrimary,
          title = "Green",
          desc = "Represents our fertile soil, rich agricultural abundance, palm oil estates, and everlasting vitality."
        )
        Spacer(modifier = Modifier.height(10.dp))
        SymbolRow(
          color = Color(0xFF9CA3AF),
          title = "White",
          desc = "Represents peace, transparency, harmony, and the purity of purpose among our people."
        )
        Spacer(modifier = Modifier.height(10.dp))
        SymbolRow(
          color = RadiantSunOrange,
          title = "Orange & The Shining Sun",
          desc = "The Light of Akwa Ibom! Symbolizing intellectual brilliance, mineral richness, optimism, and warmth."
        )
      }
    }

    // Community Values (Mandated Section 5)
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "OUR CORE COMMUNITY VALUES",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(12.dp))

        ValueItem(Icons.Default.Groups, "Unity and Togetherness", "Standing shoulder to shoulder as one indivisible family.")
        ValueItem(Icons.Default.Handshake, "Respect for Every Member", "Every member is valued regardless of background or gender.")
        ValueItem(Icons.Default.Diversity1, "Peaceful Conversations", "Constructive, polite dialogue that uplifts and builds solutions.")
        ValueItem(Icons.Default.LocalFlorist, "Pride in Akwa Ibom Identity", "Celebrating our languages, cuisine, history, and traditions.")
        ValueItem(Icons.Default.CheckCircle, "Equal Opportunities", "Fair access for all members to contribute, shine, and thrive.")
        ValueItem(Icons.Default.AutoAwesome, "Positive Representation", "Projecting the excellence and integrity of our state to the world.")
      }
    }

    // App Creator & Platform Information (Section 14)
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, AkwaGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "APP CREATOR & PLATFORM INFO",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "App Name: The Light of Akwa Ibom",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
          color = Color(0xFF1E293B)
        )
        Text(
          text = "Official Slogan: BRIGHTER WE SHINE!",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
          color = RadiantSunOrange
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "App Creator: Daniel Ekpo",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
          color = Color(0xFF1E293B)
        )
        Text(
          text = "Artist Name: A.K.A Newpage",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
          color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Purpose: A digital platform celebrating Akwa Ibom people, culture, community, creativity, and opportunities.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF334155)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Version: 1.0 • Built with Kotlin & Jetpack Compose",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF64748B)
        )
      }
    }

    // Non-Individual Centered Commitment
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "Collective Leadership Commitment",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF92400E)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "\"This platform is not built around any single individual. It belongs to the entire Akwa Ibom family. We believe in collective participation, shared ownership, transparent stewardship, and community-driven progress.\"",
          style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
          color = Color(0xFF78350F)
        )
      }
    }
  }
}

@Composable
private fun ObjectiveBullet(text: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.Top
  ) {
    Icon(
      imageVector = Icons.Default.CheckCircle,
      contentDescription = null,
      tint = RadiantSunOrange,
      modifier = Modifier
        .size(16.dp)
        .padding(top = 2.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
      color = Color(0xFF374151)
    )
  }
}

@Composable
private fun SymbolRow(color: Color, title: String, desc: String) {
  Row(verticalAlignment = Alignment.Top) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(color)
        .border(1.dp, Color(0x33000000), CircleShape)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF111827)
      )
      Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF4B5563)
      )
    }
  }
}

@Composable
private fun ValueItem(icon: ImageVector, title: String, desc: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(AkwaGreenLight),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = AkwaGreenPrimary,
        modifier = Modifier.size(18.dp)
      )
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF1F2937)
      )
      Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF6B7280)
      )
    }
  }
}
