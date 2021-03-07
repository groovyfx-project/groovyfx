/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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

import groovyx.javafx.factory.AbstractFXBeanFactory
import javafx.animation.Interpolator

/**
 *
 * @author jimclarke
 */
class KeyValueSubFactory  extends AbstractFXBeanFactory  {
    
    KeyValueSubFactory (Class beanClass) {
        super(beanClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(Interpolator.isAssignableFrom(beanClass)) {
              return getInterpolator(value);      
        }else {
            return value;
        }
     }

    static Interpolator getInterpolator(Object interpolate) {
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

