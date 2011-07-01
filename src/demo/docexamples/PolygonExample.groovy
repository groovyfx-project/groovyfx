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
        title: "Polygon Example (Groovy)",
        width: 200, height:200,
        visible: true,
    ){ 
        scene (fill: white) {
            polygon(
                points: [ 0, -10, 10, 10, -10, 10, 0, -10],
                translateX: 100, translateY: 100,
                fill: red)
        }
    }
});
