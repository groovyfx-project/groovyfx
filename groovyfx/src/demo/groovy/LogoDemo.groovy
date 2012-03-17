/*
* Copyright  201-12 the original author or authors.
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

import groovyx.javafx.Trigger
import javafx.event.EventHandler

import static groovyx.javafx.GroovyFX.start
import groovyx.javafx.SceneGraphBuilder

/**
 * Dynamic painting and animation of the GroovyFX Logo. Adapts to available size.
 * Original conversion from png data by Gerrit Grunwald.
 * @author Dierk Koenig
 */
start {

    stage title: "Logo Self Demo", x: 10, y: 10, visible: true, {
        scene {
            stackPane {
                rectangle(x:0, y:0, width: 120, height: 120, opacity: 0d)
                borderPane id: 'parent', {
                    group id: 'logo', {
                        rotates = []
                        star delegate, 12, [lightgreen.brighter(), green.brighter()]
                        star delegate,  6, [lightblue.brighter(),  blue.brighter()]
                        star delegate,  0, [yellow, orange]
                        fx = fxLabel delegate
    }   }   }   }   }

    logo.onMouseClicked = {
        def transitions = parallelTransition()
        transitions.children.addAll rotates
        transitions.children << fxFade << fxAppear << fxBigger << fxSmaller
        transitions.playFromStart()
    } as EventHandler

    new Trigger(parent, "layoutBounds", {
        double newSize = [parent.width, parent.height].min() / 120d
        logo.scaleX = newSize
        logo.scaleY = newSize
    })

}

def star(SceneGraphBuilder builder, int angle, List stops) {
    builder.with {
        path(stroke: groovyblue, strokeWidth: 0.5, rotate: angle, opacity: 0.9) {
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
            rotates << rotateTransition((2+angle/20d).s, from: angle, to: -360 + angle)
        }
    }
}

def fxLabel(SceneGraphBuilder builder) {
    builder.group {
        path strokeWidth: 0d, {
            metal = fill radialGradient(stops: [groovyblue.brighter(), groovyblue.darker()])
            moveTo x: 59, y: 46
            lineTo x: 59, y: 49
            lineTo x: 49, y: 49
            cubicCurveTo controlX1: 47, controlY1: 49, controlX2: 46, controlY2: 52, x: 46, y: 54
            cubicCurveTo controlX1: 50, controlY1: 55, controlX2: 54, controlY2: 53, x: 56, y: 57
            lineTo x: 46, y: 57
            cubicCurveTo controlX1: 45, controlY1: 60, controlX2: 45, controlY2: 62, x: 44, y: 65
            lineTo x: 41, y: 65
            lineTo x: 44, y: 51
            cubicCurveTo controlX1: 45, controlY1: 46, controlX2: 46, controlY2: 46, x: 49, y: 46
            lineTo x: 59, y: 46
        }
        path fill: metal, strokeWidth: 0d, {
            moveTo x: 58, y: 52
            lineTo x: 61, y: 56
            lineTo x: 65, y: 52
            lineTo x: 68, y: 52
            lineTo x: 62, y: 58
            lineTo x: 67, y: 65
            lineTo x: 64, y: 65
            lineTo x: 61, y: 60
            lineTo x: 56, y: 65
            lineTo x: 53, y: 65
            lineTo x: 59, y: 58
            lineTo x: 55, y: 52
            lineTo x: 58, y: 52
        }
        effect dropShadow(offsetX: 2, offsetY: 2, radius: 3)
        fxFade    = fadeTransition(0.1.s, to: 0d)
        fxAppear  = fadeTransition(1.s,  delay:1.8.s, to: 1d)
        fxBigger  = scaleTransition(0.6.s, delay: 1.8.s, from:0d, to: 2d)
        fxSmaller = scaleTransition(0.6.s, delay: 2.4.s, to: 1d)
    }
}