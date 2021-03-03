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
    stage(title: "GroovyFX Padding Demo", width: 800, height: 800, visible: true) {
        scene(fill: GROOVYBLUE) {
            vbox(spacing: 30, fillWidth: false, alignment: "center") {
                stackPane(style: "-fx-background-color: burlywood", padding: 20) {
                    text("Padding: 20", fill: SIENNA, font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [10]) {
                    text("Padding: [10]", fill: SIENNA, font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [50, 25]) {
                    text("Padding: [50, 25]", fill: SIENNA, font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [10, 20, -10]) {
                    text("Padding: [10, 20, -10]", fill: SIENNA, font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [0, 50, 0, 0]) {
                    text("Padding: [0, 50, 0, 0]", fill: SIENNA, font: "48pt")
                }
                stackPane(style: "-fx-background-color: burlywood", padding: [20, 0, 0, 20]) {
                    text("Padding: [20, 0, 0, 20]", fill: SIENNA, font: "48pt")
                }
                effect dropShadow()
            }
        }
    }
}

