package com.tfg.securerouter.ui.app.common.texts

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EditableTextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextSaved: (String) -> Unit,
    label: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    placeholder: String? = null,
    middleButton: (@Composable () -> Unit)? = null,
    onEditButtonPress: (() -> Unit)? = null
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editingValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text))
    }
    var snapshotAtEditStart by rememberSaveable { mutableStateOf(text) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(text, isEditing) {
        if (!isEditing) {
            editingValue = TextFieldValue(text, selection = TextRange(text.length))
        }
    }
    LaunchedEffect(isEditing) {
        if (isEditing) {
            snapshotAtEditStart = text
            editingValue = editingValue.copy(selection = TextRange(0, editingValue.text.length))
            focusRequester.requestFocus()
        }
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp)) {

            Crossfade(targetState = isEditing, label = "edit-crossfade") { editing ->
                if (editing) {
                    TextField(
                        value = editingValue,
                        onValueChange = { editingValue = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        label = { if (label != null) Text(label) },
                        placeholder = { if (placeholder != null) Text(placeholder) },
                        trailingIcon = {
                            IconButton(onClick = { editingValue = TextFieldValue("") }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Limpiar")
                            }
                        }
                    )
                } else {
                    TextField(
                        value = editingValue,
                        onValueChange = {},
                        singleLine = true,
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        label = { if (label != null) Text(label) },
                        placeholder = { if (placeholder != null) Text(placeholder) },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (middleButton != null) {
                                    middleButton()
                                }
                                FilledTonalIconButton(
                                    onClick = { onEditButtonPress?.invoke() ?: run { isEditing = true } }
                                ) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                }
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            editingValue = TextFieldValue(
                                snapshotAtEditStart,
                                selection = TextRange(snapshotAtEditStart.length)
                            )
                            isEditing = false
                        }
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onTextSaved(editingValue.text)
                            isEditing = false
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Guardar")
                    }
                }
            }
        }
    }
}
