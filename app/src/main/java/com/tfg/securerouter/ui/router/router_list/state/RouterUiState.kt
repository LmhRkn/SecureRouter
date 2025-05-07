package com.tfg.securerouter.ui.router.router_list.state

import com.tfg.securerouter.ui.router.router_list.model.RouterUIModel

sealed interface RouterUiState {
    object Loading : RouterUiState // La app está cargando datos
    data class Error(val throwable: Throwable) : RouterUiState // Algo falló
    data class Success(val data: List<RouterUIModel>) : RouterUiState // Datos cargados con éxito
}
