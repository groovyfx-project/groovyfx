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

package groovyx.javafx.factory.animation

import javafx.animation.*;


import javafx.util.Duration;
import groovyx.javafx.event.GroovyEventHandler
import javafx.event.EventHandler
import groovyx.javafx.factory.*
/**
 *
 * @author jimclarke
 */
class KeyFrameFactory extends AbstractFXBeanFactory {
    
    KeyFrameFactory() {
        super(KeyFrameWrapper)
    }
    KeyFrameFactory(Class<KeyFrameWrapper> beanClass) {
        super(beanClass)
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        KeyFrameWrapper keyFrame = super.newInstance(builder, name, value, attributes)
        keyFrame.time = (Duration)value;

        Object onFinished = attributes.remove("onFinished");
        if(onFinished != null) {
            if(onFinished instanceof Closure) {
                keyFrame.onFinished = new GroovyEventHandler("onFinished", onFinished);
            }else if(onFinished instanceof EventHandler) {
                keyFrame.onFinished = onFinished;
            }
        }
        keyFrame;
    }
    
    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if(child instanceof GroovyEventHandler) {
            FXHelper.setPropertyOrMethod(parent, child.property, child)
        }else {
            super.setChild(build, parent, child);
        }
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node )  {
        def keyValues = builder.context[KeyValueFactory.TARGET_HOLDERS_PROPERTY]
        keyValues?.each {
            KeyValue kv = it.getKeyValue();
            ((KeyFrameWrapper)node).values.add(kv);
        }
    }


	
}

