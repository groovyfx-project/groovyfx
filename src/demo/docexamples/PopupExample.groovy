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