package groovyx.javafx.beans

class FXBindableTest extends GroovyTestCase {

    void testSimpleBinding() {
        def source = new TestBean()
        def target = new TestBean()
        assert source.name == null
        assert target.name == null
        
        source.name = "one"
        assert source.name == "one"
        assert target.name == null
        
        target.nameProperty().bind(source.nameProperty())
        assert source.name == "one"
        assert target.name == "one" // is immediately updated with the bind()
        
        source.name = "two"
        assert source.name == "two"
        assert target.name == "two"
    }

}

@FXBindable
class TestBean {
    String name
}
