package com.tfg.securerouter.ui.app.common.tables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R


@Composable
fun RuleTableCard(
    text: String,
    onButtonClicked: () -> Unit,
    onCardClick: () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonIcon: ImageVector = Icons.Filled.Close,
    buttonTint: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    borderPercentage: Int = 25,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(2.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(percent = borderPercentage)
            )
            .clickable { onCardClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
        )

        IconButton(
            onClick = onButtonClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = stringResource(R.string.clear_button),
                tint = buttonTint
            )
        }
    }
}


