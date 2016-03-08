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
