import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tfg.securerouter.ui.components.DrawerContent
import com.tfg.securerouter.ui.components.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tfg.securerouter.R
import com.tfg.securerouter.data.model.TopBarViewModel
import com.tfg.securerouter.data.state.TopBarModel
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
            popUpTo(0)
        }
        scope.launch { drawerState.close() }
        visible = false
    }

    val onMenuClick: () -> Unit = {
        scope.launch { drawerState.open() }
        visible = true
    }

    val topBarHeightPx = remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val topBarHeightDp = with(density) { topBarHeightPx.value.toDp() }
    val topBarViewModel: TopBarViewModel = viewModel()

    val topBarState by topBarViewModel.topBarState

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val titleHome = stringResource(R.string.home_title)
    val titleAdmin = stringResource(R.string.administrar_title)
    val titleWifi = stringResource(R.string.wifi_title)
    val titleFiltros = stringResource(R.string.filtros_title)
    val titleConfig = stringResource(R.string.configuracion_title)

    LaunchedEffect(currentBackStackEntry?.destination?.route) {
        when (currentBackStackEntry?.destination?.route) {
            "home" -> topBarViewModel.updateTitle(titleHome)
            "administrar" -> topBarViewModel.updateTitle(titleAdmin)
            "wifi" -> topBarViewModel.updateTitle(titleWifi)
            "filtros" -> topBarViewModel.updateTitle(titleFiltros)
            "configuracion" -> topBarViewModel.updateTitle(titleConfig)
        }
    }

    MainNavegation(
        scope = scope,
        drawerState = drawerState,
        topBarState = topBarState,
        onMenuClick = onMenuClick,
        onDrawerItemClick = onDrawerItemClick,
        visible = visible,
        topBarHeightPx = topBarHeightPx,
        topBarHeightDp = topBarHeightDp,
        navController = navController
    )
}

@Composable
fun MainNavegation(
    scope: CoroutineScope,
    drawerState: DrawerState,
    topBarState: TopBarModel,
    onMenuClick: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    visible: Boolean,
    topBarHeightPx: MutableState<Int>,
    topBarHeightDp: Dp,
    navController: NavHostController
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                visible = visible,
                onItemClick = onDrawerItemClick,
                topPadding = topBarHeightDp
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    modifier = Modifier.onGloballyPositioned {
                        topBarHeightPx.value = it.size.height
                    },
                    scope = scope,
                    topBarModel = topBarState,
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
