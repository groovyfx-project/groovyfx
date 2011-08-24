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

package groovyx.javafx;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.value.ChangeListener;
import org.codehaus.groovy.runtime.InvokerHelper;
import javafx.beans.value.ObservableValue;


/**
 *
 * @author jimclarke
 */
public class Trigger implements PropertyChangeListener, ChangeListener<Object> {
    private Object bean; // javabean or ObservableValue
    private String propertyName; // javabean property
    private Closure action;
    private ObservableValue property;

    private boolean bound;
    private boolean boundToProperty;


    public Trigger(Object bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;

        bind();
    }

    public Trigger(Object bean, String propertyName, Closure action) {
        this.bean = bean;
        this.propertyName = propertyName;
        this.action = action;
        bind();
    }
    public Trigger(ObservableValue bean) {
        this.bean = bean;
        bind();
    }

    public Trigger(ObservableValue bean, Closure action) {
        this.bean = bean;
        this.action = action;
        bind();
    }

    // called from JavaBean
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (boundToProperty && event.getPropertyName().equals(propertyName)) {
            action.call();
        }
    }
    
    // called from JavaFXProperty
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (boundToProperty) {
            action.call();
        }
    }


    public final void bind() {
        if (!bound) {
            bound = true;
            try {
                property = (ObservableValue)InvokerHelper.invokeMethodSafe(bean, propertyName + "Property", null);
                try {
                    InvokerHelper.invokeMethodSafe(property, "addListener", new Object[] {this});
                    boundToProperty = true;
                } catch (MissingMethodException mme1) {
                    boundToProperty = false;
                    throw new RuntimeException("Properties in beans of type " + bean.getClass().getName() + " are not a JavaFX Bean).");
                }
            }catch (MissingMethodException mme) {
                try {
                    InvokerHelper.invokeMethodSafe(bean, "addPropertyChangeListener", new Object[] {propertyName, this});
                    boundToProperty = true;
                } catch (MissingMethodException mme1) {
                    try {
                        boundToProperty = false;
                        InvokerHelper.invokeMethodSafe(bean, "addPropertyChangeListener", new Object[] {this});
                    } catch (MissingMethodException mme2) {
                        throw new RuntimeException("Properties in beans of type " + bean.getClass().getName() + " are not observable in any capacity (no PropertyChangeListener support).");
                    }
                }
            }
        }
    }

    public final void unbind() {
        if (bound) {
            if(property != null) {
                try {
                     InvokerHelper.invokeMethodSafe(property, "removeListener", new Object[] {this});
                } catch (MissingMethodException mme) {
                    // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                }
            }else if (boundToProperty) {
                try {
                    InvokerHelper.invokeMethodSafe(bean, "removePropertyChangeListener", new Object[] {propertyName, this});
                } catch (MissingMethodException mme) {
                    // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                }
            } else {
                try {
                    InvokerHelper.invokeMethodSafe(bean, "removePropertyChangeListener", new Object[] {this});
                } catch (MissingMethodException mme2) {
                    // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                }
            }
            property = null;
            bound = false;
        }
    }

    public void rebind() {
        unbind();
        bind();
    }

   

}
