/*
 * Copyright 2007-2009 the original author or authors.
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
package org.codehaus.groovyfx.javafx.binding;

import groovy.lang.MissingMethodException;
import javafx.beans.value.ObservableValue;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javafx.beans.value.ChangeListener;
import javafx.beans.property.Property;


/**
 * @author <a href="mailto:shemnon@yahoo.com">Danno Ferrin</a>
 * @version $Revision: 17076 $
 * @since Groovy 1.1
 */
public class PropertyBinding implements SourceBinding, TargetBinding, TriggerBinding {

    Object bean;
    String propertyName;
    boolean nonChangeCheck;
    Property fxProperty;
    

    public PropertyBinding(Object bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
    }
    
    public PropertyBinding(Property fxProperty) {
        this.fxProperty = fxProperty;
    }

    @Override
    public void updateTargetValue(Object newValue) {
        if (nonChangeCheck) {
            if (DefaultTypeTransformation.compareEqual(getSourceValue(), newValue)) {
                // not a change, don't fire it
                return;
            }
        }
        
            if(fxProperty != null) {
                fxProperty.setValue(newValue);
            }else {
                try {
                    Object[] args = { bean, propertyName, newValue };
                    InvokerHelper.setProperty(bean, propertyName, newValue);
                } catch (InvokerInvocationException iie) {
                    if (!(iie.getCause() instanceof PropertyVetoException)) {
                        throw iie;
                    }
                    // ignore veto exceptions, just let the binding fail like a validaiton does
                }
            }
    }

    public boolean isNonChangeCheck() {
        return nonChangeCheck;
    }

    public void setNonChangeCheck(boolean nonChangeCheck) {
        this.nonChangeCheck = nonChangeCheck;
    }

    @Override
    public Object getSourceValue() {
        return fxProperty != null ? fxProperty.getValue() :
                        InvokerHelper.getPropertySafe(bean, propertyName);
    }

    @Override
    public FullBinding createBinding(SourceBinding source, TargetBinding target) {
        return new PropertyFullBinding(source, target);
    }

    class PropertyFullBinding extends AbstractFullBinding implements PropertyChangeListener, ChangeListener {

        boolean bound;
        boolean boundToProperty;
        
        PropertyFullBinding(SourceBinding source, TargetBinding target) {
            setSourceBinding(source);
            setTargetBinding(target);
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (boundToProperty || event.getPropertyName().equals(propertyName)) {
                update();
            }
        }

        
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            if(boundToProperty && (observable == bean || observable == fxProperty) ) {
                update();
            }
        }

        @Override
        public void bind() {
            if (!bound) {
                bound = true;
                if(fxProperty == null && bean != null && propertyName != null) {
                    try {
                        fxProperty = (Property)InvokerHelper.invokeMethodSafe(bean, propertyName + "Property", null);
                    } catch (MissingMethodException ignore) {
                        try {
                            // Maybe it's a @FXBindable property?
                            fxProperty = (Property)InvokerHelper.getPropertySafe(bean, propertyName+"Property");
                        } catch (MissingMethodException ignoreAgain) {
                        }
                    }
                }
                if(fxProperty != null) {
                    fxProperty.addListener(this);
                    boundToProperty = true;
                }else {
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

        @Override
        public void unbind() {
            if (bound) {
                if(fxProperty != null) {
                    fxProperty.removeListener(this);
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
                bound = false;
            }
        }

        @Override
        public void rebind() {
            if (bound) {
                unbind();
                bind();
            }
        }

        @Override
        public String toString() {
            return sourceBinding.getSourceValue().toString();
        }


    }


    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
}
