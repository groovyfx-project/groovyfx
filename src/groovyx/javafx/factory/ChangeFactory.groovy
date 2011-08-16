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

import groovyx.javafx.ClosureChangeListener
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * @author jimclarke
 */
class ChangeFactory extends AbstractFactory {
    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        ClosureChangeListener cl

        if (value instanceof ClosureChangeListener)
            cl = value
        else if (value instanceof String)
            cl = new ClosureChangeListener((String)value);
        else
            cl = new ClosureChangeListener()
        
        return cl;
    }

    @Override
    boolean isHandlesNodeChildren() { true }

    @Override
    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        ((ClosureChangeListener)node).closure = childContent
        return false
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        final ccl = (ClosureChangeListener) node
        if(parent instanceof SceneWrapper) {
            parent.addChangeListener(ccl);
        }else {
            try {
                def property = InvokerHelper.invokeMethod(parent, ccl.property + "Property", null);
                InvokerHelper.invokeMethod(property, "addListener", ccl );
            }catch(MissingMethodException ex) {
                println("No JavaFX property '" + ccl.property + "' for class " + parent.class);
            }
        }
    }
}

