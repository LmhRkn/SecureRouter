package com.tfg.securerouter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tfg.securerouter.ui.app.screens.main_screen.MainScreen
import com.tfg.securerouter.ui.theme.SecureRouterTheme

/**
 * Main entry point for the SecureRouter application.
 *
 * This [ComponentActivity] sets up:
 * - Edge-to-edge system UI layout via [enableEdgeToEdge].
 * - The Compose UI hierarchy with [SecureRouterTheme].
 * - The root screen of the app: [MainScreen].
 *
 * @see SecureRouterTheme For applying the app's Material 3 design system.
 * @see MainScreen For the primary UI container with navigation and drawer.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * Initializes the UI with Compose, applying the theme and starting the navigation graph.
     *
     * @param savedInstanceState The previously saved state, if any.
     */
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
