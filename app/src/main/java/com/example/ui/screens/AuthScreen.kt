package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.R
import com.example.ui.auth.AuthManager
import com.example.ui.theme.AkwaGreenDark
import com.example.ui.theme.AkwaGreenLight
import com.example.ui.theme.AkwaGreenPrimary
import com.example.ui.theme.RadiantSunAmber
import com.example.ui.theme.RadiantSunLight
import com.example.ui.theme.RadiantSunOrange

@Composable
fun AuthScreen(
  onAuthSuccess: () -> Unit,
  onDismiss: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .padding(24.dp)
  ) {
    if (onDismiss != null) {
      IconButton(
        onClick = onDismiss,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(top = 8.dp)
      ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
      }
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // App Sun Badge
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(
                RadiantSunAmber,
                RadiantSunOrange,
                Color(0xFFBF360C)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.WbSunny,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(44.dp)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      Text(
        text = "MEMBER SIGN IN",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Black,
          letterSpacing = 1.sp
        ),
        color = AkwaGreenDark
      )

      Text(
        text = "The Light of Akwa Ibom Community",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = RadiantSunOrange
      )

      Spacer(modifier = Modifier.height(14.dp))

      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AkwaGreenLight),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Sign in to unlock live community communication:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = AkwaGreenDark
          )
          Spacer(modifier = Modifier.height(8.dp))
          FeatureCheckItem("1-on-1 Real-time Private Messaging")
          FeatureCheckItem("Community Group Chat Hall")
          FeatureCheckItem("High-Definition Voice & Video Calls")
          FeatureCheckItem("Verified Member Directory across 31 LGAs")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      if (errorMessage != null) {
        Text(
          text = errorMessage ?: "",
          color = MaterialTheme.colorScheme.error,
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }

      // Interactive Google Sign-In Button
      Button(
        onClick = {
          isLoading = true
          errorMessage = null
          AuthManager.onGoogleSignInClicked(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = {
              isLoading = false
              onAuthSuccess()
            },
            onAuthError = { err ->
              isLoading = false
              errorMessage = err
            },
            scope = coroutineScope,
            onAuthCancelled = {
              isLoading = false
            }
          )
        },
        enabled = !isLoading,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("google_sign_in_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = AkwaGreenPrimary,
          contentColor = Color.White
        )
      ) {
        if (isLoading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = Color.White,
            strokeWidth = 2.dp
          )
        } else {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = null,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Sign in with Google",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = Color.Gray,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Zero-trust privacy • Protected by Firebase Auth & Firestore",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray
        )
      }
    }
  }
}

@Composable
private fun FeatureCheckItem(text: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.CheckCircle,
      contentDescription = null,
      tint = AkwaGreenPrimary,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      color = Color(0xFF1F2937)
    )
  }
}
