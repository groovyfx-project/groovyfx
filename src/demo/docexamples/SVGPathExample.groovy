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
        title: "SVGPath Example (Groovy)",
        width: 400, height:250,
        visible: true,
    ){ 
        scene (fill: white) {
            SVGPath(
                content: "M0,0 L100,0 A25,20 0 0,0 200,0 L300,0 150,100z",
                translateX: 50, translateY: 50,
                fill: transparent,
                stroke: red, strokeWidth: 2)
        }
    }
});
