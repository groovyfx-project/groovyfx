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
import groovyx.javafx.beans.FXBindable
import javafx.beans.binding.Bindings

/**
 *
 * @author jimclarke
 */
 
def sg = new SceneGraphBuilder()

class Time {
    @FXBindable Integer hours
    @FXBindable Integer minutes
    @FXBindable Integer seconds

    @FXBindable Double hourAngle
    @FXBindable Double minuteAngle
    @FXBindable Double secondAngle

    public Time() {
        // bind the angle properties to the clock time
        hourAngleProperty.bind((hoursProperty * 30.0) + (minutesProperty * 0.5))
        minuteAngleProperty.bind(minutesProperty * 6.0)
        secondAngleProperty.bind(secondsProperty * 6.0)

        // Set the initial clock time
        def calendar = Calendar.instance
        hours = calendar.get(Calendar.HOUR)
        minutes = calendar.get(Calendar.MINUTE)
        seconds = calendar.get(Calendar.SECOND)
    }

    /**
     * Add a second to the time
     */
    public void addOneSecond() {
        seconds = (seconds + 1) % 60
        if (seconds == 0) {
            minutes = (minutes + 1)  % 60
            if (minutes == 0) {
                hours = (hours + 1) % 12
            }
        }
    }
}

time = new Time()

GroovyFX.start {
    

    def width = 240.0
    def height = 240.0
    def radius = width / 3.0
    def centerX = width / 2.0
    def centerY = height / 2.0

    sg.stage(title: "GroovyFX Clock Demo", width: 245, height: 265, visible: true, resizable: false) {
        def hourDots = []
        for (i in 0..11) {
            def y = -Math.cos(Math.PI / 6.0 * i) * radius
            def x = ((i > 5) ? -1 : 1) * Math.sqrt(radius * radius - y * y)
            def r = i % 3 ? 2.0 : 4.0

            hourDots << circle(fill: black, layoutX: x, layoutY: y, radius: r)
        }

        scene(fill: groovyblue) {
            group(layoutX: centerX, layoutY: centerY) {
                // outer rim
                circle(radius: radius + 20) {
                    fill(radialGradient(radius: 1.0, center: [0.0, 0.0], focusDistance: 0.5, focusAngle: 0,
                                        stops: [[0.9, silver], [1.0, black]]))
                }
                // clock face
                circle(radius: radius + 10, stroke: black) {
                    fill(radialGradient(radius: 1.0, center: [0.0, 0.0], focusDistance: 4.0, focusAngle: 90,
                                        stops: [[0.0, white], [1.0, cadetblue]]))
                }
                // dots around the clock for the hours
                nodes(hourDots)
                // center
                circle(radius: 5, fill: black)
                // hour hand
                path(fill: black) {
                    rotate(angle: bind(time.hourAngleProperty))
                    moveTo(x: 4, y: -4)
                    arcTo(radiusX: -1, radiusY: -1, x: -4, y: -4)
                    lineTo(x: 0, y: -radius / 4 * 3)
                }
                // minute hand
                path(fill: black) {
                    rotate(angle: bind(time.minuteAngleProperty))
                    moveTo(x: 4, y: -4)
                    arcTo(radiusX: -1, radiusY: -1, x: -4, y: -4)
                    lineTo(x: 0, y: -radius)
                }
                // second hand
                line(endY: -radius - 3, strokeWidth: 2, stroke: red) {
                    rotate(angle: bind(time.secondAngleProperty))
                }
            }
        }
    }

    sg.sequentialTransition(cycleCount: "indefinite") {
        pauseTransition(1.s, onFinished: {time.addOneSecond()})
    }.playFromStart()
}

