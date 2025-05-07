package com.tfg.securerouter.ui.router.router_list.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.tfg.securerouter.ui.router.RouterCard
import com.tfg.securerouter.ui.router.router_list.model.RouterUIModel

@Composable
fun MainRouterCard(router: RouterUIModel, navController: NavController) {
    RouterCard(
        router = router,
        isMain = true,
        onAccessClick = {
            navController.navigate("home"
            )
        })
}