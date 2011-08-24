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



import groovyx.javafx.GroovyFX
import groovyx.javafx.TimelineBuilder
import javafx.application.Platform

x = 5.0
y = 5.0

GroovyFX.start {
    def tlb = new TimelineBuilder()
    def tl = tlb.timeline(cycleCount: 2, onFinished: { Platform.exit()}) {
        at (1.m, onFinished: { println "x = ${this.x}, y = ${this.y}"}) {
            change(this,"x") {
                    to 2.0
                    tween  "ease_both"
                }
                change(this,"y") {
                    to 125
                }
            }
    }
    tl.play()
}


