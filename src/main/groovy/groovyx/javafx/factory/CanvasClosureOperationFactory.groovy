/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2019 the original author or authors.
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

import groovyx.javafx.canvas.ClosureOperation

/**
*
* @author jimclarke
*/
class CanvasClosureOperationFactory extends AbstractFXBeanFactory {
    
    CanvasClosureOperationFactory() {
        super(ClosureOperation)
    }
    CanvasClosureOperationFactory(Class<ClosureOperation> beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
         Object instance = super.newInstance(builder,name,value, attributes);
         if(value != null && value instanceof Closure)
            instance.closure = value;
         instance;
    }
    
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        node.closure = childContent
        return false
    }
	
}

