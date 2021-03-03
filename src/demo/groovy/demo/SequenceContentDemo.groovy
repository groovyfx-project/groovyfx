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
    stage(title: "GroovyFX Sequence Content Demo", width: 400, height: 300, visible: true, resizable: true) {
        def r1 = rectangle(width: 100, height: 100, fill: RED)
        def r2 = rectangle(x: 110, width: 100, height: 100, fill: BLUE)

        scene(fill: GROOVYBLUE) {
            group(children: [r1, r2], layoutX: 20, layoutY: 20)
        }
    }
}
