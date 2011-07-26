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

package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.collections.FXCollections
import javafx.scene.chart.PieChart

def pieData = FXCollections.observableArrayList([new PieChart.Data("Yours", 42), new PieChart.Data("Mine", 58)])

GroovyFX.start {
    new SceneGraphBuilder().stage(title: 'Chart Demo (GroovyFX)', width: 1024, height: 960, visible: true) {
        scene {
            tilePane(padding: 10, prefTileWidth: 480) {
                pieChart(data: [first: 0.25f, second: 0.25f, third: 0.25f])

                stackPane(alignment: 'top_right') {
                    pieChart(data: pieData, animated: true)
                    button(text: 'Add Slice', onAction: {pieData.add(new PieChart.Data('Other', 25))})
                }

                lineChart()

                lineChart {
                    numberAxis(lowerBound: 0, upperBound: 3.5, tickUnit: 0.5, autoRanging: false, label: "X Axis")
                    numberAxis(lowerBound: -1, upperBound: 1, tickUnit: 0.2, autoRanging: false, label: "Y Axis")
                }
            }
        }
    }
}
