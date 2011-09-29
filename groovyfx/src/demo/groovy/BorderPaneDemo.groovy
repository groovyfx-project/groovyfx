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



import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX @ JavaOne", show: true) {
        scene(fill: groovyblue, width: 650, height:450) {
            borderPane {
                top(align: "center", margin: [10,0,10,0]) {
                  button("Top Button")
                }
                right(align: "center", margin: [0,10,0,1]) {
                  toggleButton("Right Toggle")
                }
                left(align: "center", margin: [0,10]) {
                  checkBox("Left Check")
                }
                bottom(align: "center", margin: 10) {
                  textField("Bottom TextField")
                }
                label("Center Label")
            }
        }
    }
}

