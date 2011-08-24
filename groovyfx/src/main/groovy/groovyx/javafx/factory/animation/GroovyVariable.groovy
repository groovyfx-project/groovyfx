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

package groovyx.javafx.factory.animation

import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import java.beans.PropertyVetoException;
import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 * bridges a javafx property over to a groovy property so that
 * things like animations can update the groovy property.
 * 
 * @author jimclarke
 */
class GroovyVariable  implements ChangeListener, WritableValue {

    Object bean;
    String property;

    Object variable;

    public void setBean(Object bean) {
        this.bean = bean;
        initialize();
    }

    public void setProperty(String property) {
        this.property = property;
        initialize();
    }

    private void initialize() {
        if(bean != null && property != null) {
            if(bean.class == Boolean.TYPE || bean.class == Boolean.class) {
                  variable = new SimpleBooleanProperty(bean, property);
            }else if (bean.class == Byte.TYPE || bean.class == Byte.class) {
                  variable = new SimpleIntegerProperty(bean, property);
            }else if (bean.class == Double.TYPE || bean.class == Double.class) {
                  variable = new SimpleDoubleProperty(bean, property);
            }else if (bean.class == Float.TYPE || bean.class == Float.class) {
                  variable = new SimpleFloatProperty(bean, property);
            }else if (bean.class == Integer.TYPE || bean.class == Integer.class) {
                  variable = new SimpleIntegerProperty(bean, property);
            }else if (bean.class == Long.TYPE || bean.class == Long.class) {
                  variable = new SimpleLongProperty(bean, property);
            }else if (bean.class == Short.TYPE || bean.class == Short.class) {
                  variable = new SimpleIntegerProperty(bean, property);
            }else if (bean.class == String.class) {
                  variable = new SimpleStringProperty(bean, property);
            }else {
                  variable = new SimpleObjectProperty(bean, property);
            }
            variable.addListener(this);
        }

    }
    
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        setPropertyValue(newValue);
    }


    public Object getValue() {
        Object value = null;
        try {
            value = InvokerHelper.getProperty(bean, property);
        } catch (InvokerInvocationException iie) {
            if (!(iie.getCause() instanceof PropertyVetoException)) {
                throw iie;
            }
            // ignore veto exceptions, just let the binding fail like a validaiton does
        }
        return value;
    }

    private void setPropertyValue(Object newValue) {
        try {
            InvokerHelper.setProperty(bean, property, newValue);
        } catch (InvokerInvocationException iie) {
            if (!(iie.getCause() instanceof PropertyVetoException)) {
                throw iie;
            }
            // ignore veto exceptions, just let the binding fail like a validaiton does
        }
    }

    /**
     * sets the Variable property to this value, that interns sets the Groovy property to this value
     */
    public void setValue(Object newValue) {
        variable.setValue(newValue);
    }

}

