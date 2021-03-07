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
package groovyx.javafx.event

import javafx.beans.InvalidationListener
import javafx.beans.Observable

/**
*
* @author jimclarke
*/
class GroovyInvalidationListener extends AbstractClosureProperty implements InvalidationListener {
    
    Observable observable;

    GroovyInvalidationListener() {
        super();
    }

    GroovyInvalidationListener(String property) {
        super(property);
    }

    GroovyInvalidationListener(String property, Closure closure) {
        super(property, closure);
    }

    GroovyInvalidationListener(Observable observable) {
        super(null);
        this.observable = observable
    }

    GroovyInvalidationListener(Observable observable, Closure closure) {
        super(null, closure);
        this.observable = observable
    }

    void invalidated(Observable observable) {
        this.closure.call(observable);
    }
	
}

