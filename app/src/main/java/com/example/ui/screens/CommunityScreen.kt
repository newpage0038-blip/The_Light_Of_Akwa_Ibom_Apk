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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommunityPoll
import com.example.data.model.DiscussionComment
import com.example.data.model.DiscussionPost
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
  discussions: List<DiscussionPost>,
  polls: List<CommunityPoll>,
  activePostForComments: DiscussionPost?,
  comments: List<DiscussionComment>,
  onToggleLike: (Long) -> Unit,
  onOpenComments: (DiscussionPost) -> Unit,
  onCloseComments: () -> Unit,
  onAddComment: (Long, String) -> Unit,
  onVotePoll: (Long, Int) -> Unit,
  onCreateDiscussion: (String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var showNewPostDialog by remember { mutableStateOf(false) }

  val categories = listOf("All", "Unity", "Youth Empowerment", "Cultural Heritage", "Economy & Innovation", "General")

  val filteredPosts = if (selectedCategory == "All") {
    discussions
  } else {
    discussions.filter { it.category.contains(selectedCategory, ignoreCase = true) }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showNewPostDialog = true },
        containerColor = AkwaGreenPrimary,
        contentColor = Color.White,
        modifier = Modifier.testTag("create_post_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Create Discussion")
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(padding),
      contentPadding = PaddingValues(bottom = 80.dp)
    ) {
      // Community Guidelines Ribbon
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(AkwaGreenLight)
            .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
          Text(
            text = "🤝 Community Space: Let all discussions reflect respect, unity, and positive development for Akwa Ibom State.",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = AkwaGreenDark
          )
        }
      }

      // Community Polls Section
      if (polls.isNotEmpty()) {
        item {
          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.HowToVote,
              contentDescription = null,
              tint = RadiantSunOrange,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "COMMUNITY POLLS & DECISIONS",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              ),
              color = RadiantSunOrange
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            items(polls) { poll ->
              PollCard(poll = poll, onVote = { option -> onVotePoll(poll.id, option) })
            }
          }
        }
      }

      // Category Chips
      item {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
          text = "COMMUNITY DISCUSSIONS",
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          ),
          color = AkwaGreenDark,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
        Spacer(modifier = Modifier.height(10.dp))
      }

      // Discussions Feed
      if (filteredPosts.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No discussions in this category yet. Be the first to share an idea!",
              style = MaterialTheme.typography.bodyMedium,
              color = Color.Gray
            )
          }
        }
      } else {
        items(filteredPosts, key = { it.id }) { post ->
          DiscussionCard(
            post = post,
            onLike = { onToggleLike(post.id) },
            onComments = { onOpenComments(post) }
          )
        }
      }
    }
  }

  // Create Post Dialog
  if (showNewPostDialog) {
    CreatePostDialog(
      categories = categories.filter { it != "All" },
      onDismiss = { showNewPostDialog = false },
      onSubmit = { title, content, category ->
        onCreateDiscussion(title, content, category)
        showNewPostDialog = false
      }
    )
  }

  // Comments Bottom Sheet / Dialog
  if (activePostForComments != null) {
    CommentsDialog(
      post = activePostForComments,
      comments = comments,
      onDismiss = onCloseComments,
      onAddComment = { text -> onAddComment(activePostForComments.id, text) }
    )
  }
}

@Composable
private fun DiscussionCard(
  post: DiscussionPost,
  onLike: () -> Unit,
  onComments: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("discussion_card_${post.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Author header
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(AkwaGreenPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = post.authorName.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = post.authorName,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF111827)
          )
          Text(
            text = "LGA: ${post.authorLga} • ${post.category}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = post.title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF1F2937)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = post.content,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF374151)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Actions row (Shine Light / Like, Comments)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLike() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (post.isLikedByMe) Icons.Default.WbSunny else Icons.Default.ThumbUp,
            contentDescription = "Shine Light",
            tint = if (post.isLikedByMe) RadiantSunOrange else Color(0xFF6B7280),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${post.likesCount} Shined",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (post.isLikedByMe) FontWeight.Bold else FontWeight.Normal,
              color = if (post.isLikedByMe) RadiantSunOrange else Color(0xFF4B5563)
            )
          )
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onComments() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Comments",
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${post.commentsCount} Comments",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF4B5563))
          )
        }
      }
    }
  }
}

