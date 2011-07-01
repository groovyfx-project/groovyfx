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
        title: "Arc Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            arc(centerX: 100, centerY: 100,
                radiusX: 50, radiusY: 50,
                startAngle: -45, length: 180,
                fill: red, type: "chord")
        }
    }
});
