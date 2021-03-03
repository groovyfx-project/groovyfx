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
import groovyx.javafx.GroovyFX;

GroovyFX.start {
    stage(x: 100, y: 100, onShown: { println "Stage 1" }, visible: true, style: 'UTILITY') {
        scene(width: 100, height: 100, fill: GROOVYBLUE)
    }
    stage(primary: false, x: 100, y: 400, visible: true) {
        onShown { println "Stage 2" }
        scene(width: 100, height: 100, fill: RED)
    }
}