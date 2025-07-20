package com.tfg.securerouter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tfg.securerouter.ui.app.screens.main_screen.MainScreen
import com.tfg.securerouter.ui.theme.SecureRouterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureRouterTheme {
                MainScreen()
            }
        }
    }
}
