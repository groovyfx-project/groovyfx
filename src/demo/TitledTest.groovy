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
    title: "TitledPane Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated"
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        vbox ( spacing: 10) {
            titledPane(id: "t1", ) {
                title() {
                    label(text: "Label 1")
                }
                content() {
                    label(text: "This is Label 1\n\nAnd there were a few empty lines just there!")
                }
            }
            titledPane(id: "t2") {
                title() {
                    label(text: "Label 2")
                }
                content() {
                    label(text: "This is Label 2\n\nAnd there were a few empty lines just there!")
                }
            }
            titledPane(id: "t3") {
                title() {
                    label(text: "Label 3")
                }
                // this is content
                label(text: "This is Label 3\n\nAnd there were a few empty lines just there!")
            }
        }
    }
}

});


