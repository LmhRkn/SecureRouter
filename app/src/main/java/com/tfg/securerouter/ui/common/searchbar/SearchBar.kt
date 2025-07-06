package com.tfg.securerouter.ui.common.searchbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Buscar...",
    onSearchChanged: (String) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Search,
    contentDescriptionLeadingIcon: String = "Buscar",
    trailingIcon: ImageVector = Icons.Default.Clear,
    contentDescriptionTrailingIcon: String = "Limpiar",
    trailingClearEnabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    searchIconColor: Color = MaterialTheme.colorScheme.primary,
    trailingClearIconColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    cursorColor: Color = MaterialTheme.colorScheme.primary
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onSearchChanged(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = {
            Text(
                text = hint,
                color = textColor,
                style = textStyle
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = contentDescriptionLeadingIcon, tint = searchIconColor)
        },
        trailingIcon = {
            if (trailingClearEnabled && query.isNotEmpty()) {
                IconButton(onClick = {
                    query = ""
                    onSearchChanged("")
                }) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = contentDescriptionTrailingIcon,
                        tint = trailingClearIconColor
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            focusedIndicatorColor = focusedIndicatorColor,
            cursorColor = cursorColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchChanged(query)
            }
        )
    )
}
