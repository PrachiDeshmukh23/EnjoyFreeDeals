package com.bizflow.enjoyfreedeals.data.model

object Validation {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun login(email: String, password: String): String? = when {
        email.isBlank() || password.isBlank() -> "Please enter both email and password."
        !emailRegex.matches(email.trim()) -> "Please enter a valid email address."
        password.length < 6 -> "Password must be at least 6 characters."
        else -> null
    }

    fun registration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? = when {
        name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
            "Please fill in all fields."
        !emailRegex.matches(email.trim()) -> "Please enter a valid email address."
        password.length < 6 -> "Password must be at least 6 characters."
        password != confirmPassword -> "Passwords do not match."
        else -> null
    }
}
