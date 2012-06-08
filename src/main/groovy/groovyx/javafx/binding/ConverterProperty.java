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

import groovy.lang.Closure;
import groovy.lang.GString;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author jimclarke
 */

public class ConverterProperty implements  ObservableValue, ChangeListener,  InvalidationListener{
    private ObservableValue baseProperty;
    private Closure converter;
    
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private List<InvalidationListener> invalidationListeners = new ArrayList<InvalidationListener>();
    
    private Object oldValue = null;
    private Object newValue = null;
    
    public ConverterProperty(ObservableValue baseProperty, Closure converter) {
        this.baseProperty = baseProperty;
        this.converter = converter;
        baseProperty.addListener((ChangeListener)this);
        baseProperty.addListener((InvalidationListener)this);
        getValue();
    }
    
    
    @Override
    public final Object getValue() {
        oldValue = newValue;
        newValue = converter.call(baseProperty.getValue());
        if(newValue instanceof GString)
            newValue = newValue.toString();
        return newValue;
    }

    @Override
    public void addListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

    @Override
    public void changed(ObservableValue obs, Object oldVal, Object newVal) {
        getValue();
        if(!changeListeners.isEmpty()) {
            ChangeListener[] l = changeListeners.toArray(new ChangeListener[changeListeners.size()]);
            for(int i = 0; i < l.length; i++) {
                l[i].changed(this, oldValue, newValue);
            }
        }
    }

    @Override
    public void invalidated(Observable obs) {
        if(!invalidationListeners.isEmpty()) {
            InvalidationListener[] l = invalidationListeners.toArray(new InvalidationListener[invalidationListeners.size()]);
            for(int i = 0; i < l.length; i++) {
                l[i].invalidated(this);
            }
        }
    }
}
