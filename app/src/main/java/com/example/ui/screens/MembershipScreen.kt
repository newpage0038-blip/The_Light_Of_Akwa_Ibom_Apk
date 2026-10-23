package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InitialData
import com.example.data.model.UserProfile
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
  profile: UserProfile?,
  onSaveProfile: (String, String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentProfile = profile ?: InitialData.DEFAULT_PROFILE

  var fullName by remember(currentProfile) { mutableStateOf(currentProfile.fullName) }
  var selectedLga by remember(currentProfile) { mutableStateOf(currentProfile.lga) }
  var occupation by remember(currentProfile) { mutableStateOf(currentProfile.occupation) }
  var bio by remember(currentProfile) { mutableStateOf(currentProfile.bio) }
  var isEditing by remember { mutableStateOf(false) }
  var expandedLga by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Badge,
        contentDescription = null,
        tint = AkwaGreenPrimary,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "OFFICIAL DIGITAL MEMBERSHIP CARD",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = AkwaGreenDark
      )
    }

    // Digital Membership Card
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("digital_membership_card")
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.linearGradient(
              colors = listOf(
                AkwaGreenDark,
                AkwaGreenPrimary,
                Color(0xFF0F5132),
                Color(0xFF1E3A2F)
              )
            )
          )
          .padding(20.dp)
      ) {
        Column {
          // Card Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
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
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "THE LIGHT OF AKWA IBOM",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                  ),
                  color = Color.White
                )
                Text(
                  text = "BRIGHTER WE SHINE!",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                  ),
                  color = RadiantSunAmber
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0x33FFFFFF))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "COMMUNITY ID",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Member Name & Avatar
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, RadiantSunOrange, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = AkwaGreenPrimary,
                modifier = Modifier.size(32.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = currentProfile.fullName,
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp
                ),
                color = Color.White
              )
              Text(
                text = currentProfile.occupation,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA7F3D0)
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // LGA & ID Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "LOCAL GOVERNMENT AREA",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF6EE7B7)
              )
              Text(
                text = "${currentProfile.lga} LGA",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "MEMBERSHIP NUMBER",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF6EE7B7)
              )
              Text(
                text = currentProfile.memberId,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = RadiantSunAmber
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Bottom card metadata
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Member Since: ${currentProfile.joinedDate}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFD1FAE5)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF34D399),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Verified Citizen",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF34D399)
              )
            }
          }
        }
      }
    }

    // Profile Details & Edit Section
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "PROFILE DETAILS",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )
          Button(
            onClick = {
              if (isEditing) {
                onSaveProfile(fullName, selectedLga, occupation, bio)
                isEditing = false
              } else {
                isEditing = true
              }
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isEditing) AkwaGreenPrimary else RadiantSunOrange
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(
              imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isEditing) "Save Profile" else "Edit Details", color = Color.White, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isEditing) {
          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          ExposedDropdownMenuBox(
            expanded = expandedLga,
            onExpandedChange = { expandedLga = !expandedLga }
          ) {
            OutlinedTextField(
              value = selectedLga,
              onValueChange = {},
              readOnly = true,
              label = { Text("Home Local Government Area (LGA)") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLga) },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
            )
            ExposedDropdownMenu(
              expanded = expandedLga,
              onDismissRequest = { expandedLga = false }
            ) {
              InitialData.LGAS.forEach { lga ->
                DropdownMenuItem(
                  text = { Text(lga) },
                  onClick = {
                    selectedLga = lga
                    expandedLga = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = occupation,
            onValueChange = { occupation = it },
            label = { Text("Profession / Craft / Talent") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Personal Bio & Community Motto") },
            modifier = Modifier
              .fillMaxWidth()
              .height(90.dp),
            maxLines = 3
          )
        } else {
          ProfileReadRow("Full Name", currentProfile.fullName)
          ProfileReadRow("Local Government", "${currentProfile.lga} LGA")
          ProfileReadRow("Profession / Talent", currentProfile.occupation)
          ProfileReadRow("Community Motto", currentProfile.bio)
        }
      }
    }

    // Community Stats
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "${currentProfile.chantsCount}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = RadiantSunOrange
          )
          Text(
            text = "Chants Shined",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF78350F)
          )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "31",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = AkwaGreenPrimary
          )
          Text(
            text = "LGAs United",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF064E3B)
          )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "100%",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = Color(0xFF1D4ED8)
          )
          Text(
            text = "Offline Ready",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF1E3A8A)
          )
        }
      }
    }
  }
}

@Composable
private fun ProfileReadRow(label: String, value: String) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF6B7280)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
      color = Color(0xFF1F2937)
    )
  }
}
