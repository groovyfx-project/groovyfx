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
        title: "Polyline Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            polyline(
                points: [ 0, -50, 50, 50, -50, 50, 0, 0],
                translateX: 100, translateY: 100,
                stroke: red, strokeWidth: 2)
        }
    }
});
