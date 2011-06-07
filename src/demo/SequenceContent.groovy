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

import groovyx.javafx.*;
import groovyx.javafx.GroovyFX

GroovyFX.start({
    def sg = new SceneGraphBuilder();
    //def a1 = sg.rectangle(width: 100, height: 100, fill: red)
    //def a2 = sg.rectangle(x: 110,width: 100, height: 100, fill: blue)
    sg.stage(
        title: "CircleT3D (Groovy)",
        width: 400, height:300,
        visible: true,
        resizable: true
    ) {
        scene() {
         group(children: [ sg.rectangle(width: 100, height: 100, fill: red), sg.rectangle(x: 110,width: 100, height: 100, fill: blue)])
        }
    }
})
