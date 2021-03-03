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
import groovyx.javafx.beans.FXBindable
import javafx.scene.control.TextField

import static groovyx.javafx.GroovyFX.start

/**
 *
 * @author jimclarke
 */
class QuickTest {
    @FXBindable String qtText = "Quick Test"
    private int clickCount = 0

    def onClick = {
        qtText = "Quick Test ${++clickCount}"
    }
}

start {
    def qt = new QuickTest()

    stage(title: "GroovyFX Bind Demo", x: 100, y: 100, width: 400, height: 400, visible: true,
            style: "decorated", onHidden: { println "Close"}) {
        scene(fill: GROOVYBLUE) {
            vbox(spacing: 10, padding: 10) {
                TextField tf = textField(text: 'Change Me!')
                //button(text: bind(source: tf, sourceProperty: 'text'), onAction: {qt.onClick()})
                button(text: bind(tf, 'text'), onAction: {qt.onClick()})
                label(text: bind(tf.textProperty()))
                label(text: bind({tf.text}))
                label(text: bind(tf.text()))
                label(text: bind(tf.text()))

                // Bind to POGO fields annotated with @FXBindable
                // These three bindings are equivalent
                label(text: bind(qt,'qtText'))
                label(text: bind(qt.qtText()))
                label(text: bind(qt.qtTextProperty()).using({"<<< ${it} >>>"}) )
                label(id: "bind2")
                label(id: "bind1");

                // bidirectional bind
                textField(id: "tf2", promptText: 'Change Me!')
                textField(text: bind(tf2.textProperty()))
            }
        }
        
    }
    bind bind1.text() to bind2.text() to qt,"qtText"  using {"Converted: ${it}"} 
}

