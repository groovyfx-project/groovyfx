/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
        Rectangle rect = new Rectangle(width: 10, height: 10, fill: Color.BLUE);
        rect.widthProperty().bind(this.widthProperty().divide(4));
        rect.heightProperty().bind(this.heightProperty().divide(4));
        return rect;
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
                onChange(property: "hover", action: {observable, oldValue, newValue -> println "hover: " + oldValue + " ==> " + newValue})
            }
        }
    }
});