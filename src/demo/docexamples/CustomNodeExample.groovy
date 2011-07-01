/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.docexamples

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;

class MyCustomNode extends Region {

    public MyCustomNode() {
        getChildren().add(create());
    }

    protected javafx.scene.Node create() {
        return new Rectangle(width: 10, height: 10, fill: Color.BLUE);
    }
}

GroovyFX.start({
    def sg = new SceneGraphBuilder();
    sg.stage(
        title: "Custom Node example",
        x: 100, y: 100, width: 200, height:200,
        visible: true,
        style: "decorated",
        onHidden: { println "Close"}
    ) {
        
        scene(fill: hsb(128, 0.5, 0.5, 0.5)) {
            node(new MyCustomNode(), layoutX: 10, layoutY: 10) {
                scale(x: 5, y: 5)
                onChange(property: "hover", changed: {observable, oldValue, newValue -> println "hover: " + oldValue + " ==> " + newValue})
            }
        }
    }
});