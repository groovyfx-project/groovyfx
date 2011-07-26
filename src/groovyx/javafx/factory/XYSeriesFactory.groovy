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

import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series

/**
 * This factory allows the definition of a series of data points for a XYChart (LineChart, BubbleChart,
 * BarChart, AreaChart, or ScatterChart).  Each series node supports two attributes:
 *
 *      name: A String that names the series and, by default, is displayed in the chart's legend.
 *      data: A List of data points.  If the list is of type ObservableList<XYChart.Data> then it
 *            is used directly.  The list can also be in the format of [x1, y1, x2, y2, ...] or
 *            [[x1, y1], [x2, y2], ...] and it will be translated to an ObservableList<XYChart.Data>.
 *            
 * @author Dean Iverson
 */
class XYSeriesFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, XYChart.Series)) {
            return value
        } else {
            def data = attributes.remove('data')
            return createSeriesForData(data)
        }
    }

    @Override
    boolean isHandlesNodeChildren() { true }

    @Override
    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        return true;
    }

    private def createSeriesForData(data) {
        if (data == null)
            return new XYChart.Series()

        if (data instanceof List) {

        } else if (data instanceof Map) {

        }
    }

    /**
     * Transforms a list of data values into XYChart.Data objects.
     * 
     * @param list The list of data points
     * @return An ObservableList of XYChart.Data objects.
     */
    private ObservableList<XYChart.Data> createXYDataFromList(List list) {

    }
}
