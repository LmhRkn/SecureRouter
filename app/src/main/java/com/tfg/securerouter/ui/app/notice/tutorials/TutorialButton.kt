package com.tfg.securerouter.ui.app.notice.tutorials

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R

@Composable
fun TutorialButton() {
    FloatingActionButton(
        onClick = { TutorialCenter.open() },
        shape = CircleShape,
        modifier = Modifier.size(46.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Help,
            contentDescription = stringResource(R.string.show_tutorial),
        )
    }
}
