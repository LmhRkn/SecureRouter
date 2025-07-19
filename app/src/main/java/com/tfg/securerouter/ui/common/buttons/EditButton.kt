package com.tfg.securerouter.ui.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size

/**
 * Composable function for rendering a reusable Edit button with a pencil icon.
 *
 * Usage:
 * This button is used throughout the UI wherever an "edit" action is required,
 * such as modifying router information or user settings.
 * The button uses a Material Design icon and supports customization of color and size.
 *
 * @param onClick Lambda to be invoked when the button is pressed.
 * @param color The color applied to the icon. Defaults to [onBackground].
 * @param iconSize The size of the icon in dp. Defaults to 24.dp.
 */
@Composable
fun EditButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground,
    iconSize: Dp = 24.dp
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.home_router_info_section_description),
            tint = color,
            modifier = Modifier.size(iconSize)
        )
    }
}
