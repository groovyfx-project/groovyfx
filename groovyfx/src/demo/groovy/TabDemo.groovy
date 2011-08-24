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

package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 *
 * @author jimclarke
 */

GroovyFX.start {
    def sg = new SceneGraphBuilder(it)

    sg.stage(title: "TabPane Example", x: 100, y: 100, width: 400, height: 400, visible: true, style: "decorated") {
        scene(fill: groovyblue) {
            tabPane {
                tab('Tab 1') {
                    label("This is Label 1\n\nAnd there were a few empty lines just there!")
                    graphic {
                        rectangle(width: 20, height: 20, fill: red)
                    }
                }
                tab('Tab 2') {
                    label("This is Label 2\n\nAnd there were a few empty lines just there!")
                    graphic {
                        rectangle(width: 20, height: 20, fill: blue)
                    }
                }
                tab('Tab 3') {
                    label("This is Label 3\n\nAnd there were a few empty lines just there!")
                    graphic {
                        rectangle(width: 20, height: 20, fill: green)
                    }
                }
            }
        }
    }
}


