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

import javafx.beans.binding.DoubleBinding
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableNumberValue
import javafx.util.Duration

/**
 * @author Andres Almiray
 */
class NumberExtension {
    static Duration getM(Number self) {
        Duration.minutes(self.doubleValue())
    }

    static Duration getS(Number self) {
        Duration.seconds(self.doubleValue())
    }

    static Duration getMs(Number self) {
        Duration.millis(self.doubleValue())
    }

    static Duration getH(Number self) {
        Duration.hours(self.doubleValue())
    }

    static DoubleBinding plus(Number self, ObservableNumberValue operand) {
        new SimpleDoubleProperty(self.doubleValue()).add(operand)
    }

    static DoubleBinding minus(Number self, ObservableNumberValue operand) {
        new SimpleDoubleProperty(self.doubleValue()).subtract(operand)
    }

    static DoubleBinding multiply(Number self, ObservableNumberValue operand) {
        new SimpleDoubleProperty(self.doubleValue()).multiply(operand)
    }

    static DoubleBinding div(Number self, ObservableNumberValue operand) {
        new SimpleDoubleProperty(self.doubleValue()).divide(operand)
    }
}
