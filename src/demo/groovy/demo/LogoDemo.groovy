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
import groovyx.javafx.SceneGraphBuilder

import static groovyx.javafx.GroovyFX.start

/**
 * Dynamic painting and animation of the GroovyFX Logo. Adapts to available size.
 * Original conversion from png data by Gerrit Grunwald.
 * @author Dierk Koenig
 */
start {

    stage title: "GroovyFX Logo", x: 10, y: 10, visible: true, {
        scene(fill: GROOVYBLUE, width: 300, height: 300) {
            stackPane {
                rectangle x: 0, y: 0, width: 120, height: 120, opacity: 0d
                borderPane id: 'parent', {
                    group id: 'logo', {
                        transitions = parallelTransition()
                        star delegate, 12, [LIGHTGREEN, GREEN]*.brighter()
                        star delegate, 6, [LIGHTBLUE, BLUE]*.brighter()
                        star delegate, 0, [YELLOW, ORANGE]
                        fxLabel delegate
                        onMouseClicked { transitions.playFromStart() }
                    }
                }
            }
        }
    }

    parent.layoutBounds().onInvalidate {
        double newSize = [parent.width, parent.height].min() / 120d
        logo.scaleX = newSize
        logo.scaleY = newSize
    }
}

def star(SceneGraphBuilder builder, int angle, List stops) {
    builder.with {
        path(stroke: GROOVYBLUE, strokeWidth: 0.5, rotate: angle, opacity: 0.9) {
            fill linearGradient(start: [0, 0], end: [1, 1], stops: stops)
            moveTo x: 50, y: 00
            lineTo x: 61, y: 36
            lineTo x: 98, y: 36
            lineTo x: 68, y: 58
            lineTo x: 79, y: 94
            lineTo x: 50, y: 72
            lineTo x: 18, y: 94
            lineTo x: 30, y: 58
            lineTo x: 00, y: 36
            lineTo x: 36, y: 36
            lineTo x: 50, y: 00
            transitions.children << rotateTransition((2 + angle / 20d).s, from: angle, to: -360 + angle)
        }
    }
}

def fxLabel(SceneGraphBuilder builder) {
    builder.group(scaleX: 0.25, scaleY: 0.25, translateX: 6, translateY: 4) {
        path strokeWidth: 0d, {
            metal = fill radialGradient(stops: [GROOVYBLUE.brighter(), GROOVYBLUE.darker()])
            moveTo x: 64, y: 0
            lineTo x: 62, y: 17
            cubicCurveTo controlX1: 62, controlY1: 17, controlX2: 28, controlY2: 17, x: 28, y: 17
            cubicCurveTo controlX1: 21, controlY1: 17, controlX2: 20, controlY2: 44, x: 20, y: 44
            lineTo x: 47, y: 44
            lineTo x: 55, y: 60
            lineTo x: 18, y: 60
            lineTo x: 13, y: 100
            lineTo x: 0, y: 100
            cubicCurveTo controlX1: 0, controlY1: 100, controlX2: 12, controlY2: 19, x: 12, y: 19
            cubicCurveTo controlX1: 12, controlY1: 14, controlX2: 16, controlY2: 0, x: 28, y: 0
            cubicCurveTo controlX1: 28, controlY1: 0, controlX2: 64, controlY2: 0, x: 64, y: 0
        }
        path fill: metal, strokeWidth: 0d, {
            moveTo x: 46, y: 26
            lineTo x: 59, y: 26
            lineTo x: 70, y: 49
            lineTo x: 85, y: 26
            lineTo x: 100, y: 26
            lineTo x: 76, y: 62
            lineTo x: 93, y: 100
            lineTo x: 80, y: 100
            lineTo x: 68, y: 74
            lineTo x: 52, y: 100
            lineTo x: 37, y: 100
            lineTo x: 62, y: 60
            lineTo x: 46, y: 26
        }
        effect dropShadow(offsetX: 4, offsetY: 4, radius: 4)
        transitions.children <<
                fadeTransition(0.1.s, to: 0d) <<
                fadeTransition(1.s, delay: 1.8.s, to: 1d) <<
                scaleTransition(0.6.s, delay: 1.8.s, to: 0.5d, from: 0d) <<
                scaleTransition(0.6.s, delay: 2.4.s, to: 0.25d)
    }

}
