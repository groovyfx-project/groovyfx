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

import javafx.scene.paint.Paint

/**
 * A factory to create stroke nodes.  A stroke node is a leaf node that can be placed under Shapes or any node
 * with a stroke property.
 * 
 * @author Dean Iverson
 */
class StrokeFactory extends AbstractGroovyFXFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        Paint paint = ColorFactory.get(value)
        if (!paint) {
            throw new RuntimeException("The value passed to the 'stroke' node must be an instance of Paint, " +
                    "LinearGradientBuilder, or RadialGradientBuilder")
        }
        return paint
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child) {
            FXHelper.setPropertyOrMethod(parent, "stroke", child)
        }
    }

    /**
     * @return True. The stroke node is a leaf.
     */
    @Override
    boolean isLeaf() {
        return true;
    }
}
