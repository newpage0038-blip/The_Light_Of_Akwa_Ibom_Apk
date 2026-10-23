package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
  currentScreen: Screen,
  isAdminMode: Boolean,
  onNavigateBack: () -> Unit,
  onToggleAdmin: () -> Unit,
  onChantClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    // Akwa Ibom State Flag ribbon (Green - White - Orange)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .height(4.dp)
          .background(AkwaGreenPrimary)
      )
      Box(
        modifier = Modifier
          .weight(1f)
          .height(4.dp)
          .background(Color.White)
      )
      Box(
        modifier = Modifier
          .weight(1f)
          .height(4.dp)
          .background(RadiantSunOrange)
      )
    }

    TopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(RadiantSunOrange),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.WbSunny,
              contentDescription = "Shining Sun",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = if (currentScreen == Screen.HOME) "THE LIGHT OF AKWA IBOM" else currentScreen.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (currentScreen == Screen.HOME) 15.sp else 18.sp
              ),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            if (currentScreen == Screen.HOME) {
              Text(
                text = "BRIGHTER WE SHINE!",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.sp
                ),
                color = RadiantSunOrange
              )
            }
          }
        }
      },
      navigationIcon = {
        if (currentScreen != Screen.HOME && currentScreen != Screen.WELCOME) {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("top_bar_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      },
      actions = {
        // Community Chant quick button
        IconButton(
          onClick = onChantClicked,
          modifier = Modifier.testTag("top_bar_chant_button")
        ) {
          Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = "Chant: THE LIGHT!",
            tint = RadiantSunAmber
          )
        }

        // Admin mode toggle button
        IconButton(
          onClick = onToggleAdmin,
          modifier = Modifier.testTag("top_bar_admin_button")
        ) {
          BadgedBox(
            badge = {
              if (isAdminMode) {
                Badge(containerColor = RadiantSunOrange) {
                  Text("ON", fontSize = 9.sp)
                }
              }
            }
          ) {
            Icon(
              imageVector = Icons.Default.AdminPanelSettings,
              contentDescription = "Administrator Mode",
              tint = if (isAdminMode) RadiantSunOrange else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
      )
    )
  }
}
