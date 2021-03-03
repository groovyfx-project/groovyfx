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

final homePage = "http://www.yahoo.com"

start {
    def goAction = { wv.engine.load(urlField.getText()) }

    stage(title: "GroovyFX WebView Demo", visible: true) {
        scene(fill: GROOVYBLUE, width: 1024, height: 800) {
            vbox {
                hbox(padding: 10, spacing: 5) {
                    urlField = textField(text: homePage, onAction: goAction, hgrow: "always")
                    button("Go", onAction: goAction)
                }
                wv = webView(vgrow: "always")
            }
        }
    }

    wv.engine.load(homePage)
}
