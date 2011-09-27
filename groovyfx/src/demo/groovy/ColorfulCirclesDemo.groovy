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

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 * Ported from Steve Chin's scalafx example at:
 *      https://code.google.com/p/scalafx/source/browse/demo/scalafx/ColorfulCircles.scala
 *
 * @author dean
 */
GroovyFX.start { primaryStage ->
  def circles
  def sg = new SceneGraphBuilder(primaryStage)

  sg.stage(title: 'GroovyFX ColorfulCircles', resizable: false, visible: true) {
    scene(width: 800, height: 600, fill: black) {
      group {
        circles = group {
          30.times {
            circle(radius: 200, fill: rgb(255, 255, 255, 0.05), stroke: rgb(255, 255, 255, 0.16),
                   strokeWidth: 4, strokeType: 'outside')
          }
          effect boxBlur(width: 10, height: 10, iterations: 3)
        }
      }
      rectangle(width: 800, height: 600, blendMode: 'overlay') {
        def stops = ['#f8bd55', '#c0fe56', '#5dfbc1', '#64c2f8', '#be4af7', '#ed5fc2', '#ef504c', '#f2660f']
        fill linearGradient(start: [0f, 1f], end: [1f, 0f], stops: stops)
      }
    }

    parallelTransition(cycleCount: 'indefinite', autoReverse: true) {
      def random = new Random()
      circles.children.each { circle ->
        translateTransition(40.s, node: circle, fromX: random.nextInt(800), fromY: random.nextInt(600),
                            toX: random.nextInt(800), toY: random.nextInt(600))
      }
    }.play()
  }
}
