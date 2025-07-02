package com.tfg.securerouter.ui.app.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tfg.securerouter.data.app.menu.MenuRegistry
import com.tfg.securerouter.data.main_screen.model.TopBarViewModel
import com.tfg.securerouter.data.main_screen.state.TopBarModel
import com.tfg.securerouter.data.navegation.MainNavegation
import com.tfg.securerouter.ui.app.screens.DrawerContent
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val topBarViewModel: TopBarViewModel = viewModel()
    val topBarState by topBarViewModel.topBarState

    var visible by remember { mutableStateOf(false) }
    val topBarHeightPx = remember { mutableStateOf(0) }
    val topBarHeightDp = with(LocalDensity.current) { topBarHeightPx.value.toDp() }

    UpdateTopBarTitle(navController, topBarViewModel)

    val onDrawerItemClick: (String) -> Unit = {
        navController.navigate(it) {
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

    NavigationDrawerContent(
        drawerState = drawerState,
        topBarState = topBarState,
        onMenuClick = onMenuClick,
        onDrawerItemClick = onDrawerItemClick,
        visible = visible,
        topBarHeightPx = topBarHeightPx,
        navController = navController
    )
}

@Composable
private fun UpdateTopBarTitle(navController: NavHostController, viewModel: TopBarViewModel) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val route = currentBackStackEntry?.destination?.route
    val matchingOption = MenuRegistry.items.find { it.route == route }

    matchingOption?.let {
        val title = stringResource(id = it.titleResId)
        LaunchedEffect(title) {
            viewModel.updateTitle(title)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    drawerState: DrawerState,
    topBarState: TopBarModel,
    onMenuClick: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    visible: Boolean,
    topBarHeightPx: MutableState<Int>,
    navController: NavHostController,
) {
    val mainNavegation = remember { MainNavegation() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                DrawerHeader()
                DrawerContent(
                    visible = visible,
                    onItemClick = onDrawerItemClick
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.onGloballyPositioned {
                        topBarHeightPx.value = it.size.height
                    },
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 56.dp)
                        ) {
                            Text(
                                text = topBarState.title,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                mainNavegation.NavGraph(navController)
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp, start = 16.dp), // más espacio arriba
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "SecureRouter",
            style = MaterialTheme.typography.headlineSmall.copy( // tamaño visible y adecuado
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}


