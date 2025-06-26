package com.tfg.securerouter.ui.common.texts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.common.buttons.EditButton

/**
 * A reusable composable function for creating an editable text field.
 *
 * Usage:
 * It displays a text field with an edit button, allowing users to edit the text.
 *
 * @property text a [String] representing the initial text in the text field.
 * @property modifier a [Modifier] for customizing the layout of the composable.
 * @property onTextSaved a lambda function that is triggered when the user saves the edited text.
 *
 * @see [EditButton] for the edit button.
 */

@Composable
fun EditableTextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextSaved: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var originalText by remember { mutableStateOf(text) }
    var textValue by remember {
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
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )
            } else {
                Text(
                    text = textValue.text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
            }

            if (!isEditing) {
                EditButton(onClick = {
                    isEditing = true
                    originalText = textValue.text
                })
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