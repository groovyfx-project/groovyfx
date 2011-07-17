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

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
/**
 *
 * @author jimclarke
 */

GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder(primaryStage);
    
    sg.stage(title: "Blend Effect Demo", width: 420, height: 420, visible: true ) {
        scene(fill: hsb(128, 1.0, 1.0, 0.5) ) {
            rectangle(width: 400, height: 400) {
                onMousePressed (onEvent: {e -> println "mouse press @" + e.x + "," + e.y })
                blend(mode: "multiply") {
                    topInput() {
                        colorInput(paint: blue, x: 50, y: 50, width: 200, height: 200)
                    }
                    bottomInput() {
                        colorInput(paint: red, x: 150, y: 150, width: 200, height: 200)
                    }
                }
            }
        }
    }
}


