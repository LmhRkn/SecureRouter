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

package com.tfg.securerouter.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.tfg.securerouter.data.RouterRepository
import com.tfg.securerouter.data.DefaultRouterRepository
import com.tfg.securerouter.data.local.database.Router
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//interface DataModule {
//
//    @Singleton
//    @Binds
//    fun bindsRouterRepository(
//        routerRepository: DefaultRouterRepository
//    ): RouterRepository
//}
interface DataModule {

    @Singleton
    @Binds
    fun bindsRouterRepository(
        fake: FakeRouterRepository
    ): RouterRepository

}

class FakeRouterRepository @Inject constructor() : RouterRepository {
    override val routers: Flow<List<Router>> = flowOf(
        fakeRouters
    )

    override suspend fun add(name: String) {
        // No-op para que no rompa
    }
}


val fakeRouters = listOf(
    Router(id = 1, name = "One", isConnected = true),
    Router(id = 2, name = "Two", isVpn = true),
    Router(id = 3, name = "Three", isVpn = true),
    Router(id = 4, name = "Four", isVpn = true),
    Router(id = 5, name = "Five", isVpn = true),
    Router(id = 6, name = "Six", isVpn = true),
    Router(id = 7, name = "Seven", isVpn = true, error = true)
)
