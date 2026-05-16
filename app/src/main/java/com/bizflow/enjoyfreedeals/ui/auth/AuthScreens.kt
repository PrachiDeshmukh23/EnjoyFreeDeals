package com.bizflow.enjoyfreedeals.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bizflow.enjoyfreedeals.R
import com.bizflow.enjoyfreedeals.theme.AppBackground
import com.bizflow.enjoyfreedeals.theme.PrimaryGreen
import com.bizflow.enjoyfreedeals.theme.PrimaryRed
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState

@Composable
fun LoginScreen(
    state: AppUiState,
    onLogin: (String, String) -> Unit,
    onGoogle: () -> Unit,
    onGuest: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    AuthShell {
        Image(painterResource(R.drawable.enjoy_free_deals_logo), null, Modifier.fillMaxWidth().height(116.dp), contentScale = ContentScale.Fit)
        Text("Welcome back", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
        Text("Sign in to discover today's best deals.", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Password") }, singleLine = true, visualTransformation = PasswordVisualTransformation())
        Button(
            onClick = { onLogin(email, password) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !state.isAuthLoading,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
            shape = RoundedCornerShape(50)
        ) {
            if (state.isAuthLoading) CircularProgressIndicator(color = Color.White) else Text("Login", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(onClick = onGoogle, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(50)) {
            Text("Continue with Google", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(onClick = onGuest, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(50)) {
            Text("Continue as Guest", fontWeight = FontWeight.Bold, color = PrimaryGreen)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Forgot password?", color = PrimaryRed, modifier = Modifier.clickable(onClick = onForgotPassword))
            Text("Create account", color = PrimaryGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onCreateAccount))
        }
    }
}

@Composable
fun RegisterScreen(
    state: AppUiState,
    onRegister: (String, String, String, String) -> Unit,
    onLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    AuthShell {
        Image(painterResource(R.drawable.enjoy_free_deals_logo), null, Modifier.fillMaxWidth().height(92.dp), contentScale = ContentScale.Fit)
        Text("Create account", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
        Text("Save deals, track alerts, and shop smarter.", color = Color.Gray)
        OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Name") }, singleLine = true)
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") }, singleLine = true)
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Password") }, singleLine = true, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(confirm, { confirm = it }, Modifier.fillMaxWidth(), label = { Text("Confirm Password") }, singleLine = true, visualTransformation = PasswordVisualTransformation())
        Button(onClick = { onRegister(name, email, password, confirm) }, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = !state.isAuthLoading, colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed), shape = RoundedCornerShape(50)) {
            if (state.isAuthLoading) CircularProgressIndicator(color = Color.White) else Text("Create account", fontWeight = FontWeight.Bold)
        }
        Text("Already have an account? Login", color = PrimaryGreen, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally).clickable(onClick = onLogin))
    }
}

@Composable
private fun AuthShell(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(AppBackground).padding(20.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
        }
    }
}
