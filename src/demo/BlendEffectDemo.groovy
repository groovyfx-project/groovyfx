/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
/**
 *
 * @author jimclarke
 */

GroovyFX.start({
def sg = new SceneGraphBuilder(it);

sg.stage(
    title: "Blend Effect Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        rectangle(x: 20, y: 20,width: 100, height: 100, fill: red) {
            blend(mode: "multiply") {
                topInput() {
                    colorInput(paint: blue, x: 10, y: 20, width: 200, height: 200)
                }
                bottomInput() {
                    colorInput(paint: red, x: 5, y: 15, width: 205, height: 205)
                }
            }
        }
    }
}

});


