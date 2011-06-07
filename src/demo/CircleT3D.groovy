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
import groovyx.javafx.TimelineBuilder
import javafx.animation.Timeline;
import javafx.scene.transform.Rotate;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;

GroovyFX.start({ primaryStage -> 
    def sg = new SceneGraphBuilder(primaryStage);
    if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            System.out.println("*************************************************************");
            System.out.println("*    WARNING: common conditional SCENE3D isn\'t supported    *");
            System.out.println("*************************************************************");
        }
    sg.stage(
        title: "CircleT3D (Groovy)",
        width: 400, height:300,
        visible: true,
        resizable: true
    ) {
         scene(fill: lightgreen, camera: new PerspectiveCamera()) {
             circle(centerX: 200, centerY: 150, radius: 75, fill: blue,
                rotate: 30, rotationAxis: Rotate.Y_AXIS)
         }
    }
})
