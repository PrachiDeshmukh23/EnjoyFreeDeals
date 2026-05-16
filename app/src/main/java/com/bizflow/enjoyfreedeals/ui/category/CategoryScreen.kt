package com.bizflow.enjoyfreedeals.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.data.model.Category
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryRed
import com.bizflow.enjoyfreedeals.ui.deals.DealCard
import com.bizflow.enjoyfreedeals.ui.home.EmptyState
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState

@Composable
fun CategoryScreen(categories: List<Category>, onCategoryClick: (Category) -> Unit) {
    androidx.compose.foundation.layout.Column(Modifier.fillMaxSize().background(AppBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Categories", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        if (categories.isEmpty()) {
            EmptyState("No categories available right now.")
        } else {
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories, key = { it.id }) { category ->
                    CategoryCard(category) { onCategoryClick(category) }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().aspectRatio(1.18f).clickable(onClick = onClick), shape = RoundedCornerShape(8.dp)) {
        Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(parseColor(category.gradientStart), parseColor(category.gradientEnd)))).padding(14.dp)) {
            Text(category.name.take(1), color = Color.White.copy(alpha = .28f), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Black)
            Text(category.name, Modifier.align(Alignment.BottomStart), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoryDealsScreen(
    categoryName: String,
    state: AppUiState,
    deals: List<Deal>,
    onOpen: (Deal) -> Unit,
    onSave: (String) -> Unit,
    onShare: (Deal) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(categoryName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black) }
        if (deals.isEmpty()) {
            item { EmptyState("No deals available in this category right now.") }
        } else {
            items(deals, key = { it.id }) { deal ->
                DealCard(deal, state.user.savedDeals.contains(deal.id), { onSave(deal.id) }, { onOpen(deal) }, { onShare(deal) })
            }
        }
    }
}

private fun parseColor(hex: String): Color = runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(PrimaryRed)
