package com.tfg.securerouter.ui.app.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.menu.screens.home.state.HomeRouterInfoState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import com.tfg.securerouter.ui.common.buttons.EditButton
import com.tfg.securerouter.ui.common.texts.EditableTextField

@Composable
fun HomeRouterInfoSection(
    state: HomeRouterInfoState,
    onEditAliasClick: (String) -> Unit
) {
    EditableTextField(
        text = state.routerAlias ?: state.macAddress,
        onTextSaved = { newAlias -> onEditAliasClick(newAlias) },
        modifier = Modifier.fillMaxWidth()
    )

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
    EditButton(onClick = {
        if (isEditing) {
            onEditAliasClick(aliasValue.text)
        }
        onToggleEdit()
    })
}
