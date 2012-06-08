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

import javafx.scene.image.Image;
import java.io.InputStream
import java.io.ByteArrayInputStream

/**
 *
 * @author jimclarke
 */
class ImageFactory extends AbstractFXBeanFactory {
    ImageFactory() {
        super(Image);
    }
    ImageFactory(Class<Image> beanClass) {
        super(beanClass);
    }
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (checkValue(name, value)) {
            return value
        }
        Image image;
        def url = attributes.remove("url");
        def widthA = attributes.remove("width");
        def heightA = attributes.remove("height");
        def preserveRatio = attributes.remove("preserveRatio");
        def smooth = attributes.remove("smooth");
        def backgroundLoading = attributes.remove("backgroundLoading");
        if(widthA == null)  widthA = 0.0;
        if(heightA == null) heightA = 0.0;
        float width = widthA.floatValue();
        float height = heightA.floatValue();
        if(preserveRatio == null) preserveRatio = false;
        if(smooth == null) smooth = false;
        if(backgroundLoading == null) backgroundLoading = false;
        if(value instanceof byte[]) {
            image = new Image(new ByteArrayInputStream(value), width, height, preserveRatio, smooth)
        }else if(value instanceof InputStream) {
            image = new Image(value, width, height, preserveRatio, smooth)
        }else {
            
            if(url == null && value != null)
                url = value;
            //println(url);
            image = new Image(url.toString(), width, height, preserveRatio, smooth, backgroundLoading);
        }
        return image;
    }
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Image && parent instanceof Image) {
            ((Image)parent).setPlaceHolder((Image)child);
        }
    }

}

