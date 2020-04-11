/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2020 the original author or authors.
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
import groovy.swing.SwingBuilder
import groovyx.javafx.SceneGraphBuilder
import javafx.embed.swing.JFXPanel

import javax.swing.WindowConstants as WC
import java.awt.BorderLayout as BL
import java.awt.Dimension

def DEFAULT_URL = "http://www.yahoo.com"
def swing = new SwingBuilder()
def sg

def fxPanel = new JFXPanel(preferredSize: new Dimension(800, 400))

def setUrl = { url ->
    sg.defer {
        wv.engine.load(url)
    }
}

def createScene = {
    sg.defer {
        def scene = sg.scene(width: 800, height: 400) {
            wv = webView(DEFAULT_URL)
        }
        fxPanel.scene = scene
    }
}

swing.edt {
    sg = new SceneGraphBuilder()
    frame = frame(title: 'GroovyFX Swing Demo', show: true, defaultCloseOperation: WC.EXIT_ON_CLOSE) {
        borderLayout()
        panel(constraints: BL.NORTH) {
            textField(id: "urlField", actionPerformed: {setUrl(urlField.text)}, columns: 40)
            button(text: "Go", actionPerformed: {setUrl(urlField.text)})
        }
        widget(fxPanel, constraints: BL.CENTER)
    }
    frame.pack()
    createScene();
}
