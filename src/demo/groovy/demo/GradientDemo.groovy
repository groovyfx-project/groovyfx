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

/**
 * A demo that shows the different ways you can define gradient fills in GroovyFX.
 *
 * @author Jim Clarke
 * @author Dean Iverson
 */
start {
    stage(title: "GroovyFX Gradient Demo", width: 1020, height: 450, visible: true) {
        scene {
            fill GROOVYBLUE

            def width = 240
            def height = 180

            tilePane(hgap: 10, vgap: 20, padding: 5) {
                rectangle(width: width, height: height, fill: linearGradient(stops: [GREEN, BLACK]))

                rectangle(width: width, height: height, fill: linearGradient(stops: [0.25: YELLOW, 1.0: BLUE])) {
                    effect dropShadow()
                }

                rectangle(width: width, height: height) {
                    fill linearGradient(start: [0, 0.4], end: [0, 0.6], stops: ["#ff0000", PINK])
                    effect dropShadow()
                }

                rectangle(width: width, height: height) {
                    fill linearGradient(endY: 0, stops: [RED, YELLOW, GREEN, CYAN, BLUE])
                }

                circle(radius: height / 2, fill: radialGradient(stops: [RED, DARKRED, BLACK]))

                circle(radius: height / 2,
                        fill: radialGradient(radius: 0.95, center: [0.5, 0.5], stops: [0: CYAN, 0.75: BLACK])) {
                    effect dropShadow()
                }

                circle(radius: height / 2) {
                    fill radialGradient(radius: 1, center: [0.5, 1], stops: [0: MAGENTA, 0.75: INDIGO, 1: BLACK])
                }

                circle(radius: height / 2) {
                    effect dropShadow()
                    fill radialGradient(radius: 0.6, center: [0.5, 0.5], focusDistance: 0.6, focusAngle: -65) {
                        stop(offset: 0.0, color: GOLD)
                        stop(offset: 0.3, color: DARKGOLDENROD)
                        stop(offset: 1.0, color: BLACK)
                    }
                }
            }
        }
    }
}
