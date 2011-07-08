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
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.RadialGradient
import javafx.builders.LinearGradientBuilder
import javafx.builders.RadialGradientBuilder

/**
 * Abstract base class containing methods used by both LinearGradientFactory and RadialGradientFactory.
 *
 * @author Dean Iverson
 */
abstract class AbstractGradientFactory extends AbstractFactory {
    private static final String GRADIENT_BUILDER = "gradientBuilder"

    /**
     * @return True if the child object is a gradient or a gradient builder.
     */
    public static Boolean childIsGradient(child) {
        return (isGradient(child) || isGradientBuilder(child))
    }

    /**
     * Used as a delegate from the setChild method for factories that need to handle gradients as
     * potential children.  The factory can use the AbstractGradientFactory.childIsGradient method
     * to check whether this method needs to be called for the child it is currently processing.
     *
     * IMPORTANT: If a factory calls this method from it's setChild method then it MUST also call
     * AbstractGradientFactory.onNodeCompletedWithGradient from its onNodeCompleted method.
     *
     * @param builder The FactoryBuilderSupport object
     * @param parent The parent object
     * @param child The child object in question
     */
    public static void setChildGradient(FactoryBuilderSupport builder, Object parent, Object child) {
        if (hasFill(parent)) {
            if (isGradient(child)) {
                parent.fill = child
            } else if (isGradientBuilder(child) && !builder.parentContext.containsKey(GRADIENT_BUILDER)) {
                // Set a flag so the gradient builder can be processed
                // when this node is complete
                builder.parentContext[GRADIENT_BUILDER] = child
            }
        }
    }

    /**
     * Used as a delegate from the onNodeCompleted method for factories that need to handle gradients as
     * potential children.
     *
     * IMPORTANT: If a factory calls this method from it's onNodeCompleted method then it MUST also call
     * AbstractGradientFactory.setChildGradient from its setChild method.
     *
     * @param builder The FactoryBuilderSupport object
     * @param parent The parent object
     * @param node The node being completed.
     */
    public static void onNodeCompletedWithGradient(FactoryBuilderSupport builder, Object parent, Object node) {
        def gradientBuilder = builder.context.remove(GRADIENT_BUILDER)
        if (gradientBuilder) {
            node.fill = gradientBuilder.build()
        }
    }

    /**
     * @return True if the child object is either a linear or radial gradient.
     */
    private static boolean isGradient(child) {
        return (child instanceof LinearGradient) || (child instanceof RadialGradient)
    }

    /**
     * @return True if the child object is either a linear or radial gradient builder.
     */
    private static boolean isGradientBuilder(child) {
        return (child instanceof LinearGradientBuilder) || (child instanceof RadialGradientBuilder)
    }

    /**
     * @return True if the parent object has a setFill method or a "fill" property.
     */
    private static boolean hasFill(node) {
        return node.metaClass.respondsTo(node, "setFill") || node.metaClass.hasProperty(node, "fill")
    }

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
        List stops = builderSupport.context.remove("stops")
        if (stops) {
            gradientBuilder.stops = stops
        }
    }
}
