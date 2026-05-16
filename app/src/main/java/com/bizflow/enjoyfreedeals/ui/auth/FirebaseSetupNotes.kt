package com.bizflow.enjoyfreedeals.ui.auth

object FirebaseSetupNotes {
    val sampleFirestoreData = """
        deals: id, title, description, imageUrl, originalPrice, discountedPrice, discountPercentage, storeName, storeLogoUrl, categoryId, dealUrl, isHotDeal, isFreeDeal, isLimitedTime, expiryDate, createdAt
        categories: id, name, iconUrl
        videos: id, title, description, imageUrl, videoUrl, createdAt
        users: uid, name, email, savedDeals, createdAt
        notifications: id, title, message, isRead, createdAt
    """.trimIndent()
}
