package com.tfg.securerouter.ui.router.windows.home.components.home_top_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R

@Composable
fun MenuButton() {
    IconButton(onClick = { }) {
        Icon(
            painterResource(id = R.drawable.ic_menu),
            contentDescription = stringResource(id = R.string.home_menu_icon_description),
            tint = Color.Unspecified
        )
    }
}