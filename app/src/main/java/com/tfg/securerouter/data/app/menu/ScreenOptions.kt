package com.tfg.securerouter.data.app.menu

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController

/**
 * Interface for defining navigable screens within the app.
 *
 * Each implementation of this interface represents a screen that can be accessed via
 * the navigation system. It specifies:
 * - A unique route identifier for navigation.
 * - The [Composable] function to render its UI content.
 *
 * This abstraction allows the app to manage and render different screens in a
 * consistent and centralized way.
 *
 * @property route A [String] defining the unique navigation route for the screen.
 * @function Content The [Composable] function that renders the UI for the screen.
 *
 * @see NavController
 *
 */

interface ScreenOptions {
    val route: String
    val arguments: List<NamedNavArgument> get() = emptyList()

    @Composable fun Content()
}


