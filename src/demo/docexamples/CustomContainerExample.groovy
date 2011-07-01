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
import javafx.geometry.HPos;
import javafx.geometry.VPos;

class SimpleContainer extends Region {

    public SimpleContainer() {
    }
    
    @Override
    protected void layoutChildren() {
        List<Node> managed = getManagedChildren();
        def width = getWidth();
        def height = getHeight();
        def partWidth = width/managed.size();
        
        def x = 0
        for(n in managed) {
            layoutInArea(n, x, 0, partWidth, height, 0,
                HPos.CENTER, VPos.CENTER);
            x += partWidth;
        }
    }

}

GroovyFX.start({
    def sg = new SceneGraphBuilder();
    sg.stage(
        title: "Custom Container example",
        x: 100, y: 100, width: 200, height:200,
        visible: true,
        style: "decorated",
        onHidden: { println "Close"}
    ) {
        
        scene(fill: hsb(128, 0.5, 0.5, 0.5)) {
            container(new SimpleContainer()) {
                button(text: "one")
                button(text: "two")
                button(text: "three")
            }
        }
    }
});