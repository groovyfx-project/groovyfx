/*
* Copyright 2011-2012 the original author or authors.
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
package shelfdemo

pane(id: 'Animation') {
    rect1 = rectangle(x: 25, y: 40, width: 100, height: 50, fill: RED)
    rect2 = rectangle(x: 25, y: 100, width: 100, height: 50, fill: GREEN)

    timeline(cycleCount: INDEFINITE, autoReverse: true) {
        at(1000.ms) {
            change(rect1, 'x') to 300 tween EASE_BOTH
            change rect2.yProperty() to 300 tween LINEAR
        }
    }.play()
}

