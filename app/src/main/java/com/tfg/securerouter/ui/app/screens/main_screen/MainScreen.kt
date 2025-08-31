package com.tfg.securerouter.ui.app.screens.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.data.app.menu.MenuRegistryBottom
import com.tfg.securerouter.data.app.menu.MenuRegistryTop
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.navegation.MainNavigation
import com.tfg.securerouter.data.app.screens.main_screen.model.TopBarViewModel
import com.tfg.securerouter.data.app.screens.main_screen.state.TopBarModel
import com.tfg.securerouter.ui.app.screens.DrawerContent
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialButton
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.app.notice.tutorials.UiReadyCenter
import kotlinx.coroutines.launch

/**
 * Root composable for the SecureRouter app.
 *
 * This screen sets up:
 * - The main navigation controller.
 * - A [ModalNavigationDrawer] with a dynamic top bar.
 * - A [Scaffold] to display content with consistent UI chrome.
 *
 * It manages state for the drawer and top bar title, and initializes navigation using
 * [MainNavigation].
 *
 * @see TopBarViewModel
 * @see MenuRegistry
 * @see MainNavigation
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val topBarViewModel: TopBarViewModel = viewModel()
    val topBarState by topBarViewModel.topBarState

    var visible by rememberSaveable { mutableStateOf(false) }
    val topBarHeightPx = rememberSaveable { mutableStateOf(0) }

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

/**
 * Observes navigation changes and updates the top bar title accordingly.
 *
 * @param navController The [NavHostController] managing app navigation.
 * @param viewModel The [TopBarViewModel] responsible for top bar state.
 */
@Composable
private fun UpdateTopBarTitle(navController: NavHostController, viewModel: TopBarViewModel) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val route = currentBackStackEntry?.destination?.route
    val menuOptions: List<MenuOption> = MenuRegistryTop.items + MenuRegistryBottom.items
    val matchingOption = menuOptions.find { it.route == route }

    matchingOption?.let {
        val title = stringResource(id = it.titleResId)
        LaunchedEffect(title) {
            viewModel.updateTitle(title)
        }
    }
}

/**
 * Builds the layout with a [ModalNavigationDrawer], a dynamic top bar, and the main navigation graph.
 *
 * @param drawerState The [DrawerState] controlling open/close behavior.
 * @param topBarState The [TopBarModel] providing the current title.
 * @param onMenuClick Action to perform when the menu icon is tapped.
 * @param onDrawerItemClick Action to perform when a drawer item is selected.
 * @param visible Whether the drawer content should be visible.
 * @param topBarHeightPx Mutable state holding the top bar’s pixel height.
 * @param navController The [NavHostController] for app navigation.
 */
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
    val mainNavegation = remember { MainNavigation() }

    val uiReady by UiReadyCenter.ready.collectAsState()
    val hasTutorial by TutorialCenter.spec.collectAsState()
    val isOpen by TutorialCenter.open.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
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
                        IconButton(
                            onClick = onMenuClick,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary
                            )
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                if (uiReady && hasTutorial != null && !isOpen) {
                    TutorialButton()
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                CompositionLocalProvider(LocalNavController provides navController) {
                    mainNavegation.NavGraph(navController)
                }
            }
        }
    }
}

/**
 * Header section displayed at the top of the navigation drawer.
 */
@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp, start = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "SecureRouter",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}


