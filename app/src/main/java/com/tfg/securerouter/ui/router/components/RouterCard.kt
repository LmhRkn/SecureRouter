package com.tfg.securerouter.ui.router

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.router.model.RouterUIModel
import com.tfg.securerouter.ui.theme.*

@Composable
fun RouterCard(router: RouterUIModel, isMain: Boolean) {
    val statusText: String
    val statusColor: Color
    val backgroundColor: Color
    val extraColors = LocalExtraColors.current

    when {
        router.error -> {
            statusText = "Error de conexión"
            statusColor = extraColors.onStatusErrorColor
            backgroundColor = extraColors.statusErrorColor
        }
        router.isConnected -> {
            statusText = "Red conectada"
            statusColor = extraColors.onStatusConnectedColor
            backgroundColor = extraColors.statusConnectedColor
        }
        else -> {
            statusText = "Desconectado"
            statusColor = extraColors.onStatusDisconnectedColor
            backgroundColor = extraColors.statusDisconnectedColor
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(if (isMain) 200.dp else 120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
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

                Text(
                    text = statusText,
                    color = statusColor,
                    modifier = Modifier
                        .background(backgroundColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Button(
                onClick = { /* TODO: lógica de acceso */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Acceder")
            }
        }
    }
}
