/*
* Copyright 2012 the original author or authors.
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
package groovyx.javafx.factory

import groovyx.javafx.event.*
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue;
import groovyx.javafx.binding.Util;

/**
*
* @author jimclarke
*/
class ChangeFactory extends AbstractFXBeanFactory {
    ChangeFactory(Class beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
         def listener = null;
         if(ChangeListener.isAssignableFrom(beanClass)) {
             if(value != null) {
                   if(value instanceof List) {
                       if(value.size() == 2) {
                           value = Util.getJavaBeanFXProperty(value[0], value[1])
                       }else {
                           throw new RuntimeException("The value of a ${name} must either be a property name in the containing object, or a JavaFX ObservableValue or a JavaBean");
                       }
                   }
                   listener = new GroovyChangeListener(value)
             }else {
                listener = new GroovyChangeListener(name)
             }
         }else {
             if(value != null) {
                   if(value instanceof List) {
                       if(value.size() == 2) {
                           value = Util.getJavaBeanFXProperty(value[0], value[1])
                       }else {
                           throw new RuntimeException("The value of a ${name} must either be a property name in the containing object, or a JavaFX ObservableValue or a JavaBean");
                       }
                   }
                   listener = new GroovyInvalidationListener(value)
             }else {
                listener = new GroovyInvalidationListener(name)
             }
         }   
         listener
    }
    
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        node.closure = childContent
        if(node.observable != null) {
            node.observable.addListener(node);
        }
        
        return false
    }
	
}

