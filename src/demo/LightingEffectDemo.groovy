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
    x: 100, y: 100, width: 500, height:300,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        text(x: 10, y: 10, content: "Light Effect", font: "bold 90pt", fill: red, textOrigin: "top") {
            lighting(surfaceScale: 5.0) {
                //distant(azimuth: -135)
                //spot(x: 0, y: 100, z: 50, pointsAtX: 100, pointsAtY: 0, pointsAtZ: 0, specularExponent: 2)
                point(x: -100, y: -100, z: 50 )
                /****
                bumpInput() {
                    dropShadow()
                }
                contentInput() {
                    glow()
                }
                ****/
            }
        }
    }
}

});


