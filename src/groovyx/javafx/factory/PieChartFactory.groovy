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
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart

/**
 * @author Dean Iverson
 */
class PieChartFactory extends NodeFactory {
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, PieChart)) {
            return value
        } else {
            return createChart(attributes)
        }
    }

    private def createChart(Map attributes) {
        def chart = new PieChart()
        def data = attributes.remove("data")

        if (data) {
            chart.data = createPieChartData(data)
        }

        return chart
    }

    private ObservableList<PieChart.Data> createPieChartData(data) {
        if (data instanceof ObservableList) {
            return data
        } else if (data instanceof Map) {
            def dataList = ((Map) data).collect {String k, Double v -> new PieChart.Data(k, v)}
            return FXCollections.observableArrayList(dataList)
        } else {
            throw new RuntimeException("Could not recognize the pie chart data '$data'.  Try an ObservableList " +
                    "of PieChart.Data objects or a Map of strings to values: ['Label 1': 75, 'Label 2': 25]")
        }
    }
}
