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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.BusinessListing
import com.example.data.model.CommunityAnnouncement
import com.example.data.model.DiscussionPost
import com.example.data.model.OpportunityItem
import com.example.data.model.SupportMessage
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange

@Composable
fun AdminScreen(
  announcements: List<CommunityAnnouncement>,
  discussions: List<DiscussionPost>,
  businesses: List<BusinessListing>,
  opportunities: List<OpportunityItem>,
  supportMessages: List<SupportMessage>,
  onAddAnnouncement: (String, String, String, String) -> Unit,
  onDeleteAnnouncement: (Long) -> Unit,
  onSetDiscussionApproved: (Long, Boolean) -> Unit,
  onDeleteDiscussion: (Long) -> Unit,
  onSetBusinessApproved: (Long, Boolean) -> Unit,
  onDeleteBusiness: (Long) -> Unit,
  onSetOpportunityApproved: (Long, Boolean) -> Unit,
  onDeleteOpportunity: (Long) -> Unit,
  onResetData: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showNewAnnouncementDialog by remember { mutableStateOf(false) }
  val tabs = listOf("Discussions", "Businesses", "Opportunities", "Announcements", "Reports & Messages")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
  ) {
    // Admin Top Strip
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF1E293B))
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = null,
            tint = RadiantSunAmber,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "ADMINISTRATOR DASHBOARD",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "Platform Moderation & Security",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF94A3B8)
            )
          }
        }

        IconButton(onClick = onResetData) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Restore Default Seeds",
            tint = Color(0xFF94A3B8)
          )
        }
      }
    }

    // Tabs
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      edgePadding = 12.dp,
      containerColor = Color(0xFF0F172A),
      contentColor = Color.White
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp,
              color = if (selectedTab == index) RadiantSunAmber else Color(0xFF94A3B8)
            )
          }
        )
      }
    }

    // Tab Content
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      when (selectedTab) {
        0 -> { // Discussions
          item {
            Text(
              text = "Manage Community Discussions (${discussions.size})",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
          }
          items(discussions) { post ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = post.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(if (post.isApproved) AkwaGreenLight else Color(0xFFFEE2E2))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = if (post.isApproved) "APPROVED" else "PENDING",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                      color = if (post.isApproved) AkwaGreenPrimary else Color(0xFFDC2626)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Author: ${post.authorName} (${post.authorLga})", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = post.content, style = MaterialTheme.typography.bodySmall, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedButton(
                    onClick = { onSetDiscussionApproved(post.id, !post.isApproved) },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(if (post.isApproved) "Hide / Suspend" else "Approve", fontSize = 11.sp)
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  IconButton(onClick = { onDeleteDiscussion(post.id) }, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                  }
                }
              }
            }
          }
        }

        1 -> { // Businesses
          item {
            Text(
              text = "Moderate Business Listings (${businesses.size})",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
          }
          items(businesses) { b ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(text = b.businessName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "${b.category} • ${b.address} (${b.lga})", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = "Proprietor: ${b.ownerName} • Phone: ${b.phoneNumber}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
                ) {
                  OutlinedButton(
                    onClick = { onSetBusinessApproved(b.id, !b.isApproved) },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(if (b.isApproved) "Unlist" else "Approve", fontSize = 11.sp)
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  IconButton(onClick = { onDeleteBusiness(b.id) }, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                  }
                }
              }
            }
          }
        }

        2 -> { // Opportunities
          item {
            Text(
              text = "Empowerment & Job Postings (${opportunities.size})",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
          }
          items(opportunities) { opp ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(text = opp.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "${opp.category} • ${opp.organization} (${opp.location})", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = "Contact: ${opp.contactOrUrl}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
                ) {
                  OutlinedButton(
                    onClick = { onSetOpportunityApproved(opp.id, !opp.isApproved) },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(if (opp.isApproved) "Suspend" else "Approve", fontSize = 11.sp)
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  IconButton(onClick = { onDeleteOpportunity(opp.id) }, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                  }
                }
              }
            }
          }
        }

        3 -> { // Announcements
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Official Announcements (${announcements.size})",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AkwaGreenDark
              )
              Button(
                onClick = { showNewAnnouncementDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Publish", fontSize = 11.sp)
              }
            }
          }
          items(announcements) { a ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(text = a.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                  IconButton(onClick = { onDeleteAnnouncement(a.id) }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                  }
                }
                Text(text = "${a.date} • Tag: ${a.tag}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = a.content, style = MaterialTheme.typography.bodySmall, color = Color(0xFF334155))
              }
            }
          }
        }

        4 -> { // Reports & Messages
          item {
            Text(
              text = "Member Messages & Reports (${supportMessages.size})",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenDark
            )
          }
          if (supportMessages.isEmpty()) {
            item {
              Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No reported issues or inquiries yet.", color = Color.Gray)
              }
            }
          } else {
            items(supportMessages) { msg ->
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (msg.type.contains("Report", ignoreCase = true)) Color(0xFFFFF1F2) else Color(0xFFF8FAFC)
                ),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = msg.subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (msg.type.contains("Report", ignoreCase = true)) Color(0xFFE11D48) else AkwaGreenPrimary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(text = msg.type.uppercase(), color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                  Text(text = "From: ${msg.name} (${msg.emailOrPhone})", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = msg.message, style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E293B))
                }
              }
            }
          }
        }
      }
    }
  }

  // New Announcement Dialog
  if (showNewAnnouncementDialog) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Today") }
    var tag by remember { mutableStateOf("Priority") }

    AlertDialog(
      onDismissRequest = { showNewAnnouncementDialog = false },
      title = { Text("Publish Community Announcement", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (e.g. Oct 2026)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = tag, onValueChange = { tag = it }, label = { Text("Tag (Priority, Event, Update)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Announcement Body") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
              onAddAnnouncement(title, content, date, tag)
              showNewAnnouncementDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
        ) {
          Text("Publish")
        }
      },
      dismissButton = {
        TextButton(onClick = { showNewAnnouncementDialog = false }) { Text("Cancel") }
      }
    )
  }
}
