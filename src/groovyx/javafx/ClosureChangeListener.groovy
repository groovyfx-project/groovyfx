/*
* Copyright 2011 the original author or authors.
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

package groovyx.javafx

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author jimclarke
 */
class ClosureChangeListener implements ChangeListener {
    public String property;
    public Closure closure;
    
    public ClosureEventHandler() {}
    
    public ClosureEventHandler(String property) {
        this.property = property;
    }
    
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(closure != null) {
            closure(observable, oldValue, newValue);
        }
    }

}

