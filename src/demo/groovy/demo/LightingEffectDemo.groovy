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
    stage(title: "Lighting Effect Demo", width: 520, height: 300, visible: true, style: "decorated") {
        scene(fill: GROOVYBLUE) {
            text(x: 10, y: 10, text: "Light Effect", font: "bold 48pt Amble", fill: YELLOW, textOrigin: TOP) {
                lighting(surfaceScale: 5.0) {
//                    distant(azimuth: -135)
//                    spot(x: 0, y: 100, z: 50, pointsAtX: 100, pointsAtY: 0, pointsAtZ: 0, specularExponent: 2)
                    point(x: -100, y: -100, z: 50)
                    /** **
                     bumpInput() {dropShadow()}contentInput() {glow()}*** */
                }
            }
        }
    }
}


