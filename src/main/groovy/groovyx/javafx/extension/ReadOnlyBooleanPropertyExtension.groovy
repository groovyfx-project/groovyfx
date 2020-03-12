/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.javafx.extension

import javafx.beans.InvalidationListener
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableBooleanValue

/**
 * @author Andres Almiray
 */
class ReadOnlyBooleanPropertyExtension {
    static BooleanBinding negative(ReadOnlyBooleanProperty self) {
        self.not()
    }

    static BooleanBinding eq(ReadOnlyBooleanProperty self, Boolean operand) {
        eq(self, new SimpleBooleanProperty(operand))
    }

    static BooleanBinding eq(ReadOnlyBooleanProperty self, ObservableBooleanValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(ReadOnlyBooleanProperty self, Boolean operand) {
        ne(self, new SimpleBooleanProperty(operand))
    }

    static BooleanBinding ne(ReadOnlyBooleanProperty self, ObservableBooleanValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(ReadOnlyBooleanProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyBooleanProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
