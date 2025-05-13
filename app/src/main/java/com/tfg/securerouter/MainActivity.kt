package com.tfg.securerouter

import HomeScreen
import PantallaPrincipal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.components.DrawerContent
import com.tfg.securerouter.ui.theme.SecureRouterTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecureRouterTheme {
                PantallaPrincipal()
            }
        }
    }
}

@Composable
fun TFGSecureRouterApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box() {
                DrawerContent(onItemClick = {
                    scope.launch { drawerState.close() }
                })
            }
        },
        gesturesEnabled = true // asegúrate de tenerlo si quieres abrir deslizando
    ) {
        HomeScreen()
    }
}

@Composable
@Preview
fun TFGSecureRouterAppPreview() {
    SecureRouterTheme {
        TFGSecureRouterApp()
    }
}
