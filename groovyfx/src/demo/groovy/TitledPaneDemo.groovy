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

    sg.stage(title: "GroovyFX TitledPane Demo", width: 400, height: 400, visible: true, style: "decorated") {
        scene(fill: groovyblue) {
            vbox(spacing: 10) {
                titledPane(id: "t1") {
                    title {
                        label("Label 1")
                    }
                    content {
                        label("This is Label 1\n\nAnd there were a few empty lines just there!")
                    }
                }
                titledPane(id: "t2") {
                    title {
                        label("Label 2")
                    }
                    content {
                        label(text: "This is Label 2\n\nAnd there were a few empty lines just there!")
                    }
                }
                titledPane(id: "t3") {
                    title {
                        label("Label 3")
                    }
                    // this is content
                    label("This is Label 3\n\nAnd there were a few empty lines just there!")
                }
            }
        }
    }
}


