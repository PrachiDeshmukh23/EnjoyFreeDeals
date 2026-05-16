package com.bizflow.enjoyfreedeals.ui.blog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bizflow.enjoyfreedeals.data.model.VideoFind
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.ui.home.EmptyState

@Composable
fun BlogScreen(videos: List<VideoFind>, onReadMore: (VideoFind) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Blog / Video Finds", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black) }
        if (videos.isEmpty()) {
            item { EmptyState("No shopping guides available right now.") }
        } else {
            items(videos, key = { it.id }) { video ->
                BlogCard(video) { onReadMore(video) }
            }
        }
    }
}

@Composable
private fun BlogCard(video: VideoFind, onReadMore: () -> Unit) {
    Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AsyncImage(video.imageUrl, video.title, Modifier.fillMaxWidth().height(144.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Text(video.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(video.description, color = Color.Gray)
            OutlinedButton(onClick = onReadMore) { Text("Read More") }
        }
    }
}
