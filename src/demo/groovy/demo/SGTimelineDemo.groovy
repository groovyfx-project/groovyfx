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
    def lX = 100.0;
    def lY = 100.0;
    def s = 2;
    def r = 90;

    stage(title: "GroovyFX Timeline Demo", width: 200, height: 200, visible: true, resizable: true) {
        scene(fill: GROOVYBLUE) {
            map = circle(radius: 25) {
                fill(RED)
            }
        }

        timeline(cycleCount: INDEFINITE, autoReverse: true) {
            at(800.ms) {
                change(map, 'layoutX') to lX tween EASE_BOTH
                change(map, 'layoutY') to lY tween EASE_BOTH
                change(map, 'scaleX') to s
                change(map, 'scaleY') to s
                change(map, 'rotate') to r
            }
        }.play()
    }
}


