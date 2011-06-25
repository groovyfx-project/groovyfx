/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.GroovyFX
import javafx.scene.paint.Color;
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.control.*;

GroovyFX.start({
    def sg = new SceneGraphBuilder();

    sg.stage(
        title: "BorderPane Example (Groovy)",
        width: 650, height:450,
        visible: true,
    ) {
         scene(fill: lightgreen ) {
             borderPane() {
                 top(align: "center", margin: [10,0,10,0]) {
                     button(text: "top")
                 }
                 right(align: "center", margin: [0,10,0,1]) {
                     button(text: "right")
                 }
                 left(align: "center", margin: [0,10]) {
                     button(text: "left")
                 }
                 bottom(align: "center", margin: [10,0]) {
                     button(text: "bottom")
                 }
                 center(align: "center") {
                     label(text: "center")
                 }
             }
         }
    }
});

