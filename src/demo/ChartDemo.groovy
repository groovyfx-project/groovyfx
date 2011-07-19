package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.chart.PieChart

def pieData = FXCollections.observableArrayList([new PieChart.Data("Yours", 42), new PieChart.Data("Mine", 58)])

GroovyFX.start {
    new SceneGraphBuilder().stage(title: 'Chart Demo (GroovyFX)', width: 1024, visible: true) {
        scene {
            tilePane {
                pieChart(data: [first: 0.25f, second: 0.25f, third: 0.25f])
                pieChart(data: pieData)
            }
        }
    }
}
