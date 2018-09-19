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
import javafx.beans.binding.DoubleBinding
import javafx.beans.binding.NumberBinding
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue

/**
 * @author Andres Almiray
 */
class NumberBindingExtension {
    static NumberBinding plus(NumberBinding self, Number operand) {
        self.add(operand.doubleValue())
    }

    static NumberBinding plus(NumberBinding self, ObservableNumberValue operand) {
        self.add(operand.doubleValue())
    }

    static NumberBinding minus(NumberBinding self, Number operand) {
        self.subtract(operand.doubleValue())
    }

    static NumberBinding minus(NumberBinding self, ObservableNumberValue operand) {
        self.subtract(operand.doubleValue())
    }

    static NumberBinding div(NumberBinding self, Number operand) {
        self.divide(operand.doubleValue())
    }

    static NumberBinding div(NumberBinding self, ObservableNumberValue operand) {
        self.divide(operand.doubleValue())
    }

    static NumberBinding negative(NumberBinding self) {
        self.negate()
    }

    static BooleanBinding gt(NumberBinding self, Number operand) {
        self.greaterThan(operand.doubleValue())
    }

    static BooleanBinding gt(NumberBinding self, ObservableNumberValue operand) {
        self.greaterThan(operand.doubleValue())
    }

    static BooleanBinding ge(NumberBinding self, Number operand) {
        self.greaterThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding ge(NumberBinding self, ObservableNumberValue operand) {
        self.greaterThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding lt(NumberBinding self, Number operand) {
        self.lessThan(operand.doubleValue())
    }

    static BooleanBinding lt(NumberBinding self, ObservableNumberValue operand) {
        self.lessThan(operand.doubleValue())
    }

    static BooleanBinding le(NumberBinding self, Number operand) {
        self.lessThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding le(NumberBinding self, ObservableNumberValue operand) {
        self.lessThanOrEqualTo(operand.doubleValue())
    }

    static BooleanBinding eq(NumberBinding self, Number operand) {
        self.isEqualTo(operand.longValue())
    }

    static BooleanBinding eq(NumberBinding self, ObservableNumberValue operand) {
        self.isEqualTo(operand)
    }

    static BooleanBinding ne(NumberBinding self, Number operand) {
        self.isNotEqualTo(operand.longValue())
    }

    static BooleanBinding ne(NumberBinding self, ObservableNumberValue operand) {
        self.isNotEqualTo(operand)
    }

    static void onChange(NumberBinding self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(NumberBinding self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
