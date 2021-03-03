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

import javafx.scene.paint.Paint

/**
 * A factory to create fill nodes.  A fill node is a leaf node that can be placed under Shapes and Scenes - anything
 * with a fill property.
 * 
 * @author Dean Iverson
 */
class FillFactory extends AbstractFXBeanFactory {
    private static final String FILL_PROPERTY = "__fill"
    
    FillFactory() {
        super(Paint, true)
    }
    FillFactory(Class<Paint> beanClass) {
        super(beanClass, true)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        Paint paint = ColorFactory.get(value)
        if (!paint) {
            throw new RuntimeException("The value passed to the 'fill' node must be an instance of Paint, " +
                    "LinearGradientBuilder, or RadialGradientBuilder")
        }
        return paint
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child) {
            FXHelper.setPropertyOrMethod(parent, "fill", child)
        }
    }

}
