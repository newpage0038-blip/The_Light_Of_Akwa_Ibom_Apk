package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange

@Composable
fun AppBottomBar(
  currentScreen: Screen,
  onNavigate: (Screen) -> Unit,
  modifier: Modifier = Modifier
) {
  val primaryScreens = listOf(
    Screen.HOME,
    Screen.COMMUNITY,
    Screen.CULTURE,
    Screen.OPPORTUNITIES,
    Screen.MEMBERSHIP
  )

  NavigationBar(
    modifier = modifier
      .fillMaxWidth()
      .height(64.dp),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    primaryScreens.forEach { screen ->
      val isSelected = currentScreen == screen
      NavigationBarItem(
        selected = isSelected,
        onClick = { onNavigate(screen) },
        icon = {
          Icon(
            imageVector = screen.icon,
            contentDescription = screen.title,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = screen.shortLabel,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              fontSize = 10.sp
            )
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Color.White,
          selectedTextColor = AkwaGreenPrimary,
          indicatorColor = AkwaGreenPrimary,
          unselectedIconColor = Color(0xFF6B7280),
          unselectedTextColor = Color(0xFF6B7280)
        ),
        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
      )
    }
  }
}
