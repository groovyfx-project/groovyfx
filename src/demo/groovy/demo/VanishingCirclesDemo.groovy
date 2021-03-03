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
    def rand = new Random().&nextInt
    def circles = []

    stage(title: 'Vanishing Circles', show: true) {
        scene(fill: BLACK, width: 800, height: 600) {
            50.times {
                circles << circle(centerX: rand(800), centerY: rand(600), radius: 150, stroke: WHITE,
                        strokeWidth: bind('hover', converter: {val -> val ? 4 : 0})) {
                    fill rgb(rand(255), rand(255), rand(255), 0.2)
                    effect boxBlur(width: 10, height: 10, iterations: 3)
                    onMouseClicked { e ->
                        timeline {
                            at(3.s) { change e.source.radiusProperty() to 0 }
                        }.play()
                    }
                }
            }
        }

        parallelTransition(cycleCount: INDEFINITE, autoReverse: true) {
            circles.each { circle ->
                translateTransition(40.s, node: circle, toX: rand(800), toY: rand(600))
            }
        }.play()
    }
}
