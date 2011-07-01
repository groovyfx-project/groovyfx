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
        title: "CubicCurve Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            cubicCurve(startX: 10, startY: 10,
                endX: 100, endY: 100,
                controlX1: 40, controlY1: 0,
                controlX2: 50, controlY2: 110,
                stroke: red, strokeWidth: 2, fill: null)
        }
    }
});
