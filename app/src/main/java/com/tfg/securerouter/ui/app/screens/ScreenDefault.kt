package com.tfg.securerouter.ui.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault

open class ScreenDefault {
    private val components = mutableStateListOf<@Composable () -> Unit>()

    fun addComponents(vararg newComponents: @Composable () -> Unit) {
        newComponents.forEach { component ->
            components.add(component)
        }
    }

    @Composable
    fun ScreenInit(coordinator: ScreenCoordinatorDefault) {
        val isReady by coordinator.isReady.collectAsState()

        if (!isReady) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ScreenContent(coordinator)
        }
    }

    @Composable
    open fun ScreenContent(coordinator: ScreenCoordinatorDefault ) {}

    @Composable
    fun RenderScreen() {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            components.forEachIndexed { index, component ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }

                // Simplemente llama al composable
                component()
            }
        }
    }

}
