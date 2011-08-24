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

    sg.stage(title: "GroovyFX ToolBar Demo", width: 820, height: 400, visible: true, style: "decorated") {
        scene(fill: groovyblue) {
            borderPane {
                top {
                    toolBar(orientation: "horizontal") {
                        button("button 1")
                        button("button 2")
                        button("button 3")
                        button("button 4")
                        button("button 5")
                        button("button 6")
                        button("button 7")
                        button("button 8")
                        button("button 9")
                        button("button 10")
                    }
                }
            }
        }
    }
}


