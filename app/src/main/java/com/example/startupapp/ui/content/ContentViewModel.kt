package com.example.startupapp.ui.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.startupapp.StartupApp
import com.example.startupapp.data.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ContentUiState(
    val registeredEmail: String? = null
) {
    val isAuthorized: Boolean
        get() = registeredEmail != null
}

class ContentViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val uiState: StateFlow<ContentUiState> = sessionRepository.registeredUserEmail
        .map { email -> ContentUiState(registeredEmail = email) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContentUiState(registeredEmail = sessionRepository.registeredUserEmail.value)
        )

    fun onLogout() {
        sessionRepository.clearSession()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as StartupApp
                ContentViewModel(app.sessionRepository)
            }
        }
    }
}
