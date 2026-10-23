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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.OpportunityItem
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpportunitiesScreen(
  opportunities: List<OpportunityItem>,
  onSubmitOpportunity: (String, String, String, String, String, String, String) -> Unit,
  onShowFeedback: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var showSubmitDialog by remember { mutableStateOf(false) }

  val categories = listOf("All", "Job Vacancy", "Scholarship", "Training & Skills", "Business Grant", "Auditions")

  val filteredItems = if (selectedCategory == "All") {
    opportunities
  } else {
    opportunities.filter { it.category.contains(selectedCategory, ignoreCase = true) }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showSubmitDialog = true },
        containerColor = RadiantSunOrange,
        contentColor = Color.White,
        modifier = Modifier.testTag("submit_opportunity_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Share Opportunity")
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header
      item {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Handshake,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "TALENTS & EMPOWERMENT",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Discover verified scholarships, jobs, training bootcamps, and business grants for Akwa Ibom people.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF6B7280)
        )
      }

      // Filter Chips
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text(cat, fontSize = 12.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AkwaGreenPrimary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }

      // Opportunities List
      if (filteredItems.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("No opportunities found in this category.", color = Color.Gray)
          }
        }
      } else {
        items(filteredItems, key = { it.id }) { item ->
          OpportunityCard(
            item = item,
            onContact = { onShowFeedback("Contact for ${item.title}: ${item.contactOrUrl}") }
          )
        }
      }
    }
  }

  // Submit Opportunity Dialog
  if (showSubmitDialog) {
    SubmitOpportunityDialog(
      categories = categories.filter { it != "All" },
      onDismiss = { showSubmitDialog = false },
      onSubmit = { title, cat, org, loc, ddl, desc, contact ->
        onSubmitOpportunity(title, cat, org, loc, ddl, desc, contact)
        showSubmitDialog = false
      }
    )
  }
}

@Composable
private fun OpportunityCard(
  item: OpportunityItem,
  onContact: () -> Unit
) {
  val (badgeBg, badgeFg) = when {
    item.category.contains("Scholarship", ignoreCase = true) -> RadiantSunLight to RadiantSunOrange
    item.category.contains("Job", ignoreCase = true) -> AkwaGreenLight to AkwaGreenPrimary
    item.category.contains("Grant", ignoreCase = true) -> Color(0xFFFEF3C7) to Color(0xFFB45309)
    else -> Color(0xFFEDE9FE) to Color(0xFF6D28D9)
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("opportunity_card_${item.id}")
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
            .background(badgeBg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = item.category.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = badgeFg
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Deadline: ${item.deadline}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF111827)
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Organization: ${item.organization}",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = AkwaGreenDark
        )
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = Color(0xFF9CA3AF),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = item.location,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF6B7280)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = item.description,
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
        color = Color(0xFF374151)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = item.contactOrUrl,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = RadiantSunOrange
          )
        }

        Button(
          onClick = onContact,
          colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text("How to Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubmitOpportunityDialog(
  categories: List<String>,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String, String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var organization by remember { mutableStateOf("") }
  var location by remember { mutableStateOf("Uyo / Akwa Ibom") }
  var deadline by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var contact by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(categories.first()) }
  var expanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Share an Opportunity", fontWeight = FontWeight.Bold, color = AkwaGreenDark)
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Opportunity Title") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        ExposedDropdownMenuBox(
          expanded = expanded,
          onExpandedChange = { expanded = !expanded }
        ) {
          OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
          ) {
            categories.forEach { cat ->
              DropdownMenuItem(
                text = { Text(cat) },
                onClick = {
                  selectedCategory = cat
                  expanded = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = organization,
          onValueChange = { organization = it },
          label = { Text("Organization / Agency") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Location") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = deadline,
          onValueChange = { deadline = it },
          label = { Text("Application Deadline (e.g. Dec 15, 2026)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = contact,
          onValueChange = { contact = it },
          label = { Text("Contact Email / Phone / Link") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Details & Eligibility") },
          modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
          maxLines = 4
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && organization.isNotBlank()) {
            onSubmit(title, selectedCategory, organization, location, deadline.ifBlank { "Ongoing" }, description, contact)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = RadiantSunOrange)
      ) {
        Text("Share Now")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
