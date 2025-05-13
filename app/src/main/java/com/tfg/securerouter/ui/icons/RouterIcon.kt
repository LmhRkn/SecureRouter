package com.tfg.securerouter.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R

@Composable
fun RouterIcon(size: Int = 128) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Icon(
            painterResource(id = R.drawable.ic_router),
            contentDescription = "Router icon",
            modifier = Modifier.size(size.dp)
        )
    }
}