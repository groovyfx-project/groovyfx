/*
* Copyright 2011-2012 the original author or authors.
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
package shelfdemo

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
            onAction: { primaryStage.close() },
            enabled: false)
    fxaction(id: 'cutAction',
            name: 'Cut',
            onAction: { println "Cut" })
    fxaction(id: 'copyAction',
            name: 'Copy',
            onAction: { println "Copy" })
    fxaction(id: 'pasteAction',
            name: 'Paste',
            onAction: { println "Paste" })
    fxaction(id: 'checkAction',
            name: 'Check',
            onAction: { println "Check" })
}

borderPane(id: 'Action Menu') {
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
