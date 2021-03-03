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
import javafx.scene.control.TextField

import static groovyx.javafx.GroovyFX.start

/**
 Demo of including a custom text field that rejects invalid characters giving
 visual feedback (shake).
 Shows how the SGB can be extended with nodes for new controls, how new
 controls can can be customized with new state, and how to customize
 their runtime behaviour, i.e. visualizing rejection with a shake.
 @author Dierk Koenig
 */

start {
    registerBeanFactory "rejectField", RejectField

    stage title: 'Shake on Reject', visible: true, {
        scene {
            gridPane {
                rejectField text: 'only a-z allowed', id: 'input', allow: '[a-z]*', row: 0, column: 0, {
                    onMouseEntered { println "enter"}
                    shake = sequentialTransition {
                        rotateTransition to: 3, cycleCount: 3, cycleDuration: 0.1.s
                        rotateTransition 0.1.s, to: 0
                    }
                }
            }
        }
    }
    input.onReject = { shake.playFromStart() }
}

class RejectField extends TextField {
    Closure onReject
    String allow = ".*"

    void replaceText(int start, int end, String text) {
        if (text.matches(allow)) {
            super.replaceText start, end, text
        } else {
            onReject?.call this
        }
    }

    void replaceSelection(String text) {
        if (text.matches(allow)) {
            super.replaceSelection text
        } else {
            onReject?.call this
        }
    }
}
