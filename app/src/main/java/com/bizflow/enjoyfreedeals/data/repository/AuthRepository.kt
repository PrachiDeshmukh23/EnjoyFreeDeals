package com.bizflow.enjoyfreedeals.data.repository

import com.bizflow.enjoyfreedeals.data.mock.MockData
import com.bizflow.enjoyfreedeals.data.model.AppUser
import com.bizflow.enjoyfreedeals.data.model.AuthResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth? = firebaseAuthOrNull(),
    private val firestore: FirebaseFirestore? = firestoreOrNull()
) {
    private var guestUser: AppUser? = null

    val isLoggedIn: Boolean get() = auth?.currentUser != null || guestUser != null
    val isFirebaseConfigured: Boolean get() = auth != null && firestore != null

    suspend fun login(email: String, password: String): AuthResult = runCatching {
        val auth = auth ?: return AuthResult(false, FIREBASE_NOT_CONFIGURED)
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        AuthResult(true)
    }.getOrElse {
        AuthResult(false, it.message?.takeIf { msg -> msg.isNotBlank() } ?: "We couldn't sign you in. Please check your email and password.")
    }

    suspend fun register(name: String, email: String, password: String): AuthResult = runCatching {
        val auth = auth ?: return AuthResult(false, FIREBASE_NOT_CONFIGURED)
        val firestore = firestore ?: return AuthResult(false, FIREBASE_NOT_CONFIGURED)
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = result.user?.uid ?: return AuthResult(false, "Your session expired. Please try again.")
        firestore.collection("users").document(uid).set(AppUser(uid, name.trim(), email.trim())).await()
        AuthResult(true)
    }.getOrElse {
        AuthResult(false, it.message?.takeIf { msg -> msg.isNotBlank() } ?: "We couldn't create your account right now. Please try again.")
    }

    fun continueAsGuest(): AuthResult {
        guestUser = MockData.user.copy(
            uid = "guest-user",
            name = "Guest Deal Hunter",
            email = "guest@enjoyfreedeals.local"
        )
        return AuthResult(true)
    }

    suspend fun loadUser(): AppUser {
        guestUser?.let { return it }
        val uid = auth?.currentUser?.uid ?: return MockData.user
        val email = auth.currentUser?.email.orEmpty()
        val firestore = firestore ?: return MockData.user.copy(uid = uid, email = email)
        return runCatching {
            firestore.collection("users").document(uid).get().await().toObject(AppUser::class.java)
                ?: MockData.user.copy(uid = uid, email = email)
        }.getOrDefault(MockData.user.copy(uid = uid, email = email))
    }

    suspend fun saveDeal(dealId: String, save: Boolean, current: AppUser): AppUser {
        val updatedIds = if (save) current.savedDeals + dealId else current.savedDeals - dealId
        val updated = current.copy(savedDeals = updatedIds.distinct())
        if (guestUser != null) {
            guestUser = updated
            return updated
        }
        auth?.currentUser?.uid?.let { uid ->
            runCatching { firestore?.collection("users")?.document(uid)?.update("savedDeals", updated.savedDeals)?.await() }
        }
        return updated
    }

    fun signOut() {
        guestUser = null
        auth?.signOut()
    }

    companion object {
        const val FIREBASE_NOT_CONFIGURED = "Firebase is not configured. Please add google-services.json."

        private fun firebaseAuthOrNull(): FirebaseAuth? = runCatching {
            FirebaseApp.getInstance()
            FirebaseAuth.getInstance()
        }.getOrNull()

        private fun firestoreOrNull(): FirebaseFirestore? = runCatching {
            FirebaseApp.getInstance()
            FirebaseFirestore.getInstance()
        }.getOrNull()
    }
}
