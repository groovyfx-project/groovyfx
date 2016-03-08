/*
 * Copyright 2011-2016 the original author or authors.
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
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue

/**
 * @author Andres Almiray
 */
class ReadOnlyIntegerPropertyExtension {
    static IntegerBinding plus(ReadOnlyIntegerProperty self, Number operand) {
        self.add(operand.intValue())
    }

    static IntegerBinding plus(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.add(operand.intValue())
    }

    static IntegerBinding minus(ReadOnlyIntegerProperty self, Number operand) {
        self.subtract(operand.intValue())
    }

    static IntegerBinding minus(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.subtract(operand.intValue())
    }

    static IntegerBinding div(ReadOnlyIntegerProperty self, Number operand) {
        self.divide(operand.intValue())
    }

    static IntegerBinding div(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.divide(operand.intValue())
    }

    static IntegerBinding negative(ReadOnlyIntegerProperty self) {
        self.negate()
    }

    static BooleanBinding gt(ReadOnlyIntegerProperty self, Number operand) {
        self.greaterThan(operand.intValue())
    }

    static BooleanBinding gt(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.greaterThan(operand.intValue())
    }

    static BooleanBinding ge(ReadOnlyIntegerProperty self, Number operand) {
        self.greaterThanOrEqualTo(operand.intValue())
    }

    static BooleanBinding ge(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.greaterThanOrEqualTo(operand.intValue())
    }

    static BooleanBinding lt(ReadOnlyIntegerProperty self, Number operand) {
        self.lessThan(operand.intValue())
    }

    static BooleanBinding lt(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.lessThan(operand.intValue())
    }

    static BooleanBinding le(ReadOnlyIntegerProperty self, Number operand) {
        self.lessThanOrEqualTo(operand.intValue())
    }

    static BooleanBinding le(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.lessThanOrEqualTo(operand.intValue())
    }

    static BooleanBinding eq(ReadOnlyIntegerProperty self, Number operand) {
        self.isEqualTo(operand.longValue())
    }

    static BooleanBinding eq(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(ReadOnlyIntegerProperty self, Number operand) {
        self.isNotEqualTo(operand.longValue())
    }

    static BooleanBinding ne(ReadOnlyIntegerProperty self, ObservableNumberValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(ReadOnlyIntegerProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyIntegerProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