@Composable
private fun PollCard(
  poll: CommunityPoll,
  onVote: (Int) -> Unit
) {
  val totalVotes = (poll.votes1 + poll.votes2 + poll.votes3).coerceAtLeast(1)
  val pct1 = (poll.votes1.toFloat() / totalVotes * 100).toInt()
  val pct2 = (poll.votes2.toFloat() / totalVotes * 100).toInt()
  val pct3 = (poll.votes3.toFloat() / totalVotes * 100).toInt()

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = RadiantSunLight),
    modifier = Modifier.width(300.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = poll.question,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF78350F)
      )
      Spacer(modifier = Modifier.height(10.dp))

      PollOptionRow(
        text = poll.option1,
        pct = pct1,
        isSelected = poll.userVotedOption == 1,
        onSelect = { onVote(1) }
      )
      Spacer(modifier = Modifier.height(6.dp))
      PollOptionRow(
        text = poll.option2,
        pct = pct2,
        isSelected = poll.userVotedOption == 2,
        onSelect = { onVote(2) }
      )
      Spacer(modifier = Modifier.height(6.dp))
      PollOptionRow(
        text = poll.option3,
        pct = pct3,
        isSelected = poll.userVotedOption == 3,
        onSelect = { onVote(3) }
      )

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Total votes: ${poll.votes1 + poll.votes2 + poll.votes3} • Click option to vote",
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF92400E)
      )
    }
  }
}

@Composable
private fun PollOptionRow(
  text: String,
  pct: Int,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) Color(0xFFFFE0B2) else Color.White)
      .clickable { onSelect() }
      .padding(8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
        color = Color(0xFF1F2937),
        modifier = Modifier.weight(1f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Selected",
          tint = RadiantSunOrange,
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "$pct%",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = RadiantSunOrange
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = { pct / 100f },
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .clip(RoundedCornerShape(2.dp)),
      color = RadiantSunOrange,
      trackColor = Color(0xFFE5E7EB)
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePostDialog(
  categories: List<String>,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(categories.first()) }
  var expanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Share with Community", fontWeight = FontWeight.Bold, color = AkwaGreenDark)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Topic Title") },
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
          value = content,
          onValueChange = { content = it },
          label = { Text("Share your thoughts, idea, or achievement...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
          maxLines = 5
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && content.isNotBlank()) {
            onSubmit(title, content, selectedCategory)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = AkwaGreenPrimary)
      ) {
        Text("Post Topic")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
private fun CommentsDialog(
  post: DiscussionPost,
  comments: List<DiscussionComment>,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  var newCommentText by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Comments (${comments.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = AkwaGreenDark
        )
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .height(350.dp)
      ) {
        Text(
          text = post.title,
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF1F2937),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (comments.isEmpty()) {
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "No comments yet. Write the first response!",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.Gray
                )
              }
            }
          } else {
            items(comments) { comment ->
              Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                    text = comment.authorName,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = AkwaGreenPrimary
                    )
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF374151)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = newCommentText,
            onValueChange = { newCommentText = it },
            placeholder = { Text("Write a comment...", fontSize = 12.sp) },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          Spacer(modifier = Modifier.width(6.dp))
          IconButton(
            onClick = {
              if (newCommentText.isNotBlank()) {
                onAddComment(newCommentText)
                newCommentText = ""
              }
            },
            modifier = Modifier
              .clip(CircleShape)
              .background(AkwaGreenPrimary)
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = "Send",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    },
    confirmButton = {}
  )
}
