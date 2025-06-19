import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tfg.securerouter.ui.components.home_screen.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.home.HomeCoordinator
import com.tfg.securerouter.data.menu.screens.home.model.ConnectedDeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.HomeRouterInfoModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault


class HomeScreen: ScreenDefault() {
    @Composable
    fun HomeScreenInit() {
        val coordinator: HomeCoordinator = viewModel()
        val isReady by coordinator.isReady.collectAsState()

        if (!isReady) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            HomeContent(coordinator)
        }

    }

    @Composable
    private fun HomeContent(coordinator: HomeCoordinator) {
        val routerInfoModel = coordinator.modules.filterIsInstance<HomeRouterInfoModel>().first()
        val connectedDevicesModel = coordinator.modules.filterIsInstance<ConnectedDeviceModel>().first()

        val routerState = routerInfoModel.state.collectAsState().value
        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            { HomeRouterInfoSection(state = routerState, onEditAliasClick = { /* TODO */ }) },
            { RouterIcon() },
            { ConnectedDevicesList(devices_state = devicesState) },
        )

        RenderScreen()
    }


//    @Preview
//    @Composable
//    fun HomeContentPreview() {
//        val state = HomeUiState(
//            routerAlias = "Router Pepe",
//            macAddress = "00:00:00:00:00:00",
//            isLoading = false
//        )
//        Surface(color = Color.White) {
//            HomeContent(state, onEditAliasClick = {})
//        }
//    }
}
