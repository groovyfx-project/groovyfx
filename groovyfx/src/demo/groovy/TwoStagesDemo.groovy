import groovyx.javafx.GroovyFX;

GroovyFX.start {
    stage(x: 100, y: 100, onShown: { println "Stage 1" }, visible: true) {
        scene(width: 100, height: 100, fill: groovyblue)
    }
    stage(primary: false, x: 100, y: 400, visible: true) {
        onShown { println "Stage 2" }
        scene(width: 100, height: 100, fill: red)
    }
}