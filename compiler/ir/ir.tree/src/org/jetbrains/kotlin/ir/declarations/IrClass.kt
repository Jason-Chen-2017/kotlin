/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.ir.declarations

import org.jetbrains.kotlin.descriptors.ClassDescriptor

interface IrClass : IrDeclaration, IrDeclarationContainer {
    override val declarationKind: IrDeclarationKind
        get() = IrDeclarationKind.CLASS

    override val descriptor: ClassDescriptor

    interface Builder : IrDeclarationContainer.Builder {
        val startOffset: Int
        val endOffset: Int
        var origin: IrDeclarationOrigin
        val descriptor: ClassDescriptor

        override fun build(): IrClass
    }
}

fun IrClass.getInstanceInitializerMembers() =
        declarations.filter {
            when (it) {
                is IrAnonymousInitializer ->
                    true
                is IrProperty ->
                    it.backingField?.initializer != null
                is IrField ->
                    it.initializer != null
                else -> false
            }
        }
