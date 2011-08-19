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

/**
 *
 * @author jimclarke
 */
class ImageFactory extends AbstractGroovyFXFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof Image) {
            return value;
        }
        Image image;
        def url = attributes.remove("url");
        if(url == null && value != null)
            url = value;
        //println(url);
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

        image = new Image(url.toString(), width, height, preserveRatio, smooth, backgroundLoading);
        //FXHelper.fxAttributes(image, attributes);
        return image;
    }
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Image && parent instanceof Image) {
            ((Image)parent).setPlaceHolder((Image)child);
        }
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {

    }
}

