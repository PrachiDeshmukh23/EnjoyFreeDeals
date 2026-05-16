package com.bizflow.enjoyfreedeals.data.mock

import com.bizflow.enjoyfreedeals.data.model.AppInfo
import com.bizflow.enjoyfreedeals.data.model.AppUser
import com.bizflow.enjoyfreedeals.data.model.Category
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.data.model.DealNotification
import com.bizflow.enjoyfreedeals.data.model.VideoFind

object MockData {
    private const val electronics = "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=900"
    private const val fashion = "https://images.unsplash.com/photo-1445205170230-053b83016050?w=900"
    private const val beauty = "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=900"
    private const val grocery = "https://images.unsplash.com/photo-1542838132-92c53300491e?w=900"
    private const val home = "https://images.unsplash.com/photo-1556911220-bff31c812dba?w=900"
    private const val dealsImage = "https://images.unsplash.com/photo-1607083206968-13611e3d76db?w=900"

    val stores = listOf("Amazon", "Flipkart", "Meesho", "Myntra", "Snapdeal", "Ajio", "TataCliq", "Nykaa", "Croma", "JioMart", "BigBasket")

    val categories = listOf(
        Category("electronics", "Electronics", electronics, "#E91B23", "#FF6B6B"),
        Category("mobiles", "Mobiles", electronics, "#006B2E", "#00A651"),
        Category("fashion", "Fashion", fashion, "#1E1E1E", "#E91B23"),
        Category("beauty", "Beauty", beauty, "#E91B23", "#FFD600"),
        Category("grocery", "Grocery", grocery, "#006B2E", "#FFD600"),
        Category("home-kitchen", "Home & Kitchen", home, "#2D3748", "#006B2E"),
        Category("free-samples", "Free Samples", beauty, "#E91B23", "#006B2E"),
        Category("coupons", "Coupons", dealsImage, "#FFD600", "#E91B23"),
        Category("recharge", "Recharge Offers", electronics, "#006B2E", "#00BCD4"),
        Category("bank", "Bank Offers", dealsImage, "#1E1E1E", "#FFD600"),
        Category("student", "Student Deals", fashion, "#006B2E", "#E91B23"),
        Category("festival", "Festival Deals", dealsImage, "#E91B23", "#FFD600"),
        Category("travel", "Travel Deals", dealsImage, "#006B2E", "#1E88E5"),
        Category("food", "Food Deals", grocery, "#E91B23", "#FF9800"),
        Category("baby", "Baby Products", dealsImage, "#FFD600", "#006B2E"),
        Category("gaming", "Gaming", electronics, "#1E1E1E", "#E91B23")
    )

    val deals = listOf(
        Deal("d1", "boAt Bluetooth Earbuds", "Amazon audio deal with fast delivery and bank savings.", electronics, 2999.0, 1199.0, 60, "Amazon", "", "electronics", "https://www.amazon.in", true, false, true, System.currentTimeMillis() + 3_600_000),
        Deal("d2", "Realme Smartphone", "Flipkart phone offer with card cashback.", electronics, 14999.0, 11999.0, 20, "Flipkart", "", "mobiles", "https://www.flipkart.com"),
        Deal("d3", "Women Kurti", "Trending Meesho fashion pick at a low price.", fashion, 999.0, 299.0, 70, "Meesho", "", "fashion", "https://www.meesho.com", true),
        Deal("d4", "Sports Shoes", "Myntra running shoes with limited-time coupon.", fashion, 3999.0, 1799.0, 55, "Myntra", "", "fashion", "https://www.myntra.com", false, false, true, System.currentTimeMillis() + 7_200_000),
        Deal("d5", "Kitchen Storage Set", "Snapdeal storage combo for organized kitchens.", home, 1299.0, 714.0, 45, "Snapdeal", "", "home-kitchen", "https://www.snapdeal.com"),
        Deal("d6", "Men's T-Shirt", "Ajio casual t-shirt with extra coupon savings.", fashion, 799.0, 399.0, 50, "Ajio", "", "fashion", "https://www.ajio.com", true),
        Deal("d7", "Smartwatch", "TataCliq smartwatch with fitness tracking.", electronics, 4999.0, 2999.0, 40, "TataCliq", "", "electronics", "https://www.tatacliq.com"),
        Deal("d8", "Beauty Combo", "Nykaa beauty combo with skincare essentials.", beauty, 1999.0, 1299.0, 35, "Nykaa", "", "beauty", "https://www.nykaa.com"),
        Deal("d9", "Bluetooth Speaker", "Croma portable speaker with clear sound.", electronics, 2499.0, 1749.0, 30, "Croma", "", "electronics", "https://www.croma.com"),
        Deal("d10", "Grocery Combo", "JioMart grocery pack for monthly essentials.", grocery, 999.0, 749.0, 25, "JioMart", "", "grocery", "https://www.jiomart.com"),
        Deal("d11", "Fruit Basket", "BigBasket fresh fruit basket at a weekend price.", grocery, 699.0, 559.0, 20, "BigBasket", "", "grocery", "https://www.bigbasket.com"),
        Deal("d12", "Skincare Sample Kit", "Claim a free skincare sample kit while stocks last.", beauty, 499.0, 0.0, 100, "Free Samples", "", "free-samples", "", false, true, true)
    )

    val videos = listOf(
        VideoFind("v1", "How to Find Best Deals Online", "A simple guide to spotting genuine shopping savings.", dealsImage, "https://www.youtube.com"),
        VideoFind("v2", "Top 10 Amazon Sale Tips", "Use wishlists, bank offers, and lightning deal alerts.", electronics, "https://www.youtube.com"),
        VideoFind("v3", "Best Flipkart Shopping Tricks", "Make every major sale easier to shop.", electronics, "https://www.youtube.com"),
        VideoFind("v4", "How to Use Coupon Codes", "Avoid expired coupons and apply the right stack.", dealsImage, "https://www.youtube.com"),
        VideoFind("v5", "Cashback and Bank Offer Guide", "Understand instant discounts and delayed cashback.", dealsImage, "https://www.youtube.com"),
        VideoFind("v6", "Meesho Shopping Guide", "Find value picks across fashion and home categories.", fashion, "https://www.youtube.com"),
        VideoFind("v7", "Myntra Fashion Sale Tips", "Plan fashion wishlists for bigger markdowns.", fashion, "https://www.youtube.com"),
        VideoFind("v8", "Dress Offers and Fashion Deals", "Choose fashion deals that are actually worth buying.", fashion, "https://www.youtube.com"),
        VideoFind("v9", "Product Offers and Discount Deals", "Compare discount depth, store reputation, and timing.", dealsImage, "https://www.youtube.com")
    )

    val notifications = listOf(
        DealNotification("n1", "Hot Deal Live", "boAt earbuds are 60% off on Amazon.", "d1", "https://www.amazon.in"),
        DealNotification("n2", "Free Deal Alert", "Skincare sample kit is available for free.", "d12", ""),
        DealNotification("n3", "Expiring Soon", "Myntra sports shoes offer ends soon.", "d4", "https://www.myntra.com")
    )

    val user = AppUser("mock-user", "Deal Hunter", "hunter@enjoyfreedeals.in", listOf("d1"))
    val appInfo = AppInfo()
}
