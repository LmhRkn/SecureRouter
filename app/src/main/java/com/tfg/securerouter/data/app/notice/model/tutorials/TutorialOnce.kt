package com.tfg.securerouter.data.app.notice.model.tutorials

import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

object TutorialOnce {
    fun shouldAutoOpen(screenKey: String): Boolean {
        if (AppSession.routerId == null) return false
        val info = RouterSelectorCache.getRouter(AppSession.routerId.toString()) ?: return false
        return screenKey !in info.shownTutorials
    }

    fun markShown(screenKey: String) {
        if (AppSession.routerId == null) return
        RouterSelectorCache.update(AppSession.routerId.toString()) { r ->
            r.copy(shownTutorials = r.shownTutorials + screenKey)
        }
    }
}
