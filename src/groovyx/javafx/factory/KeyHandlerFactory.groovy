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

import groovyx.javafx.input.GroovyKeyHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author jimclarke
 */
class KeyHandlerFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        EventHandler handler;
        if (FactoryBuilderSupport.checkValueIsType(value, name, EventHandler.class)) {
            handler = value
        } else {
            handler = new GroovyKeyHandler(name);
            def closure = attributes.remove("onEvent");
            if(closure != null && closure instanceof Closure)
                ((GroovyKeyHandler)handler).setClosure(closure);
            else if(value instanceof Closure) {
                handler.closure = value;
            }
        }
        return handler;
    }

}

