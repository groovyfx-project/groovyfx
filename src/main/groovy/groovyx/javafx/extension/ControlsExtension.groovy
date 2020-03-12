/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2020 the original author or authors.
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
package groovyx.javafx.extension

import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.control.ButtonBase
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ComboBox
import javafx.scene.control.ListView
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TreeView
import javafx.stage.Window
import javafx.util.Callback

/**
 * @author Andres Almiray
 */
class ControlsExtension {
    static void cellFactory(ComboBox self, Closure closure) { self.setCellFactory(closure as Callback) }

    static void onSelect(ComboBox self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void onSelect(ChoiceBox self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void onAction(ButtonBase self, Closure closure) {
        self.setOnAction(closure as EventHandler)
    }

    static void cellFactory(ListView self, Closure closure) { self.setCellFactory(closure as Callback) }

    static void onSelect(ListView self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void onSelect(TabPane self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void rowFactory(TableView self, Closure closure) { self.setRowFactory(closure as Callback) }

    static void columnResizePolicy(TableView self, Closure closure) { self.setColumnResizePolicy(closure as Callback) }

    static void sortPolicy(TableView self, Closure closure) { self.setSortPolicy(closure as Callback) }

    static void onSelect(TableView self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void cellFactory(TableColumn self, Closure closure) { self.setCellFactory(closure as Callback) }

    static void cellValueFactory(TableColumn self, Closure closure) { self.setCellValueFactory(closure as Callback) }

    static void onEditCancel(TableColumn self, Closure closure) { self.setOnEditCancel(closure as EventHandler) }

    static void onEditCommit(TableColumn self, Closure closure) { self.setOnEditCommit(closure as EventHandler) }

    static void onEditStart(TableColumn self, Closure closure) { self.setOnEditStart(closure as EventHandler) }

    static void cellFactory(TreeView self, Closure closure) { self.setCellFactory(closure as Callback) }

    static void onSelect(TreeView self, Closure closure) {
        self.selectionModel.selectedItemProperty().addListener(closure as ChangeListener)
    }

    static void onEditCancel(TreeView self, Closure closure) { self.setOnEditCancel(closure as EventHandler) }

    static void onEditCommit(TreeView self, Closure closure) { self.setOnEditCommit(closure as EventHandler) }

    static void onEditStart(TreeView self, Closure closure) { self.setOnEditStart(closure as EventHandler) }

    static void onClosed(Tab self, Closure closure) { self.setOnClosed(closure as EventHandler) }

    static void onCloseRequest(Tab self, Closure closure) { self.setOnCloseRequest(closure as EventHandler) }

    static void onSelectionChanged(Tab self, Closure closure) { self.setOnSelectionChanged(closure as EventHandler) }

    static void onHidden(Menu self, Closure closure) { self.setOnHidden(closure as EventHandler) }

    static void onHiding(Menu self, Closure closure) { self.setOnHiding(closure as EventHandler) }

    static void onShowing(Menu self, Closure closure) { self.setOnShowing(closure as EventHandler) }

    static void onShown(Menu self, Closure closure) { self.setOnShown(closure as EventHandler) }

    static void onAction(Menu self, Closure closure) { self.setOnAction(closure as EventHandler) }

    static void onMenuValidation(Menu self, Closure closure) { self.setOnMenuValidation(closure as EventHandler) }

    static void onHidden(Window self, Closure closure) { self.setOnHidden(closure as EventHandler) }

    static void onHiding(Window self, Closure closure) { self.setOnHiding(closure as EventHandler) }

    static void onShowing(Window self, Closure closure) { self.setOnShowing(closure as EventHandler) }

    static void onShown(Window self, Closure closure) { self.setOnShown(closure as EventHandler) }

    static void onCloseRequest(Window self, Closure closure) { self.setOnCloseRequest(closure as EventHandler) }

    static void onAction(MenuItem self, Closure closure) { self.setOnAction(closure as EventHandler) }
}