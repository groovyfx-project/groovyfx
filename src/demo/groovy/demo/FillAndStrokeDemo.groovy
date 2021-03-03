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
    stage(title: "GroovyFX Fill & Stroke Demo", width: 600, height: 700, visible: true) {
        scene {
            fill GROOVYBLUE

            def width = 240
            def height = 180

            tilePane(hgap: 10, vgap: 20, padding: 10) {
                def redToBlack = linearGradient(stops: [[0, RED], [1, BLACK]])
                def blackToRed = linearGradient(stops: [[0, BLACK], [1, RED]])

                rectangle(width: width, height: height, strokeWidth: 10, fill: GREEN, stroke: rgb(0, 255, 0))

                rectangle(width: width, height: height, strokeWidth: 10) {
                    fill("#00FF00")
                    stroke("#008000")
                }

                rectangle(width: width, height: height, strokeWidth: 10, stroke: INDIGO,
                        fill: linearGradient(stops: [[0, VIOLET], [1, PURPLE]]))

                rectangle(width: width, height: height, strokeWidth: 10, fill: redToBlack, stroke: blackToRed)

                circle(radius: height / 2, strokeWidth: 10) {
                    fill linearGradient(stops: [[0, ORANGE], [1, BLACK]])
                    stroke radialGradient(center: [0.5, 0.5], stops: [[0, BLACK], [1, ORANGE]])
                }

                circle(radius: height / 2, strokeWidth: 10) {
                    fill(blackToRed)
                    stroke(redToBlack)
                }
            }
        }
    }
}
