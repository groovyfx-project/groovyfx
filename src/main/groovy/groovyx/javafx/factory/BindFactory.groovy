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


package groovyx.javafx.factory

import javafx.beans.property.*;
import javafx.beans.property.adapter.*;
import javafx.beans.value.*;
import groovyx.javafx.binding.*


import org.codehaus.groovy.runtime.InvokerHelper;

/**
* bind myTextField.text() to myButton.label(), { optionalConversion(it) }
* @author jimclarke
*/
class BindFactory extends AbstractFXBeanFactory {
    
    private static final String BIND_TO_PROPERTY = "__bindToList"
    
    public BindFactory() {
        super(ObservableValue)
    }
    
    public BindFactory(Class<ObservableValue> beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
          BindingHolder bh = new BindingHolder(value);
          bh;
    }
    
    public boolean isHandlesNodeChildren() {
        return true;
    }
    
     public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        node.observable = new GroovyClosureProperty(childContent);
        false
    }
    
     @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node)
    }
	
}