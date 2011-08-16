/*
* Copyright 2011 the original author or authors.
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

package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.scene.control.TextField

/**
 *
 * @author jimclarke
 */
GroovyFX.start {
    def sg = new SceneGraphBuilder(it)

    sg.stage(title: "GroovyFX Bind Demo", x: 100, y: 100, width: 400, height: 400, visible: true,
             style: "decorated", onHidden: { println "Close"}) {

        scene(fill: groovyblue) {
            stylesheets(urls: ["foo.css "])

            vbox(spacing: 10, padding: 10) {
                TextField tf = textField(text: 'Change Me!')
                button(text: bind(source: tf, sourceProperty: 'text'))
                label(text: bind(tf.textProperty()))
                label(text: bind({tf.text}))
            }
        }
    }
}

