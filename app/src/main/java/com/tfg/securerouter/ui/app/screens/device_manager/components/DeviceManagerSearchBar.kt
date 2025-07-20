package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.common.searchbar.FilterButton
import com.tfg.securerouter.ui.app.common.searchbar.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable for a search bar with filter support in the Device Manager screen.
 *
 * Features:
 * - Combines a [SearchBar] and a [FilterButton] in a horizontal layout.
 * - Sends [DeviceManagerScreenEvent.SearchSomething] when the search query changes.
 * - Sends [DeviceManagerScreenEvent.FilterSomething] when filters are applied.
 *
 * Layout:
 * - The search bar occupies 90% of the row width.
 * - The filter button occupies 10% of the row width.
 *
 * @param parent The parent [ScreenDefault] providing access to the event bus for emitting events.
 *
 * @see SearchBar
 * @see FilterButton
 * @see DeviceManagerScreenEvent
 */
@Composable
fun DeviceManagerSearchBar(
    parent: ScreenDefault
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(
            modifier = Modifier.weight(0.9f),
            onSearchChanged = {
                CoroutineScope(Dispatchers.Main).launch {
                    parent.sendEvent(DeviceManagerScreenEvent.SearchSomething(it))
                }
            },
            hint = stringResource(R.string.dive_manger_search_hint)
        )

        FilterButton(
            modifier = Modifier.weight(0.1f),
            selectedFilters = emptySet(),
            onFiltersChanged = {
                CoroutineScope(Dispatchers.Main).launch {
                    parent.sendEvent(DeviceManagerScreenEvent.FilterSomething(it))
                }
            }
        )
    }

}