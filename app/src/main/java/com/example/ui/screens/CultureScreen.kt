package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CultureItem
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun CultureScreen(
  cultureItems: List<CultureItem>,
  selectedItem: CultureItem?,
  onSelectItem: (CultureItem?) -> Unit,
  onShowFeedback: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedType by remember { mutableStateOf("All") }
  val types = listOf("All", "Language", "Delicacy", "Dance", "Festival", "Landmark")

  val filteredItems = if (selectedType == "All") {
    cultureItems
  } else {
    cultureItems.filter { it.type.equals(selectedType, ignoreCase = true) }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Explore,
          contentDescription = null,
          tint = RadiantSunAmber,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "CULTURE, HERITAGE & LANGUAGE",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
          color = AkwaGreenDark
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Explore the vibrant language, mouthwatering cuisine, dances, and historic landmarks of Akwa Ibom.",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF6B7280)
      )
    }

    // Filter Chips
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(types) { type ->
          FilterChip(
            selected = selectedType == type,
            onClick = { selectedType = type },
            label = { Text(type, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = RadiantSunOrange,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    // List of items
    items(filteredItems, key = { it.id }) { item ->
      CultureCard(
        item = item,
        onClick = { onSelectItem(item) },
        onPlayAudio = { onShowFeedback("Pronunciation for \"${item.localPhrase.ifEmpty { item.title }}\": \"${item.meaning}\"") }
      )
    }
  }

  // Details Modal
  if (selectedItem != null) {
    CultureDetailDialog(
      item = selectedItem,
      onDismiss = { onSelectItem(null) }
    )
  }
}

@Composable
private fun CultureCard(
  item: CultureItem,
  onClick: () -> Unit,
  onPlayAudio: () -> Unit
) {
  val icon = when (item.type.lowercase()) {
    "language" -> Icons.Default.RecordVoiceOver
    "delicacy" -> Icons.Default.Fastfood
    "dance" -> Icons.Default.MusicNote
    "landmark" -> Icons.Default.Landscape
    else -> Icons.Default.WbSunny
  }

  val badgeColor = when (item.type.lowercase()) {
    "language" -> Color(0xFF0284C7)
    "delicacy" -> Color(0xFFE65100)
    "dance" -> Color(0xFF7C3AED)
    "landmark" -> AkwaGreenPrimary
    else -> RadiantSunAmber
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("culture_card_${item.id}")
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(badgeColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = badgeColor,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(badgeColor.copy(alpha = 0.1f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = item.type.uppercase(),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
              ),
              color = badgeColor
            )
          }
        }

        Text(
          text = item.subtitle,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
          color = RadiantSunOrange
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = item.description,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF4B5563),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        if (item.localPhrase.isNotEmpty()) {
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF3F4F6))
              .clickable { onPlayAudio() }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Pronounce",
              tint = AkwaGreenPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "\"${item.localPhrase}\"",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
              ),
              color = AkwaGreenDark
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "• ${item.meaning}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF6B7280),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CultureDetailDialog(
  item: CultureItem,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark
          )
          Text(
            text = item.subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = RadiantSunOrange
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (item.localPhrase.isNotEmpty()) {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = RadiantSunLight)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "Native Expression:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = RadiantSunOrange
              )
              Text(
                text = "\"${item.localPhrase}\"",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF78350F)
              )
              Text(
                text = "Meaning: ${item.meaning}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF451A03)
              )
            }
          }
        }

        Text(
          text = item.details,
          style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
          color = Color(0xFF374151)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
      ) {
        Text("Done")
      }
    }
  )
}
