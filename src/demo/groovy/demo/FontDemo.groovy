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
import javafx.scene.text.Font

import static groovyx.javafx.GroovyFX.start

start {
    stage(show: true) {
        scene(fill: GROOVYBLUE) {
            vbox {
                text('Text with font "80pt"', font: '80pt')
                hbox(spacing: 20) {
                    text('Text with font("serif", 36)', font: Font.font('serif', 36))
                    text('Text with font "36pt serif"', font: '36pt serif')
                }
                hbox(spacing: 20) {
                    text('Text with font("sanserif", 24)', font: Font.font('sanserif', 24))
                    text('Text with font "24pt sanserif"', font: '24pt sanserif')
                }
                hbox(spacing: 20) {
                    text('Text with font("Courier New", 24)', font: Font.font('Courier New', 24))
                    text('Text with font "24pt Courier New"', font: '24pt "Courier New"')
                }
            }
        }
    }
}