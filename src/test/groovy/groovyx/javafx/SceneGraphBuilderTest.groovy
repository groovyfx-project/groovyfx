package groovyx.javafx

class SceneGraphBuilderTest extends GroovyTestCase {

    void testId() {
        def sgb = new SceneGraphBuilder()
        def expected = sgb.label id:'myLabel'
        assert expected == sgb.myLabel
        assert expected.id == 'myLabel'
    }
    
}
