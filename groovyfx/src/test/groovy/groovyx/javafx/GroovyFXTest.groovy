package groovyx.javafx

import javafx.stage.Stage
import java.util.concurrent.CountDownLatch

public class GroovyFXTest extends GroovyTestCase {

    void testStartSetsDelegate() {
        Stage startParameter
        def latch = new CountDownLatch(1)
        com.sun.javafx.application.PlatformImpl.startup { // not an official API
            startParameter = new Stage()
            latch.countDown()
        }
        latch.await()
        def closure
        closure = { primaryStage ->
            assert primaryStage.is(startParameter)
            assert closure.delegate in SceneGraphBuilder
        }
        def application = new GroovyFX()
        application.stage = true
        application.closure = closure
        application.start(startParameter)
    }
}
