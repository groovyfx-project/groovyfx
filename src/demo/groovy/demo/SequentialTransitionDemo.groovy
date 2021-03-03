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
import javafx.animation.Transition

import static groovyx.javafx.GroovyFX.start

start {
    Transition rectTransition = null

    def playTransition = {
        rectTransition.playFromStart()
    }

    stage(title: "GroovyGX Sequential Transition Demo", width: 400, height: 300, visible: true, resizable: true) {
        scene(fill: GROOVYBLUE) {
            rectangle(x: 20, y: 20, width: 100, height: 50, fill: BLUE, onMousePressed: {playTransition()}) {
                rectTransition = sequentialTransition(delay: 100.ms, onFinished: {println "sequential done"}) {
                    translateTransition(2.s, interpolator: EASE_OUT, to: 100, onFinished: {println "translate done"})
                    pauseTransition(500.ms, onFinished: {println "pause done"})
                    rotateTransition(2.s, interpolator: LINEAR, to: 180.0, onFinished: {println "rotate done"})
                    scaleTransition(2.s, interpolator: EASE_IN, to: 0.5, onFinished: {println "scale done"})
                    fillTransition(2.s, interpolator: EASE_IN, to: RED, onFinished: {println "fill done"})
                }
            }
        }
    }
    playTransition()
}

