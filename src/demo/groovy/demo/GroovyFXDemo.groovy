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
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

import static groovyx.javafx.GroovyFX.start

class Custom extends Region {
    public Custom() {
        getChildren().add(create());
    }

    protected javafx.scene.Node create() {
        return new Rectangle(width: 10, height: 10, fill: Color.BLUE);
    }
}

start {
    stage(title: "GroovyFX Demo", x: 100, y: 100, width: 480, height: 800, visible: true, style: "decorated",
            onHidden: { println "Close"}) {

        scene(fill: GROOVYBLUE, root: group(), stylesheets: resource("/demo/groovyfx.css")) {
            onMousePressed {e -> println "scene press @" + e.x + "," + e.y }
            onKeyReleased { e -> println "scene key" + e.text}
            onChange("width") { observable, oldValue, newValue ->
                println "Width: " + oldValue + " ==> " + newValue
            }
            node(new Custom(), layoutX: 10, layoutY: 10) {
                scale(x: 5, y: 5)
                onChange("hover") { observable, oldValue, newValue ->
                    println "hover: " + oldValue + " ==> " + newValue
                }
            }

            circle(centerX: 50, centerY: 50, radius: 25, fill: rgb(0, 0, 255), onMousePressed: {println 'jim'})

            rectangle(x: 100, y: 50, width: 50, height: 50, fill: RED) {
                effect reflection {
                    dropShadow()
                }
            }

            path(fill: YELLOW) {
                onMousePressed {println "foo"}
                moveTo(x: 150, y: 50)
                lineTo(x: 150, y: 100)
                arcTo(x: 200, y: 75, radiusX: 10, radiusY: 20)
                closePath()
            }

            vbox(layoutX: 25, layoutY: 125, spacing: 10) {
                label("I'm a label", textFill: "rgb(255,255,0)")
                text("This is Text", id: 'header')

                vbox(spacing: 10) {
                    hbox(spacing: 10) {
                        button("This is a Button", styleClass: "custom-button", font: "16pt Courier") {
                            onAction { println "button pressed"}
                            tooltip "This button has a tooltip!"
                        }
                        checkBox("Check", font: "16pt Courier", selected: true) {
                            graphic(circle(radius: 8, fill: GREEN))
                            tooltip "This checkbox has a graphic! (and a tooltip!)"
                        }
                    }
                    separator()
                    hbox(spacing: 10, padding: 10) {
                        scrollBar(min: 0, max: 100, value: 50, orientation: HORIZONTAL, prefWidth: 200)
                        slider(min: 0, max: 100, value: 50, orientation: HORIZONTAL, showTickMarks: true,
                                prefWidth: 200)
                    }
                    listView(items: ["one", "two", "three"], prefWidth: 200, prefHeight: 400)
                    comboBox(items: ["one", "two", "three"], prefWidth: 200, value: "one")
                }
            }
        }
    }
}
