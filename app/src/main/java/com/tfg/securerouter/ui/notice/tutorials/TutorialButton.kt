package com.tfg.securerouter.ui.notice.tutorials

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TutorialButton() {
    FloatingActionButton(
        onClick = { TutorialCenter.open() },
        shape = CircleShape,
        modifier = Modifier.size(46.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Help,
            contentDescription = "Mostrar tutorial",
        )
    }
}
