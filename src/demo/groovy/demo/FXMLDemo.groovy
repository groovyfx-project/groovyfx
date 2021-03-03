/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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
import static groovyx.javafx.GroovyFX.start

def xml = """
    <?import javafx.scene.*?>
    <?import javafx.scene.shape.*?>
    <?import javafx.scene.control.*?>
    <Group xmlns:fx="http://javafx.com/fxml">
            <children>
                 <Rectangle x="20.0" y="40.0" width="50.0" height="150" fill="blue" />
                 <Rectangle x="150.0" y="80.0" width="100.0" height="100" fill="red" />
                 <Rectangle x="10.0" y="200.0" width="70.0" height="20" fill="green" />
            </children>
    </Group>
    """

start {
    stage(title: "GroovyFX FXML Demo", visible: true) {
        scene(fill: GROOVYBLUE, width: 640, height: 800) {
            vbox(padding: 10) {
                stackPane {
                    fxml """
                        <?import javafx.scene.*?>
                        <?import javafx.scene.shape.*?>
                        <Group xmlns:fx="http://javafx.com/fxml">
                                <clip>
                                  <Rectangle height="200.0" width="400.0" strokeWidth="0.0050"/>
                                </clip>
                                <children>
                                     <Rectangle x="10.0" y="10.0" width="100.0" height="50" fill="blue" />
                                     <Rectangle x="50.0" y="80.0" width="100.0" height="100" fill="red" />
                                     <Rectangle x="100.0" y="150.0" width="75.0" height="80" fill="green" />
                                </children>
                        </Group>
                        """

                }
                stackPane {
                    fxml xml
                }
                stackPane {
                    fxml resource("/demo/FXMLDemo.fxml"), {
                        onMouseEntered { println "Entered"}
                    }
                }
                stackPane {
                    fxml resource("/demo/FXMLDemoInline.fxml"), {
                        onMouseEntered { println "Entered"}
                        buttonCSSID.onAction {
                            labelCSSID.text = "Clicked - CSS";
                        }
                        buttonFXID.onAction {
                            labelFXID.text = "Clicked - FXML";
                        }
                    }
                }
            }
        }
    }
}




