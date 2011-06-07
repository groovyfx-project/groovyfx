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

/**
 *
 * @author jimclarke
 */
GroovyFX.start({
    def sg = new SceneGraphBuilder();

    sg.stage(
        title: "Gradient Test (Groovy)",
        width: 650, height:450,
        visible: true,
    ) {
         scene(fill: lightgreen ) {
             rect1 = rectangle (x: 25, y: 40,
                width: 100, height: 50, 
                fill: "radial 100% stops (0.0,red) (0.50,darkgray) (1.0,black)" )
             rect2 = rectangle (x: 25, y: 100,
                width: 100, height: 50,
                fill: "linear (0%,0%) to (100%,100%) stops (0.0,red) (1.0,black)")
         }
    }
});
