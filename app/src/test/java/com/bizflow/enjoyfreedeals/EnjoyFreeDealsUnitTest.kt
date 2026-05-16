package com.bizflow.enjoyfreedeals

import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.DealSort
import com.bizflow.enjoyfreedeals.data.model.Validation
import com.bizflow.enjoyfreedeals.data.repository.AuthRepository
import com.bizflow.enjoyfreedeals.data.repository.DealRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class EnjoyFreeDealsUnitTest {
    private val repository = DealRepository(null)

    @Test
    fun loginValidation_handlesEmptyAndInvalidInputs() {
        assertThat(Validation.login("", "")).contains("email")
        assertThat(Validation.login("wrong", "secret1")).contains("valid email")
        assertThat(Validation.login("user@test.com", "secret1")).isNull()
    }

    @Test
    fun loginShowsSetupMessageWhenFirebaseIsMissing() = runBlocking {
        val authRepository = AuthRepository(null, null)

        val result = authRepository.login("abc.123@gmail.com", "secret1")

        assertThat(result.success).isFalse()
        assertThat(authRepository.isLoggedIn).isFalse()
        assertThat(result.message).isEqualTo(AuthRepository.FIREBASE_NOT_CONFIGURED)
    }

    @Test
    fun guestModeWorksWithoutFirebase() = runBlocking {
        val authRepository = AuthRepository(null, null)

        val result = authRepository.continueAsGuest()
        val user = authRepository.loadUser()

        assertThat(result.success).isTrue()
        assertThat(authRepository.isLoggedIn).isTrue()
        assertThat(user.name).isEqualTo("Guest Deal Hunter")
    }

    @Test
    fun registrationValidation_handlesMobilePasswordAndValidRegistration() {
        assertThat(Validation.registration("", "", "", "")).contains("all fields")
        assertThat(Validation.registration("Om", "om@test.com", "secret1", "secret2")).contains("match")
        assertThat(Validation.registration("Om", "om@test.com", "secret1", "secret1")).isNull()
    }

    @Test
    fun dealsLoadAndSearchWorksWithMockData() {
        val amazon = repository.filterDeals(MockData.deals, "Amazon", null, null, DealSort.Newest)
        val free = repository.filterDeals(MockData.deals, "free skincare", null, null, DealSort.Newest)
        val none = repository.filterDeals(MockData.deals, "not-a-real-store", null, null, DealSort.Newest)

        assertThat(MockData.deals).hasSize(12)
        assertThat(amazon.first().storeName).isEqualTo("Amazon")
        assertThat(free.any { it.isFreeDeal }).isTrue()
        assertThat(none).isEmpty()
    }

    @Test
    fun categoryClickFiltersDealsAndEmptyCategoryIsEmpty() {
        val electronics = repository.filterDeals(MockData.deals, "", null, "electronics", DealSort.Newest)
        val empty = repository.filterDeals(MockData.deals, "", null, "missing", DealSort.Newest)

        assertThat(MockData.categories.map { it.name }).contains("Electronics")
        assertThat(electronics).isNotEmpty()
        assertThat(empty).isEmpty()
    }

    @Test
    fun notificationsUnreadBadgeUpdatesWhenMarkedReadLocally() {
        val read = MockData.notifications.map {
            if (it.id == "n1") it.copy(isRead = true) else it
        }
        assertThat(MockData.notifications.count { !it.isRead }).isEqualTo(3)
        assertThat(read.count { !it.isRead }).isEqualTo(2)
    }

    @Test
    fun profileAndAboutMockDataAreAvailable() {
        assertThat(MockData.user.email).isNotEmpty()
        assertThat(MockData.appInfo.appName).isEqualTo("EnjoyFreeDeals")
    }
}
