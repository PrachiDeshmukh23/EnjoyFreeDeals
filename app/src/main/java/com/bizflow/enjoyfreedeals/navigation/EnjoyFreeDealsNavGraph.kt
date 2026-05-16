package com.bizflow.enjoyfreedeals.navigation

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.data.model.VideoFind
import com.bizflow.enjoyfreedeals.ui.about.AboutScreen
import com.bizflow.enjoyfreedeals.ui.auth.LoginScreen
import com.bizflow.enjoyfreedeals.ui.auth.RegisterScreen
import com.bizflow.enjoyfreedeals.ui.blog.BlogScreen
import com.bizflow.enjoyfreedeals.ui.category.CategoryDealsScreen
import com.bizflow.enjoyfreedeals.ui.category.CategoryScreen
import com.bizflow.enjoyfreedeals.ui.deals.AllDealsScreen
import com.bizflow.enjoyfreedeals.ui.deals.openDeal
import com.bizflow.enjoyfreedeals.ui.deals.shareDeal
import com.bizflow.enjoyfreedeals.ui.home.HomeScreen
import com.bizflow.enjoyfreedeals.ui.notification.NotificationsScreen
import com.bizflow.enjoyfreedeals.ui.profile.ProfileScreen
import com.bizflow.enjoyfreedeals.ui.splash.SplashScreen
import com.bizflow.enjoyfreedeals.viewmodel.AppUiState
import com.bizflow.enjoyfreedeals.viewmodel.AppViewModel

object Routes {
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val Main = "main"
    const val Home = "home"
    const val Deals = "deals"
    const val Category = "category"
    const val CategoryDeals = "categoryDeals"
    const val Blog = "blog"
    const val Notifications = "notifications"
    const val Profile = "profile"
    const val About = "about"
}

private data class BottomItem(val route: String, val label: String, val icon: @Composable (Int) -> Unit)

@Composable
fun EnjoyFreeDealsNavGraph(state: AppUiState, viewModel: AppViewModel, snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()
    val context = LocalContext.current
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(state.isLoggedIn) { loggedIn ->
                navController.navigate(if (loggedIn) Routes.Main else Routes.Login) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
        }
        composable(Routes.Login) {
            LoginScreen(
                state = state,
                onLogin = { email, password -> viewModel.login(email, password) { navController.navigateToMain() } },
                onGoogle = viewModel::googleLoginUnavailable,
                onGuest = { viewModel.continueAsGuest { navController.navigateToMain() } },
                onForgotPassword = viewModel::forgotPassword,
                onCreateAccount = { navController.navigate(Routes.Register) }
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                state = state,
                onRegister = { name, email, password, confirm -> viewModel.register(name, email, password, confirm) { navController.navigateToMain() } },
                onLogin = { navController.popBackStack() }
            )
        }
        composable(Routes.Main) {
            MainScaffold(
                state = state,
                viewModel = viewModel,
                onOpenDeal = { deal -> openDeal(context, deal, viewModel::show) },
                onShareDeal = { deal -> shareDeal(context, deal) },
                onOpenVideo = { video -> openUrl(context = context, url = video.videoUrl, showMessage = viewModel::show) },
                rootNav = navController
            )
        }
        composable(Routes.About) { AboutScreen() }
    }
}

@Composable
private fun MainScaffold(
    state: AppUiState,
    viewModel: AppViewModel,
    onOpenDeal: (Deal) -> Unit,
    onShareDeal: (Deal) -> Unit,
    onOpenVideo: (VideoFind) -> Unit,
    rootNav: NavHostController
) {
    val context = LocalContext.current
    val bottomNav = rememberNavController()
    val items = listOf(
        BottomItem(Routes.Deals, "All Deals") { _ -> Icon(Icons.Filled.LocalOffer, null) },
        BottomItem(Routes.Category, "Category") { _ -> Icon(Icons.Filled.Category, null) },
        BottomItem(Routes.Blog, "Blog") { _ -> Icon(Icons.AutoMirrored.Filled.Article, null) },
        BottomItem(Routes.Notifications, "Notifications") { unread ->
            BadgedBox(badge = { if (unread > 0) Badge { Text(unread.toString()) } }) { Icon(Icons.Filled.Notifications, null) }
        },
        BottomItem(Routes.Profile, "Profile") { _ -> Icon(Icons.Filled.AccountCircle, null) }
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val current by bottomNav.currentBackStackEntryAsState()
                items.forEach { item ->
                    NavigationBarItem(
                        selected = current?.destination?.route == item.route,
                        onClick = {
                            bottomNav.navigate(item.route) {
                                popUpTo(bottomNav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { item.icon(state.unreadCount) },
                        label = { Text(item.label, maxLines = 1) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(bottomNav, startDestination = Routes.Home, modifier = Modifier.padding(padding)) {
            composable(Routes.Home) {
                HomeScreen(
                    state = state,
                    deals = viewModel.filteredDeals(null),
                    onRefresh = viewModel::refresh,
                    onQuery = viewModel::setSearch,
                    onStore = viewModel::setStore,
                    onOpen = onOpenDeal,
                    onSave = viewModel::saveDeal,
                    onShare = onShareDeal
                )
            }
            composable(Routes.Deals) {
                AllDealsScreen(state, viewModel.filteredDeals(null), viewModel::setSearch, viewModel::setStore, viewModel::setSort, onOpenDeal, viewModel::saveDeal, onShareDeal)
            }
            composable(Routes.Category) {
                CategoryScreen(state.categories) { category ->
                    viewModel.selectCategory(category.id)
                    bottomNav.navigate(Routes.CategoryDeals)
                }
            }
            composable(Routes.CategoryDeals) {
                val categoryName = state.categories.firstOrNull { it.id == state.selectedCategory }?.name ?: "Category Deals"
                CategoryDealsScreen(categoryName, state, viewModel.filteredDeals(state.selectedCategory), onOpenDeal, viewModel::saveDeal, onShareDeal)
            }
            composable(Routes.Blog) { BlogScreen(state.videos, onOpenVideo) }
            composable(Routes.Notifications) {
                NotificationsScreen(state.notifications, state.unreadCount) { notification ->
                    viewModel.markNotificationRead(notification)
                    val deal = state.deals.firstOrNull { it.id == notification.dealId }
                    if (notification.targetUrl.isNotBlank()) {
                        openUrl(context, notification.targetUrl, viewModel::show)
                    } else if (deal != null) {
                        onOpenDeal(deal)
                    } else {
                        viewModel.show("Deal link is not available right now.")
                    }
                }
            }
            composable(Routes.Profile) {
                ProfileScreen(
                    state = state,
                    onAbout = { rootNav.navigate(Routes.About) },
                    onPrivacy = { rootNav.navigate(Routes.About) },
                    onSupport = { viewModel.show("Contact support at support@enjoyfreedeals.in") },
                    onLogout = { viewModel.logout { rootNav.navigate(Routes.Login) { popUpTo(Routes.Main) { inclusive = true } } } }
                )
            }
        }
    }
}

@Composable
fun EnjoyScaffold(snackbarHostState: SnackbarHostState, content: @Composable () -> Unit) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { content() }
}

private fun NavHostController.navigateToMain() {
    navigate(Routes.Main) {
        popUpTo(Routes.Login) { inclusive = true }
    }
}

private fun openUrl(context: android.content.Context, url: String, showMessage: (String) -> Unit) {
    if (url.isBlank()) {
        showMessage("Deal link is not available right now.")
        return
    }
    runCatching { CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url)) }
        .onFailure { showMessage("Deal link is not available right now.") }
}
