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
package groovyx.javafx.factory

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
*
* @author jimclarke
*/
class ImageViewFactory extends AbstractNodeFactory {
    
    ImageViewFactory() {
        super(ImageView);
    }

    ImageViewFactory(Class<ImageView> beanClass) {
        super(beanClass);
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        ImageView iv = super.newInstance(builder, name, value, attributes);
        if(value != null) {
            switch(value) {
                case Image:
                    iv.image = value;
                    break;
                case File:
                    iv.image = new Image(value.toURL().toString());
                    break;
                default:
                    iv.image = new Image(value.toString());
                    break;
            }
        }
        iv
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child ) {
        switch(child) {
            case Image:
                parent.image = child;
                break;
            case File:
                parent.image = new Image(value.toURL().toString());
                break;
            case URL:
            case URI:
                parent.image = new Image(value.toString());
                break;
            default:
                super.setChild(builder, parent, child);
                break;
        }
    }
	
}

