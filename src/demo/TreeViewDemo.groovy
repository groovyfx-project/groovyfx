/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License")
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

/**
 * @author jimclarke
 */
GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder(primaryStage)

    popup = sg.popup(autoHide: true) {
        stackPane() {
            rectangle(width: 200, height: 200, fill: lightgray)
            button("Dismiss", layoutX: 10, layoutY: 20, onAction: {popup.hide()})
            dropShadow()
        }
    }

    sg.stage(title: "GroovyFX TreeView Demo", x: 100, y: 100, visible: true, style: "decorated") {
        scene(width: 400, height: 400, fill: groovyblue) {
            treeView(showRoot: false) {
                //onEditCancel()
                //onEditStart()
                //onEditCommit()
                treeItem(expanded: true, value: "Root") {
                    treeItem(value: "one") {
                        branchCollapse(onEvent: {popup.show(primaryStage, 150, 150)})
                        branchExpand(onEvent: {println "one expand"})
                        treeItem(value: "one.one")
                        treeItem(value: "one.two")
                        treeItem(value: "one.three")
                        graphic {
                            rectangle(width: 20, height: 20, fill: red)
                        }
                    }
                    treeItem(value: "two") {
                        treeItem(value: "two.one")
                        treeItem(value: "two.two")
                        treeItem(value: "two.three")
                        graphic {
                            rectangle(width: 20, height: 20, fill: green)
                        }
                    }
                    treeItem(value: "three") {
                        treeItem(value: "three.one")
                        treeItem(value: "three.two")
                        treeItem(value: "three.three")
                        graphic {
                            rectangle(width: 20, height: 20, fill: blue)
                        }
                    }
                    treeItem(value: "four") {
                        treeItem(value: "four.one")
                        treeItem(value: "four.two")
                        treeItem(value: "four.three")
                        graphic {
                            rectangle(width: 20, height: 20, fill: orange)
                        }
                    }
                    treeItem(value: "five") {
                        treeItem(value: "five.one")
                        treeItem(value: "five.two")
                        treeItem(value: "five.three")
                        graphic {
                            rectangle(width: 20, height: 20, fill: yellow)
                        }
                    }
                }
            }
        }
    }
}




