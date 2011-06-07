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
class ShapeFactory  extends NodeFactory {


    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Shape shape;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Shape.class)) {
            shape = value
        } else {
            switch(name) {
                case 'arc':
                    shape = new Arc();
                    break;
                case 'circle':
                    shape = new Circle();
                    break;
                case 'cubicCurve':
                    shape = new CubicCurve();
                    break;
                case 'ellipse':
                    shape = new Ellipse();
                    break;
                case 'line':
                    shape = new Line();
                    break;
                case 'polygon':
                    shape = new Polygon();
                    break;
                case 'polyline':
                    shape = new Polyline();
                    break;
                case 'quadCurve':
                    shape = new QuadCurve();
                    break;
                case 'rectangle':
                    shape = new Rectangle();
                    break;
                case 'SVGPath':
                    shape = new SVGPath();
                    break;
                case 'path':
                    shape = new Path();
                    break;
                default:
                    println "Cannot Handle shape = ${name}"
            }
        }

        //FXHelper.fxAttributes(shape, attributes);

        return shape;
    }
}

