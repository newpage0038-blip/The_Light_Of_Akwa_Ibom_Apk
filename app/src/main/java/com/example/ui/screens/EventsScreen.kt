package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InitialData
import com.example.data.model.CommunityEvent
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
  events: List<CommunityEvent>,
  onToggleRegister: (CommunityEvent) -> Unit,
  onToggleBookmark: (CommunityEvent) -> Unit,
  onSubmitEvent: (String, String, String, String, String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddDialog by remember { mutableStateOf(false) }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = AkwaGreenPrimary,
        contentColor = Color.White,
        modifier = Modifier.testTag("submit_event_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Submit Event")
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header
      item {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Event,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "COMMUNITY EVENTS & GATHERINGS",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Connect in person: meetings, cultural celebrations, networking, and state festivals.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6B7280)
        )
      }

      if (events.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("No scheduled events currently.", color = Color.Gray)
          }
        }
      } else {
        items(events, key = { it.id }) { event ->
          EventCard(
            event = event,
            onToggleRegister = { onToggleRegister(event) },
            onToggleBookmark = { onToggleBookmark(event) }
          )
        }
      }
    }
  }

  if (showAddDialog) {
    SubmitEventDialog(
      lgas = InitialData.LGAS,
      onDismiss = { showAddDialog = false },
      onSubmit = { title, cat, date, time, venue, lga, desc ->
        onSubmitEvent(title, cat, date, time, venue, lga, desc)
        showAddDialog = false
      }
    )
  }
}

@Composable
private fun EventCard(
  event: CommunityEvent,
  onToggleRegister: () -> Unit,
  onToggleBookmark: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("event_card_${event.id}")
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
            .background(RadiantSunLight)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = event.category.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = RadiantSunOrange
          )
        }

        IconButton(onClick = onToggleBookmark, modifier = Modifier.size(28.dp)) {
          Icon(
            imageVector = if (event.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Bookmark Event",
            tint = if (event.isBookmarked) RadiantSunOrange else Color.Gray,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = event.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF111827)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Event,
          contentDescription = null,
          tint = AkwaGreenPrimary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = event.date,
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
          color = Color(0xFF374151)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
          imageVector = Icons.Default.AccessTime,
          contentDescription = null,
          tint = AkwaGreenPrimary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = event.time,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF4B5563)
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = RadiantSunOrange,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${event.venue} (${event.lga} LGA)",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF4B5563)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = event.description,
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
        color = Color(0xFF4B5563)
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${event.attendeesCount} Attending",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = AkwaGreenDark
            )
          )
        }

        if (event.isRegistered) {
          OutlinedButton(
            onClick = onToggleRegister,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = AkwaGreenPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Registered", color = AkwaGreenPrimary, fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = onToggleRegister,
            colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
          ) {
            Text("RSVP / Attend", color = Color.White, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubmitEventDialog(
  lgas: List<String>,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String, String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Community Meeting") }
  var date by remember { mutableStateOf("") }
  var time by remember { mutableStateOf("10:00 AM") }
  var venue by remember { mutableStateOf("") }
  var selectedLga by remember { mutableStateOf("Uyo") }
  var description by remember { mutableStateOf("") }
  var expandedLga by remember { mutableStateOf(false) }

  val categories = listOf("Cultural Celebration", "Community Meeting", "Youth Summit", "Networking", "Festival", "Workshop")
  var expandedCat by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Submit Community Event", fontWeight = FontWeight.Bold, color = AkwaGreenDark)
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Event Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        ExposedDropdownMenuBox(
          expanded = expandedCat,
          onExpandedChange = { expandedCat = !expandedCat }
        ) {
          OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedCat,
            onDismissRequest = { expandedCat = false }
          ) {
            categories.forEach { cat ->
              DropdownMenuItem(
                text = { Text(cat) },
                onClick = {
                  category = cat
                  expandedCat = false
                }
              )
            }
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (e.g. Nov 20)") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = venue,
          onValueChange = { venue = it },
          label = { Text("Venue / Landmark") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        ExposedDropdownMenuBox(
          expanded = expandedLga,
          onExpandedChange = { expandedLga = !expandedLga }
        ) {
          OutlinedTextField(
            value = selectedLga,
            onValueChange = {},
            readOnly = true,
            label = { Text("Local Government Area") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLga) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedLga,
            onDismissRequest = { expandedLga = false }
          ) {
            lgas.forEach { lga ->
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

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Event Description") },
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
          maxLines = 3
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && venue.isNotBlank() && date.isNotBlank()) {
            onSubmit(title, category, date, time, venue, selectedLga, description)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
      ) {
        Text("Schedule Event")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
