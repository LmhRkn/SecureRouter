package com.tfg.securerouter.data.app.screens.wifi.utils

import com.tfg.securerouter.R

fun validatePassword(password: String): Int? {
    if (password.length < 8) {
        return R.string.wifi_not_enough_characters
    }

    return null
}
