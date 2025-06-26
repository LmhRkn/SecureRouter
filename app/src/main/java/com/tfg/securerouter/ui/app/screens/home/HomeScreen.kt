import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tfg.securerouter.ui.common.home_screen.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.home.HomeCoordinator
import com.tfg.securerouter.data.menu.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.menu.screens.home.model.send.SendRouterName
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.common.texts.TextWithToggleOption


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
            { HomeRouterInfoSection(
                state = routerState,
                onEditAliasClick = { newAlias ->
                    SendRouterName.updateRouterAlias(newAlias) { success ->
                        println("Alias actualizado: $success")
                    }
                }
            ) },
            { RouterIcon() },
            { ConnectedDevicesList(devices_state = devicesState) },
            { TextWithToggleOption(text = "Prueba:", initialChecked = true, onToggleChanged = { /* TODO */ }) }
        )

        RenderScreen()
    }
}
