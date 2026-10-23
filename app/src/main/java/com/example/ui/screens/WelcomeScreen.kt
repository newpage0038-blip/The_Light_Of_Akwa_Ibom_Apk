package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.LightGreetingBanner
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun WelcomeScreen(
  onGetStarted: () -> Unit,
  onChantClicked: () -> Unit,
  chantsCount: Int,
  isChanting: Boolean,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .verticalScroll(scrollState)
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top State Flag bar
    Row(
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(4.dp)
        .clip(RoundedCornerShape(2.dp))
    ) {
      Box(modifier = Modifier.weight(1f).height(4.dp).background(AkwaGreenPrimary))
      Box(modifier = Modifier.weight(1f).height(4.dp).background(Color(0xFFE0E0E0)))
      Box(modifier = Modifier.weight(1f).height(4.dp).background(RadiantSunOrange))
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Hero Logo Image with Shining Sun
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFFE2E8E4), RoundedCornerShape(24.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                Color(0xFFFFF8E1),
                Color.White
              )
            )
          )
          .padding(12.dp),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.banner_light_of_akwa_ibom),
          contentDescription = "The Light of Akwa Ibom Logo - Brighter We Shine",
          modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(18.dp)),
          contentScale = ContentScale.Fit
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // App Name & Slogan
    Text(
      text = "THE LIGHT OF AKWA IBOM",
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp
      ),
      color = AkwaGreenDark,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Slogan Badge
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(RadiantSunOrange)
        .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.WbSunny,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "BRIGHTER WE SHINE!",
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp
          ),
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Welcome to The Light of Akwa Ibom.",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      ),
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Core Vision Quote Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "\"We are a community united by hope, driven by purpose, and committed to making our state and our people shine brighter. Together, we celebrate our identity, share opportunities, support our talents, and build a brighter future for Akwa Ibom.\"",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = FontStyle.Italic,
            lineHeight = 22.sp,
            color = AkwaGreenDark,
            fontWeight = FontWeight.Medium
          ),
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Interactive Greeting Banner
    LightGreetingBanner(
      chantsCount = chantsCount,
      isChanting = isChanting,
      onChantClicked = onChantClicked
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Highlights Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      HighlightPill(
        icon = Icons.Default.Diversity3,
        title = "31 LGAs",
        subtitle = "One People",
        color = AkwaGreenPrimary,
        bgColor = AkwaGreenLight,
        modifier = Modifier.weight(1f)
      )
      HighlightPill(
        icon = Icons.Default.Palette,
        title = "Culture",
        subtitle = "Afang & Arts",
        color = RadiantSunOrange,
        bgColor = RadiantSunLight,
        modifier = Modifier.weight(1f)
      )
      HighlightPill(
        icon = Icons.Default.Handshake,
        title = "Opportunities",
        subtitle = "Jobs & Grants",
        color = RadiantSunAmber,
        bgColor = Color(0xFFFEF3C7),
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(28.dp))

    // GET STARTED Button
    Button(
      onClick = onGetStarted,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .testTag("get_started_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = AkwaGreenPrimary,
        contentColor = Color.White
      ),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "GET STARTED",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Works 100% Offline • Made for Akwa Ibom People Everywhere",
      style = MaterialTheme.typography.bodySmall,
      color = Color(0xFF6B7280),
      textAlign = TextAlign.Center
    )
  }
}

@Composable
private fun HighlightPill(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  color: Color,
  bgColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = bgColor),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = color,
        maxLines = 1
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF4B5563),
        maxLines = 1
      )
    }
  }
}
