package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
  val title: String,
  val icon: ImageVector,
  val shortLabel: String
) {
  WELCOME("Welcome", Icons.Default.Star, "Welcome"),
  HOME("Home", Icons.Default.Home, "Home"),
  ENTERTAINMENT("Entertainment Centre", Icons.Default.Celebration, "Entertainment"),
  FOOTBALL("Football Centre", Icons.Default.SportsSoccer, "Football"),
  ABOUT("About Us", Icons.Default.Info, "About"),
  COMMUNITY("Community", Icons.Default.Diversity3, "Community"),
  NEWS("News & Updates", Icons.Default.Newspaper, "News"),
  CULTURE("Culture & Heritage", Icons.Default.Explore, "Culture"),
  OPPORTUNITIES("Talents & Opportunities", Icons.Default.Handshake, "Opportunities"),
  BUSINESS("Business & Showcase", Icons.Default.Business, "Business"),
  EVENTS("Events & Gatherings", Icons.Default.Event, "Events"),
  MEMBERSHIP("Membership Card", Icons.Default.Person, "Membership"),
  CONTACT("Contact & Support", Icons.Default.Mail, "Contact"),
  ADMIN("Admin Center", Icons.Default.Security, "Admin")
}
