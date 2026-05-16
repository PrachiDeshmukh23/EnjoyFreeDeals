package com.bizflow.enjoyfreedeals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.AppUser
import com.bizflow.enjoyfreedeals.data.model.Category
import com.bizflow.enjoyfreedeals.data.model.Deal
import com.bizflow.enjoyfreedeals.data.model.DealNotification
import com.bizflow.enjoyfreedeals.data.model.DealSort
import com.bizflow.enjoyfreedeals.data.model.Validation
import com.bizflow.enjoyfreedeals.data.model.VideoFind
import com.bizflow.enjoyfreedeals.data.repository.AuthRepository
import com.bizflow.enjoyfreedeals.data.repository.DealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isAuthLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val message: String? = null,
    val user: AppUser = MockData.user,
    val deals: List<Deal> = emptyList(),
    val categories: List<Category> = emptyList(),
    val videos: List<VideoFind> = emptyList(),
    val notifications: List<DealNotification> = emptyList(),
    val query: String = "",
    val selectedStore: String? = null,
    val selectedCategory: String? = null,
    val sort: DealSort = DealSort.Newest,
    val darkMode: Boolean = false
) {
    val unreadCount: Int get() = notifications.count { !it.isRead }
    val savedDeals: List<Deal> get() = deals.filter { user.savedDeals.contains(it.id) }
}

class AppViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val dealRepository: DealRepository = DealRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(isLoggedIn = authRepository.isLoggedIn))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.deals.isEmpty(), isRefreshing = it.deals.isNotEmpty(), message = null) }
            val user = authRepository.loadUser()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    isLoggedIn = authRepository.isLoggedIn,
                    user = user,
                    deals = dealRepository.deals(),
                    categories = dealRepository.categories(),
                    videos = dealRepository.videos(),
                    notifications = dealRepository.notifications()
                )
            }
        }
    }

    fun filteredDeals(categoryId: String? = _uiState.value.selectedCategory): List<Deal> {
        val state = _uiState.value
        return dealRepository.filterDeals(state.deals, state.query, state.selectedStore, categoryId, state.sort)
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        Validation.login(email, password)?.let { show(it); return }
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, message = null) }
            val result = authRepository.login(email, password)
            _uiState.update { it.copy(isAuthLoading = false, message = result.message.takeIf(String::isNotBlank)) }
            if (result.success) {
                refresh()
                onSuccess()
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String, onSuccess: () -> Unit) {
        Validation.registration(name, email, password, confirmPassword)?.let { show(it); return }
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, message = null) }
            val result = authRepository.register(name, email, password)
            _uiState.update { it.copy(isAuthLoading = false, message = result.message.takeIf(String::isNotBlank)) }
            if (result.success) {
                refresh()
                onSuccess()
            }
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        val result = authRepository.continueAsGuest()
        if (result.success) {
            refresh()
            onSuccess()
        } else {
            show(result.message)
        }
    }

    fun googleLoginUnavailable() = show(AuthRepository.FIREBASE_NOT_CONFIGURED)
    fun forgotPassword() = show("Password reset will be available after Firebase Authentication is configured.")
    fun setSearch(query: String) = _uiState.update { it.copy(query = query) }
    fun setStore(store: String?) = _uiState.update { it.copy(selectedStore = store) }
    fun setSort(sort: DealSort) = _uiState.update { it.copy(sort = sort) }
    fun selectCategory(categoryId: String?) = _uiState.update { it.copy(selectedCategory = categoryId) }
    fun toggleDarkMode() = _uiState.update { it.copy(darkMode = !it.darkMode) }
    fun show(message: String) = _uiState.update { it.copy(message = message) }
    fun clearMessage() = _uiState.update { it.copy(message = null) }

    fun saveDeal(dealId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            val alreadySaved = state.user.savedDeals.contains(dealId)
            _uiState.update {
                it.copy(
                    user = authRepository.saveDeal(dealId, !alreadySaved, state.user),
                    message = if (alreadySaved) "Deal removed from saved deals." else "Deal saved."
                )
            }
        }
    }

    fun markNotificationRead(notification: DealNotification) {
        viewModelScope.launch {
            dealRepository.markNotificationRead(notification.id)
            _uiState.update {
                it.copy(notifications = it.notifications.map { item ->
                    if (item.id == notification.id) item.copy(isRead = true) else item
                })
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        authRepository.signOut()
        _uiState.update { it.copy(isLoggedIn = false, user = MockData.user.copy(savedDeals = emptyList())) }
        onDone()
    }
}
