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

package com.tfg.securerouter.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tfg.securerouter.ui.navigation.MainNavigation
import dagger.hilt.android.AndroidEntryPoint
import com.tfg.securerouter.ui.theme.MyApplicationTheme
import com.tfg.securerouter.utils.applyAppLocale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BORRAR idioma guardado para forzar LanguageScreen
//        getSharedPreferences("settings", MODE_PRIVATE)
//            .edit()
//            .remove("language")
//            .apply()

        setContent {
            MyApplicationTheme {
                MainNavigation()
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        val lang = base.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
        val context = applyAppLocale(base, lang)
        super.attachBaseContext(context)
    }
}


