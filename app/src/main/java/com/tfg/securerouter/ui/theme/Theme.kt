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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

data class ExtraColors(
    val connectedStatus: Color = Color.White,
    val onConnectedStatus: Color = Color.White,
    val disconnectedStatus: Color = Color.White,
    val onDisconnectedStatus: Color = Color.White,
    val errorStatus: Color = Color.White,
    val onErrorStatus: Color = Color.White,
)

val LocalExtraColors = compositionLocalOf<ExtraColors> {
    error("No ExtraColors provided")
}

private val ExtraColorsLight = ExtraColors(
    connectedStatus = ConnectedStatusLight,
    onConnectedStatus = OnConnectedStatusLight,
    disconnectedStatus = DisconnectedStatusLight,
    onDisconnectedStatus = OnDisconnectedStatusLight,
    errorStatus = ErrorStatusLight,
    onErrorStatus = OnErrorStatusLight
)

private val ExtraColorsDark = ExtraColors(
    connectedStatus = ConnectedStatusLight,
    onConnectedStatus = OnConnectedStatusLight,
    disconnectedStatus = DisconnectedStatusLight,
    onDisconnectedStatus = OnDisconnectedStatusLight,
    errorStatus = ErrorStatusLight,
    onErrorStatus = OnErrorStatusLight
)


private val LightColorScheme = darkColorScheme(
    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,

    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,

    // primaryFixed = PrimaryFixedLight,
    //onPrimaryFixed = OnPrimaryFixedLight,
    //
    //primaryFixedDim = PrimaryFixedDimLight,
    //onPrimaryFixedVariant = OnPrimaryFixedVariantLight,

    inversePrimary = InversePrimaryLight,

    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,

    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,

    //secondaryFixed = SecondaryFixedLight,
    //onSecondaryFixed = OnSecondaryFixedLight,
    //
    //secondaryFixedDim = SecondaryFixedDimLight,
    //onSecondaryFixedVariant = OnSecondaryFixedVariantLight,

    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,

    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,

    //tertiaryFixed = TertiaryFixedLight,
    //onTertiaryFixed = OnTertiaryFixedLight,
    //
    //tertiaryFixedDim = TertiaryFixedDimLight,
    //onTertiaryFixedVariant = OnTertiaryFixedVariantLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,

    surfaceBright = SurfaceBrightLight,

    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,

    //surfaceDim = SurfaceDimLight,
    //surfaceTint = SurfaceTintLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,

    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,

    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,

    scrim = ScrimLight
)

private val DarkColorScheme = lightColorScheme(
    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,

    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,

    // primaryFixed = PrimaryFixedLight,
    //onPrimaryFixed = OnPrimaryFixedLight,
    //
    //primaryFixedDim = PrimaryFixedDimLight,
    //onPrimaryFixedVariant = OnPrimaryFixedVariantLight,

    inversePrimary = InversePrimaryLight,

    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,

    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,

    //secondaryFixed = SecondaryFixedLight,
    //onSecondaryFixed = OnSecondaryFixedLight,
    //
    //secondaryFixedDim = SecondaryFixedDimLight,
    //onSecondaryFixedVariant = OnSecondaryFixedVariantLight,

    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,

    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,

    //tertiaryFixed = TertiaryFixedLight,
    //onTertiaryFixed = OnTertiaryFixedLight,
    //
    //tertiaryFixedDim = TertiaryFixedDimLight,
    //onTertiaryFixedVariant = OnTertiaryFixedVariantLight,

    surface = SurfaceLight,
    onSurface = OnSurfaceLight,

    surfaceBright = SurfaceBrightLight,

    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,

    //surfaceDim = SurfaceDimLight,
    //surfaceTint = SurfaceTintLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,

    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,

    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,

    scrim = ScrimLight
)

@Composable
fun SecureRouterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extraColors = if (darkTheme) ExtraColorsDark else ExtraColorsLight

    CompositionLocalProvider(
        LocalExtraColors provides extraColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}