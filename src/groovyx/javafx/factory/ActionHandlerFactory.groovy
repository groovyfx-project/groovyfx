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

import groovyx.javafx.event.GroovyActionHandler
import javafx.event.EventHandler

/**
 * @author Dean Iverson
 */
class ActionHandlerFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        EventHandler handler

        if (FactoryBuilderSupport.checkValueIsType(value, name, EventHandler.class)) {
            handler = (EventHandler)value
        } else {
            handler = new GroovyActionHandler(name.toString())
        }

        return handler
    }

    @Override
    boolean isHandlesNodeChildren() { true }

    @Override
    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        if(childContent)
            ((GroovyActionHandler)node).setClosure(childContent)
        return false
    }
}

