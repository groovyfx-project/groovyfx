package groovyx.javafx.factory

import javafx.scene.chart.Chart
import javafx.scene.chart.PieChart
import javafx.collections.ObservableList
import javafx.collections.FXCollections

/**
 * @author Dean Iverson
 */
class ChartFactory extends NodeFactory {
    private final Class<? extends Chart> chartClass


    ChartFactory(Class<? extends Chart> chartClass) {
        if (chartClass == null)
            throw new IllegalArgumentException("chartClass cannot be null")

        this.chartClass = chartClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(value, name, chartClass)) {
            return value
        } else {
            return createChart(attributes)
        }
    }

    private def createChart(Map attributes) {
        def chart = chartClass.newInstance()
        def data = attributes.remove("data")

        if (data) {
            if (chart instanceof PieChart) {
                chart.data = createPieChartData(data)
            }
        }

        return chart
    }

    private ObservableList<PieChart.Data> createPieChartData(data) {
        if (data instanceof ObservableList) {
            return data
        } else if (data instanceof Map) {
            return FXCollections.observableArrayList(((Map)data).collect {k, v -> new PieChart.Data(k,v)})
        } else {
            throw new RuntimeException("Could not recognize the pie chart data '$data'.  Try an ObservableList " +
                    "of PieChart.Data objects or a Map of strings to values: ['Label 1': 75, 'Label 2': 25]")
        }
    }
}
