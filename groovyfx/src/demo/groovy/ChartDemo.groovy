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
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import javafx.scene.layout.Region
import javafx.scene.chart.XYChart
import javafx.scene.shape.Circle
import javafx.scene.control.Button

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

                        lineChart(XAxis: categoryAxis(label: "X Axis")) {
                            numberAxis(lowerBound: -1.2, upperBound: 1.2, tickUnit: 0.2, autoRanging: false,
                                       label: "Y Axis")
                            series(name: 'First Series', data: ["A", 0, "B", 1, "C", -1])
                            series(name: 'Second Series', data: [["A", 0], ["B", -1], ["C", 1], ["D", 0]])
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

                        b = barChart(barGap: 0, categoryGap: 0) {
//                            series(name: 'Sales', data: ['Q1', 1534, 'Q2', 2698, 'Q3', 1945, 'Q4', 3156])
                            //series(name: 'Your Score', data: ['1', 200, '2', 150, 'You', 125, '4', 100, '5', 80, '6', 75])
//                            series(name: 'All Drivers', data: [data])
                        }
                        b.barGap = 0
                        b.categoryGap = 0
                        data1 = new XYChart.Data('1', 200)
                        data2 = new XYChart.Data('2', 150)
                        data3 = new XYChart.Data('3', 125)
                        data4 = new XYChart.Data('4', 110)
//                        data.node = new Rectangle(b.XAxis.categorySpacing, b.YAxis.toNumericValue(125), Color.RED)
//                        b.getData().get(0).getData().set(2, data)
//                        b.getData().get(0).getData().get(2).getNode().setStyle("""
                        reg = new Region()
                        reg.setStyle("""
                          -fx-background-color: grey, white, linear-gradient(lightgreen, green);
                          -fx-background-insets: 0, 1, 2 1 1 1;
                          """)
                        data3.setNode(reg)
//                        b.getData().get(0).getData().get(2).setNode(new Button('Hello'))
                        list = FXCollections.observableArrayList(data1, data2, data3, data4)
                        b.setData(FXCollections.observableArrayList(new XYChart.Series(list)))
                    }
                }
            }
        }
    }
}
