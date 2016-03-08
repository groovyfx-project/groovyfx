/*
 * Copyright 2011-2016 the original author or authors.
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

import javafx.beans.InvalidationListener
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener

/**
 * @author Andres Almiray
 */
class ReadOnlyObjectPropertyExtension {
    static void onChange(ReadOnlyObjectProperty self, Closure listener) {
        self.addListener(listener as ChangeListener)
    }

    static void onInvalidate(ReadOnlyObjectProperty self, Closure listener) {
        self.addListener(listener as InvalidationListener)
    }
}
