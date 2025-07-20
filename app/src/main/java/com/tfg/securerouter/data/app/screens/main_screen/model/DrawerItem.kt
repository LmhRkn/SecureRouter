package com.tfg.securerouter.data.app.screens.main_screen.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing an item in the navigation drawer.
 *
 * Usage:
 * This model is used to build the list of items shown in the app's navigation drawer.
 * It allows for easy routing, consistent labeling, and icon-based representation.
 *
 * @property route A [String] representing the navigation route for the item.
 * @property labelResId A [String] resource ID (Int) used for the item's label, supporting localization.
 * @property icon An [ImageVector] representing the item's icon.
 */
data class DrawerItemModel(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)
