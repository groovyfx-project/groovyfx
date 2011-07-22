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
import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;

/**
 * @author Dean Iverson
 */

GroovyFX.start({
    def sg = new SceneGraphBuilder()

    sg.stage(title: "Padding Test", width: 800, height: 800, visible: true) {
        scene(fill: "antiquewhite" ) {
            vbox(spacing: 30, fillWidth: false, alignment: "center") {
                stackPane(style: "-fx-background-color: burlywood", padding: 20) {
                    text(text: "Padding: 20", fill: "sienna", font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [10]) {
                    text(text: "Padding: [10]", fill: "sienna", font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [50, 25]) {
                    text(text: "Padding: [50, 25]", fill: "sienna", font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [10, 20, -10]) {
                    text(text: "Padding: [10, 20, -10]", fill: "sienna", font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [0, 50, 0, 0]) {
                    text(text: "Padding: [0, 50, 0, 0]", fill: "sienna", font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [20, 0, 0, 20]) {
                    text(text: "Padding: [20, 0, 0, 20]", fill: "sienna", font: "48pt")
                }
                dropShadow()
            }
        }
    }
})

