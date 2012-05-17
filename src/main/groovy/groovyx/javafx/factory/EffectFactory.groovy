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

import javafx.scene.effect.*

/**
 *
 * @author jimclarke
 */
class EffectFactory extends AbstractFXBeanFactory {
    public static final String ATTACH_EFFECT_KEY = '__attachEffect'
    
    EffectFactory(Class beanClass) {
        super(beanClass)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        def effect = super.newInstance(builder, name, value, attributes);
        if(EffectWrapper.isAssignableFrom(beanClass)) {
            effect.property = name;
        }
        effect;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Effect) {
            if(parent.metaClass.hasProperty(parent, "input")) {
               FXHelper.setPropertyOrMethod(parent, "input", child)
            } else if (parent instanceof EffectWrapper) {
                parent.effect =  child;
            } else {
                super.setChild(build, parent, child);
            }
        } else if (parent instanceof ImageInput) {
                parent.source = child; // image
        } else if (parent instanceof Blend) {
            // do nothing
        } else if (parent instanceof Lighting) {
            if (child instanceof Light) {
                parent.light = child;
            }else {
                super.setChild(builder, parent, child);
            }
        } else {
            super.setChild(builder, parent, child);
        }
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child) {
            FXHelper.setPropertyOrMethod(parent, "effect", child)
        }
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(node instanceof EffectWrapper) {
            if (parent instanceof Blend) {
                if (node.property == "topInput") {
                    parent.topInput = node.effect;
                } else if (node.property == "bottomInput") {
                    parent.bottomInput = node.effect;
                }
            } else if (parent instanceof Lighting) {
                if (node.property == "bumpInput") {
                    parent.bumpInput = node.effect;
                } else if (node.property == "contentInput") {
                    parent.contentInput = node.effect;
                }
            }
        }
    }
}

