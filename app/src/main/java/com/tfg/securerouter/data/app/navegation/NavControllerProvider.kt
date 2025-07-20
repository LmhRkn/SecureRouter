package com.tfg.securerouter.data.app.navegation

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

/**
 * A [CompositionLocal] that provides access to the current [NavHostController].
 *
 * Usage:
 * This allows Composables deeper in the hierarchy to access the app's navigation controller
 * without requiring it to be passed explicitly through every Composable.
 *
 * If accessed without a provider, this CompositionLocal throws an error:
 * `"No NavController provided"`.
 *
 * Example: Providing a NavController
 * ```
 * CompositionLocalProvider(LocalNavController provides navController) {
 *     AppContent()
 * }
 * ```
 *
 * Example: Using the NavController
 * ```
 * val navController = LocalNavController.current
 * navController.navigate("settings")
 * ```
 *
 * @see NavHostController for navigation actions.
 */

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}
