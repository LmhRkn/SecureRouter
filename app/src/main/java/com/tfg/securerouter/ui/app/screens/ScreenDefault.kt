package com.tfg.securerouter.ui.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

open class ScreenDefault {
    private val components = mutableStateListOf<@Composable () -> Unit>()

    fun addComponents(vararg newComponents: @Composable () -> Unit) {
        newComponents.forEach { component ->
            components.add(component)
        }
    }

    @Composable
    fun RenderScreen() {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            components.forEachIndexed { index, component ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
                component()
            }
        }
    }
}
