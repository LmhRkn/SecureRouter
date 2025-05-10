import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.components.DrawerContent
import com.tfg.securerouter.ui.components.TopBar
import com.tfg.securerouter.ui.screens.HomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tfg.securerouter.ui.screens.AdministrarDispositivosScreen
import com.tfg.securerouter.ui.screens.ConfigurationScreen
import com.tfg.securerouter.ui.screens.FiltrosScreen
import com.tfg.securerouter.ui.screens.WifiScreen

@Composable
fun PantallaPrincipal() {
    val navController = rememberNavController()
    var visible by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val onDrawerItemClick: (String) -> Unit = { screen ->
        navController.navigate(screen) {
            launchSingleTop = true
            popUpTo(0) // evita duplicados en el stack
        }
        scope.launch { drawerState.close() }
        visible = false
    }

    val onMenuClick: () -> Unit = {
        scope.launch { drawerState.open() }
        visible = true
    }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) visible = false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(visible = visible, onItemClick = onDrawerItemClick)
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    scope = scope,
                    title = "SecureRouter",
                    router_connected = true,
                    vpn_connected = false,
                    drawerState = drawerState,
                    onMenuClick = onMenuClick
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen() }
                    composable("administrar") { AdministrarDispositivosScreen() }
                    composable("wifi") { WifiScreen() }
                    composable("filtros") { FiltrosScreen() }
                    composable("configuracion") { ConfigurationScreen() }
                }
            }
        }
    }
}


@Composable
fun MenuDesplegable(
    drawerState: DrawerState,
    visible: Boolean,
    drawerOnClick: (String) -> Unit,
    topBarOnClick: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(visible = visible, onItemClick = drawerOnClick)
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopBar(
                    scope = scope,
                    title = "Pantalla Principal",
                    router_connected = true,
                    vpn_connected = false,
                    drawerState = drawerState,
                    onMenuClick = topBarOnClick
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeScreen()
            }
        }
    }
}
