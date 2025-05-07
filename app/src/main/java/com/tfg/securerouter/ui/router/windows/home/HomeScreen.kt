package com.tfg.securerouter.ui.router.windows.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tfg.securerouter.ui.router.windows.home.components.HomeTopBar
import com.tfg.securerouter.ui.router.windows.home.state.HomeUiState
import com.tfg.securerouter.ui.router.windows.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.router.windows.home.components.ConnectedDevicesList
import com.tfg.securerouter.ui.router.windows.home.components.RouterIcon

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    HomeContent(state, onEditAliasClick = { /* TODO */ })
}

@Composable
private fun HomeContent(state: HomeUiState, onEditAliasClick: () -> Unit) {
    Scaffold(
        topBar = { HomeTopBar(state) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            HomeRouterInfoSection(state, onEditAliasClick)
            Spacer(modifier = Modifier.height(16.dp))
            RouterIcon()
            Spacer(modifier = Modifier.height(8.dp))
            ConnectedDevicesList(state.connectedDevices)
        }
    }
}
