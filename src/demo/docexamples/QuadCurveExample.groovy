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
        title: "QuadCurve Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            quadCurve(startX: 10, startY: 10,
                endX: 100, endY: 100,
                controlX: 75, controlY: 0,
                stroke: red, strokeWidth: 2, fill: null)
        }
    }
});
