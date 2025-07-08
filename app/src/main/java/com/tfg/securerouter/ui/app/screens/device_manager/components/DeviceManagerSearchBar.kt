package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.common.searchbar.FilterButton
import com.tfg.securerouter.ui.common.searchbar.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    parent.sendEvent(com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent.SearchSomething(it))
                }
            },
            hint = "Buscar dispositivo..."
        )

        FilterButton(
            modifier = Modifier.weight(0.1f),
            selectedFilters = emptySet(),
            onFiltersChanged = {
                CoroutineScope(Dispatchers.Main).launch {
                    parent.sendEvent(com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent.FilterSomething(it))
                }
            }
        )
    }

}