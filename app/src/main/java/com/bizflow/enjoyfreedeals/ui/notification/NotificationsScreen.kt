package com.bizflow.enjoyfreedeals.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.data.model.DealNotification
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryRed
import com.bizflow.enjoyfreedeals.ui.home.EmptyState

@Composable
fun NotificationsScreen(notifications: List<DealNotification>, unreadCount: Int, onClick: (DealNotification) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Notifications", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                if (unreadCount > 0) Badge { Text(unreadCount.toString()) }
            }
        }
        if (notifications.isEmpty()) {
            item { EmptyState("No notifications yet.") }
        } else {
            items(notifications, key = { it.id }) { notification ->
                NotificationCard(notification) { onClick(notification) }
            }
        }
    }
}

@Composable
private fun NotificationCard(notification: DealNotification, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (notification.isRead) MaterialTheme.colorScheme.surface else Color(0xFFFFF8E1))
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(10.dp).clip(CircleShape).background(if (notification.isRead) Color.LightGray else PrimaryRed))
            Column {
                Text(notification.title, fontWeight = FontWeight.Bold)
                Text(notification.message, color = Color.Gray)
            }
        }
    }
}
