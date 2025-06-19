package com.tfg.securerouter.ui.app.main_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tfg.securerouter.data.app.menu.MenuRegistry
import com.tfg.securerouter.data.main_screen.model.TopBarViewModel
import com.tfg.securerouter.data.main_screen.state.TopBarModel
import com.tfg.securerouter.data.navegation.MainNavegation
import com.tfg.securerouter.ui.app.screens.DrawerContent
import com.tfg.securerouter.ui.app.main_screen.top_bar.TopBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var visible by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val topBarViewModel: TopBarViewModel = viewModel()
    val topBarState by topBarViewModel.topBarState

    val topBarHeightPx = remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val topBarHeightDp = with(density) { topBarHeightPx.value.toDp() }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val route = currentBackStackEntry?.destination?.route
    val matchingOption = MenuRegistry.items.find { it.route == route }

    matchingOption?.let {
        val title = stringResource(id = it.titleResId)
        LaunchedEffect(title) {
            topBarViewModel.updateTitle(title)
        }
    }

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

    Navegation(
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
fun Navegation(
    drawerState: DrawerState,
    topBarState: TopBarModel,
    onMenuClick: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    visible: Boolean,
    topBarHeightPx: MutableState<Int>,
    topBarHeightDp: Dp,
    navController: NavHostController
) {
    val mainNavegation = remember { MainNavegation() }

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
                    topBarModel = topBarState,
                    onMenuClick = onMenuClick
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                mainNavegation.NavGraph(navController)
            }
        }
    }
}
