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

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start {
    def sg = new SceneGraphBuilder(it)
    def lX = 100.0;
    def lY = 100.0;
    def s = 2;
    def r = 90;
    sg.stage(title: "GroovyFX Timeline Demo", width: 200, height: 200, visible: true, resizable: true) {
        scene(fill: groovyblue) {
            map = sg.circle(radius: 25) {
                fill(red)
            }
        }

        timeline(cycleCount: indefinite, autoReverse: true) {
            at(800.ms) {
                change(map, 'layoutX') to lX tween easeboth
                change(map, 'layoutY') to lY tween easeboth
                change(map, 'scaleX') to s
                change(map, 'scaleY') to s
                change(map, 'rotate') to r
            }
        }.play()
    }
}


