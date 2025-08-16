package com.tfg.securerouter.ui.app.common.tables

fun <T> saveRuleGeneric(
    previewLabel: String,
    crossesMidnight: Boolean,
    oldRule: T?,
    currentRules: List<T>,
    nextIndex: Int,
    onBumpToEnd: (String) -> Int,
    onRemoveRule: (T) -> Unit,
    indexOf: (T) -> Int,
    index2Of: (T) -> Int?,
    labelOf: (T) -> String,
    buildRule: (index: Int, index2: Int?) -> T,
    onAddRemote: (T) -> Unit,
    onSaveLocal: (T) -> Unit
) {
    val exists = currentRules.any { labelOf(it).trim() == previewLabel.trim() }
    if (exists) {
        val newIdx = onBumpToEnd(previewLabel)

        if (oldRule != null && labelOf(oldRule).trim() != previewLabel.trim()) {
            onRemoveRule(oldRule)
        }

        val newRule = buildRule(newIdx, if (crossesMidnight) newIdx + 1 else null)
        onSaveLocal(newRule)
        onAddRemote(newRule)
        return
    }

    val removedSlots = when {
        oldRule?.let { index2Of(it) } != null -> 2
        oldRule != null                       -> 1
        else                                  -> 0
    }
    if (oldRule != null) onRemoveRule(oldRule)

    val newIdx = nextIndex - removedSlots
    val newRule = buildRule(newIdx, if (crossesMidnight) newIdx + 1 else null)
    onSaveLocal(newRule)
    onAddRemote(newRule)
}