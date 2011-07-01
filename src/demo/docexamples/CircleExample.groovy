/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.docexamples

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start({
    def sg = new SceneGraphBuilder();

    sg.stage(
        title: "Circle Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            circle(centerX: 100, centerY: 100,
                radius: 50, fill: red)
        }
    }
});
