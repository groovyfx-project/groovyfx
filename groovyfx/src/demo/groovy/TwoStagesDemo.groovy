import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
def sg = new SceneGraphBuilder();
Thread.startDaemon({ GroovyFX.start{} });
sg.defer {
	def stage1 = sg.stage(primary: false, x:100, y: 100, onShown: { println "Stage 1"}) {
		scene(width: 100, height: 100, fill:groovyblue)
        }
	stage1.show();
}

sg.defer {
	def stage2 = sg.stage(primary: false, x: 100, y: 400) {
            onShown { println "Stage 2" }
            scene(width: 100, height: 100, fill:red)
        }
	stage2.show()
}