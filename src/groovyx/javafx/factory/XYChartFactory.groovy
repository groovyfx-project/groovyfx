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

import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

/**
 * @author Dean Iverson
 */
class XYChartFactory extends AbstractFactory {
    private final Class<? extends XYChart> chartClass

    XYChartFactory (Class<? extends XYChart> chartClass) {
        if (chartClass == null)
            throw new IllegalArgumentException("chartClass cannot be null")

        this.chartClass = chartClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, chartClass)) {
            return value
        } else {
            def builderClass = Class.forName("javafx.builders.${chartClass.getSimpleName()}Builder")
            def chartBuilder = builderClass.newInstance()

            // Set default axes so the builder doesn't blow up with a NPE
            chartBuilder.XAxis(new NumberAxis(0, 1, 0.2))
            chartBuilder.YAxis(new NumberAxis(0, 1, 0.2))

            return chartBuilder
        }
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def animated = attributes.remove('animated') ?: false
        FXHelper.setPropertyOrMethod(node, 'animated', animated)
    }


}

