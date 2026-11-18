package com.example.startupapp

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CheckResult {
    REGISTERED,
    NOT_FOUND
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val result: CheckResult? = null
) {
    val isEmailValid: Boolean
        get() = email.isNotBlank() && emailError == null

    val canCheckRegistration: Boolean
        get() = isEmailValid && password.isNotBlank()
}

class LoginViewModel : ViewModel() {

    private val registeredUsers = mapOf(
        "user@mail.com" to "1234",
        "student@optima.ua" to "qwerty",
        "admin@test.com" to "admin123"
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        val trimmed = value.trim()
        val isValid = trimmed.isNotEmpty() && isEmailAddressValid(trimmed)
        _uiState.update { state ->
            state.copy(
                email = value,
                emailError = if (trimmed.isEmpty() || isValid) null else "Некоректний формат електронної пошти",
                result = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                result = null
            )
        }
    }

    fun onCheckRegistration() {
        val state = _uiState.value
        if (!state.canCheckRegistration) return

        val normalizedEmail = state.email.trim().lowercase()
        val isRegistered = registeredUsers[normalizedEmail] == state.password

        _uiState.update {
            it.copy(
                result = if (isRegistered) CheckResult.REGISTERED else CheckResult.NOT_FOUND
            )
        }
    }

    private fun isEmailAddressValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS?.matcher(email)?.matches() ?: EMAIL_REGEX.matches(email)
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
