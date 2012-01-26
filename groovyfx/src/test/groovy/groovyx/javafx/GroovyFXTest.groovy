package groovyx.javafx

import javafx.stage.Stage;

public class GroovyFXTest extends GroovyTestCase {

    // todo dk: this will System.exit!
    void testBuildSetsDelegate() {
        def closure
        closure = { primaryStage ->
            assert primaryStage in Stage
            assert closure.delegate in SceneGraphBuilder
            def stage = stage visible:false
            stage.close() // closing the last window stops the application
        }
        GroovyFX.build closure
    }
}
