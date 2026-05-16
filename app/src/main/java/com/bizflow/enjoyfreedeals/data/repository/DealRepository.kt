package com.bizflow.enjoyfreedeals.data.repository

import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.Category
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.data.model.DealNotification
import com.bizflow.enjoyfreedeals.data.model.DealSort
import com.bizflow.enjoyfreedeals.data.model.VideoFind
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DealRepository(private val firestore: FirebaseFirestore? = firestoreOrNull()) {
    suspend fun deals(): List<Deal> = fetch("deals", Deal::class.java).ifEmpty { MockData.deals }
    suspend fun categories(): List<Category> = fetch("categories", Category::class.java).ifEmpty { MockData.categories }
    suspend fun videos(): List<VideoFind> = fetch("videos", VideoFind::class.java).ifEmpty { MockData.videos }
    suspend fun notifications(): List<DealNotification> = fetch("notifications", DealNotification::class.java).ifEmpty { MockData.notifications }

    suspend fun markNotificationRead(id: String) {
        runCatching { firestore?.collection("notifications")?.document(id)?.update("isRead", true)?.await() }
    }

    fun filterDeals(
        source: List<Deal>,
        query: String,
        store: String?,
        categoryId: String?,
        sort: DealSort
    ): List<Deal> {
        val needle = query.trim().lowercase()
        return source
            .filter { deal ->
                (store == null || deal.storeName.equals(store, ignoreCase = true)) &&
                    (categoryId == null || deal.categoryId == categoryId) &&
                    (needle.isBlank() ||
                        deal.title.lowercase().contains(needle) ||
                        deal.description.lowercase().contains(needle) ||
                        deal.storeName.lowercase().contains(needle))
            }
            .let { list ->
                when (sort) {
                    DealSort.Newest -> list.sortedByDescending { it.createdAt }
                    DealSort.HighestDiscount -> list.sortedByDescending { it.discountPercentage }
                    DealSort.LowestPrice -> list.sortedBy { it.discountedPrice }
                    DealSort.FreeDeals -> list.filter { it.isFreeDeal || it.discountedPrice == 0.0 }
                    DealSort.ExpiringSoon -> list.sortedBy { it.expiryDate }
                }
            }
    }

    private suspend fun <T : Any> fetch(collection: String, clazz: Class<T>): List<T> = runCatching {
        val firestore = firestore ?: return emptyList()
        firestore.collection(collection).get().await().documents.mapNotNull { document ->
            document.toObject(clazz)?.let { item ->
                item
            }
        }
    }.getOrDefault(emptyList())

    companion object {
        private fun firestoreOrNull(): FirebaseFirestore? = runCatching {
            FirebaseApp.getInstance()
            FirebaseFirestore.getInstance()
        }.getOrNull()
    }
}
