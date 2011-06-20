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

import javafx.scene.paint.Stop

/**
 * Abstract base class containing methods used by both LinearGradientFactory and RadialGradientFactory.
 *
 * @author Dean Iverson
 */
abstract class AbstractGradientFactory extends AbstractFactory {
    /**
     * Checks for the "stops" attribute in the Map.  The value of the stops attribute should be a List
     * of 2-element Lists containing an offset and color: [[0.0, black], [1.0, red]].
     *
     * @param attributes A Map containing the attributes used to create the gradient.  If the stops
     *                   attribute is present, it will be removed from the map and given to the
     *                   gradientBuilder.
     * @param gradientBuilder Either a LinearGradientBuilder or a RadialGradientBuilder.
     */
    protected void handleStopsAttributeIfPresent(Map attributes, gradientBuilder) {
        List stops = attributes.remove("stops")
        if (stops) {
            gradientBuilder.stops(stops.collect {it as Stop})
        }
    }

    /**
     * If any "stop" nodes were specified as child nodes of the gradient node, this method will remove
     * them from the gradient's context (where they are stored during the build process) and set them
     * on the gradientBuilder.
     *
     * @param builderSupport The FactoryBuilderSupport that stores the stop nodes in its context.
     * @param gradientBuilder Either a LinearGradientBuilder or a RadialGradientBuilder.
     */
    protected void handleStopNodesIfPresent(FactoryBuilderSupport builderSupport, gradientBuilder) {
        if (builderSupport.context["stops"]) {
            gradientBuilder.stops = builderSupport.context["stops"]
            builderSupport.context.remove("stops")
        }
    }

    /**
     * @return True if the parent object has a setFill method or a "fill" property.
     */
    protected boolean parentHasFill(parent) {
        return parent.metaClass.respondsTo(parent, "setFill") || parent.metaClass.hasProperty(parent, "fill")
    }
}
