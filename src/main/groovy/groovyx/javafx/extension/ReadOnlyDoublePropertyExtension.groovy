/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2019 the original author or authors.
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
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue

/**
 * @author Andres Almiray
 */
class ReadOnlyDoublePropertyExtension {
    static DoubleBinding plus(ReadOnlyDoubleProperty self, Number operand) {
        self.add(operand.doubleValue())
    }

    static DoubleBinding plus(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.add(operand.doubleValue())
    }

    static DoubleBinding minus(ReadOnlyDoubleProperty self, Number operand) {
        self.subtract(operand.doubleValue())
    }

    static DoubleBinding minus(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.subtract(operand.doubleValue())
    }

    static DoubleBinding div(ReadOnlyDoubleProperty self, Number operand) {
        self.divide(operand.doubleValue())
    }

    static DoubleBinding div(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.divide(operand.doubleValue())
    }

    static DoubleBinding negative(ReadOnlyDoubleProperty self) {
        self.negate()
    }

    static BooleanBinding gt(ReadOnlyDoubleProperty self, Number operand) {
        self.greaterThan(operand.doubleValue())
    }

    static BooleanBinding gt(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.greaterThan(operand.doubleValue())
    }

    static BooleanBinding ge(ReadOnlyDoubleProperty self, Number operand) {
        self.greaterThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding ge(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.greaterThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding lt(ReadOnlyDoubleProperty self, Number operand) {
        self.lessThan(operand.doubleValue())
    }

    static BooleanBinding lt(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.lessThan(operand.doubleValue())
    }

    static BooleanBinding le(ReadOnlyDoubleProperty self, Number operand) {
        self.lessThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding le(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.lessThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding eq(ReadOnlyDoubleProperty self, Number operand) {
        self.isEqualTo(operand.longValue())
    }

    static BooleanBinding eq(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(ReadOnlyDoubleProperty self, Number operand) {
        self.isNotEqualTo(operand.longValue())
    }

    static BooleanBinding ne(ReadOnlyDoubleProperty self, ObservableNumberValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(ReadOnlyDoubleProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyDoubleProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
