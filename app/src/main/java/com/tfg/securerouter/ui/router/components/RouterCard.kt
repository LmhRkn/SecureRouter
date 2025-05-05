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

@Composable
fun RouterCard(router: RouterUIModel, isMain: Boolean) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(if (isMain) 200.dp else 120.dp),
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
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = if (router.isConnected) "Red conectada" else "Desconectado",
                    color = if (router.isConnected) Color.Green else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (router.isConnected) Color(0xFFA5D6A7) else Color(0xFFB0BEC5),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Button(
                onClick = { /* TODO: lógica de acceso */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acceder")
            }
        }
    }
}
