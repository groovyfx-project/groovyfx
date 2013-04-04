package groovyx.javafx

class JdkUtil {
    static boolean jdkIsBefore8() {
        System.properties['java.version'][0..2].toFloat() < 1.8f
    }
}
