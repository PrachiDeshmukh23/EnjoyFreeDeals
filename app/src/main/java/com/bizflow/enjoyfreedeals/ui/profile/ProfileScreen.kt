package com.bizflow.enjoyfreedeals.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState

@Composable
fun ProfileScreen(state: AppUiState, onAbout: () -> Unit, onLogout: () -> Unit, onPrivacy: () -> Unit, onSupport: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccountCircle, null, Modifier.size(76.dp), tint = PrimaryGreen)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(state.user.name.ifBlank { "Deal Hunter" }, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    Text(state.user.email.ifBlank { "Not signed in" }, color = Color.Gray)
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Saved deals", state.savedDeals.size.toString(), Modifier.weight(1f))
                MetricCard("Unread alerts", state.unreadCount.toString(), Modifier.weight(1f))
            }
        }
        item { OutlinedButton(onClick = onAbout, Modifier.fillMaxWidth()) { Icon(Icons.Filled.Info, null); Spacer(Modifier.width(8.dp)); Text("About EnjoyFreeDeals") } }
        item { OutlinedButton(onClick = onPrivacy, Modifier.fillMaxWidth()) { Text("Privacy Policy") } }
        item { OutlinedButton(onClick = onSupport, Modifier.fillMaxWidth()) { Text("Contact Support") } }
        item { Button(onClick = onLogout, Modifier.fillMaxWidth()) { Icon(Icons.AutoMirrored.Filled.Logout, null); Spacer(Modifier.width(8.dp)); Text("Logout") } }
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = PrimaryGreen)
            Text(label, color = Color.Gray)
        }
    }
}
