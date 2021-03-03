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
import javafx.beans.property.SimpleStringProperty

import static groovyx.javafx.GroovyFX.start

def selectedProperty = new SimpleStringProperty("");
start {

    final fileChooser = fileChooser(initialDirectory: ".", title: "FileChooser Demo") {
        filter("images", extensions: ["*.jpg", "*.gif", "*.bmp", "*.png"])
    }
    final dirChooser = directoryChooser(initialDirectory: ".", title: "DirectoryChooserDemo");

    stage(title: "GroovyFX Chooser Demo", width: 400, height: 300, visible: true, resizable: true) {
        scene(fill: GROOVYBLUE) {
            vbox(spacing: 10, padding: 10) {
                hbox(spacing: 10, padding: 10) {
                    button("Open file", onAction: {
                        selectedfile = fileChooser.showOpenDialog(primaryStage)
                        selectedProperty.set(selectedfile ? selectedfile.toString() : "")
                    })
                    button("Save file", onAction: {
                        selectedfile = fileChooser.showSaveDialog(primaryStage)
                        selectedProperty.set(selectedfile ? selectedfile.toString() : "")
                    })
                    button("Select directory", onAction: {
                        selectedfile = dirChooser.showDialog(primaryStage)
                        selectedProperty.set(selectedfile ? selectedfile.toString() : "")
                    })
                }
                label(id: 'selected')

            }
        }
    }
    selected.textProperty().bind(selectedProperty)
}



