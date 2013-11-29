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

import javafx.scene.Node
import javafx.scene.Parent

/**
 * Handles custom nodes and containers.
 * @author jimclarke
 */
class CustomNodeFactory extends AbstractNodeFactory {


    public CustomNodeFactory(Class beanClass) {
        super(beanClass)
    }
    
    public CustomNodeFactory(Class beanClass, boolean leaf) {
        super(beanClass, leaf)
    }
    

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if ((value != null) && checkValue(name, value)) {
            return value;
        } else {
            throw new RuntimeException("$name must have either a value argument that must be of type $beanClass.name");
        }
    }
    

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Parent && child instanceof Node)  {
            parent.children.add(child);
        }else {
            super.setChild(builder, parent, child);
        }
    }

}

