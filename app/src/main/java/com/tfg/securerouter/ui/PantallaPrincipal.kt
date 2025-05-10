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

@Composable
fun PantallaPrincipal() {
    var visible by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerOnItemClick: (String) -> Unit = {
        visible = false
        scope.launch { drawerState.close() }
    }
    val topBarOnClick: () -> Unit = {
        visible = true
        scope.launch { drawerState.open() }
    }


    // Efecto para detectar cuando se cierra el drawer
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed && visible) {
            visible = false
        }
    }

    MenuDesplegable(
        drawerState = drawerState,
        visible = visible,
        drawerOnClick = drawerOnItemClick,
        topBarOnClick = topBarOnClick,
        scope = scope
    )
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

@Composable
fun Drawer(visible: Boolean = false , onItemClick: (String) -> Unit) {
    val menuItems = listOf("Prueba 1", "Prueba 2", "Prueba 3", "Prueba 4")

    if (visible) Surface(
        color = MaterialTheme.colorScheme.surface, // Fondo opaco
        tonalElevation = 2.dp, // Puedes ajustar si quieres sombra
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.85f) // ancho típico del drawer
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            menuItems.forEach { item ->
                TextButton(
                    onClick = { onItemClick(item) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
