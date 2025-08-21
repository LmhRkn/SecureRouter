package com.tfg.securerouter.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.ui.notice.alerts.AlertModal

@Composable
fun PromptHost() {
    var current by remember { mutableStateOf<AlertSpec?>(null) }
    var resolver by remember { mutableStateOf<((Boolean) -> Unit)?>(null) }

    LaunchedEffect(Unit) {
        com.tfg.securerouter.data.utils.PromptBus.flow.collect { req ->
            current = req.spec
            resolver = req.reply
        }
    }

    current?.let { spec ->
        AlertModal(
            spec = spec,
            onConfirm = { resolver?.invoke(true); current = null; resolver = null },
            onCancel  = { resolver?.invoke(false); current = null; resolver = null }
        )
    }
}