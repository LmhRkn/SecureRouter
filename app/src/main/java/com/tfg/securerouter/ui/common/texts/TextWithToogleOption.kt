package com.tfg.securerouter.ui.common.texts

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tfg.securerouter.ui.common.buttons.ToggleButton

/**
 * A reusable composable function for creating a text with a toggle button.
 *
 * Usage:
 * It displays a text with a toggle button for enabling or disabling a feature.
 *
 * @property text a [String] representing the feature that will be toggle.
 * @property initialChecked a [Boolean] representing the initial state of the toggle button.
 * @property modifier a [Modifier] for customizing the layout of the composable.
 * @property onToggleChanged a lambda function that is triggered when the user toggles the button.
 *
 * @see [ToggleButton] for the toggle button.
 */

@Composable
fun TextWithToggleOption(
    text: String,
    initialChecked: Boolean = false,
    modifier: Modifier = Modifier,
    onToggleChanged: (Boolean) -> Unit = {},
) {

    Row(modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
        )

        ToggleButton(
            checked = initialChecked,
            onCheckedChange = {
                onToggleChanged(it)
            }
        )
    }
}
