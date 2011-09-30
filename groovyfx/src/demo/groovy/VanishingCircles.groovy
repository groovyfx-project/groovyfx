import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License")
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

/**
 * @author dean
 */
GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder()
    def rand = new Random().&nextInt
    def circles = []

    sg.stage(title: 'Vanishing Circles', show: true) {
        scene(fill: black, width: 800, height: 600) {
            50.times {
                circles << circle(centerX: rand(800), centerY: rand(600), radius: 150,
                                 stroke: white, strokeWidth: 0) {
                    fill rgb(rand(255), rand(255), rand(255), 0.2)
                    effect boxBlur(width: 10, height: 10, iterations: 3)
                    onMouseEntered { e -> e.source.strokeWidth = 4 }
                    onMouseExited { e -> e.source.strokeWidth = 0 }
                    onMouseClicked { e ->
                        timeline {
                            at(3.s) { change e.source.radiusProperty() to 0 }
                        }.play()
                    }
                }
            }
        }

        parallelTransition(cycleCount: 'indefinite', autoReverse: true) {
            circles.each { circle ->
                translateTransition(40.s, node: circle, toX: rand(800), toY: rand(600))
            }
        }.play()
    }
}
