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
import static groovyx.javafx.GroovyFX.start

/**
 * Example of the AccordionDemo when written in Dierk's code style
 * @author Dierk Koenig
 */
start {
    stage title: "Dierk's Code Style Demo", x: 100, y: 100, visible: true, {
        scene fill: GROOVYBLUE, width: 400, height: 400, {
            accordion {
                titledPane "Look inside", {
                    label "Hi! Also try the other groups."
                }
                titledPane "Hidden treasure", {
                    label "You wouldn't believe it.\n\n\nWe have empty lines for you!"
                }
                titledPane "Click me!", {
                    vbox {
                        label "More than one child ..."
                        label "... requires an extra container."
                    }
                }
            }
        }
    }
}
