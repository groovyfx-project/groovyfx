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
        title: "AnchorPane Example (Groovy)",
        width: 650, height:450,
        visible: true,
    ) {
         scene(fill: lightgreen ) {
             anchorPane() {
                 button(text: "ONE", topAnchor: 10, bottomAnchor: 10,
                    rightAnchor: 110, leftAnchor: 10)
                 button(text: "TWO", rightAnchor: 10, topAnchor: 10)
             }
         }
    }
});


