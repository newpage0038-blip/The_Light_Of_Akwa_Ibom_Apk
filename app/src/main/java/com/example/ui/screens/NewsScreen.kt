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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsArticle
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange

@Composable
fun NewsScreen(
  news: List<NewsArticle>,
  selectedArticle: NewsArticle?,
  onSelectArticle: (NewsArticle?) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Development", "Infrastructure", "Education & Tech", "Culture & Sports")

  val filteredNews = news.filter { article ->
    val matchesCat = selectedCategory == "All" || article.category.contains(selectedCategory, ignoreCase = true)
    val matchesQuery = searchQuery.isBlank() ||
      article.title.contains(searchQuery, ignoreCase = true) ||
      article.summary.contains(searchQuery, ignoreCase = true)
    matchesCat && matchesQuery
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
          imageVector = Icons.Default.Newspaper,
          contentDescription = null,
          tint = AkwaGreenPrimary,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "NEWS & POSITIVE DEVELOPMENTS",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
          color = AkwaGreenDark
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Celebrating progress, achievements, and milestones of Akwa Ibom people.",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF6B7280)
      )
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search updates, milestones, projects...", fontSize = 13.sp) },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("news_search_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )
    }

    // Category Filter Chips
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

    // News Items
    if (filteredNews.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("No articles found matching your search.", color = Color.Gray)
        }
      }
    } else {
      items(filteredNews, key = { it.id }) { item ->
        NewsCard(
          article = item,
          onClick = { onSelectArticle(item) }
        )
      }
    }
  }

  // Full Article Reading Dialog
  if (selectedArticle != null) {
    ArticleReaderDialog(
      article = selectedArticle,
      onDismiss = { onSelectArticle(null) }
    )
  }
}

@Composable
private fun NewsCard(
  article: NewsArticle,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("news_card_${article.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AkwaGreenLight)
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = article.category,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = AkwaGreenPrimary
            )
          }
          if (article.isFeatured) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Featured",
              tint = RadiantSunAmber,
              modifier = Modifier.size(16.dp)
            )
          }
        }
        Text(
          text = article.date,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF9CA3AF)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = article.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF111827)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = article.summary,
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
        color = Color(0xFF4B5563),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = article.readTime,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF9CA3AF)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Read Full Story",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RadiantSunOrange
            )
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = RadiantSunOrange,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ArticleReaderDialog(
  article: NewsArticle,
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
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AkwaGreenLight)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = article.category,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenPrimary
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(380.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        item {
          Text(
            text = article.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${article.date} • ${article.readTime}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280)
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = article.fullContent,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = Color(0xFF374151)
          )
        }
      }
    },
    confirmButton = {
      androidx.compose.material3.Button(
        onClick = onDismiss,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
      ) {
        Text("Done")
      }
    }
  )
}
