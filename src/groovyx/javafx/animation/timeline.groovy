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

ExpandoMetaClass.enableGlobally()
Number.metaClass.getMin = { -> new Duration(delegate*1000.0*60.0)};
Number.metaClass.getS = { -> new Duration(delegate*1000.0)};
Number.metaClass.getMs = { -> new Duration(delegate)};
Number.metaClass.getH = { -> new Duration(delegate*1000.0*60.0*60.0)};


/*****
class TargetHolder {
    Object bean;
    String property;
    Object endValue;
    Interpolator interp = Interpolator.LINEAR;

    public String toString() {
        "bean = ${bean}, property = ${property}, endValue = ${endValue}, interp = ${interp}"
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

***/
def values(Closure c) { c }

def at(Duration duration) {
    def tvs = []
    [ update: { Closure c ->
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
        clone();
        return tvs;
    }]
}

x = 4.0;

def c;

//34567890
at 2.s update values {
    change this,"x" to 5.0 tween "ease_both"
}
println("done, c = ${c}");
