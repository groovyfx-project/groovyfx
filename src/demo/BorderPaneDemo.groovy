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
import javafx.scene.paint.Color;
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.control.*;

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX BorderPane Demo", width: 650, height:450, visible: true) {
         scene(fill: groovyblue) {
             borderPane {
                 top(align: "center", margin: [10,0,10,0]) {
                     button("Top")
                 }
                 right(align: "center", margin: [0,10,0,1]) {
                     button("Right")
                 }
                 left(align: "center", margin: [0,10]) {
                     button("Left")
                 }
                 bottom(align: "center", margin: [10,0]) {
                     button("Bottom")
                 }
                 label("Center")
             }
         }
    }
}

