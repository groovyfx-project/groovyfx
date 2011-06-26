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
    title: "TabPane Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        tabPane ( ) {
            tab(text: 'Tab 1') {
                label(text: "This is Label 1\n\nAnd there were a few empty lines just there!")
                graphic() {
                    rectangle(width: 20, height: 20, fill: red)
                }
            }
            tab(text: 'Tab 2') {
                label(text: "This is Label 2\n\nAnd there were a few empty lines just there!")
                graphic() {
                    rectangle(width: 20, height: 20, fill: blue)
                }
            }
            tab(text: 'Tab 3') {
                label(text: "This is Label 3\n\nAnd there were a few empty lines just there!")
                graphic() {
                    rectangle(width: 20, height: 20, fill: green)
                }
            }
        }
    }
}

});


