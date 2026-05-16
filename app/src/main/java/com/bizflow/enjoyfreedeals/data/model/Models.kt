package com.bizflow.enjoyfreedeals.data.model

data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val savedDeals: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class Deal(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val discountPercentage: Int = 0,
    val storeName: String = "",
    val storeLogoUrl: String = "",
    val categoryId: String = "",
    val dealUrl: String = "",
    val isHotDeal: Boolean = false,
    val isFreeDeal: Boolean = false,
    val isLimitedTime: Boolean = false,
    val expiryDate: Long = System.currentTimeMillis() + 86_400_000,
    val createdAt: Long = System.currentTimeMillis()
)

data class Category(
    val id: String = "",
    val name: String = "",
    val iconUrl: String = "",
    val gradientStart: String = "#E91B23",
    val gradientEnd: String = "#006B2E"
)

data class VideoFind(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class DealNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val dealId: String = "",
    val targetUrl: String = "",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class AppInfo(
    val appName: String = "EnjoyFreeDeals",
    val version: String = "1.0.0",
    val createdBy: String = "BizFlow Team",
    val description: String = "EnjoyFreeDeals helps shoppers find deals, coupons, cashback offers, free samples, and discounts from Amazon, Flipkart, Meesho, Myntra, Snapdeal, Ajio, TataCliq, Nykaa, Croma, JioMart, BigBasket, and more.",
    val contactEmail: String = "support@enjoyfreedeals.in",
    val privacyPolicy: String = "We collect only the information required to manage your account, saved deals, and notifications."
)

enum class DealSort(val label: String) {
    Newest("Newest"),
    HighestDiscount("Highest Discount"),
    LowestPrice("Lowest Price"),
    FreeDeals("Free Deals"),
    ExpiringSoon("Expiring Soon")
}

data class AuthResult(val success: Boolean, val message: String = "")
