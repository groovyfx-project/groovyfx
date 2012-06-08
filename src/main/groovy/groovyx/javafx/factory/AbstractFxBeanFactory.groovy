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

package groovyx.javafx.factory

/**
 *
 * @author jimclarke
 */
class AbstractFXBeanFactory extends AbstractFactory {
    final Class beanClass
    final boolean leaf
    
    Map<String, Factory> childFactories;

    public AbstractFXBeanFactory(Class beanClass) {
        this(beanClass, false)
    }

    public AbstractFXBeanFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (checkValue(name, value)) {
            return value
        }
        return beanClass.newInstance()
    }

    public boolean checkValue(Object name, Object value) {
        return value != null && beanClass.isAssignableFrom(value.class)
    }

    /**
     * If a factory overrides the onHandleNodeAttributes method then it should return super.onHandleNodeAttributes(...)
     * at the end of the method. This ensures that the remaining attributes are processed by the default handling code
     * in AbstractGroovyFX Factory.
     *
     * The only exception is when the overriding method processes all possible attributes itself and returns false
     * to indicate that no other processing is required.
     */
    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        // set the properties
        // noinspection unchecked
        FXHelper.fxAttributes(node, attributes)
        super.onHandleNodeAttributes(builder, node, attributes);
        return true;
    }
    
    public boolean hasChildFactories() {
        return childFactories != null;
    }
    
    
    
    public void registerFactory(String name, Factory factory) {
        if(childFactories == null)
            childFactories = [(name):factory]
        else
            childFactories.put(name, factory);
    }
    
    public Factory resolveFactory(Object name, Map attributes, Object value) {
        childFactories == null? null : childFactories[name];
    }
}

