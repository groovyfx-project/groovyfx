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

import javafx.scene.paint.RadialGradient
import javafx.builders.RadialGradientBuilder

/**
 * @author Dean Iverson
 */
class RadialGradientFactory extends AbstractGradientFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, RadialGradient))
            return value;
        else
            return new RadialGradientBuilder()
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (node instanceof RadialGradient)
            return false;

        RadialGradientBuilder rgb = node as RadialGradientBuilder

        List<Double> center = attributes.remove("center") as List<Double>
        if (center) {
            rgb.centerX(center[0])
            rgb.centerY(center[1])
        }

        handleStopsAttributeIfPresent(attributes, rgb)
        return true;
    }


    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (parentHasFill(parent)) {
            if (node instanceof RadialGradient)
                parent.fill = (node as RadialGradient)
            else{
                handleStopNodesIfPresent(builder, node)
                parent.fill = node.build()
            }
        }
    }
}
