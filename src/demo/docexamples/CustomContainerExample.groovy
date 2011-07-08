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