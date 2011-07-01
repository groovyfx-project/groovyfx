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
        title: "Path Example (Groovy)",
        width: 400, height:250,
        visible: true,
    ){ 
        scene (fill: white) {
            path( translateX: 50, translateY: 50, stroke: red) {
                moveTo(x: 25, y: 50)
                hLineTo(x: 250)
                vLineTo(y: 150)
            }
        }
    }
});
