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

import javafx.builders.LinearGradientBuilder
import javafx.scene.paint.LinearGradient

/**
 * @author Dean Iverson
 */
class LinearGradientFactory extends AbstractGradientFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, LinearGradient))
            return value;
        else
            return new LinearGradientBuilder()
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (node instanceof LinearGradient)
            return false;

        LinearGradientBuilder lgb = node as LinearGradientBuilder

        List<Double> start = attributes.remove("start") as List<Double>
        if (start) {
            lgb.startX(start[0])
            lgb.startY(start[1])
        }

        List<Double> end = attributes.remove("end") as List<Double>
        if (end) {
            lgb.endX(end[0])
            lgb.endY(end[1])
        }

        handleStopsAttributeIfPresent(attributes, lgb)
        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        handleStopNodesIfPresent(builder, node)
    }
}
