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
/**
 *
 * @author Andres Almiray
 */

import static groovyx.javafx.GroovyFX.start

start {

    actions {
        fxaction(id: 'openAction',
                name: 'Open',
                onAction: { println "Open" })
        fxaction(id: 'saveAction',
                name: 'Save',
                onAction: { println "Save" })
        fxaction(id: 'saveAsAction',
                name: 'Save As...',
                onAction: { println "Save As ..." })
        fxaction(id: 'exitAction',
                name: 'Exit',
                onAction: { primaryStage.close() })
        fxaction(id: 'cutAction',
                name: 'Cut',
                icon: 'icons/cut.png',
                onAction: { println "Cut" })
        fxaction(id: 'copyAction',
                name: 'Copy',
                icon: 'icons/copy.png',
                onAction: { println "Copy" })
        fxaction(id: 'pasteAction',
                name: 'Paste',
                icon: 'icons/paste.png',
                onAction: { println "Paste" })
        fxaction(id: 'checkAction',
                name: 'Check',
                onAction: { println "Check" })
    }

    stage(title: "GroovyFX Menu Demo", width: 650, height: 450, visible: true) {
        scene(fill: GROOVYBLUE) {
            borderPane {
                top {
                    menuBar {
                        menu("File") {
                            menuItem(openAction) {
                                rectangle(width: 16, height: 16, fill: RED)
                            }
                            menuItem(saveAction) {
                                graphic(circle(radius: 8, fill: BLUE))
                            }
                            saveAs = menuItem(saveAsAction)
                            separatorMenuItem()
                            menuItem(exitAction)
                        }
                        menu(text: "Edit") {
                            menuItem(cutAction)
                            menuItem(copyAction)
                            menuItem(pasteAction)
                            separatorMenuItem()
                            checkMenuItem(checkAction)
                            radioMenuItem("Radio", selected: true)
                            menu("Foo") {
                                menuItem("Bar")
                                menuItem("FooBar")
                            }
                        }
                    }
                }
                center {
                    vbox(spacing: 20, padding: 10) {
                        checkBox("Enable 'Open' menu", id: 'cb')
                        actions {
                            bean(openAction, enabled: bind(cb.selectedProperty()))
                        }
                    }
                }
            }
        }
    }
}




