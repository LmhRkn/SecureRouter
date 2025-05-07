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

package com.tfg.securerouter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfg.securerouter.ui.language.LanguageScreen
import com.tfg.securerouter.ui.router.router_list.RouterScreen
import com.tfg.securerouter.ui.router.windows.home.HomeScreen

@Composable
fun MainNavigation(viewModel: NavigationViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val startDestination = viewModel.startDestination.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.determineStartDestination()
    }

    if (startDestination != "loading") {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("language") {
                LanguageScreen {
                    navController.navigate("main") {
                        popUpTo("language") { inclusive = true }
                    }
                }
            }
            composable("main") {
                RouterScreen(navController)
            }
            composable("home") {
                HomeScreen() // O usa hiltViewModel si aplicas Hilt en Home
            }
        }
    }
}
