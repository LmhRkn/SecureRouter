package com.tfg.securerouter.data.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.components.DrawerItem

val items = listOf(
    DrawerItem("home", R.string.home_title, Icons.Default.Home),
    DrawerItem("administrar", R.string.administrar_title, Icons.Default.Warning),
    DrawerItem("wifi", R.string.wifi_title, Icons.Default.Warning),
    DrawerItem("filtros", R.string.filtros_title, Icons.Default.Warning),
    DrawerItem("configuracion", R.string.configuracion_title, Icons.Default.Settings)
)