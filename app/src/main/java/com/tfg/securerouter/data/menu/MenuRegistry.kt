package com.tfg.securerouter.data.app.menu

import com.tfg.securerouter.data.menu.menu_screens.AdministrarDispositivosMenuOption
import com.tfg.securerouter.data.app.menu.menu_screens.FilterMenuOption
import com.tfg.securerouter.data.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.menu.menu_screens.SettingsMenuOption
import com.tfg.securerouter.data.app.menu.menu_screens.WifiMenuOption
import com.tfg.securerouter.data.other_screen.LanguageScreenOption
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen

/**
 * Registry object that holds all available [MenuOption]s used in the navigation drawer.
 *
 * This object acts as a centralized list where new menu options can be added.
 * It avoids the need to modify other parts of the code when extending the drawer's contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render the navigation drawer menu.
 * - New menu options can be registered simply by adding them to the [items] list.
 *
 * @property items A list of all registered [MenuOption]s in the order they should appear.
 *
 * @see MenuOption for the interface each menu screen must implement.
 */

object MenuRegistry {
    val items = listOf(
        HomeMenuOption,
        AdministrarDispositivosMenuOption,
        WifiMenuOption,
        FilterMenuOption,
        SettingsMenuOption
        // Agrega aquí nuevas opciones sin tocar otra lógica
    )
}

object OtherScreenRegistry {
    val items = listOf(
        LanguageScreenOption
    )
}