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
        title: "Line Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            line(startX: 10, startY: 10,
                endX: 150, endY: 150,
                stroke: red, strokeWidth: 2)
        }
    }
});
