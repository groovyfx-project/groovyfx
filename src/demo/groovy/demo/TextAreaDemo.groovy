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
    stage(title: "GroovyFX TextArea Demo", x: 100, y: 100, visible: true, style: "decorated", primary: true) {
        scene(fill: GROOVYBLUE, width: 400, height: 400) {
            borderPane(padding: 10) {
                text = textArea(prefRowCount: 10, prefColumnCount: 80)
                bottom(align: "center", margin: [10, 0]) {
                    button("Print Text", onAction: { println text.text })
                }
            }
        }
    }
}


