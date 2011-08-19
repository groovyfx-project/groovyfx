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

import javafx.scene.shape.*;

/**
 *
 * @author jimclarke
 */
class PathElementFactory extends AbstractGroovyFXFactory {
    private final Class<? extends PathElement> pathElementClass

    PathElementFactory(Class<? extends PathElement> pathElementClass) {
        this.pathElementClass = pathElementClass
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attr) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, pathElementClass)) {
            return value
        } else {
            return pathElementClass.newInstance()
        }
    }


//    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
//        PathElement pe;
//        if (FactoryBuilderSupport.checkValueIsType(value, name, PathElement.class)) {
//            pe = value
//        } else {
//            switch(name) {
//                case 'arcTo':
//                    pe = new ArcTo();
//                    break;
//                case 'closePath':
//                    pe = new ClosePath();
//                    break;
//                case 'cubicCurveTo':
//                    pe = new CubicCurveTo();
//                    break;
//                case 'hLineTo':
//                    pe = new HLineTo();
//                    break;
//                case 'lineTo':
//                    pe = new LineTo();
//                    break;
//                case 'moveTo':
//                    pe = new MoveTo();
//                    break;
//                case 'quadCurveTo':
//                    pe = new QuadCurveTo();
//                    break;
//                case 'vLineTo':
//                    pe = new VLineTo();
//                    break;
//                default:
//                    println "Cannot Handle path element = ${name}"
//            }
//        }
//
//        return pe;
//    }
	
}

