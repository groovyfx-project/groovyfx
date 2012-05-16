package structure

import groovyx.javafx.SceneGraphBuilder
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage

import static javafx.geometry.HPos.LEFT
import static javafx.geometry.HPos.RIGHT
import static javafx.scene.layout.Priority.ALWAYS

class DemoStyle {

    static style(SceneGraphBuilder sgb) {
        Stage frame = sgb.primaryStage
        Scene scene = frame.scene
        scene.stylesheets << 'demo.css'
//        def GROOVYBLUE = sgb.groovyblue
//        scene.fill = sgb.radialGradient(stops: [
//            GROOVYBLUE.brighter(),
//            GROOVYBLUE.darker()]
//        ).build() // a scene fill cannot be set via css atm

        GridPane grid = scene.root
        grid.styleClass << 'form'
        grid.hgap = 5  // for some reason, the gaps are not taken from the css atm
        grid.vgap = 10
        grid.columnConstraints << sgb.columnConstraints(halignment: RIGHT, hgrow: ALWAYS)
        grid.columnConstraints << sgb.columnConstraints(halignment: LEFT, hgrow: ALWAYS, prefWidth: 300)

        sgb.translateTransition(1.s, node: grid, fromY: -100, toY: 0).play()

    }
}
