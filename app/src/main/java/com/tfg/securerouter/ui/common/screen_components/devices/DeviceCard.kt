package com.tfg.securerouter.ui.common.screen_components.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.data.common.screen_components.DeviceModel

/**
 * Composable function for displaying a card representing a network device.
 *
 * Usage:
 * This component presents device information (icon, hostname, IP address, and MAC address)
 * inside a clickable [Card]. It is typically used within device lists such as [DeviceList].
 * When the card is clicked, the provided [onClick] callback is triggered.
 *
 * @param device The [DeviceModel] containing data to be displayed.
 * @param onClick Lambda to be invoked when the card is clicked.
 *
 * @see DeviceList
 */
@Composable
fun DeviceCard(device: DeviceModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = device.icon,
                contentDescription = stringResource(id = device.iconDescription),
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.hostname ?: device.ip,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = device.ip,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = device.mac,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
