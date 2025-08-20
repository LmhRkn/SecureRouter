package com.tfg.securerouter.ui.notice

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.notice.model.NoticeSpec

@Composable
fun NoticeHost(
    notices: List<NoticeSpec>,
    onDismiss: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        notices.forEachIndexed { idx, spec ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                NoticeCard(spec = spec, onDismiss = { onDismiss(idx) })
            }
        }
    }
}
