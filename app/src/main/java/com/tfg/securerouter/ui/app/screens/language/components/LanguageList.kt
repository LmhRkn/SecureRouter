package com.tfg.securerouter.ui.app.screens.language.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.language.model.LanguageScreenEvent
import com.tfg.securerouter.data.app.screens.language.registry.AvailableLanguages.availableLanguages
import com.tfg.securerouter.data.utils.height_weight_to_dp
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LanguageList(
    parent: ScreenDefault
) {
    BoxWithConstraints {
        val heightDp = height_weight_to_dp(maxHeight = maxHeight, weight = 0.7f)
        var firstIteration: Boolean = true

        val selectedStates = remember {
            mutableStateListOf<Boolean>().apply {
                repeat(availableLanguages.size) { add(false) }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp)
        ) {

            itemsIndexed(availableLanguages) { index, language ->
                val text = stringResource(language.displayName)
                val abbreviation = stringResource(language.abbreviation)

                if (text == stringResource(R.string.language_selected_language) && firstIteration) {
                    selectedStates[index] = true
                    firstIteration = false
                }

                LanguageCard(
                    language = text,
                    isSelected = selectedStates[index],
                    onClick = {
                        val previousIndex = getIndexPreviouslySelected(selectedStates)
                        if (previousIndex != -1) selectedStates[previousIndex] = false
                        selectedStates[index] = !selectedStates[index]

                        CoroutineScope(Dispatchers.Main).launch {
                            parent.trySendEvent(LanguageScreenEvent.LanguageSelected(abbreviation))
                        }
                    }
                )
            }
        }
    }
}

fun getIndexPreviouslySelected(languagesStatus: List<Boolean>): Int {
    for (i in languagesStatus.indices) {
        if (languagesStatus[i]) {
            return i
        }
    }

    return -1
}
