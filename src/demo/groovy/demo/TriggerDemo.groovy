/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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
import javafx.scene.paint.Color

import static groovyx.javafx.GroovyFX.start

start {
    stage(title: "GroovyFX Trigger Demo", width: 600, height: 450, visible: true) {
        scene(fill: GROOVYBLUE) {
            rect = rectangle(x: 25, y: 40, width: 200, height: 150, fill: RED)
        }
    }

    rect.hover().onInvalidate { rect.fill = rect.isHover() ? Color.GREEN : Color.RED }
}
