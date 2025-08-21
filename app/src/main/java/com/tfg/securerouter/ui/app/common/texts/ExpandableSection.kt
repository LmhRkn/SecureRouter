package com.tfg.securerouter.ui.app.common.texts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R

@Composable
fun ExpandableSection(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null
) {
    val internal = rememberSaveable { mutableStateOf(initiallyExpanded) }
    val isControlled = (expanded != null && onExpandedChange != null)

    val isExpanded = if (isControlled) expanded!! else internal.value
    val setExpanded: (Boolean) -> Unit =
        if (isControlled) onExpandedChange!!
        else { { internal.value = it } }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = title, style = titleStyle, color = titleColor)
            IconButton(onClick = { setExpanded(!isExpanded) }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.Remove else Icons.Default.Add,
                    contentDescription = if (isExpanded)
                        stringResource(id = R.string.expandable_section_collapse)
                    else
                        stringResource(id = R.string.expandable_section_expand),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            content()
        }
    }
}
