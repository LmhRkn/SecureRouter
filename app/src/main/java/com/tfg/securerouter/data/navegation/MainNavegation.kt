package com.tfg.securerouter.data.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tfg.securerouter.data.app.menu.MenuRegistry
import com.tfg.securerouter.data.app.menu.OtherScreenRegistry

/**
 * Class responsible for managing the app's main navigation graph.
 *
 * This class defines the navigation flow using Jetpack Navigation Compose.
 * It dynamically builds the navigation graph using the registered menu options
 * from [MenuRegistry], allowing for a scalable and modular navigation structure.
 *
 * Usage:
 * - Called from the main screen to render the navigation system.
 * - Each registered [com.tfg.securerouter.data.app.menu.MenuOption] defines its own route and screen content.
 */
class MainNavegation {

    /**
     * Composable function that sets up the NavHost for the main app navigation.
     *
     * @param navController The [NavHostController] that handles navigation actions and back stack.
     *
     * It uses the [MenuRegistry] to register all available destinations dynamically.
     * The start destination is set to "home".
     *
     * @see MenuRegistry
     * @see androidx.navigation.compose.NavHost
     */
    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "home") {
            MenuRegistry.items.forEach { menuOption ->
                composable(menuOption.route) {
                    menuOption.Content()
                }
            }
            OtherScreenRegistry.items.forEach { menuOption ->
                composable(menuOption.route) {
                    menuOption.Content()
                }
            }
        }
    }
}
