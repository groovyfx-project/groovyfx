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
package demo;

import groovyx.javafx.GroovyFX;
import groovyx.javafx.SceneGraphBuilder
import javafx.animation.Transition;

GroovyFX.start({ primaryStage -> 
    def sg = new SceneGraphBuilder(primaryStage);
    Transition rectTransition = null;
    sg.stage(
        title: "Path Transition Test",
        width: 400, height:300,
        visible: true,
        resizable: true
    ) {
         scene(fill: lightgreen) {
             def thePath  = path( translateX: 50, translateY: 50, fill: transparent, stroke: red, strokeWidth: 5) {
                moveTo(x: 0, y: 0)
                lineTo(x: 100, y: 0)
                arcTo(x: 200, y: 0, radiusX: 25, radiusY: 20)
                lineTo(x: 300, y: 0)
                lineTo(x: 150, y: 100)
                closePath()
             }
             rectangle(width: 20, height: 50, fill: blue) {
                rectTransition = pathTransition(20.s, delay: 100.ms, path: thePath,  onFinished: { println "done"}) 
             }
         }
    }
    rectTransition.playFromStart();
})

