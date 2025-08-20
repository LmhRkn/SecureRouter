package com.tfg.securerouter.ui.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent
import com.tfg.securerouter.data.notice.model.NoticeEvent
import com.tfg.securerouter.ui.app.screens.device_manager.DeviceManagerScreen
import com.tfg.securerouter.ui.app.screens.home.HomeScreen
import com.tfg.securerouter.ui.notice.NoticeHost
import com.tfg.securerouter.data.notice.model.NoticeSpec
import com.tfg.securerouter.data.notice.model.alerts.ActiveAlert
import com.tfg.securerouter.data.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.notice.tutorials.UiReadyCenter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Base class for all composable screens in the app.
 *
 * This class provides:
 * - A common structure for initializing screens via [ScreenCoordinatorDefault].
 * - A loading indicator while modules are being prepared.
 * - An event bus to enable communication between screen components.
 * - Utilities for dynamically adding and rendering UI components with dividers.
 *
 * All concrete screens (e.g., [DeviceManagerScreen], [HomeScreen]) should extend this class
 * and override [ScreenContent] to define their specific UI.
 *
 * @property eventBus Shared flow for broadcasting [ScreenEvent]s to child components.
 *
 * @see ScreenCoordinatorDefault
 * @see ScreenEvent
 */
open class ScreenDefault {
    open val initialTutorialSpec: TutorialSpec? = null

    private val components = mutableStateListOf<@Composable () -> Unit>()
    private val noticeQueue = mutableStateListOf<NoticeSpec>()

    private val _eventBus = MutableSharedFlow<ScreenEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventBus = _eventBus.asSharedFlow()

    /**
     * Emits a [ScreenEvent] to the shared event bus.
     *
     * @param event The event to send.
     */
    suspend fun sendEvent(event: ScreenEvent) {
        _eventBus.emit(event)
    }

    fun trySendEvent(event: ScreenEvent): Boolean = _eventBus.tryEmit(event)

    /**
     * Adds one or more composable components to the screen.
     *
     * @param newComponents Vararg list of composables to add.
     */
    fun addComponents(vararg newComponents: @Composable () -> Unit) {
        newComponents.forEach { component ->
            components.add(component)
        }
    }

    /**
     * Initializes the screen by observing the coordinator's loading state.
     *
     * Displays a [CircularProgressIndicator] while [ScreenCoordinatorDefault.isReady] is `false`,
     * then calls [ScreenContent] once data is loaded.
     *
     * @param coordinator The screen’s coordinator responsible for data loading.
     */
    @Composable
    fun ScreenInit(coordinator: ScreenCoordinatorDefault) {
        val isReady by coordinator.isReady.collectAsState()
        var activeAlert by rememberSaveable { mutableStateOf<ActiveAlert?>(null) }

        LaunchedEffect(isReady) {
            UiReadyCenter.setReady(isReady)
            if (isReady) {
                TutorialCenter.register(initialTutorialSpec)
            }
        }

        LaunchedEffect(Unit) {
            eventBus.collect { ev ->
                when (ev) {
                    is NoticeEvent.Show -> noticeQueue.add(0, ev.notice)
                    is NoticeEvent.ClearAll -> noticeQueue.clear()
                    else -> Unit
                }
            }
        }

        if (!isReady) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ScreenContent(coordinator)
        }
    }

    /**
     * Defines the UI content of the screen.
     *
     * Subclasses should override this method to declare their layout and behavior.
     *
     * @param coordinator The screen’s coordinator providing data and state.
     */
    @Composable
    open fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
    }

    /**
     * Renders all added components in a vertically scrollable column.
     *
     * Adds a [HorizontalDivider] between components for visual separation.
     */
    @Composable
    fun RenderScreen() {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            NoticeHost(
                notices = noticeQueue,
                onDismiss = { idx -> noticeQueue.removeAt(idx) },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            components.forEachIndexed { index, component ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
                component()
            }
        }
    }

    fun setComponents(vararg newComponents: @Composable () -> Unit) {
        components.clear()
        newComponents.forEach { component ->
            components.add(component)
        }
    }
}

