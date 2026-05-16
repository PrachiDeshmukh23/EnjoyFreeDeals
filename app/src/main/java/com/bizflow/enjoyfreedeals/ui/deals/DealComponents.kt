package com.bizflow.enjoyfreedeals.ui.deals

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bizflow.enjoyfreedeals.R
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.theme.AccentYellow
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen
import com.bizflow.enjoyfreedeals.theme.PrimaryRed

@Composable
fun DealCard(
    deal: Deal,
    isSaved: Boolean,
    onSave: () -> Unit,
    onOpen: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box {
                AsyncImage(
                    model = deal.imageUrl,
                    contentDescription = deal.title,
                    modifier = Modifier.fillMaxWidth().height(152.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground)
                )
                Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (deal.isHotDeal) DealBadge("Hot Deal", PrimaryRed)
                    if (deal.isFreeDeal || deal.discountedPrice == 0.0) DealBadge("Free", PrimaryGreen)
                    if (deal.isLimitedTime) DealBadge("Limited", AccentYellow, Color.Black)
                }
            }
            Text(deal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            PriceRow(deal)
            Text(deal.description, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                StoreLogo(deal.storeName, deal.storeLogoUrl)
                Spacer(Modifier.width(8.dp))
                Text(deal.storeName, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Text("${deal.discountPercentage}% OFF", color = PrimaryRed, fontWeight = FontWeight.Bold)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onOpen, modifier = Modifier.weight(1f)) { Text("View Deal") }
                IconButton(onClick = onSave) { Icon(if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder, contentDescription = "Save deal", tint = PrimaryRed) }
                IconButton(onClick = onShare) { Icon(Icons.Filled.Share, contentDescription = "Share deal", tint = PrimaryGreen) }
            }
        }
    }
}

@Composable
private fun PriceRow(deal: Deal) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(if (deal.discountedPrice == 0.0) "Free" else "Rs ${deal.discountedPrice.toInt()}", color = PrimaryGreen, fontWeight = FontWeight.Black)
        Spacer(Modifier.width(8.dp))
        Text("Rs ${deal.originalPrice.toInt()}", color = Color.Gray, textDecoration = TextDecoration.LineThrough)
    }
}

@Composable
private fun StoreLogo(storeName: String, logoUrl: String) {
    if (logoUrl.isBlank()) {
        Box(Modifier.size(34.dp).clip(CircleShape).background(PrimaryGreen), contentAlignment = Alignment.Center) {
            Text(storeName.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }
    } else {
        AsyncImage(model = logoUrl, contentDescription = storeName, modifier = Modifier.size(34.dp).clip(CircleShape), contentScale = ContentScale.Crop)
    }
}

@Composable
fun DealBadge(text: String, color: Color, contentColor: Color = Color.White) {
    Surface(color = color, shape = RoundedCornerShape(50)) {
        Text(text, Modifier.padding(horizontal = 8.dp, vertical = 3.dp), color = contentColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

fun openDeal(context: Context, deal: Deal, showMessage: (String) -> Unit) {
    if (deal.dealUrl.isBlank()) {
        showMessage("Deal link is not available right now.")
        return
    }
    val dealUri = Uri.parse(deal.dealUrl)
    if (openInStoreApp(context, deal.storeName, dealUri)) return

    runCatching {
        CustomTabsIntent.Builder().build().launchUrl(context, dealUri)
    }.onFailure {
        showMessage("Deal link is not available right now.")
    }
}

private fun openInStoreApp(context: Context, storeName: String, dealUri: Uri): Boolean {
    val packageNames = storeAppPackages[storeName.normalizedStoreName()].orEmpty()
    return packageNames.any { packageName ->
        val intent = Intent(Intent.ACTION_VIEW, dealUri).apply {
            setPackage(packageName)
            addCategory(Intent.CATEGORY_BROWSABLE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching {
            if (intent.resolveActivity(context.packageManager) == null) {
                false
            } else {
                context.startActivity(intent)
                true
            }
        }.getOrDefault(false)
    }
}

private fun String.normalizedStoreName(): String = lowercase()
    .replace("&", "")
    .replace(" ", "")
    .replace("-", "")

private val storeAppPackages = mapOf(
    "amazon" to listOf("in.amazon.mShop.android.shopping", "com.amazon.mShop.android.shopping"),
    "flipkart" to listOf("com.flipkart.android"),
    "meesho" to listOf("com.meesho.supply"),
    "myntra" to listOf("com.myntra.android"),
    "snapdeal" to listOf("com.snapdeal.main"),
    "ajio" to listOf("com.ril.ajio"),
    "tatacliq" to listOf("com.tul.tatacliq"),
    "nykaa" to listOf("com.fsn.nykaa"),
    "croma" to listOf("croma.consumer.android"),
    "jiomart" to listOf("com.jpl.jiomart"),
    "bigbasket" to listOf("com.bigbasket.mobileapp")
)

fun shareDeal(context: Context, deal: Deal) {
    val message = buildString {
        appendLine("Check this deal on EnjoyFreeDeals!")
        appendLine()
        appendLine(deal.title)
        appendLine("Price: ${if (deal.discountedPrice == 0.0) "Free" else "Rs ${deal.discountedPrice.toInt()}"}")
        appendLine("Store: ${deal.storeName}")
        if (deal.dealUrl.isNotBlank()) appendLine("View Deal: ${deal.dealUrl}")
    }
    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }, "Share Deal"))
}
