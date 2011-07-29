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


import groovyx.javafx.factory.FXHelper;
import javafx.beans.value.ChangeListener;


/**
 * @author <a href="mailto:shemnon@yahoo.com">Danno Ferrin</a>
 * @version $Revision: 17076 $
 * @since Groovy 1.1
 */
public class PropertyBinding implements SourceBinding, TargetBinding, TriggerBinding {

    Object bean;
    String propertyName;
    boolean nonChangeCheck;

    public PropertyBinding(Object bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
    }

    @Override
    public void updateTargetValue(Object newValue) {
        if (nonChangeCheck) {
            if (DefaultTypeTransformation.compareEqual(getSourceValue(), newValue)) {
                // not a change, don't fire it
                return;
            }
        }
        try {
            Object[] args = { bean, propertyName, newValue };
            if(!(Boolean)InvokerHelper.invokeStaticMethod(FXHelper.class, "fxAttribute", args ) )
                InvokerHelper.setProperty(bean, propertyName, newValue);
        } catch (InvokerInvocationException iie) {
            if (!(iie.getCause() instanceof PropertyVetoException)) {
                throw iie;
            }
            // ignore veto exceptions, just let the binding fail like a validaiton does
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
        return InvokerHelper.getPropertySafe(bean, propertyName);
    }

    @Override
    public FullBinding createBinding(SourceBinding source, TargetBinding target) {
        return new PropertyFullBinding(source, target);
    }

    class PropertyFullBinding extends AbstractFullBinding implements PropertyChangeListener, ChangeListener {

        Object boundBean;
        Object boundProperty;
        boolean bound;
        boolean boundToProperty;
        ObservableValue property;

        PropertyFullBinding(SourceBinding source, TargetBinding target) {
            setSourceBinding(source);
            setTargetBinding(target);
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (boundToProperty || event.getPropertyName().equals(boundProperty)) {
                update();
            }
        }

        
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            if(boundToProperty && (observable == bean || observable == property) ) {
                update();
            }
        }

        @Override
        public void bind() {
            if (!bound) {
                bound = true;
                boundBean = bean;
                boundProperty = propertyName;
                try {
                    property = (ObservableValue)InvokerHelper.invokeMethodSafe(boundBean, propertyName + "Property", null);
                    try {
                        InvokerHelper.invokeMethodSafe(property, "addListener", new Object[] {this});
                        boundToProperty = true;
                    } catch (MissingMethodException mme1) {
                        boundToProperty = false;
                        throw new RuntimeException("Properties in beans of type " + boundBean.getClass().getName() + " are not a JavaFX Bean).");
                    }
                }catch (MissingMethodException mme) {
                    try {
                        InvokerHelper.invokeMethodSafe(boundBean, "addPropertyChangeListener", new Object[] {boundProperty, this});
                        boundToProperty = true;
                    } catch (MissingMethodException mme1) {
                        try {
                            boundToProperty = false;
                            InvokerHelper.invokeMethodSafe(boundBean, "addPropertyChangeListener", new Object[] {this});
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
                if(property != null) {
                    try {
                         InvokerHelper.invokeMethodSafe(property, "removeListener", new Object[] {this});
                    } catch (MissingMethodException mme) {
                        // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                    }
                }else if (boundToProperty) {
                    try {
                        InvokerHelper.invokeMethodSafe(boundBean, "removePropertyChangeListener", new Object[] {boundProperty, this});
                    } catch (MissingMethodException mme) {
                        // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                    }
                } else {
                    try {
                        InvokerHelper.invokeMethodSafe(boundBean, "removePropertyChangeListener", new Object[] {this});
                    } catch (MissingMethodException mme2) {
                        // ignore, too bad so sad they don't follow conventions, we'll just leave the listener attached
                    }
                }
                boundBean = null;
                boundProperty = null;
                property = null;
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
