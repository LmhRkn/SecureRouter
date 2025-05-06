/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tfg.securerouter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.compose.runtime.CompositionLocalProvider

data class ExtraColors(
    val statusErrorColor: Color = Color.White,
    val onStatusErrorColor: Color = Color.White,
    val statusConnectedColor: Color = Color.White,
    val onStatusConnectedColor: Color = Color.White,
    val statusDisconnectedColor: Color = Color.White,
    val onStatusDisconnectedColor: Color = Color.White
)

val DarkExtraColors = ExtraColors(
    statusErrorColor = StatusErrorColorDark,
    onStatusErrorColor = OnStatusErrorColorDark,
    statusConnectedColor = StatusConnectedColorDark,
    onStatusConnectedColor = OnStatusConnectedColorDark,
    statusDisconnectedColor = StatusDisconnectedColorDark,
    onStatusDisconnectedColor = OnStatusDisconnectedColorDark
)

val LightExtraColors = ExtraColors(
    statusErrorColor = StatusErrorColorLight,
    onStatusErrorColor = OnStatusErrorColorLight,
    statusConnectedColor = StatusConnectedColorLight,
    onStatusConnectedColor = OnStatusConnectedColorLight,
    statusDisconnectedColor = StatusDisconnectedColorLight,
    onStatusDisconnectedColor = OnStatusDisconnectedColorLight
)

val LocalExtraColors = compositionLocalOf { LightExtraColors }

private val DarkColorScheme = darkColorScheme(
    background = MainBackgroundColorDark,
    onBackground = OnMainBackgroundColorDark,

    surface = SecondBackgroundColorDark,
    onSurface = OnSecondBackgroundColorDark,

    primary = PrimaryColorDark,
    onPrimary = OnPrimaryColorDark,

    secondary = SecondaryColorDark,
    onSecondary = OnSecondaryColorDark,

    tertiary = TertiaryColorDark,
    onTertiary = OnTertiaryColorDark
)

private val LightColorScheme = lightColorScheme(
    background = MainBackgroundColorLight,
    onBackground = OnMainBackgroundColorLight,

    surface = SecondBackgroundColorLight,
    onSurface = OnSecondBackgroundColorLight,

    primary = PrimaryColorLight,
    onPrimary = OnPrimaryColorLight,

    secondary = SecondaryColorLight,
    onSecondary = OnSecondaryColorLight,

    tertiary = TertiaryColorLight,
    onTertiary = OnTertiaryColorLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extraColors = if (darkTheme) DarkExtraColors else LightExtraColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

