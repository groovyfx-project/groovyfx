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

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.paint.Color;
import groovyx.javafx.Trigger;

/**
 *
 * @author jimclarke
 */
GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX Trigger Demo", width: 600, height:450, visible: true) {
         scene(fill: groovyblue) {
             rect = rectangle(x: 25, y: 40, width: 200, height: 150, fill: red)
         }
    }

    new Trigger(rect, "hover", { rect.fill = rect.isHover() ? Color.GREEN : Color.RED })
}
