/*
 * Copyright (C) 2025 The Android Open Source Project
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
package com.android.app.displaylib

import android.hardware.display.DisplayManager
import android.os.Handler
import android.view.IWindowManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Manual implementation of [DisplayLibComponent] that wires up all displaylib singletons.
 *
 * This replaces Dagger-generated code to avoid KSP code generation issues with complex Kotlin
 * patterns (fun interfaces, nested @Component.Factory, generic @AssistedFactory).
 */
internal class DisplayLibComponentImpl(
    displayManager: DisplayManager,
    windowManager: IWindowManager,
    bgHandler: Handler,
    bgApplicationScope: CoroutineScope,
    backgroundCoroutineDispatcher: CoroutineDispatcher,
) : DisplayLibComponent {

    override val displayRepository: DisplayRepository by lazy {
        DisplayRepositoryImpl(
            displayManager,
            bgHandler,
            bgApplicationScope,
            backgroundCoroutineDispatcher,
        )
    }

    override val displaysWithDecorationsRepository: DisplaysWithDecorationsRepository by lazy {
        DisplaysWithDecorationsRepositoryImpl(windowManager, bgApplicationScope, displayRepository)
    }

    override val displaysWithDecorationsRepositoryCompat: DisplaysWithDecorationsRepositoryCompat
        by lazy {
            DisplaysWithDecorationsRepositoryCompat(bgApplicationScope, displaysWithDecorationsRepository)
        }
}
