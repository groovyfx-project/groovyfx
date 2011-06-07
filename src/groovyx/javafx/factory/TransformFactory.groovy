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

import javafx.scene.transform.*;

/**
 *
 * @author jimclarke
 */
class TransformFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Transform transform;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Transform.class)) {
            transform = (Transform)value
        } else {
            switch(name) {
                case 'affine':
                    transform = new Affine();
                    break;
                case 'rotate':
                    transform = new Rotate();
                    break;
                case 'scale':
                    transform = new Scale();
                    break;
                case 'shear':
                    transform = new Shear();
                    break;
                case 'translate':
                    transform = new Translate();
                    break;
            }
        }

        //FXHelper.fxAttributes(transform, attributes);
        return transform;
    }

}

