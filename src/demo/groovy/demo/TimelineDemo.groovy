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

x = 5.0
y = 5.0

start {
    stage(x: 100, y: 100, onShown: { println "Stage 1"}, visible: true) {
        scene(width: 500, height: 500, fill: GROOVYBLUE) {
            r = rectangle(x: bind(this, "x"), y: bind(this, "y"), width: 50, height: 25) {
                fill RED
            }
        }
    }

    timeline(cycleCount: 1, autoReverse: true) {
        onFinished { x = 200; y = 200; println "F: ${this.x}, ${this.y}" }
        at(10.s) {
            change(this, "x") to 400.0 tween EASE_BOTH
            change(this, "y") to 400
            onFinished { println "10 seconds elapsed"}
        }
    }.play()
}
