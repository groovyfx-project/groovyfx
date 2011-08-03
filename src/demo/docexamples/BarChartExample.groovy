package demo.docexamples

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 * @author dean
 */
GroovyFX.start {
    def sg = new SceneGraphBuilder()
    sg.stage(width: 420, height: 320, visible: true) {
        scene {
            barChart(prefWidth: 400, prefHeight: 300) {
                series(name: 'Sales', data: ['Q1', 1534, 'Q2', 2698, 'Q3', 1945, 'Q4', 3156])
            }
        }
    }
}