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
    stage(title: "GroovyFX ToggleButton Demo", x: 100, y: 100, width: 400, height: 400, visible: true,
            style: "decorated", onHidden: { println "Close" }) {

        scene(fill: GROOVYBLUE) {
            hbox(spacing: 10, padding: 10) {
                toggleButton("One", font: "16pt Courier", selected: true, toggleGroup: "Group1")
                toggleButton("Two", font: "16pt Courier", toggleGroup: "Group1")
            }
        }
    }
}
