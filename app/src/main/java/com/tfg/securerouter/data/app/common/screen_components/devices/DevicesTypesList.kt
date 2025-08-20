package com.tfg.securerouter.data.app.common.screen_components.devices

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import com.tfg.securerouter.R

/**
 * Lista de palabras clave para dispositivos de tipo teléfono.
 * Estos son los fabricantes más comunes de teléfonos móviles.
 */
val phoneList: List<String> = listOf(
    "apple",
    "samsung",
    "huawei",
    "xiaomi",
    "oneplus",
    "google",
    "oppo",
    "vivo"
)

/**
 * Configuración del tipo de dispositivo para teléfonos móviles.
 * Utiliza la lista de palabras clave de `phoneList` y asocia un icono,
 * una descripción y una etiqueta para el tipo de dispositivo.
 */
val phoneType: DeviceTypeConfig = DeviceTypeConfig(
    keywords = phoneList,
    icon = Icons.Filled.PhoneAndroid,
    descriptionRes = R.string.device_phone_icon,
    label = DeviceLabel.Phone
)

/**
 * Lista de palabras clave para dispositivos de tipo PC.
 * Contiene los nombres de fabricantes comunes de ordenadores portátiles y de escritorio.
 */
val pcList: List<String> = listOf(
    "dell",
    "hp",
    "lenovo",
    "asus",
    "msi",
    "acer",
    "gigabyte",
    "microsoft"
)

/**
 * Configuración del tipo de dispositivo para PCs.
 * Este tipo se asocia con los fabricantes definidos en `pcList` y tiene
 * su propio icono, descripción y etiqueta.
 */
val pcType: DeviceTypeConfig = DeviceTypeConfig(
    keywords = pcList,
    icon = Icons.Filled.Laptop,
    descriptionRes = R.string.device_pc_icon,
    label = DeviceLabel.PC
)

/**
 * Lista de palabras clave para dispositivos de tipo consola de videojuegos.
 * Estos son los nombres de los fabricantes más comunes de consolas de videojuegos.
 */
val consoleList: List<String> = listOf(
    "sony",
    "nintendo",
    "microsoft"
)

/**
 * Configuración del tipo de dispositivo para consolas de videojuegos.
 * Asocia la lista de palabras clave de consolas, un icono, una descripción y una etiqueta.
 */
val consoleType: DeviceTypeConfig = DeviceTypeConfig(
    keywords = consoleList,
    icon = Icons.Filled.SportsEsports,
    descriptionRes = R.string.device_console_icon,
    label = DeviceLabel.Console
)

/**
 * Configuración para dispositivos genéricos o no clasificados.
 * Este tipo cubre cualquier dispositivo que no esté relacionado con los tipos anteriores,
 * como IoT, dispositivos de redes, etc.
 */
val otherType: DeviceTypeConfig = DeviceTypeConfig(
    keywords = listOf(),
    icon = Icons.Filled.DevicesOther,
    descriptionRes = R.string.device_other_device_icon,
    label = DeviceLabel.Other
)

/**
 * Arreglo de todos los tipos de dispositivos definidos previamente.
 * Se pueden agregar más tipos de dispositivos en el futuro siguiendo la misma estructura.
 */
val deviceTypes = arrayOf(
    phoneType,
    pcType,
    consoleType,
    otherType   // Always last
)
