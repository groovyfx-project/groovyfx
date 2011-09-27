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
import javafx.scene.paint.LinearGradientBuilder
import javafx.scene.paint.RadialGradientBuilder
import javafx.scene.paint.Color

/**
 * Abstract base class containing methods used by both LinearGradientFactory and RadialGradientFactory.
 *
 * @author Dean Iverson
 */
abstract class AbstractGradientFactory extends AbstractGroovyFXFactory {
    /**
     * Checks for the "stops" attribute in the Map.  The value of the stops attribute should be a List
     * of 2-element Lists containing an offset and color: [[0.0, black], [1.0, red]].  The value can
     * also be a List of colors: [red, black], in which case the stops will be evenly distributed.
     *
     * @param attributes A Map containing the attributes used to create the gradient.  If the stops
     *                   attribute is present, it will be removed from the map and given to the
     *                   gradientBuilder.
     * @param gradientBuilder Either a LinearGradientBuilder or a RadialGradientBuilder.
     */
    protected void handleStopsAttributeIfPresent(Map attributes, gradientBuilder) {
        def stops = attributes.remove("stops")
        if ((stops instanceof List) && stops) {
            if (stops[0] instanceof List) {
                stops = stops.collect { new Stop(it[0], ColorFactory.get(it[1])) }
                gradientBuilder.stops(stops)
            } else {
                gradientBuilder.stops(createStopList(stops))
            }
        }
    }

    /**
     * If any "stop" nodes were specified as child nodes of the gradient node, this method will remove
     * them from the gradient's context (where they are stored by the StopFactory during the build process)
     * and set them on the gradientBuilder.
     *
     * @param builder The FactoryBuilderSupport that stores the stop nodes in its context.
     * @param gradientBuilder Either a LinearGradientBuilder or a RadialGradientBuilder.
     */
    protected void handleStopNodesIfPresent(FactoryBuilderSupport builder, gradientBuilder) {
        def stops = builder.context.remove(StopFactory.STOPS_PROPERTY)
        if (stops) {
            gradientBuilder.stops = stops
        }
    }

    /**
     * Creates and returns a list of Stop objects based on a list of colors, distributing the stops
     * evenly from 0.0 to 1.0 depending on the number of colors.
     * 
     * @param colors A List of colors.
     * @return A List of Stop objects.
     */
    private List<Stop> createStopList(List colors) {
        List<Stop> stopList = []
        if (colors.size() == 1) {
            stopList << new Stop(0.0d, ColorFactory.get(colors[0]))
        } else {
            Double stopStep = 1.0 / (colors.size() - 1);
            colors.eachWithIndex { color, i ->
                stopList << new Stop(i * stopStep, ColorFactory.get(color))
            }
        }
        return stopList
    }
}
