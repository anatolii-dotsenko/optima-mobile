package com.example.startupapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.startupapp.navigation.AppShell
import com.example.startupapp.ui.theme.StartupAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StartupAppTheme {
                AppShell()
            }
        }
    }
}
