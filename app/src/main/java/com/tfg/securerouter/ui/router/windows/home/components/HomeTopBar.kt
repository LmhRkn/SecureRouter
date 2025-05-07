package com.tfg.securerouter.ui.router.windows.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.router.windows.home.components.home_top_bar.RouterConnectedIcon
import com.tfg.securerouter.ui.router.windows.home.components.home_top_bar.VpnBadgeIcon
import com.tfg.securerouter.ui.router.windows.home.components.home_top_bar.MenuButton
import com.tfg.securerouter.ui.router.windows.home.state.HomeUiState
import com.tfg.securerouter.ui.theme.LocalExtraColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(state: HomeUiState) {
    val colors = LocalExtraColors.current

    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.home_title)) },
        navigationIcon = {
            MenuButton()
        },
        actions = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                RouterConnectedIcon(state)
                VpnBadgeIcon(state)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
