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
            path( translateX: 50, translateY: 50, fill: red) {
                moveTo(x: 0, y: 0)
                lineTo(x: 100, y: 0)
                arcTo(x: 200, y: 0, radiusX: 25, radiusY: 20)
                lineTo(x: 300, y: 0)
                lineTo(x: 150, y: 100)
                closePath()
            }
        }
    }
});
