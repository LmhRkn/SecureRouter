package com.tfg.securerouter.ui.router

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.router.router_list.model.RouterUIModel
import com.tfg.securerouter.ui.theme.*
import com.tfg.securerouter.ui.router.router_list.components.router_card.AccessButton
import com.tfg.securerouter.ui.router.router_list.components.router_card.StatusText

@Composable
fun RouterCard(
    router: RouterUIModel,
    isMain: Boolean,
    onAccessClick: () -> Unit
) {
    val (statusText, statusColor, backgroundColor) = getRouterStatusData(router)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(if (isMain) 200.dp else 120.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = router.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                StatusText(statusText, statusColor, backgroundColor)
            }

            AccessButton(onClick = onAccessClick)
        }
    }
}

@Composable
fun getRouterStatusData(router: RouterUIModel): Triple<String, Color, Color> {
    return when {
        router.error -> Triple(
            stringResource(id = R.string.status_error),
            LocalExtraColors.current.onStatusErrorColor,
            LocalExtraColors.current.statusErrorColor
        )
        router.isConnected -> Triple(
            stringResource(id = R.string.status_connected),
            LocalExtraColors.current.onStatusConnectedColor,
            LocalExtraColors.current.statusConnectedColor
        )
        else -> Triple(
            stringResource(id = R.string.status_disconnected),
            LocalExtraColors.current.onStatusDisconnectedColor,
            LocalExtraColors.current.statusDisconnectedColor
        )
    }
}