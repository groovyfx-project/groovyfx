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
package groovyx.javafx.binding;

import groovy.lang.Script;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Wraps a groovy script variable in a JavaFX Property
 * 
 * @author jimclarke
 */
public class ScriptVariableProperty<T> extends SimpleObjectProperty<T>{
    
    public ScriptVariableProperty(Script script, String propertyName) {
        super(script, propertyName);
    }

    @Override
    public T getValue() {
        return (T)((Script)getBean()).getProperty(getName());
    }

    @Override
    public void setValue(T newValue) {
        ((Script)getBean()).setProperty(getName(), newValue);
    }
    
    @Override
    public T get() {
        return getValue();
    }

    @Override
    public void set(T newValue) {
        setValue(newValue);
    }
}
