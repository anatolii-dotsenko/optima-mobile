package com.example.startupapp

import android.app.Application
import com.example.startupapp.data.SessionRepository

/**
 * Application container providing app-wide singleton dependencies.
 */
class StartupApp : Application() {
    val sessionRepository: SessionRepository by lazy { SessionRepository() }
}
