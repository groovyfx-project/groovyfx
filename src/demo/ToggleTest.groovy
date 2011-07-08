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

GroovyFX.start({
    def sg = new SceneGraphBuilder();
    sg.stage(
        title: "Stage Frame",
        x: 100, y: 100, width: 400, height:400,
        visible: true,
        style: "decorated",
        onHidden: { println "Close"}
    ) {
        
        scene(fill: hsb(128, 0.5, 0.5, 0.5), root: group()) {
            toggleButton (
                layoutX: 25,
                layoutY: 300,
                font: "16pt Courier",
                text: "One",
                selected: true,
                toggleGroup: "Group1"
            )
            toggleButton (
                layoutX: 125,
                layoutY: 300,
                font: "16pt Courier",
                text: "Two",
                selected: true,
                toggleGroup: "Group1"
            )

        }
    }
});
