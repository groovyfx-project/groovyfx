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

import javafx.util.Duration
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Andres Almiray
 */
@Unroll
class NumberExtensionSpec extends Specification {
    def "seconds #number (#type)"() {
        expect:
        number.s == duration

        where:
        number           | type         || duration
        42 as int        | Integer.TYPE || Duration.seconds(42)
        42 as long       | Long.TYPE    || Duration.seconds(42)
        42 as float      | Float.TYPE   || Duration.seconds(42)
        42 as double     | Double.TYPE  || Duration.seconds(42)
        42 as BigInteger | BigInteger   || Duration.seconds(42)
        42 as BigDecimal | BigDecimal   || Duration.seconds(42)
    }

    def "minutes #number (#type)"() {
        expect:
        number.m == duration

        where:
        number           | type         || duration
        42 as int        | Integer.TYPE || Duration.minutes(42)
        42 as long       | Long.TYPE    || Duration.minutes(42)
        42 as float      | Float.TYPE   || Duration.minutes(42)
        42 as double     | Double.TYPE  || Duration.minutes(42)
        42 as BigInteger | BigInteger   || Duration.minutes(42)
        42 as BigDecimal | BigDecimal   || Duration.minutes(42)
    }

    def "hours #number (#type)"() {
        expect:
        number.h == duration

        where:
        number           | type         || duration
        42 as int        | Integer.TYPE || Duration.hours(42)
        42 as long       | Long.TYPE    || Duration.hours(42)
        42 as float      | Float.TYPE   || Duration.hours(42)
        42 as double     | Double.TYPE  || Duration.hours(42)
        42 as BigInteger | BigInteger   || Duration.hours(42)
        42 as BigDecimal | BigDecimal   || Duration.hours(42)
    }

    def "millis #number (#type)"() {
        expect:
        number.ms == duration

        where:
        number           | type         || duration
        42 as int        | Integer.TYPE || Duration.millis(42)
        42 as long       | Long.TYPE    || Duration.millis(42)
        42 as float      | Float.TYPE   || Duration.millis(42)
        42 as double     | Double.TYPE  || Duration.millis(42)
        42 as BigInteger | BigInteger   || Duration.millis(42)
        42 as BigDecimal | BigDecimal   || Duration.millis(42)
    }
}
