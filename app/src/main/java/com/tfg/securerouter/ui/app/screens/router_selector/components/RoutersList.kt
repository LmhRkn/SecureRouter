package com.tfg.securerouter.ui.app.screens.router_selector.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tfg.securerouter.data.app.screens.router_selector.RouterLabel
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.screens.router_selector.model.load.detectEphemeralOpenWrt
import com.tfg.securerouter.data.app.screens.router_selector.model.load.getRouterList
import com.tfg.securerouter.data.app.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.screens.router_selector.components.extras.RouterCard
import com.tfg.securerouter.data.app.screens.other_screen.ShLoginScreenOption
import com.tfg.securerouter.data.app.screens.router_selector.model.load.findMatchingNoIpRouter
import com.tfg.securerouter.data.app.automatization.registry.AutomatizationRegistryAfterSHLogin

@Composable
fun RoutersList(
    navController: NavController
) {
    var ephemeral by remember { mutableStateOf<RouterInfo?>(null) }
    var match by remember { mutableStateOf<RouterInfo?>(null) }
    var runnerRouter by remember { mutableStateOf<RouterInfo?>(null) }

    val everyRouter = RouterSelectorCache.getRouters()

    LaunchedEffect(Unit) {
        ephemeral = detectEphemeralOpenWrt()
        if (ephemeral == null) {
            match = findMatchingNoIpRouter(everyRouter)
            Log.d("RoutersList", "Match por IP pública/no-ip: $match")
        }
    }

    val baseRouters: List<RouterInfo> = getRouterList(ephemeral) // ← SOLO ephemeral real
    val routers: List<RouterInfo> =
        if (match != null) {
            baseRouters.map {
                if (it.id == match!!.id)
                    it.copy(
                        labels = it.labels - RouterLabel.Offline + RouterLabel.Online,
                        isVpn  = true
                    )
                else it
            }
        } else baseRouters
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

                            AppSession.routerId = toSave.id
                            AppSession.routerIp  = toSave.localIp
                            RouterSelectorCache.put(toSave)
                            runnerRouter = toSave
                        } else {
                            if (RouterLabel.Online in router.labels) {
                                AppSession.routerId = router.id
                                AppSession.routerIp = router.localIp
                                runnerRouter = router
                            }
                        }
                        Log.d("RouterSelectionScreen", "AppSession: ${AppSession.routerId}, ${AppSession.routerIp}")
                    }
                )
            }
        }
    } else {
        ExecuteAutomationsBlockingUI(
            router = runnerRouter,
            factories = AutomatizationRegistryAfterSHLogin.factories,
            sh = ::shUsingLaunch,
            content = {
                LaunchedEffect(Unit) {
                    val route = ShLoginScreenOption.route
                    navController.navigate(route) {
                        popUpTo(route) { inclusive = false }
                    }
                }
            }
        )
    }
}
