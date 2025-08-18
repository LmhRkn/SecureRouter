package com.tfg.securerouter.ui.app.common.tables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun <T, C> GenericRuleTable(
    container: C,
    mac: String = " ",
    rulesOf: (C) -> List<T>,
    encode: (T) -> String,
    decode: (String) -> T,
    macOf: (T) -> String = {" "},
    identityOf: (T) -> Any,
    labelOf: (T) -> String,
    nextIndexFrom: (C, List<T>) -> Int,
    removeAndShift: (List<T>, T) -> List<T>,
    onRemoveRemote: (T) -> Unit = {},
    AddOrEdit: @Composable (
        oldRule: T?,
        mac: String,
        nextIndex: Int,
        currentRules: List<T>,
        onSave: (T) -> Unit,
        onCancel: () -> Unit,
        onBumpToEnd: (String) -> Int,
        onRemoveRule: (T) -> Unit
    ) -> Unit
) {
    fun normalizeMac(s: String): String {
        val noSep = s.lowercase().replace(Regex("[^0-9a-f]"), "")
        return if (noSep.isEmpty()) "" else noSep.chunked(2).joinToString(":")
    }

    val macKey = normalizeMac(mac)

    val listSaver = Saver<List<T>, List<String>>(
        save = { it.map(encode) },
        restore = { it.map(decode) }
    )

    var rules by rememberSaveable(macKey, stateSaver = listSaver) {
        mutableStateOf(emptyList<T>())
    }
    var isAdding by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var ruleToEdit by remember { mutableStateOf<T?>(null) }

    val incomingAll = rulesOf(container)

    LaunchedEffect(macKey, incomingAll) {
        val incoming = incomingAll.filter { rule ->
            macKey.isBlank() || normalizeMac(macOf(rule)) == macKey
        }

        if (rules.isEmpty()) {
            rules = incoming
        } else if (incoming.isNotEmpty()) {
            val byIdIncoming = incoming.associateBy(identityOf)
            val byIdLocal    = rules.associateBy(identityOf)

            val updated = rules.map { local -> byIdIncoming[identityOf(local)] ?: local }
            val added = incoming.filter { identityOf(it) !in byIdLocal.keys }

            rules = updated + added
        }
    }

    val nextIndex = nextIndexFrom(container, rules)

    val removeThenCreateByTitle: (String) -> Int = { title ->
        val pos = rules.indexOfFirst { labelOf(it).trim() == title.trim() }
        if (pos != -1) {
            val victim = rules[pos]
            onRemoveRemote(victim)
            rules = removeAndShift(rules, victim)
        }
        nextIndexFrom(container, rules)
    }

    val onRemoveRule: (T) -> Unit = { ruleToRemove ->
        onRemoveRemote(ruleToRemove)
        rules = removeAndShift(rules, ruleToRemove)
    }

    when {
        isAdding -> {
            AddOrEdit(
                /* oldRule      */ null,
                /* mac          */ mac,
                /* nextIndex    */ nextIndex,
                /* currentRules */ rules,
                /* onSave       */ { rule ->
                    rules = rules + rule
                    isAdding = false
                },
                /* onCancel     */ { isAdding = false },
                /* onBumpToEnd  */ removeThenCreateByTitle,
                /* onRemoveRule */ { /* alta: no aplica */ }
            )
        }
        isEditing -> {
            AddOrEdit(
                /* oldRule      */ ruleToEdit,
                /* mac          */ mac,
                /* nextIndex    */ nextIndex,
                /* currentRules */ rules,
                /* onSave       */ { rule ->
                    rules = rules + rule
                    isEditing = false
                    ruleToEdit = null
                },
                /* onCancel     */ {
                    isEditing = false
                    ruleToEdit = null
                },
                /* onBumpToEnd  */ removeThenCreateByTitle,
                /* onRemoveRule */ { ruleToRemove ->
                    onRemoveRule(ruleToRemove)
                    isEditing = false
                    ruleToEdit = null
                }

            )
        }
        else -> {
            RuleTable(
                rules = rules,
                labelOf = labelOf,
                onAddRule = {
                    isAdding = true
                    isEditing = false
                    ruleToEdit = null
                },
                onRemoveRule = { ruleToRemove ->
                    onRemoveRemote(ruleToRemove)
                    rules = removeAndShift(rules, ruleToRemove)
                },
                onCardClick = { clicked ->
                    isAdding = false
                    isEditing = true
                    ruleToEdit = clicked
                }
            )
        }
    }
}

fun <T> calcNextIndexPreferLocalGeneric(
    baseFromRouter: Int,
    localRules: List<T>,
    indexOf: (T) -> Int,
    index2Of: (T) -> Int? = { null }
): Int {
    val localMax = localRules.maxOfOrNull { maxOf(indexOf(it), index2Of(it) ?: -1) } ?: -1
    return if (localMax >= 0) localMax + 1 else baseFromRouter
}

fun <T> removeRuleAndShiftGeneric(
    rules: List<T>,
    toRemove: T,
    indexOf: (T) -> Int,
    index2Of: (T) -> Int? = { null },
    recalc: (T, newIndex: Int, newIndex2: Int?) -> T
): List<T> {
    val removedPrimary = indexOf(toRemove)
    val removedSecondary = index2Of(toRemove)
    val removedSlots = if (removedSecondary != null) 2 else 1
    val upperBound = removedSecondary ?: removedPrimary

    return rules
        .filterNot { indexOf(it) == removedPrimary && index2Of(it) == removedSecondary }
        .map { r ->
            val i1 = indexOf(r)
            val i2 = index2Of(r)
            val newIndex  = if (i1 > upperBound) i1 - removedSlots else i1
            val newIndex2 = i2?.let { if (it > upperBound) it - removedSlots else it }
            recalc(r, newIndex, newIndex2)
        }
        .sortedWith(compareBy<T> { indexOf(it) }.thenBy { index2Of(it) ?: Int.MAX_VALUE })
}