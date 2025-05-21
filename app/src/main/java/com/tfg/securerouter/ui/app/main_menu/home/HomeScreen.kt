import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.components.home_screen.ConnectedDevicesList
import com.tfg.securerouter.ui.components.home_screen.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.home.model.HomeViewModel
import com.tfg.securerouter.data.menu.screens.home.state.HomeUiState

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()

    val state = viewModel.uiState.collectAsState().value
    println("Composing HomeContent with state: $state")

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        HomeContent(state, onEditAliasClick = { /* TODO */ })
    }
}



@Composable
private fun HomeContent(state: HomeUiState, onEditAliasClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        HomeRouterInfoSection(state, onEditAliasClick)
        Spacer(modifier = Modifier.height(16.dp))
        RouterIcon()
        Spacer(modifier = Modifier.height(8.dp))
        ConnectedDevicesList(state.connectedDevices)
    }
}