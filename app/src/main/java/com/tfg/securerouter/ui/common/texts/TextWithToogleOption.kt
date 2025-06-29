package com.tfg.securerouter.ui.common.texts

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
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
 * @property textColour a [Color] for customizing the text color.
 * @property textStyle a [TextStyle] for customizing the appearance of the text.
 * @property buttonActiveColor a [Color] for customizing the color of the toggle button when it is checked.
 * @property buttonInactiveColor a [Color] for customizing the color of the toggle button when it is not checked.
 * @property buttonThumbColor a [Color] for customizing the color of the thumb of the toggle button.
 * @property buttonSize a [Int] for customizing the size of the toggle button.
 *
 * @see [ToggleButton] for the toggle button.
 */

@Composable
fun TextWithToggleOption(
    text: String,
    initialChecked: Boolean = false,
    modifier: Modifier = Modifier,
    onToggleChanged: (Boolean) -> Unit = {},
    textColour: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    buttonActiveColor: Color? = null,
    buttonInactiveColor: Color? = null,
    buttonThumbColor: Color? = null,
    buttonSize: Int? = null
) {

    Row(modifier = modifier) {
        Text(
            text = text,
            style = textStyle,
            color = textColour,
            modifier = Modifier
                .weight(1f)
        )

        ToggleButton(
            checked = initialChecked,
            onCheckedChange = {
                onToggleChanged(it)
            },
            activeColor = buttonActiveColor ?: MaterialTheme.colorScheme.primary,
            inactiveColor = buttonInactiveColor ?: MaterialTheme.colorScheme.secondary,
            thumbColor = buttonThumbColor ?: MaterialTheme.colorScheme.onPrimary,
            buttonSize = buttonSize?.dp ?: (textStyle.fontSize.value.dp * 1.25f)
        )
    }
}
