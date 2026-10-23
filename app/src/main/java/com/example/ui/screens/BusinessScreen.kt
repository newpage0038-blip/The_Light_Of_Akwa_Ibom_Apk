package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InitialData
import com.example.data.model.BusinessListing
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(
  businesses: List<BusinessListing>,
  onRegisterBusiness: (String, String, String, String, String, String, String, String) -> Unit,
  onShowFeedback: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedCategory by remember { mutableStateOf("All") }
  var showRegisterDialog by remember { mutableStateOf(false) }

  val categories = listOf("All", "Catering & Delicacies", "Fashion & Crafts", "Tech & Digital", "Agriculture & Trade", "Services")

  val filteredBusinesses = if (selectedCategory == "All") {
    businesses
  } else {
    businesses.filter { it.category.contains(selectedCategory, ignoreCase = true) }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showRegisterDialog = true },
        containerColor = AkwaGreenPrimary,
        contentColor = Color.White,
        modifier = Modifier.testTag("register_business_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "List Your Business")
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
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "BUSINESS & MSME DIRECTORY",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = AkwaGreenDark
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Patronize and support local Akwa Ibom entrepreneurs, artisans, and service providers.",
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

      // Business List
      if (filteredBusinesses.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text("No businesses listed in this category yet.", color = Color.Gray)
          }
        }
      } else {
        items(filteredBusinesses, key = { it.id }) { item ->
          BusinessCard(
            item = item,
            onCall = {
              try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phoneNumber}"))
                context.startActivity(intent)
              } catch (e: Exception) {
                onShowFeedback("Dialing: ${item.phoneNumber}")
              }
            },
            onWhatsApp = {
              try {
                val url = "https://api.whatsapp.com/send?phone=${item.whatsapp}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
              } catch (e: Exception) {
                onShowFeedback("WhatsApp: ${item.whatsapp}")
              }
            }
          )
        }
      }
    }
  }

  // Register Business Dialog
  if (showRegisterDialog) {
    RegisterBusinessDialog(
      categories = categories.filter { it != "All" },
      lgas = InitialData.LGAS,
      onDismiss = { showRegisterDialog = false },
      onSubmit = { name, cat, desc, lga, addr, phone, wa, owner ->
        onRegisterBusiness(name, cat, desc, lga, addr, phone, wa, owner)
        showRegisterDialog = false
      }
    )
  }
}

@Composable
private fun BusinessCard(
  item: BusinessListing,
  onCall: () -> Unit,
  onWhatsApp: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("business_card_${item.id}")
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
            .background(AkwaGreenLight)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = item.category.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = AkwaGreenPrimary
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = RadiantSunAmber,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = item.rating.toString(),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF374151)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = item.businessName,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.Default.Verified,
          contentDescription = "Verified Local Business",
          tint = AkwaGreenPrimary,
          modifier = Modifier.size(16.dp)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = Color(0xFF9CA3AF),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Proprietor: ${item.ownerName}",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF6B7280)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = Color(0xFF9CA3AF),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${item.address} • LGA: ${item.lga}",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF6B7280)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = item.description,
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
        color = Color(0xFF374151)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Direct Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onCall,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = null,
            tint = AkwaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Call", color = AkwaGreenPrimary, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = onWhatsApp,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterBusinessDialog(
  categories: List<String>,
  lgas: List<String>,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String, String, String, String, String, String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var owner by remember { mutableStateOf("") }
  var address by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var whatsapp by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var selectedLga by remember { mutableStateOf("Uyo") }
  var selectedCategory by remember { mutableStateOf(categories.first()) }
  var expandedCat by remember { mutableStateOf(false) }
  var expandedLga by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("List Your Business", fontWeight = FontWeight.Bold, color = AkwaGreenDark)
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Business / Enterprise Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = owner,
          onValueChange = { owner = it },
          label = { Text("Owner / Contact Person") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        // Category dropdown
        ExposedDropdownMenuBox(
          expanded = expandedCat,
          onExpandedChange = { expandedCat = !expandedCat }
        ) {
          OutlinedTextField(
            value = selectedCategory,
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
                  selectedCategory = cat
                  expandedCat = false
                }
              )
            }
          }
        }

        // LGA dropdown
        ExposedDropdownMenuBox(
          expanded = expandedLga,
          onExpandedChange = { expandedLga = !expandedLga }
        ) {
          OutlinedTextField(
            value = selectedLga,
            onValueChange = {},
            readOnly = true,
            label = { Text("Local Government Area (LGA)") },
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
          value = address,
          onValueChange = { address = it },
          label = { Text("Street Address") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = whatsapp,
            onValueChange = { whatsapp = it },
            label = { Text("WhatsApp (e.g. 2348...)") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description of Products/Services") },
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
          if (name.isNotBlank() && phone.isNotBlank()) {
            onSubmit(name, selectedCategory, description, selectedLga, address, phone, whatsapp.ifBlank { phone }, owner)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
      ) {
        Text("List Business")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
