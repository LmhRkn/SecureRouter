package com.tfg.securerouter.ui.common.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.ui.app.common.screen_components.devices.DeviceList

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
 * @param iconTint [Color] to be applied to the device icon.
 * @param iconSize [Dp] of the device icon.
 * @param textNameStyle [TextStyle] to be applied to the device hostname.
 * @param textNameColor [Color] to be applied to the device hostname.
 * @param textIpStyle [TextStyle] to be applied to the device IP address.
 * @param textIpColor [Color] to be applied to the device IP address.
 * @param textMacStyle [TextStyle] to be applied to the device MAC address.
 * @param textMacColor [Color] to be applied to the device MAC address.
 *
 * @see DeviceModel
 * @see DeviceList
 */

@Composable
fun DeviceCard(
    device: DeviceModel,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onBackground,
    iconSize: Dp = 40.dp,
    textNameStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textNameColor: Color = MaterialTheme.colorScheme.onBackground,
    textIpStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textIpColor: Color = MaterialTheme.colorScheme.onBackground,
    textMacStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textMacColor: Color = MaterialTheme.colorScheme.onBackground
) {
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
                imageVector = device.icon ?: Icons.Filled.DevicesOther,
                contentDescription = device.iconDescription?.let { stringResource(it) } ?: "",
                modifier = Modifier.size(iconSize),
                tint = iconTint
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.hostname ?: device.ip,
                    style = textNameStyle,
                    color = textNameColor
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = device.ip,
                        style = textIpStyle,
                        color = textIpColor
                    )
                    Text(
                        text = device.mac,
                        style = textMacStyle,
                        modifier = Modifier.padding(end = 16.dp),
                        color = textMacColor
                    )
                }
            }
        }
    }
}



