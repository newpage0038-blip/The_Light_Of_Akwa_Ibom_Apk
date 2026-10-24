package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.CallSession
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunOrange
import kotlinx.coroutines.delay

@Composable
fun IncomingCallBanner(
  call: CallSession,
  onAnswer: () -> Unit,
  onReject: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("incoming_call_banner")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(AkwaGreenPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (call.callType == "video") Icons.Default.Videocam else Icons.Default.Call,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = call.callerName.ifBlank { "Akwa Ibom Member" },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
          Text(
            text = "Incoming ${if (call.callType == "video") "Video" else "Audio"} Call...",
            style = MaterialTheme.typography.bodySmall,
            color = RadiantSunAmber
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        // Reject button
        IconButton(
          onClick = onReject,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFFDC2626))
            .testTag("reject_call_button")
        ) {
          Icon(
            imageVector = Icons.Default.CallEnd,
            contentDescription = "Reject",
            tint = Color.White
          )
        }

        // Answer button
        IconButton(
          onClick = onAnswer,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF16A34A))
            .testTag("answer_call_button")
        ) {
          Icon(
            imageVector = if (call.callType == "video") Icons.Default.Videocam else Icons.Default.Call,
            contentDescription = "Answer",
            tint = Color.White
          )
        }
      }
    }
  }
}

@Composable
fun ActiveCallScreen(
  call: CallSession,
  isMyCall: Boolean,
  onEndCall: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isMuted by remember { mutableStateOf(false) }
  var isSpeaker by remember { mutableStateOf(true) }
  var isVideoEnabled by remember { mutableStateOf(call.callType == "video") }
  var isFrontCamera by remember { mutableStateOf(true) }
  var callDurationSeconds by remember { mutableIntStateOf(0) }

  val otherPartyName = if (isMyCall) call.receiverName else call.callerName

  LaunchedEffect(call.status) {
    if (call.status == "accepted") {
      while (true) {
        delay(1000)
        callDurationSeconds++
      }
    }
  }

  val minutes = callDurationSeconds / 60
  val seconds = callDurationSeconds % 60
  val durationString = String.format("%02d:%02d", minutes, seconds)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF0F172A))
      .testTag("active_call_screen")
  ) {
    if (isVideoEnabled) {
      CameraPreviewView(
        isFrontCamera = isFrontCamera,
        modifier = Modifier.fillMaxSize()
      )

      // Gradient overlay for readability
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Black.copy(alpha = 0.6f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.8f)
              )
            )
          )
      )
    }

    // Top info bar
    Column(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 48.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (!isVideoEnabled) {
        Box(
          modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(AkwaGreenPrimary)
            .border(3.dp, RadiantSunOrange, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(54.dp)
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
      }

      Text(
        text = otherPartyName.ifBlank { "Akwa Ibom Citizen" },
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp
        ),
        color = Color.White
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = when (call.status) {
          "ringing" -> if (isMyCall) "Ringing member..." else "Incoming call..."
          "accepted" -> durationString
          "rejected" -> "Call declined"
          "ended" -> "Call ended"
          else -> call.status
        },
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = if (call.status == "accepted") Color(0xFF4ADE80) else RadiantSunAmber
      )
    }

    // Bottom Controls Bar
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 40.dp, start = 20.dp, end = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Mute Toggle
        IconButton(
          onClick = { isMuted = !isMuted },
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(if (isMuted) Color.White else Color(0x33FFFFFF))
        ) {
          Icon(
            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
            contentDescription = "Mute",
            tint = if (isMuted) Color.Black else Color.White
          )
        }

        // Camera Flip (if video)
        if (call.callType == "video") {
          IconButton(
            onClick = { isFrontCamera = !isFrontCamera },
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0x33FFFFFF))
          ) {
            Icon(
              imageVector = Icons.Default.Cameraswitch,
              contentDescription = "Switch Camera",
              tint = Color.White
            )
          }

          // Video On/Off Toggle
          IconButton(
            onClick = { isVideoEnabled = !isVideoEnabled },
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (!isVideoEnabled) Color.White else Color(0x33FFFFFF))
          ) {
            Icon(
              imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
              contentDescription = "Video Toggle",
              tint = if (!isVideoEnabled) Color.Black else Color.White
            )
          }
        } else {
          // Speaker Toggle for Audio Call
          IconButton(
            onClick = { isSpeaker = !isSpeaker },
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (isSpeaker) Color.White else Color(0x33FFFFFF))
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Speaker",
              tint = if (isSpeaker) Color.Black else Color.White
            )
          }
        }

        // End Call Button
        IconButton(
          onClick = onEndCall,
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFFDC2626))
            .testTag("end_call_button")
        ) {
          Icon(
            imageVector = Icons.Default.CallEnd,
            contentDescription = "End Call",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }
      }
    }
  }
}
