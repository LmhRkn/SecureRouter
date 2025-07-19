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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R


/**
 * Composable function for a customizable search bar with leading and trailing icons.
 *
 * Usage:
 * Displays an input field with optional leading and trailing icons for search and clear actions.
 * It supports real-time updates of the query string and provides extensive customization options
 * for styling and behavior.
 *
 * @param modifier Modifier applied to the search bar container. Defaults to [Modifier].
 * @param hint Placeholder text displayed when the input field is empty. Defaults to a localized string.
 * @param onSearchChanged Lambda called whenever the query text changes or when search is triggered.
 * @param leadingIcon Icon displayed at the start of the text field. Defaults to [Icons.Default.Search].
 * @param contentDescriptionLeadingIcon Content description for accessibility for the leading icon.
 * @param trailingIcon Icon displayed at the end of the text field for clearing the query. Defaults to [Icons.Default.Clear].
 * @param contentDescriptionTrailingIcon Content description for accessibility for the trailing icon.
 * @param trailingClearEnabled If true, shows the trailing clear button when the query is not empty. Defaults to `true`.
 * @param textColor Color of the query text and placeholder. Defaults to [MaterialTheme.colorScheme.primary].
 * @param textStyle Style applied to the query text. Defaults to [MaterialTheme.typography.bodyMedium].
 * @param searchIconColor Color applied to the leading search icon. Defaults to [MaterialTheme.colorScheme.primary].
 * @param trailingClearIconColor Color applied to the trailing clear icon. Defaults to [MaterialTheme.colorScheme.primary].
 * @param unfocusedIndicatorColor Color of the underline when the field is not focused. Defaults to [MaterialTheme.colorScheme.primary].
 * @param focusedIndicatorColor Color of the underline when the field is focused. Defaults to [MaterialTheme.colorScheme.primary].
 * @param cursorColor Color of the text cursor. Defaults to [MaterialTheme.colorScheme.primary].
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = stringResource(id = R.string.search_bar_hint),
    onSearchChanged: (String) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Search,
    contentDescriptionLeadingIcon: String = stringResource(id = R.string.search_bar_search_icon_description),
    trailingIcon: ImageVector = Icons.Default.Clear,
    contentDescriptionTrailingIcon: String = stringResource(id = R.string.search_bar_trailing_icon_description),
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
            Icon(
                imageVector = leadingIcon,
                contentDescription = contentDescriptionLeadingIcon,
                tint = searchIconColor
            )
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