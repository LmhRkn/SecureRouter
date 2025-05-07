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

package com.tfg.securerouter.data.router.repository

import kotlinx.coroutines.flow.Flow
import com.tfg.securerouter.data.local.database.Router
import com.tfg.securerouter.data.local.database.RouterDao
import javax.inject.Inject

interface RouterRepository {
    val routers: Flow<List<Router>>

    suspend fun add(name: String)
}

class DefaultRouterRepository @Inject constructor(
    private val routerDao: RouterDao
) : RouterRepository {

    override val routers: Flow<List<Router>> = routerDao.getRouters()

    override suspend fun add(name: String) {
        // Por defecto el router se agrega como desconectado y no VPN
        val newRouter = Router(name = name, isConnected = false, isVpn = false)
        routerDao.insertRouter(newRouter)
    }
}
