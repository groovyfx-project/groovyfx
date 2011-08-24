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
import groovyx.javafx.factory.animation.GroovyVariable;
import org.codehaus.groovyfx.javafx.binding.GroovyProperty;
import javafx.beans.value.WritableValue;

/**
 *
 * @author jimclarke
 */
class TargetHolder {
    public WritableValue property;
    public Object bean;
    public String propertyName;
    public Object endValue;
    public Interpolator interpolator = Interpolator.LINEAR;

    public String toString() {
        "bean = ${bean}, property = ${propertyName}, endValue = ${endValue}, interpolator = ${interpolator}"
    }

    public KeyValue getKeyValue() {
        if(property == null) {
            property = new GroovyProperty(bean, propertyName);
        }
        return new KeyValue(property, endValue, interpolator );
    }
    

    def to(value) {
        endValue = value;
        this
    }

    def tween(interp) {
        interp = getInterpolator(interp)
        this
    }

    Interpolator getInterpolator(Object interpolate) {
        if(interpolate == null)
            return Interpolator.LINEAR;
        if(interpolate instanceof Interpolator) {
            return (Interpolator)interpolate;
        }
        String str = interpolate.toUpperCase();
        switch(str) {
            case 'DISCRETE':
                return Interpolator.DISCRETE;
            case 'LINEAR':
                return Interpolator.LINEAR;
            case 'EASE_BOTH':
                return Interpolator.EASE_BOTH;
            case 'EASE_IN':
                return Interpolator.EASE_IN;
            case 'EASE_OUT':
                return Interpolator.EASE_OUT;
            default:
                throw new IllegalArgumentException("Unkown Interpotor string: ${str}")
        }
    }
}

