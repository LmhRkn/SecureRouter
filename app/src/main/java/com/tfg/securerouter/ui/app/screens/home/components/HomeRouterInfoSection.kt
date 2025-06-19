package com.tfg.securerouter.ui.app.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.menu.screens.home.state.HomeRouterInfoState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange

@Composable
fun HomeRouterInfoSection(
    state: HomeRouterInfoState,
    onEditAliasClick: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var aliasValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = state.routerAlias ?: state.macAddress,
                selection = TextRange(0, (state.routerAlias ?: state.macAddress).length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }

    // Solicita foco y selecciona todo al entrar en edición
    LaunchedEffect(isEditing) {
        if (isEditing) {
            aliasValue = aliasValue.copy(selection = TextRange(0, aliasValue.text.length))
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            EditAliasTextField(
                aliasValue = aliasValue,
                onValueChange = { aliasValue = it },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f)
            )
        } else {
            DisplayAlias(
                aliasValue = aliasValue,
                modifier = Modifier.weight(1f)
            )
        }

        EditAliasButton(
            isEditing = isEditing,
            aliasValue = aliasValue,
            onToggleEdit = { isEditing = !isEditing },
            onEditAliasClick = { newAlias -> onEditAliasClick(newAlias) }
        )

    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        state.macAddress,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun EditAliasTextField(
    aliasValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = aliasValue,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier.focusRequester(focusRequester)
    )
}

@Composable
private fun DisplayAlias(
    aliasValue: TextFieldValue,
    modifier: Modifier = Modifier
) {
    Text(
        text = aliasValue.text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@Composable
private fun EditAliasButton(
    isEditing: Boolean,
    aliasValue: TextFieldValue,
    onToggleEdit: () -> Unit, // 🔁 nuevo parámetro para cambiar el estado
    onEditAliasClick: (String) -> Unit
) {
    IconButton(onClick = {
        if (isEditing) {
            onEditAliasClick(aliasValue.text)
        }
        onToggleEdit()
    }) {
        Icon(
            Icons.Default.Edit,
            contentDescription = stringResource(id = R.string.home_router_info_section_description),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
