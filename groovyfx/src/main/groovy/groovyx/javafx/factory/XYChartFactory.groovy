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

import javafx.collections.FXCollections
import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.collections.ObservableList

/**
 * @author Dean Iverson
 */
class XYChartFactory extends AbstractGroovyFXFactory {
    private final Class<? extends XYChart> chartClass

    XYChartFactory(Class<? extends XYChart> chartClass) {
        if (chartClass == null)
            throw new IllegalArgumentException("chartClass cannot be null")

        this.chartClass = chartClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, chartClass)) {
            return value
        } else {
            def builderClass = Class.forName("javafx.scene.chart.${chartClass.getSimpleName()}Builder")
            def chartBuilder = builderClass.newInstance()

            // Set default axes so the builder doesn't blow up with a NPE
            // TODO: Set the axes correctly depending on bar chart orientation
            if (chartClass == BarChart)
                chartBuilder.XAxis(new CategoryAxis())
            else
                chartBuilder.XAxis(new NumberAxis())

            chartBuilder.YAxis(new NumberAxis())
            return chartBuilder
        }
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def data = attributes.remove('data')
        if (data) {
            if (data instanceof Map) {
                data = createXYSeriesFromMap(data)
            }
            FXHelper.setPropertyOrMethod(node, 'data', data)
        }

        attributes.each { name, value ->
            FXHelper.setPropertyOrMethod(node, name, value)
        }

        attributes.clear()
        return false;
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        def seriesList = builder.context.remove(XYSeriesFactory.SERIES_LIST_PROPERTY)
        if (seriesList) {
            node.data(FXCollections.observableArrayList(seriesList))
        }
    }

    private ObservableList<XYChart.Series> createXYSeriesFromMap(Map map) {
        def seriesList = []
        map.each { name, data ->
            if (data instanceof List)
                seriesList << new XYChart.Series(name, XYSeriesFactory.createXYDataFromList(data))
        }
        
        return FXCollections.observableArrayList(seriesList)
    }
}

