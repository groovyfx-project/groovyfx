/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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
import static groovyx.javafx.GroovyFX.start

start {
    stage(title: "GridPane Demo", width: 400, height: 500, visible: true) {
        scene(fill: GROOVYBLUE) {
            gridPane(hgap: 5, vgap: 10, padding: 25, alignment: "top_center") {
                columnConstraints(minWidth: 50, halignment: "right")
                columnConstraints(prefWidth: 250, hgrow: 'always')

                label("Please Send Us Your Feedback", style: "-fx-font-size: 18px;", textFill: WHITE,
                        row: 0, columnSpan: 2, halignment: "center", margin: [0, 0, 10]) {
                    onMouseEntered { e -> e.source.parent.gridLinesVisible = true }
                    onMouseExited { e -> e.source.parent.gridLinesVisible = false }
                }

                label("Name", hgrow: "never", row: 1, column: 0, textFill: WHITE)
                textField(promptText: "Your name", row: 1, column: 1)

                label("Email", row: 2, column: 0, textFill: WHITE)
                textField(promptText: "Your email address", row: 2, column: 1)

                label("Message", row: 3, column: 0, valignment: "baseline", textFill: WHITE)
                textArea(prefRowCount: 8, row: 3, column: 1, vgrow: 'always')

                button("Send Message", row: 4, column: 1, halignment: "right")
            }
        }
    }
}
