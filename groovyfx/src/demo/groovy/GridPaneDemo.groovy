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
import javafx.scene.layout.GridPane

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX GridPane Demo", width: 400, height: 500, visible: true) {
        scene(fill: groovyblue) {
            gridPane(hgap: 5, vgap: 10, padding: 25, alignment: "top_center") {
                columnConstraints(minWidth: 50, halignment: "right")
                columnConstraints(prefWidth: 250)

                label("Send Us Your Feedback", font: "24pt sanserif", row: 0, columnSpan: GridPane.REMAINING,
                      halignment: "center", margin: [0, 0, 10]) {
                    onMouseEntered { e -> e.source.parent.gridLinesVisible = true }
                    onMouseExited { e -> e.source.parent.gridLinesVisible = false }
                }

                label("Name: ", hgrow: "never", row: 1, column: 0, halignment: "right")
                textField(promptText: "Your name", row: 1, column: 1, hgrow: 'always')

                label("Email:", row: 2, column: 0, halignment: "right")
                textField(promptText: "Your email address", row: 2, column: 1, hgrow: 'always')

                label("Message:", row: 3, column: 0, halignment: "right", valignment: "baseline")
                textArea(prefRowCount: 8, row: 3, column: 1, hgrow: 'always', vgrow: 'always')

                button("Send Message", row: 4, column: 1, halignment: "right")
            }
        }
    }
}
