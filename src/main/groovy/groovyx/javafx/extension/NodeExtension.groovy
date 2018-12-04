/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2018 the original author or authors.
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
package groovyx.javafx.extension

import javafx.event.EventHandler
import javafx.scene.Node

/**
 * @author Andres Almiray
 */
class NodeExtension {
    static void onDragDetected(Node self, Closure listener) { self.setOnDragDetected(listener as EventHandler) }

    static void onDragDone(Node self, Closure listener) { self.setOnDragDone(listener as EventHandler) }

    static void onDragDropped(Node self, Closure listener) { self.setOnDragDropped(listener as EventHandler) }

    static void onDragEntered(Node self, Closure listener) { self.setOnDragEntered(listener as EventHandler) }

    static void onDragExited(Node self, Closure listener) { self.setOnDragExited(listener as EventHandler) }

    static void onDragOver(Node self, Closure listener) { self.setOnDragOver(listener as EventHandler) }

    static void onInputMethodTextChanged(Node self, Closure listener) {
        self.setOnInputMethodTextChanged(listener as EventHandler)
    }

    static void onKeyPressed(Node self, Closure listener) { self.setOnKeyPressed(listener as EventHandler) }

    static void onKeyReleased(Node self, Closure listener) { self.setOnKeyReleased(listener as EventHandler) }

    static void onKeyTyped(Node self, Closure listener) { self.setOnKeyTyped(listener as EventHandler) }

    static void onMouseDragEntered(Node self, Closure listener) { self.setOnMouseDragEntered(listener as EventHandler) }

    static void onMouseClicked(Node self, Closure listener) { self.setOnMouseClicked(listener as EventHandler) }

    static void onMouseDragExited(Node self, Closure listener) { self.setOnMouseDragExited(listener as EventHandler) }

    static void onMouseDragged(Node self, Closure listener) { self.setOnMouseDragged(listener as EventHandler) }

    static void onMouseDragOver(Node self, Closure listener) { self.setOnMouseDragOver(listener as EventHandler) }

    static void onMouseDragReleased(Node self, Closure listener) {
        self.setOnMouseDragReleased(listener as EventHandler)
    }

    static void onMouseEntered(Node self, Closure listener) { self.setOnMouseEntered(listener as EventHandler) }

    static void onMouseExited(Node self, Closure listener) { self.setOnMouseExited(listener as EventHandler) }

    static void onMouseMoved(Node self, Closure listener) { self.setOnMouseMoved(listener as EventHandler) }

    static void onMousePressed(Node self, Closure listener) { self.setOnMousePressed(listener as EventHandler) }

    static void onMouseReleased(Node self, Closure listener) { self.setOnMouseReleased(listener as EventHandler) }

    static void onScroll(Node self, Closure listener) { self.setOnScroll(listener as EventHandler) }
}
