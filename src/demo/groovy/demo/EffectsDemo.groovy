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
import javafx.scene.effect.DisplacementMap
import javafx.scene.effect.FloatMap

import static groovyx.javafx.GroovyFX.start

DisplacementMap displacementMap(int w, int h) {
    FloatMap map = new FloatMap()
    map.setWidth(w)
    map.setHeight(h)

    for (int i = 0; i < w; i++) {
        double v = (Math.sin(i / 50.0 * Math.PI) - 0.5) / 40.0
        for (int j = 0; j < h; j++) {
            map.setSamples(i, j, 0.0f, (float) v)
        }
    }

    DisplacementMap dm = new DisplacementMap()
    dm.setMapData(map)

    return dm
}

// argument (it) is the primaryStage
start {
    stage(title: "GroovyFX Effects Demo", visible: true) {
        scene(root: group(), width: 840, height: 680, fill: GROOVYBLUE) {
            /// Perspective
            group(cache: true, layoutX: 0, layoutY: 0) {
                rectangle(x: 10, y: 10, width: 280, height: 80, fill: BLUE)
                text(x: 20, y: 65, text: "Perspective", fill: YELLOW, font: "bold 36pt C")
                effect perspectiveTransform(ulx: 10, uly: 10, urx: 310, ury: 40, lrx: 310, lry: 60, llx: 10, lly: 60)
            }

            /// DropShadow
            group(layoutX: 0, layoutY: 270) {
                text(cache: true, x: 10, y: 0, fill: RED, text: "JavaFX drop shadow...", font: "bold 32pt Amble") {
                    effect dropShadow(offsetY: 3, color: [0.4, 0.4, 0.4])
                }
                circle(centerX: 50, centerY: 55, radius: 30, fill: ORANGE, cache: true) {
                    effect dropShadow(offsetY: 4)
                }
            }

            /// BlendMode
            group(blendMode: "multiply") {
                rectangle(x: 290, y: 50, width: 50, height: 50, fill: BLUE)
                circle(fill: "rgba(255, 00, 0, 0.5)", centerX: 290, centerY: 50, radius: 25)
            }

            /// Bloom
            group(cache: true, translateX: 350) {
                rectangle(x: 10, y: 10, width: 160, height: 80, fill: DARKBLUE)
                text(text: "Bloom!", fill: YELLOW, font: "bold 36pt Amble", x: 25, y: 65)
                effect bloom()
            }

            text(text: "ColorAdjust Text!", fill: RED, font: "bold 20pt Amble", x: 10, y: 10, textOrigin: TOP) {
                colorAdjust(contrast: 4, brightness: -0.8, hue: 0.2)
            }

            /// BoxBlur
            text(text: "Blurry Text!", fill: RED, font: "bold 36pt Amble", x: 10, y: 40,
                    translateX: 300, translateY: 100) {
                effect boxBlur(width: 15, height: 15, iterations: 3)
            }

            /// DisplacementMap
            group(effect: displacementMap(220, 100), translateX: 300, translateY: 200) {
                rectangle(x: 20, y: 20, width: 220, height: 100, fill: BLUE)
                text(x: 40, y: 80, text: "Wavy Text", fill: YELLOW, font: "bold 36pt Amble")
            }

            /// InnerShadow
            text(x: 20, y: 100, text: "InnerShadow", fill: YELLOW, font: "bold 80pt Amble",
                    translateX: 300, translateY: 300) {
                effect innerShadow(offsetX: 4, offsetY: 4)
            }

            /// Lighting
            text(text: "Lighting!", fill: RED, font: "bold 70pt Amble",
                    x: 10, y: 10, textOrigin: TOP, translateX: 0, translateY: 320) {
                effect lighting(surfaceScale: 5.0) {
                    distant(azimuth: -135.0)
                }
            }

            /// MotionBlur
            text(x: 20, y: 100, text: "Motion", fill: RED, font: "bold 60pt Amble",
                    translateX: 300, translateY: 150) {
                effect motionBlur(radius: 15, angle: -30)
            }

            /// Reflection
            text(translateY: 400, x: 10, y: 50, cache: true,
                    text: "Reflections on JavaFX...", fill: RED, font: "bold 30pt Amble") {
                effect reflection(fraction: 0.7)
            }

            /// GaussianBlur
            text(x: 10, y: 140, cache: true, text: "Blurry Text", fill: RED, font: "bold 36pt Amble") {
                effect gaussianBlur()
            }

            /// ColorInput
            text(text: "Some Text!", fill: RED, font: "bold 36pt Amble", x: 10, y: 200, textOrigin: TOP) {
                effect colorInput(paint: BLUE, x: 55, y: 205, width: 50, height: 20)
            }

            /// Distant Light
            group(translateY: 460) {
                rectangle(fill: "black")
                text(text: "Distant Light", fill: RED, font: "bold 70pt Amble", x: 10, y: 10, textOrigin: TOP) {
                    effect lighting(surfaceScale: 5.0) {
                        distant(azimuth: -135.0, elevation: 30.0)
                    }
                }
            }
        }
    }
}
