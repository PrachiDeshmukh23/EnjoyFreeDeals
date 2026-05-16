package com.bizflow.enjoyfreedeals.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bizflow.enjoyfreedeals.R
import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.theme.AccentYellow
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen
import com.bizflow.enjoyfreedeals.theme.PrimaryRed
import com.bizflow.enjoyfreedeals.ui.deals.DealCard
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: AppUiState,
    deals: List<Deal>,
    onRefresh: () -> Unit,
    onQuery: (String) -> Unit,
    onStore: (String?) -> Unit,
    onOpen: (Deal) -> Unit,
    onSave: (String) -> Unit,
    onShare: (Deal) -> Unit
) {
    PullToRefreshBox(isRefreshing = state.isRefreshing, onRefresh = onRefresh) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(AppBackground),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { HomeHeader(state.unreadCount) }
            item { SearchAndStores(state, onQuery, onStore) }
            item { BannerSlider(state.deals) }
            item { DealSection("Hot deals", deals.filter { it.isHotDeal }, state, onOpen, onSave, onShare) }
            item { DealSection("Today's deals", deals.take(6), state, onOpen, onSave, onShare) }
            item { DealSection("Free deals", deals.filter { it.isFreeDeal || it.discountedPrice == 0.0 }, state, onOpen, onSave, onShare) }
            item { DealSection("Expiring soon", deals.sortedBy { it.expiryDate }.take(5), state, onOpen, onSave, onShare) }
        }
    }
}

@Composable
private fun HomeHeader(unreadCount: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Hello, Deal Hunter \uD83D\uDC4B", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text("Fresh coupons, samples, cashback and discounts", color = Color.Gray)
        }
        BadgedBox(badge = { if (unreadCount > 0) Badge { Text(unreadCount.toString()) } }) {
            Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = PrimaryRed)
        }
    }
}

@Composable
private fun SearchAndStores(state: AppUiState, onQuery: (String) -> Unit, onStore: (String?) -> Unit) {
    OutlinedTextField(
        value = state.query,
        onValueChange = onQuery,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Search deals, stores, coupons...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        singleLine = true
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { FilterChip(selected = state.selectedStore == null, onClick = { onStore(null) }, label = { Text("All") }) }
        items(MockData.stores) { store ->
            FilterChip(selected = state.selectedStore == store, onClick = { onStore(store) }, label = { Text(store) })
        }
    }
}

@Composable
private fun BannerSlider(deals: List<Deal>) {
    var index by remember { mutableIntStateOf(0) }
    LaunchedEffect(deals.size) {
        while (deals.isNotEmpty()) {
            delay(2800)
            index = (index + 1) % minOf(deals.size, 4)
        }
    }
    val deal = deals.getOrNull(index)
    Card(Modifier.fillMaxWidth().height(158.dp), shape = RoundedCornerShape(8.dp)) {
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(PrimaryRed, PrimaryGreen)))) {
            AsyncImage(deal?.imageUrl, null, Modifier.fillMaxSize().alpha(.25f), contentScale = ContentScale.Crop)
            Row(Modifier.fillMaxSize().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.enjoy_free_deals_logo), null, Modifier.width(120.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(deal?.storeName ?: "EnjoyFreeDeals", color = AccentYellow, fontWeight = FontWeight.Bold)
                    Text(deal?.title ?: "Save More. Earn More.", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text("${deal?.discountPercentage ?: 100}% OFF", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun DealSection(title: String, deals: List<Deal>, state: AppUiState, onOpen: (Deal) -> Unit, onSave: (String) -> Unit, onShare: (Deal) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (deals.isEmpty()) {
            EmptyState("No deals available here right now.")
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(deals) { deal ->
                    DealCard(
                        deal = deal,
                        isSaved = state.user.savedDeals.contains(deal.id),
                        onSave = { onSave(deal.id) },
                        onOpen = { onOpen(deal) },
                        onShare = { onShare(deal) },
                        modifier = Modifier.width(304.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(text: String) {
    Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
        Text(text, color = Color.Gray)
    }
}
