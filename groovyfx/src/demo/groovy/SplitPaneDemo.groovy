/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License")
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



import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 *
 * @author jimclarke
 */

GroovyFX.start {
    def sg = new SceneGraphBuilder(it)

    sg.stage(title: "GroovyfX, SplitPane Demo", x: 100, y: 100, width: 800, height: 400, visible: true,
             style: "decorated") {

        scene(fill: groovyblue) {
            splitPane(orientation: "horizontal", prefHeight: 200) {
                dividerPosition(index: 0, position: 0.25)
                dividerPosition(index: 1, position: 0.50)
                dividerPosition(index: 2, position: 1.0)
                label("This is Label 1\n\nAnd there were a few empty lines just there!") // left or top
                label("This is Label 2\n\nAnd there were a few empty lines just there!") // right or bottom
                label("This is Label 3\n\nAnd there were a few empty lines just there!")
            }
        }
    }
}


