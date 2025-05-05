/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tfg.securerouter.ui.router

import com.tfg.securerouter.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tfg.securerouter.ui.router.model.RouterUIModel

@Composable
fun RouterScreen(modifier: Modifier = Modifier, viewModel: RouterViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is RouterUiState.Loading -> CircularProgressIndicator()
        is RouterUiState.Error -> Text("Error cargando routers")
        is RouterUiState.Success -> {
            val routers = (uiState as RouterUiState.Success).data
            RouterScreen(
                items = routers,
                onSave = viewModel::addRouter,
                modifier = modifier
            )
        }
    }
}

@Composable
internal fun RouterScreen(
    items: List<RouterUIModel>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nameRouter by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    // Filtrar por búsqueda
    val filteredItems = items.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val (mainRouter, vpnRouters) = filteredItems.partition { !it.isVpn }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Campo de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar router...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Router principal (si existe)
        mainRouter.firstOrNull()?.let {
            RouterCard(router = it, isMain = true)
        }

        // Routers VPN
        vpnRouters.forEach {
            RouterCard(router = it, isMain = false)
        }

        // Si no hay coincidencias
        if (filteredItems.isEmpty()) {
            Text("No se encontraron routers con ese nombre.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true, widthDp = 400)
@Composable
fun PreviewRouterScreen() {
    val fakeData = listOf(
        RouterUIModel(1, "Piso Madrid", isConnected = true, isVpn = false),
        RouterUIModel(2, "Casa Pueblo", isConnected = false, isVpn = true),
        RouterUIModel(3, "Casa Julia", isConnected = false, isVpn = true)
    )

    MyApplicationTheme {
        RouterScreen(
            items = fakeData,
            onSave = {}
        )
    }
}
