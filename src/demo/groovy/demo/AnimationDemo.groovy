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

/**
 *
 * @author jimclarke
 */
start {
    stage(title: "GroovyFX Animation Demo", width: 650, height: 450, visible: true) {
        scene(fill: GROOVYBLUE) {
            rect1 = rectangle(x: 25, y: 40, width: 100, height: 50, fill: RED)
            rect2 = rectangle(x: 25, y: 100, width: 100, height: 50, fill: GREEN)
        }
    }

    def tl = timeline(cycleCount: INDEFINITE, autoReverse: true) {
        at(1000.ms, onFinished: { println "done" }) {
            change(rect1, 'x') to 200 tween EASE_BOTH
            change rect2.yProperty() to 200 tween LINEAR
        }
    }

    tl.play()
}

