/*
 * Copyright 2011-2015 the original author or authors.
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
import javafx.collections.ObservableList
import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

/**
 * @author Dean Iverson
 */
class XYChartFactory extends AbstractFXBeanFactory {

    XYChartFactory(Class<? extends XYChart> beanClass) {
        super(beanClass)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (checkValue(name,value)) {
            return value
        } else {
            def xAxis = attributes.remove('xAxis')
            def yAxis = attributes.remove('yAxis')

            if(beanClass.isAssignableFrom(BarChart)) {
                // For a bar chart, one of the axes must be a CategoryAxis:
                //   1) If neither axis is defined, the x axis will be the CategoryAxis
                //   2) If both are defined, one of them must be a category axis
                //   3) If only one axis is defined, then create the other as the opposite
                //      of the defined one (i.e. if a NumberAxis is defined, then the other
                //      must be defined as a CategoryAxis and vice-versa.
                if (!xAxis && !yAxis) {
                    // Neither are defined
                    xAxis = new CategoryAxis()
                    yAxis = new NumberAxis()
                } else if (xAxis && yAxis) {
                    // Both are defined.  Neither is a CategoryAxis?
                    if (!(yAxis instanceof CategoryAxis) && !(xAxis instanceof CategoryAxis)) {
                        throw new Exception("In $name either the x or y axis must be a CategoryAxis.")
                    }
                } else if (xAxis) {
                    // Only xAxis is defined
                    yAxis = (xAxis instanceof CategoryAxis) ? new NumberAxis() : new CategoryAxis()
                } else if (yAxis) {
                    // Only yAxis is defined
                    xAxis = (yAxis instanceof CategoryAxis) ? new NumberAxis() : new CategoryAxis()
                }
            } else {
                // For any other kind of chart, just create a default auto-ranging NumberAxis
                // if either x or y axis is not already defined by the user.
                xAxis = xAxis ?: new NumberAxis()
                yAxis = yAxis ?: new NumberAxis()
            }

            def data = attributes['data']
            if (data && (data instanceof Map)) {
                attributes['data'] = createXYSeriesFromMap(data)
            }

            beanClass.newInstance(xAxis, yAxis)
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        def seriesList = builder.context.remove(XYSeriesFactory.SERIES_LIST_PROPERTY)
        if (seriesList) {
            node.setData(FXCollections.observableArrayList(seriesList))
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

