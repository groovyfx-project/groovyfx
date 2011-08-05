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

import groovyx.javafx.GroovyFX;
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder(primaryStage)
    
    def fileChooser = sg.fileChooser(initialDirectory: ".", title: "FileChooser Demo") {
        filter("images", extensions: ["jpg", "gif", "bmp"])
    }

    def stage = sg.stage(title: "GroovyFX FileChooser Demo", width: 400, height:300, visible: true, resizable: true) {
         scene(fill: groovyblue) {
             stackPane {
                 hbox(spacing: 10, padding: 10) {
                    button("Open file", onAction: { println(fileChooser.showOpenDialog(stage)) })
                    button("Save file", onAction: { println(fileChooser.showSaveDialog(stage)) })
                 }
             }
         }
    }
}



