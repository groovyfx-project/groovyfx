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
/**
 *
 * @author jimclarke
 */

import static groovyx.javafx.GroovyFX.start

start {
    stage(title: "GroovyFX Menu Demo", width: 650, height: 450, visible: true) {
        scene(fill: GROOVYBLUE) {
            borderPane {
                top {
                    menuBar {
                        menu("File") {
                            menuItem("Open", onAction: { println "Open" }) {
                                rectangle(width: 16, height: 16, fill: RED)
                            }
                            menuItem("Save", onAction: { println "Save" }) {
                                graphic(circle(radius: 8, fill: BLUE))
                            }
                            saveAs = menuItem("SaveAs..", onAction: { println "Save As" })
                            separatorMenuItem()
                            menuItem("Exit", onAction: { primaryStage.close() })
                        }
                        menu(text: "Edit") {
                            menuItem("Cut", onAction: { println "Cut" })
                            menuItem("Copy", onAction: { println "Copy" })
                            menuItem("Paste..", onAction: { println "Paste" })
                            separatorMenuItem()
                            checkMenuItem("Check")
                            radioMenuItem("Radio", selected: true)
                            menu("Foo") {
                                menuItem("Bar")
                                menuItem("FooBar")
                            }
                        }
                    }
                }
                center {
                    hbox(spacing: 20, padding: 10) {
                        menuButton("Choose") {
                            menuItem("one", onAction: { println "One"})
                            menuItem("two", onAction: { println "Two"})
                            menuItem("three", onAction: { println "Three"})
                        }
                        splitMenuButton("Split", onAction: { println "Split Action"}) {
                            menuItem("11111", onAction: { println "11111"})
                            menuItem("22222", onAction: { println "22222"})
                            menuItem("33333", onAction: { println "33333"})
                        }
                        button("Context Menu") {
                            onMouseEntered {println "over"}
                            contextMenu() {
                                menuItem("Write", onAction: { println "Write" })
                                menuItem("Cancel", onAction: { println "Cancel" })
                            }
                        }
                    }
                }
            }
        }
    }
}




