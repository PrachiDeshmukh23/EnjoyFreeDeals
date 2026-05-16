package com.bizflow.enjoyfreedeals.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.R
import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen

@Composable
fun AboutScreen() {
    val info = MockData.appInfo
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Image(painterResource(R.drawable.enjoy_free_deals_logo), null, Modifier.fillMaxWidth().height(110.dp), contentScale = ContentScale.Fit) }
        item { Text(info.appName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black) }
        item { Text("Version: ${info.version}") }
        item { Text("Created By: ${info.createdBy}") }
        item { Text(info.description, color = Color.DarkGray) }
        item { Text("Privacy Policy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        item { Text(info.privacyPolicy, color = Color.DarkGray) }
        item { Text("Contact Support: ${info.contactEmail}", color = PrimaryGreen, fontWeight = FontWeight.Bold) }
        item { Text("Firebase setup: add your app's google-services.json inside the app/ folder, enable Authentication, Firestore, and Cloud Messaging, then create the deals, categories, videos, users, and notifications collections with the fields listed in the project request.", color = Color.Gray) }
    }
}
