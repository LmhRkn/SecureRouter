package com.tfg.securerouter.ui.app.common.texts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.app.common.buttons.EditButton

/**
 * A reusable composable function for creating an editable text field.
 *
 * Usage:
 * It displays a text field with an edit button, allowing users to edit the text.
 *
 * @property text a [String] representing the initial text in the text field.
 * @property modifier a [Modifier] for customizing the layout of the composable.
 * @property onTextSaved a lambda function that is triggered when the user saves the edited text.
 * @property textStyle a [TextStyle] for customizing the appearance of the text field.
 * @property textColor a [Color] for customizing the text color.
 * @property buttonSize a [Int] for customizing the size of the edit button.
 * @property buttonColor a [Color] for customizing the color of the edit button.
 *
 * @see [EditButton] for the edit button.
 */

@Composable
fun EditableTextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextSaved: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    buttonSize: Int? = null,
    buttonColor: Color? = null
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var originalText by rememberSaveable { mutableStateOf(text) }
    var textValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(0, text.length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            textValue = textValue.copy(selection = TextRange(0, textValue.text.length))
            focusRequester.requestFocus()
        }
    }

    Column(modifier = modifier) {
        Row {
            if (isEditing) {
                BasicTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    singleLine = true,
                    textStyle = textStyle.copy(
                        color = textColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )
            } else {
                Text(
                    text = textValue.text,
                    style = textStyle,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
            }

            if (!isEditing) {
                EditButton(
                    onClick = {
                        isEditing = true
                        originalText = textValue.text
                    },
                    color = buttonColor ?: textColor,
                    iconSize = buttonSize?.dp ?: (textStyle.fontSize.value.dp * 0.75f)

                )
            }
        }

        if (isEditing) {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = {
                    textValue = TextFieldValue(originalText)
                    isEditing = false
                }) {
                    Text(stringResource(R.string.cancel_button))
                }
                TextButton(onClick = {
                    onTextSaved(textValue.text)
                    isEditing = false
                }) {
                    Text(stringResource(R.string.save_button))
                }
            }
        }
    }
}