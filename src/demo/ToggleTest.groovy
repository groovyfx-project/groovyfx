/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start({
    def sg = new SceneGraphBuilder();
    sg.stage(
        title: "Stage Frame",
        x: 100, y: 100, width: 400, height:400,
        visible: true,
        style: "decorated",
        onHidden: { println "Close"}
    ) {
        
        scene(fill: hsb(128, 0.5, 0.5, 0.5), parent: group()) {
            toggleButton (
                layoutX: 25,
                layoutY: 300,
                font: "16pt Courier",
                text: "One",
                selected: true,
                toggleGroup: "Group1"
            )
            toggleButton (
                layoutX: 125,
                layoutY: 300,
                font: "16pt Courier",
                text: "Two",
                selected: true,
                toggleGroup: "Group1"
            )

        }
    }
});
