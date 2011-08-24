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
import javafx.scene.*;

import org.codehaus.groovy.runtime.InvokerHelper;
/**
 * Handles custom nodes and containers.
 * @author jimclarke
 */
class CustomNodeFactory extends NodeFactory {

    final Class restrictedType;
    protected final boolean leaf

    public CustomNodeFactory(Class restrictedType, boolean leaf) {
        this.restrictedType = restrictedType
        this.leaf = leaf
    }

    boolean isLeaf() {
        return leaf
    }


    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value == null) {
            value = attributes.remove(name);
        }
        if ((value != null) && FactoryBuilderSupport.checkValueIsType(value, name, restrictedType)) {
            return value;
        } else {
            throw new RuntimeException("$name must have either a value argument or an attribute named $name that must be of type $restrictedType.name");
        }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Parent && child instanceof Node)  {
            ((Parent)parent).getChildren().add((Node)child);
        }else {
            super.setChild(builder, parent, child);
        }
    }

}

