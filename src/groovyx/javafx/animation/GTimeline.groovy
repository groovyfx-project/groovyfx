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

package groovyx.javafx.animation

import javafx.animation.*;
import javafx.util.Duration;
import com.sun.javafx.functions.Function0;

/**
 *
 * @author jimclarke
 */


class FunctionWrapper implements Function0 {
    public Closure closure;

    public Object invoke() {
        return closure.call();
    }
}

class GTimeline {
    
    static timeline(Closure c) {

        ExpandoMetaClass.enableGlobally()
        Number.metaClass.getMin = { -> new Duration(delegate*1000.0*60.0)};
        Number.metaClass.getS = { -> new Duration(delegate*1000.0)};
        Number.metaClass.getMs = { -> new Duration(delegate)};
        Number.metaClass.getH = { -> new Duration(delegate*1000.0*60.0*60.0)};
        def clone = c.clone()
        clone.delegate = new GTimeline()
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone()
        return clone.delegate.timeline;
    }

    Timeline timeline = new Timeline();

    KeyFrame currentFrame;

    def rate(float val) { timeline.rate = val;}

    def time(Duration duration) { timeline.time = duration}

    def interpolate(boolean val) { timeline.interpolate = val}

    def repeatCount(Integer count) { timeline.repeatCount = count; }

    def autoReverse(boolean val) { timeline.autoReverse = val}


    def frames(Closure c) {
        def clone = c.clone()
        clone.delegate = this
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone()
    }

    def at( Duration duration, Closure a) {
        currentFrame = new KeyFrame(duration);
        timeline.getKeyFrames().add(currentFrame);
        def cloneA = a.clone()
        cloneA.delegate = this
        cloneA.resolveStrategy = Closure.DELEGATE_ONLY
        cloneA()
    }

    def values(Closure c) {
        def tvs = []
        def clone = c.clone();
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone.delegate = new Expando([
            change:{ Object bean, String property ->
                def change = new TargetHolder(bean: bean, property: property);
                println("change = ${change}");
                tvs << change
                change;

            }
        ])
        clone()
        for(it in tvs) {
            currentFrame.getValues().add(it.getKeyValue());
        }

    }


    def action (Closure c) { currentFrame.action = new FunctionWrapper(closure: c) }
	
}



