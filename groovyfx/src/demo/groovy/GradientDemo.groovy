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



import groovyx.javafx.GroovyFX;
import groovyx.javafx.SceneGraphBuilder

/**
 * A demo that shows the different ways you can define gradient fills in GroovyFX.
 * 
 * @author Jim Clarke
 * @author Dean Iverson
 */
GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX Gradient Demo", width: 1020, height: 450, visible: true) {
        scene {
            fill groovyblue

            def width = 240
            def height = 180

            tilePane(hgap: 10, vgap: 20, padding: 5) {
                rectangle(width: width, height: height, fill: linearGradient(stops: [green, black]))

                rectangle(width: width, height: height, fill: linearGradient(stops: [[0.25, yellow], [1.0, blue]])) {
                    effect dropShadow()
                }

                rectangle(width: width, height: height) {
                    fill linearGradient(start: [0, 0.4], end: [0, 0.6], stops: [red, pink])
                    effect dropShadow()
                }

                rectangle(width: width, height: height) {
                    fill linearGradient(endY: 0, stops: [red, yellow, green, cyan, blue])
                }

                circle(radius: height / 2, fill: radialGradient(stops: [red, darkred, black]))

                circle(radius: height / 2,
                       fill: radialGradient(radius: 0.95, center:[0.5, 0.5], stops:[[0, cyan], [0.75, black]])) {
                    effect dropShadow()
                }

                circle(radius: height / 2) {
                    fill radialGradient(radius: 1, center:[0.5, 1], stops:[[0, magenta], [0.75, indigo], [1, black]])
                }

                circle(radius: height / 2) {
                    effect dropShadow()
                    fill radialGradient(radius: 0.6, center:[0.5, 0.5], focusDistance: 0.6, focusAngle: -65) {
                        stop(offset: 0.0, color: gold)
                        stop(offset: 0.3, color: darkgoldenrod)
                        stop(offset: 1.0, color: black)
                    }
                }
            }
        }
    }
}
