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

import javafx.scene.Node;

/**
 *
 * @author jimclarke
 */
public class ClipFactory extends AbstractFactory {
    
    private static final String CLIP_PROPERTY = "__clip"
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value != null && value instanceof Node) {
            builder.context[CLIP_PROPERTY] = value;
        } 
        
        return this;
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Node) {
            builder.context[CLIP_PROPERTY] = child;
        }
    }
    
    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        def clip = builder.context.remove(CLIP_PROPERTY)
        if (clip) {
            FXHelper.setPropertyOrMethod(parent, "clip", clip)
        }
    }
}


