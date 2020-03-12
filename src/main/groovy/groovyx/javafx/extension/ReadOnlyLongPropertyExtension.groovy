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
import javafx.beans.binding.LongBinding
import javafx.beans.property.ReadOnlyLongProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue

/**
 * @author Andres Almiray
 */
class ReadOnlyLongPropertyExtension {
    static LongBinding plus(ReadOnlyLongProperty self, Number operand) {
        self.add(operand.longValue())
    }

    static LongBinding plus(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.add(operand.longValue())
    }

    static LongBinding minus(ReadOnlyLongProperty self, Number operand) {
        self.subtract(operand.longValue())
    }

    static LongBinding minus(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.subtract(operand.longValue())
    }

    static LongBinding div(ReadOnlyLongProperty self, Number operand) {
        self.divide(operand.longValue())
    }

    static LongBinding div(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.divide(operand.longValue())
    }

    static LongBinding negative(ReadOnlyLongProperty self) {
        self.negate()
    }

    static BooleanBinding gt(ReadOnlyLongProperty self, Number operand) {
        self.greaterThan(operand.longValue())
    }

    static BooleanBinding gt(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.greaterThan(operand.longValue())
    }

    static BooleanBinding ge(ReadOnlyLongProperty self, Number operand) {
        self.greaterThanOrEqualTo(operand.longValue())
    }

    static BooleanBinding ge(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.greaterThanOrEqualTo(operand.longValue())
    }

    static BooleanBinding lt(ReadOnlyLongProperty self, Number operand) {
        self.lessThan(operand.longValue())
    }

    static BooleanBinding lt(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.lessThan(operand.longValue())
    }

    static BooleanBinding le(ReadOnlyLongProperty self, Number operand) {
        self.lessThanOrEqualTo(operand.longValue())
    }

    static BooleanBinding le(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.lessThanOrEqualTo(operand.longValue())
    }

    static BooleanBinding eq(ReadOnlyLongProperty self, Number operand) {
        self.isEqualTo(operand.longValue())
    }

    static BooleanBinding eq(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(ReadOnlyLongProperty self, Number operand) {
        self.isNotEqualTo(operand.longValue())
    }

    static BooleanBinding ne(ReadOnlyLongProperty self, ObservableNumberValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(ReadOnlyLongProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyLongProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
