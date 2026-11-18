package com.example.startupapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton repository storing shared session state across screens.
 */
class SessionRepository {

    private val _registeredUserEmail = MutableStateFlow<String?>(null)
    val registeredUserEmail: StateFlow<String?> = _registeredUserEmail.asStateFlow()

    fun setRegisteredUser(email: String) {
        _registeredUserEmail.value = email
    }

    fun clearSession() {
        _registeredUserEmail.value = null
    }
}
