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



import javafx.collections.FXCollections
import javafx.scene.chart.PieChart
import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

def pieData = FXCollections.observableArrayList([new PieChart.Data("Yours", 42), new PieChart.Data("Mine", 58)])

GroovyFX.start {
    new SceneGraphBuilder().stage(title: 'GroovyFX Chart Demo', width: 1024, height: 960, visible: true) {
        scene {
            stackPane {
                scrollPane {
                    tilePane(padding: 10, prefTileWidth: 480, prefColumns: 2) {
                        pieChart(data: [first: 0.25f, second: 0.25f, third: 0.25f])

                        stackPane(alignment: 'top_right') {
                            pieChart(data: pieData, animated: true)
                            button('Add Slice') {
                                onAction {
                                    pieData.add(new PieChart.Data('Other', 25))
                                }
                            }
                        }

                        lineChart(data: [First: [0, 0.25, 0.5, 1.5, 2, 1.0], Second: [0.25, 0, 0.5, 0.5, 1.5, 0.75]])

                        lineChart {
                            numberAxis(lowerBound: 0, upperBound: 3.5, tickUnit: 0.5, autoRanging: false,
                                       label: "X Axis")
                            numberAxis(lowerBound: -1.2, upperBound: 1.2, tickUnit: 0.2, autoRanging: false,
                                       label: "Y Axis")
                            series(name: 'First Series', data: [0, 0, 0.5, 1, 1.5, -1, 2])
                            series(name: 'Second Series', data: [[0, 0], [0.5, -1], [1.5, 1], [2, 0]])
                        }

                        areaChart {
                            series(name: 'First Series', data: [0, 0, 0.5, 0.2, 1.5, 0.6, 2, 0.8])
                            series(name: 'Second Series', data: [0, 0, 0.25, 0.2, 0.5, 0.6, 2.25, 0.4])
                        }

                        bubbleChart {
                            series(name: 'First Series', data: [[0, 0.2, 0.1], [0.5, 0.2, 0.25], [1.5, 0.1, 0.5]])
                            series(name: 'Second Series', data: [[0, 0.1, 0.25], [0.2, 0.5, 0.2]])
                        }

                        scatterChart {
                            series(name: 'First Series', data: [0, 0, 0.5, 0.2, 1.5, 0.6, 2, 0.8])
                            series(name: 'Second Series', data: [0, 0, 0.25, 0.2, 0.5, 0.6, 2.25, 0.4])
                        }

                        barChart {
                            series(name: 'Sales', data: ['Q1', 1534, 'Q2', 2698, 'Q3', 1945, 'Q4', 3156])
                        }
                    }
                }
            }
        }
    }
}
