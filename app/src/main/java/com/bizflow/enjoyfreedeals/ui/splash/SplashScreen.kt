package com.bizflow.enjoyfreedeals.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizflow.enjoyfreedeals.R
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(isLoggedIn: Boolean, onDone: (Boolean) -> Unit) {
    var started by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (started) 1f else .72f, tween(900), label = "splashLogoScale")
    LaunchedEffect(Unit) {
        started = true
        delay(1400)
        onDone(isLoggedIn)
    }
    Box(Modifier.fillMaxSize().background(AppBackground), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Image(painterResource(R.drawable.enjoy_free_deals_logo), contentDescription = "EnjoyFreeDeals", modifier = Modifier.fillMaxWidth(.82f).scale(scale))
            AnimatedVisibility(started) {
                Text("Save More. Earn More.", color = PrimaryGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CircularProgressIndicator(color = PrimaryGreen)
        }
    }
}
