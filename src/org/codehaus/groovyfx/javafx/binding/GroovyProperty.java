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

package org.codehaus.groovyfx.javafx.binding;

//TODO import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;

/**
 * Bridges javafx property to Groovy property
 * @author jimclarke
 */
public class GroovyProperty extends ObjectProperty /* TODO SimpleObjectProperty */{
    
    public GroovyProperty(Object bean, String propertyName) {
        super(bean, propertyName);
    }
    
    @Override
    public Object get() {
        Object rs = super.get();
        return InvokerHelper.getProperty(getBean(), this.getName());
    }
    
    @Override
    public void set(Object v) {
        super.set(v);
        if(v != null) {
            try {
                InvokerHelper.setProperty(getBean(), this.getName(), v);
            } catch(InvokerInvocationException e) {
                System.out.println(e);
            }
        }
    }
}
