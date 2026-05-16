package com.bizflow.enjoyfreedeals.ui.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.data.model.DealSort
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.ui.home.EmptyState
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState

@Composable
fun AllDealsScreen(
    state: AppUiState,
    deals: List<Deal>,
    onQuery: (String) -> Unit,
    onStore: (String?) -> Unit,
    onSort: (DealSort) -> Unit,
    onOpen: (Deal) -> Unit,
    onSave: (String) -> Unit,
    onShare: (Deal) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().background(AppBackground), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("All Deals", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                OutlinedTextField(state.query, onQuery, Modifier.fillMaxWidth(), label = { Text("Search deals, stores, coupons...") }, leadingIcon = { Icon(Icons.Filled.Search, null) }, singleLine = true)
                StoreAndSortFilters(state, onStore, onSort)
            }
        }
        if (deals.isEmpty()) {
            item { EmptyState("No deals found. Try another search or filter.") }
        } else {
            items(deals, key = { it.id }) { deal ->
                DealCard(deal, state.user.savedDeals.contains(deal.id), { onSave(deal.id) }, { onOpen(deal) }, { onShare(deal) })
            }
        }
    }
}

@Composable
private fun StoreAndSortFilters(state: AppUiState, onStore: (String?) -> Unit, onSort: (DealSort) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { FilterChip(selected = state.selectedStore == null, onClick = { onStore(null) }, label = { Text("All") }) }
        items(MockData.stores) { store ->
            FilterChip(selected = state.selectedStore == store, onClick = { onStore(store) }, label = { Text(store) })
        }
    }
    androidx.compose.foundation.layout.Box {
        AssistChip(onClick = { expanded = true }, label = { Text("Sort: ${state.sort.label}", color = Color.DarkGray) })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DealSort.entries.forEach { sort ->
                DropdownMenuItem(text = { Text(sort.label) }, onClick = { onSort(sort); expanded = false })
            }
        }
    }
}
