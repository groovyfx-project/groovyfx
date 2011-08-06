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
package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.animation.Transition

GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder(primaryStage)
    Transition rectTransition = null

    def playTransition = {
        rectTransition.playFromStart()
    }

    sg.stage(title: "GroovyGX Sequential Transition Demo", width: 400, height:300, visible: true, resizable: true) {
         scene(fill: groovyblue) {
             rectangle(x: 20, y: 20, width: 100, height: 50, fill: blue, onMousePressed: {playTransition()}) {
                rectTransition = sequentialTransition(delay: 100.ms, onFinished: {println "sequential done"}) {
                    translateTransition(5.s, interpolator: "ease_out", to: 100, onFinished: {println "translate done"})
                    pauseTransition(100.ms, onFinished: {println "pause done"})
                    rotateTransition(5.s,  interpolator: "linear", to: 180.0, onFinished: {println "rotate done"})
                    scaleTransition(5.s,  interpolator: "ease_in", to: 0.5, onFinished: {println "scale done"})
                    fillTransition(5.s,  interpolator: "ease_in", to: red, onFinished: {println "fill done"})
                }
             }
         }
    }
    playTransition()
}

