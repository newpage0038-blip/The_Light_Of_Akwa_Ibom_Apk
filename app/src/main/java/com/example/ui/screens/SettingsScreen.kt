package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.Screen
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun SettingsScreen(
  userProfile: UserProfile?,
  currentUserId: String?,
  currentUserName: String,
  onNavigate: (Screen) -> Unit,
  onOpenSignIn: () -> Unit,
  onSignOut: () -> Unit,
  onShowFeedback: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  var notificationsEnabled by remember { mutableStateOf(true) }
  var matchRemindersEnabled by remember { mutableStateOf(true) }
  var offlineCacheEnabled by remember { mutableStateOf(true) }
  var showPrivacyDialog by remember { mutableStateOf(false) }
  var showTermsDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAF9))
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Section Header
    Text(
      text = "SETTINGS & PREFERENCES",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.5.sp
      ),
      color = AkwaGreenDark
    )

    // Account & Profile Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(AkwaGreenPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (currentUserId != null) currentUserName else (userProfile?.fullName ?: "Guest Member"),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF1E293B)
            )
            Text(
              text = if (currentUserId != null) "Verified Member • Online" else "Offline Guest Mode",
              style = MaterialTheme.typography.bodySmall,
              color = if (currentUserId != null) AkwaGreenPrimary else Color.Gray
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (currentUserId == null) {
          Button(
            onClick = onOpenSignIn,
            colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("settings_sign_in_btn")
          ) {
            Text("Sign In with Google", fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = onSignOut,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("settings_sign_out_btn")
          ) {
            Text("Sign Out", fontWeight = FontWeight.Bold, color = Color.White)
          }
        }
      }
    }

    // Notifications Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "NOTIFICATION PREFERENCES",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Community Updates", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text("Announcements, news, and state events", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
          Switch(
            checked = notificationsEnabled,
            onCheckedChange = {
              notificationsEnabled = it
              onShowFeedback(if (it) "Notifications enabled" else "Notifications muted")
            },
            colors = SwitchDefaults.colors(checkedThumbColor = AkwaGreenPrimary, checkedTrackColor = AkwaGreenLight)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Football Match Reminders", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text("EPL & UCL kickoff notifications (WAT)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
          Switch(
            checked = matchRemindersEnabled,
            onCheckedChange = {
              matchRemindersEnabled = it
              onShowFeedback(if (it) "Match reminders enabled" else "Match reminders muted")
            },
            colors = SwitchDefaults.colors(checkedThumbColor = RadiantSunOrange, checkedTrackColor = RadiantSunLight)
          )
        }
      }
    }

    // Offline Storage & Cache
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "DATA & OFFLINE CACHE",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Offline Mode Storage", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text("Keep culture, history & quizzes available offline", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
          Switch(
            checked = offlineCacheEnabled,
            onCheckedChange = {
              offlineCacheEnabled = it
              onShowFeedback("Offline cache active (Room Database)")
            },
            colors = SwitchDefaults.colors(checkedThumbColor = AkwaGreenPrimary, checkedTrackColor = AkwaGreenLight)
          )
        }
      }
    }

    // Privacy & Legal Links
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "PRIVACY & SECURITY",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(10.dp))

        SettingsRowItem(
          icon = Icons.Default.Lock,
          title = "Privacy Policy",
          subtitle = "How your information is protected",
          onClick = { showPrivacyDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsRowItem(
          icon = Icons.Default.Security,
          title = "Terms & Community Guidelines",
          subtitle = "Standards for our respectful community",
          onClick = { showTermsDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsRowItem(
          icon = Icons.Default.Mail,
          title = "Help & Support",
          subtitle = "Contact the administration team",
          onClick = { onNavigate(Screen.CONTACT) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsRowItem(
          icon = Icons.Default.Info,
          title = "About The Light of Akwa Ibom",
          subtitle = "App creator, vision, and mission",
          onClick = { onNavigate(Screen.ABOUT) }
        )
      }
    }

    // App Creator Credit Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "THE LIGHT OF AKWA IBOM",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
          color = AkwaGreenDark
        )
        Text(
          text = "Official Slogan: BRIGHTER WE SHINE!",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = RadiantSunOrange
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "App Creator: Daniel Ekpo (A.K.A Newpage)",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
          color = Color(0xFF1E293B)
        )
        Text(
          text = "Version 1.0 (Build 2026.1) • All Rights Reserved",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF475569)
        )
      }
    }
  }

  // Privacy Policy Dialog
  if (showPrivacyDialog) {
    AlertDialog(
      onDismissRequest = { showPrivacyDialog = false },
      title = { Text("Privacy Policy", fontWeight = FontWeight.Bold, color = AkwaGreenDark) },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "At The Light of Akwa Ibom, your privacy and trust are paramount.\n\n" +
              "1. Information We Collect:\n" +
              "When you sign in using Google, we access only your basic public profile (name, email address, profile picture) to identify you in the community chat.\n\n" +
              "2. Cloud Communications:\n" +
              "Group chat messages and direct messages are stored securely in Google Cloud Firestore with zero-trust authenticated security rules. Messages are only accessible to authenticated community members.\n\n" +
              "3. Offline Privacy:\n" +
              "Cultural articles, dialect dictionaries, and quiz records stored locally on your device are never uploaded without your consent.\n\n" +
              "4. Zero Data Selling:\n" +
              "We never sell, rent, or trade your personal information to third parties.\n\n" +
              "For inquiries or data deletion requests, contact us through the Help & Support section.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = Color(0xFF334155)
          )
        }
      },
      confirmButton = {
        TextButton(onClick = { showPrivacyDialog = false }) {
          Text("Close", fontWeight = FontWeight.Bold, color = AkwaGreenPrimary)
        }
      }
    )
  }

  // Terms Dialog
  if (showTermsDialog) {
    AlertDialog(
      onDismissRequest = { showTermsDialog = false },
      title = { Text("Terms & Guidelines", fontWeight = FontWeight.Bold, color = AkwaGreenDark) },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "Welcome to The Light of Akwa Ibom — BRIGHTER WE SHINE!\n\n" +
              "1. Respect and Civility:\n" +
              "Members are expected to treat one another with dignity, brotherhood, and respect across all 31 LGAs of Akwa Ibom State.\n\n" +
              "2. Prohibited Content:\n" +
              "Defamation, hate speech, explicit content, spam, and political hostility are strictly prohibited and subject to immediate administrative moderation.\n\n" +
              "3. Intellectual Property:\n" +
              "All cultural content, slogans, and community resources are protected under applicable copyright laws.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = Color(0xFF334155)
          )
        }
      },
      confirmButton = {
        TextButton(onClick = { showTermsDialog = false }) {
          Text("Accept & Close", fontWeight = FontWeight.Bold, color = AkwaGreenPrimary)
        }
      }
    )
  }
}

@Composable
private fun SettingsRowItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(Color(0xFFF1F5F9)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = AkwaGreenPrimary, modifier = Modifier.size(18.dp))
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = Color(0xFF1E293B))
      Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForward,
      contentDescription = null,
      tint = Color.LightGray,
      modifier = Modifier.size(16.dp)
    )
  }
}
