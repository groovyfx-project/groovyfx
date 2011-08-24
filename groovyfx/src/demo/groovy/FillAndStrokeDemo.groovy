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
    sg.stage(title: "GroovyFX Fill & Stroke Demo", width: 600, height: 700, visible: true) {
        scene {
            fill groovyblue
            
            def width = 240
            def height = 180

            tilePane(hgap: 10, vgap: 20, padding: 10) {
                def redToBlack = linearGradient(stops: [[0, red], [1, black]])
                def blackToRed = linearGradient(stops: [[0, black], [1, red]])

                rectangle(width: width, height: height, strokeWidth: 10, fill: green, stroke: rgb(0, 255, 0))

                rectangle(width: width, height: height, strokeWidth: 10) {
                    fill("#00FF00")
                    stroke("#008000")
                }

                rectangle(width: width, height: height, strokeWidth: 10, stroke: indigo,
                        fill: linearGradient(stops: [[0, violet], [1, purple]]))

                rectangle(width: width, height: height, strokeWidth: 10, fill: redToBlack, stroke: blackToRed)

                circle(radius: height/2, strokeWidth: 10) {
                    fill linearGradient(stops: [[0, orange], [1, black]])
                    stroke radialGradient(center: [0.5, 0.5], stops: [[0, black], [1, orange]])
                }

                circle(radius: height/2, strokeWidth: 10) {
                    fill(blackToRed)
                    stroke(redToBlack)
                }
            }
        }
    }
}