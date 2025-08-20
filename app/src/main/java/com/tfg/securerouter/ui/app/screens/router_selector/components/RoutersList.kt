package com.tfg.securerouter.ui.app.screens.router_selector.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.screens.router_selector.RouterLabel
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.screens.router_selector.model.load.detectEphemeralOpenWrt
import com.tfg.securerouter.data.app.screens.router_selector.model.load.getRouterList
import com.tfg.securerouter.data.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.automatization.executeAutomationsBlocking
import com.tfg.securerouter.data.automatization.registry.AutomatizationRegistryBeforeOpening
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.screens.router_selector.components.extras.RouterCard
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun RoutersList(
    navController: NavController
) {
    var ephemeral by remember { mutableStateOf<RouterInfo?>(null) }
    var runnerRouter by remember { mutableStateOf<RouterInfo?>(null) }

    LaunchedEffect(Unit) {
        ephemeral = detectEphemeralOpenWrt()
    }

    val routers: List<RouterInfo> = getRouterList(ephemeral)
    val anyOnline = routers.any { RouterLabel.Online in it.labels }

    if (runnerRouter == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Selecciona un Router", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (!anyOnline) {
                Text(
                    text = "No hay ningún router conectado ahora mismo.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            routers.forEach { router ->
                RouterCard(
                    router = router,
                    onClick = {
                        if (router.id < 0) {
                            val newId = RouterSelectorCache.nextId()

                            fun suggestName(r: RouterInfo): String =
                                when {
                                    !r.localIp.isNullOrBlank() -> "Router ${r.localIp}"
                                    r.mac.length >= 5          -> "Router ${r.mac.takeLast(5)}"
                                    else                       -> "Router nuevo"
                                }

                            val toSave = router.copy(
                                id = newId,
                                name = suggestName(router),
                                labels = router.labels - RouterLabel.New + RouterLabel.Online
                            )

                            RouterSelectorCache.put(toSave)
                            AppSession.routerId = toSave.id
                            AppSession.routerIp  = toSave.localIp
                            runnerRouter = toSave
                        } else {
                            AppSession.routerId = router.id
                            AppSession.routerIp = router.localIp
                            runnerRouter = router
                        }
                    }
                )
            }

            println(RouterSelectorCache.dumpPretty())
        }
    } else {
        ExecuteAutomationsBlockingUI(
            router = runnerRouter,
            factories = AutomatizationRegistryBeforeOpening.factories,
            sh = ::shUsingLaunch,
            content = {
                LaunchedEffect(Unit) {
                    val route = HomeMenuOption.route
                    navController.navigate(route) {
                        popUpTo(route) { inclusive = false }
                    }
                }
            }
        )
    }
}
