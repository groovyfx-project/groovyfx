/*
 * Copyright 2011-2018 the original author or authors.
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
import javafx.beans.binding.FloatBinding
import javafx.beans.property.ReadOnlyFloatProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue

/**
 * @author Andres Almiray
 */
class ReadOnlyFloatPropertyExtension {
    static FloatBinding plus(ReadOnlyFloatProperty self, Number operand) {
        self.add(operand.floatValue())
    }

    static FloatBinding plus(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.add(operand.floatValue())
    }

    static FloatBinding minus(ReadOnlyFloatProperty self, Number operand) {
        self.subtract(operand.floatValue())
    }

    static FloatBinding minus(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.subtract(operand.floatValue())
    }

    static FloatBinding div(ReadOnlyFloatProperty self, Number operand) {
        self.divide(operand.floatValue())
    }

    static FloatBinding div(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.divide(operand.floatValue())
    }

    static FloatBinding negative(ReadOnlyFloatProperty self) {
        self.negate()
    }

    static BooleanBinding gt(ReadOnlyFloatProperty self, Number operand) {
        self.greaterThan(operand.floatValue())
    }

    static BooleanBinding gt(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.greaterThan(operand.floatValue())
    }

    static BooleanBinding ge(ReadOnlyFloatProperty self, Number operand) {
        self.greaterThanOrEqualTo(operand.floatValue())
    }

    static BooleanBinding ge(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.greaterThanOrEqualTo(operand.floatValue())
    }

    static BooleanBinding lt(ReadOnlyFloatProperty self, Number operand) {
        self.lessThan(operand.floatValue())
    }

    static BooleanBinding lt(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.lessThan(operand.floatValue())
    }

    static BooleanBinding le(ReadOnlyFloatProperty self, Number operand) {
        self.lessThanOrEqualTo(operand.floatValue())
    }

    static BooleanBinding le(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.lessThanOrEqualTo(operand.floatValue())
    }

    static BooleanBinding eq(ReadOnlyFloatProperty self, Number operand) {
        self.isEqualTo(operand.longValue())
    }

    static BooleanBinding eq(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(ReadOnlyFloatProperty self, Number operand) {
        self.isNotEqualTo(operand.longValue())
    }

    static BooleanBinding ne(ReadOnlyFloatProperty self, ObservableNumberValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(ReadOnlyFloatProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyFloatProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
