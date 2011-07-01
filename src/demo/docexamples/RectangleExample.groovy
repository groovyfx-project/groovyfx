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
        title: "Rectangle Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            rectangle( x: 25, y: 25, width: 150, height: 75,
                arcWidth: 20, arcHeight: 20,
               fill: blue, stroke: red, strokeWidth: 2)
        }
    }
});
