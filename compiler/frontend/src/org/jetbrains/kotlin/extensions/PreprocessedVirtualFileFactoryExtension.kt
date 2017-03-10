/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.extensions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile

interface PreprocessedVirtualFileFactoryExtension {
    companion object : ProjectExtensionDescriptor<PreprocessedVirtualFileFactoryExtension>(
            "org.jetbrains.kotlin.preprocessedVirtualFileFactoryExtension",
            PreprocessedVirtualFileFactoryExtension::class.java
    )
    fun isPassThrough(): Boolean

    /**
     * Creates a preprocessed virtual file for the source [file]
     */
    fun createPreprocessedFile(file: VirtualFile?): VirtualFile?
    fun createPreprocessedLightFile(file: LightVirtualFile?): LightVirtualFile?
}

class PreprocessedFileCreator(val project: Project) {

    private val validExts: Sequence<PreprocessedVirtualFileFactoryExtension> by lazy {
        PreprocessedVirtualFileFactoryExtension.getInstances(project).filterNot { it.isPassThrough() }.asSequence()
    }

    fun create(file: VirtualFile): VirtualFile = validExts.mapNotNull { it.createPreprocessedFile(file) }.firstOrNull() ?: file

    fun createLight(file: LightVirtualFile): LightVirtualFile = validExts.mapNotNull { it.createPreprocessedLightFile(file) }.firstOrNull() ?: file
}
