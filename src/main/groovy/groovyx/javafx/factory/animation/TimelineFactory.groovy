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

import groovyx.javafx.event.GroovyEventHandler
import groovyx.javafx.factory.AbstractFXBeanFactory
import javafx.animation.Timeline
import groovyx.javafx.factory.FXHelper

/**
 *
 * @author jimclarke
 */
class TimelineFactory extends AbstractFXBeanFactory {

    public List<KeyFrameWrapper> frames;
    public List<GroovyEventHandler> eventHandlers;

    TimelineFactory() {
        super(Timeline)
    }
    TimelineFactory(Class<Timeline> beanClass) {
        super(beanClass)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Timeline timeline 
        frames = new ArrayList<KeyFrameWrapper>();
        def framerate = attributes.remove("framerate");
        if(framerate != null)
            timeline = new Timeline(framerate)
        else
            timeline = new Timeline()
            
        Object onFinished = attributes.remove("onFinished");
        if(onFinished != null) {
            if(onFinished instanceof Closure) {
                timeline.onFinished = new GroovyEventHandler("onFinished", onFinished);
            }else {
                timeline.onFinished = onFinished;
            }
        }
        return timeline;
        
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if(child instanceof KeyFrameWrapper) {
            frames.add(child);
        }else if(child instanceof GroovyEventHandler) {
            FXHelper.setPropertyOrMethod(parent, child.property, child)
        }else {
            super.setChild(build, parent, child);
        }
    }


     public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node )  {
        if(node instanceof Timeline) {
            Timeline tl = (Timeline) node;
            for(KeyFrameWrapper w : frames) {
                tl.getKeyFrames().add(w.createKeyFrame());
            }
        }
    }

	
}

