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

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX Grid Demo", width: 700, height:500, visible: true) {
         scene(fill: groovyblue) {
             vbox {
                 gridPane(hgap: 4, vgap: 4, padding: [18,18,18,18], alignment: "center", gridLinesVisible: true) {
                    label (text: "Name: ") {
                        constraint(row: 0, column: 0, halignment: "right")
                    }
                    label (text: "Jim Clarke") {
                        constraint(row: 0, column:1, columnSpan: 5,halignment: "left")
                    }
                    label (text: "Address:") {
                        constraint(row: 1, column:0, halignment: "right")
                    }
                    label (text: "123 Main St") {
                        constraint(row: 1, column:1, columnSpan: 5,halignment: "left")
                    }
                    label (text: "City:") {
                        constraint(row: 2, column:0, halignment: "right")
                    }
                    label (text: "Orlando") {
                        constraint(row: 2, column:1, halignment: "left")
                    }
                    label (text: "State:") {
                        constraint(row: 2, column:2, halignment: "right")
                    }
                    label (text: "FL") {
                        constraint(row: 2, column:3, halignment: "left", hgrow: "never")
                    }
                    label (text: "Zipcode:") {
                        constraint(row: 2, column:4, halignment: "right")
                    }
                    label (text: "32817") {
                        constraint(row: 2, column:5, halignment: "left", hgrow: "never")
                    }
                    button(text: "Register") {
                        constraint(row: 3, column: 0,columnSpan: 6, halignment: "center")
                    }
                    row(index: 4) {
                        label (text: "one")
                        label (text: "two")
                        label (text: "three")
                    }
                    column(index: 6) {
                        label (text: "one")
                        label (text: "two")
                        label (text: "three")
                    }
                }
             }
         }
    }
}

