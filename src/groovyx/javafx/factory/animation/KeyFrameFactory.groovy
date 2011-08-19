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
import javafx.beans.*;
import javafx.util.Duration;
import groovyx.javafx.ClosureEventHandler
import groovyx.javafx.factory.AbstractGroovyFXFactory;
/**
 *
 * @author jimclarke
 */
class KeyFrameFactory extends AbstractGroovyFXFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        KeyFrameWrapper keyFrame = new KeyFrameWrapper();
        keyFrame.time = (Duration)value;

        Object action = attributes.remove("onFinished");
        if(action != null) {
            if(action instanceof Closure) {
                keyFrame.onFinished = new ClosureEventHandler(action: action);
            }else {
                keyFrame.onFinished = action;
            }
        }
        return keyFrame;
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        println(child);
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node )  {
        println("KeyFrameFactory.nodeComplete node = ${node}")
    }


	
}

