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


import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 *
 * @author jimclarke
 */

GroovyFX.start {
    def sg = new SceneGraphBuilder(it)

    sg.stage(title: "Lighting Effect Demo", width: 520, height: 300, visible: true, style: "decorated") {
        scene(fill: groovyblue) {
            text(x: 10, y: 10, text: "Light Effect", font: "bold 90pt Amble", fill: yellow, textOrigin: "top") {
                lighting(surfaceScale: 5.0) {
                    //distant(azimuth: -135)
                    //spot(x: 0, y: 100, z: 50, pointsAtX: 100, pointsAtY: 0, pointsAtZ: 0, specularExponent: 2)
                    point(x: -100, y: -100, z: 50)
                    /** **
                     bumpInput() {dropShadow()}contentInput() {glow()}*** */
                }
            }
        }
    }
}


