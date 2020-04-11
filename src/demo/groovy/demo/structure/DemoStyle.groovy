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
package structure

import groovyx.javafx.SceneGraphBuilder
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.stage.Stage

import static javafx.geometry.HPos.LEFT
import static javafx.geometry.HPos.RIGHT
import static javafx.scene.layout.Priority.ALWAYS

class DemoStyle {

    static style(SceneGraphBuilder sgb) {
        Stage frame = sgb.primaryStage
        Scene scene = frame.scene
        scene.stylesheets << 'demo/demo.css'
//        def GROOVYBLUE = sgb.groovyblue
//        scene.fill = sgb.radialGradient(stops: [
//            GROOVYBLUE.brighter(),
//            GROOVYBLUE.darker()]
//        ).build() // a scene fill cannot be set via css atm

        GridPane grid = scene.root
        grid.styleClass << 'form'
        grid.hgap = 5  // for some reason, the gaps are not taken from the css atm
        grid.vgap = 10
        grid.columnConstraints << sgb.columnConstraints(halignment: RIGHT, hgrow: ALWAYS)
        grid.columnConstraints << sgb.columnConstraints(halignment: LEFT, hgrow: ALWAYS, prefWidth: 300)

        sgb.translateTransition(1.s, node: grid, fromY: -100, toY: 0).play()

    }
}
