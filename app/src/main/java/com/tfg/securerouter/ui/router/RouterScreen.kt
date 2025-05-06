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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import com.tfg.securerouter.ui.theme.MainBackgroundColorLight

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

    val filteredItems = items.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val (mainRouter, vpnRouters) = filteredItems.partition { !it.isVpn }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Campo de búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar router...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Router principal
            mainRouter.firstOrNull()?.let {
                item {
                    RouterCard(router = it, isMain = true)
                }
            }

            // Routers VPN
            items(vpnRouters.size) { index ->
                RouterCard(router = vpnRouters[index], isMain = false)
            }

            // Mensaje si no hay resultados
            if (filteredItems.isEmpty()) {
                item {
                    Text("No se encontraron routers con ese nombre.")
                }
            }
        }

        // FAB de volver arriba
        if (showScrollToTop) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Volver arriba",
                    tint = MainBackgroundColorLight
                )
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 400)
@Composable
fun PreviewRouterScreen() {
    val fakeData = listOf(
        RouterUIModel(1, "Piso Madrid", isConnected = true, isVpn = false, error = false),
        RouterUIModel(2, "Casa Pueblo", isConnected = false, isVpn = true, error = false),
        RouterUIModel(3, "Casa Julia", isConnected = false, isVpn = true, error = true)
    )

    MyApplicationTheme {
        RouterScreen(
            items = fakeData,
            onSave = {}
        )
    }
}
