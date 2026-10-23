package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
  onSendMessage: (String, String, String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var name by remember { mutableStateOf("") }
  var contact by remember { mutableStateOf("") }
  var subject by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("") }
  var messageType by remember { mutableStateOf("Inquiry") }
  var expandedType by remember { mutableStateOf(false) }

  val types = listOf("Inquiry", "Suggestion", "Report Inappropriate Content", "Partnership")

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
        imageVector = Icons.Default.Mail,
        contentDescription = null,
        tint = AkwaGreenPrimary,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "CONTACT & COMMUNITY SUPPORT",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = AkwaGreenDark
      )
    }

    // Official Secretariat Info
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "The Light of Akwa Ibom Secretariat",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Unity Hub, Udo Udoma Avenue, Uyo, Akwa Ibom State",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1F2937)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "support@thelightofakwaibom.org",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1F2937)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "+234 (0) 808 LIGHT 00 / +234 803 000 7788",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1F2937)
          )
        }
      }
    }

    // Submit Message / Report Form
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Send Message or Report Inappropriate Content",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Your Full Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = contact,
          onValueChange = { contact = it },
          label = { Text("Email or Phone Number") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
          expanded = expandedType,
          onExpandedChange = { expandedType = !expandedType }
        ) {
          OutlinedTextField(
            value = messageType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type of Submission") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedType,
            onDismissRequest = { expandedType = false }
          ) {
            types.forEach { t ->
              DropdownMenuItem(
                text = { Text(t) },
                onClick = {
                  messageType = t
                  expandedType = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = subject,
          onValueChange = { subject = it },
          label = { Text("Subject") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = message,
          onValueChange = { message = it },
          label = { Text("Describe your inquiry, suggestion, or reported issue...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
          maxLines = 5
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            if (name.isNotBlank() && message.isNotBlank()) {
              onSendMessage(name, contact, subject.ifBlank { messageType }, message, messageType)
              name = ""
              contact = ""
              subject = ""
              message = ""
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (messageType.contains("Report", ignoreCase = true)) Color(0xFFDC2626) else AkwaGreenPrimary
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("send_support_message_button")
        ) {
          Icon(
            imageVector = if (messageType.contains("Report", ignoreCase = true)) Icons.Default.ReportProblem else Icons.Default.Send,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (messageType.contains("Report", ignoreCase = true)) "Submit Report to Moderation" else "Submit to Secretariat",
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Community Guidelines Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "COMMUNITY GUIDELINES & SAFETY",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = RadiantSunOrange
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "• Mutual Respect: Treat every brother and sister with dignity.\n• Peaceful Conversations: No partisan insults, ethnic bigotry, or harassment.\n• Truth & Accuracy: Share only genuine opportunities and verified news.\n• Data Privacy: Never post another member's confidential numbers without consent.",
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
          color = Color(0xFF451A03)
        )
      }
    }
  }
}
