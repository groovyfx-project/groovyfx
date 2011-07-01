/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.docexamples

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
/**
 *
 * @author jimclarke
 */

GroovyFX.start({
def sg = new SceneGraphBuilder(it);

def popup = null;
popup = sg.popup(autoHide: true) {
    stackPane() {
        rectangle(width: 200, height: 200, fill: blue, 
            stroke: cyan, strokeWidth: 5, arcHeight: 20, arcWidth: 20)
        button( text: "OK", onAction: {popup.hide()})
    }
    
}

def stage = sg.stage(
    title: "Popup Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        stackPane() {
            button(text: "Show Popup", onAction: { popup.show(stage) })
        }
    }
}
});