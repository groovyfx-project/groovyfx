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
package groovyx.javafx.factory

import groovyx.javafx.event.GroovyCallback
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ContentDisplay
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import org.codehaus.groovy.runtime.InvokerHelper

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 *
 * @author jimclarke
 */

class EditingCallback implements javafx.util.Callback {
    Object call(Object column) {
        return new EditingCell();
    }
}

class EditingCell extends TableCell implements EventHandler {
    private TextField textField;

    EditingCell() {
        this.setEditable(true);
    }


    @Override
    void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }

        if (textField == null) {
            textField = new TextField();
            textField.setOnKeyReleased(this);
        } 
        textField.setText(getItem().toString());
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    void commitEdit(Object t) {
        super.commitEdit(t);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            if (textField != null) {
                textField.setText(item.toString());
            }
            setText(item.toString());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    void handle(Event t) {
        if (t.getCode() == KeyCode.ENTER) {
            commitEdit(textField.getText());
        } else if (t.getCode() == KeyCode.ESCAPE) {
            cancelEdit();
        }
    }

}

class EnumEditingCallback implements javafx.util.Callback {
    public Class enumClass;

    EnumEditingCallback(Class enumClass) {
        this.enumClass = enumClass;
    }

    Object call(Object column) {
        return new EnumEditingCell(enumClass);
    }
}


// Generic enum choicebox editor.
class EnumEditingCell extends TableCell implements ChangeListener {
    private ChoiceBox choiceBox;
    public Class enumClass;

    EnumEditingCell(Class enumClass) {
        this.setEditable(true);
        this.enumClass = enumClass;
    }

    @Override
    void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }

        if (choiceBox == null) {
            createChoiceBox();
        } else {
            choiceBox.getSelectionModel().select(getItem());
        }
        setGraphic(choiceBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        choiceBox.requestFocus();
    }

    @Override
    void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    void commitEdit(Object t) {
        super.commitEdit(t);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            if (choiceBox != null) {
                choiceBox.getSelectionModel().select(item);
            }
            setText(item.name());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    private void createChoiceBox() {
        choiceBox = new ChoiceBox();
        ObservableList items = FXCollections.observableArrayList();
        try {
            Method valuesMethod = enumClass.getMethod("values");
            Object[] values = (Object[])valuesMethod.invoke(null);
            items.setAll(values);
        } catch (NoSuchMethodException | IllegalAccessException | SecurityException | InvocationTargetException ex) {
            Logger.getLogger(EnumEditingCell.class.getName()).log(Level.SEVERE, null, ex);
        } 

        choiceBox.setItems(items);
        choiceBox.getSelectionModel().select(getItem());
        choiceBox.getSelectionModel().selectedItemProperty().addListener(this);

    }

    void changed(ObservableValue observable, Object oldValue, Object newValue) {
        commitEdit(newValue); 
    }
}

// creates a mapping between two different types of ObservableValues
// For example String and Date
class ConverterPropertyValueFactory extends PropertyValueFactory implements ChangeListener {
    public Closure converter;
    private StringProperty destination;

    ConverterPropertyValueFactory(String property, Closure converter) {
        super(property);
        this.converter = converter; 
    }

    ObservableValue call(TableColumn.CellDataFeatures param) {
        ObservableValue origin = super.call(param);
        Object originValue = origin.getValue();
        String result = originValue == null ? "" : converter.call(originValue);
        destination = new SimpleStringProperty(result);
        if(origin != null) {
            origin.addListener(this);
            return destination;
        }else {
            return destination;
        }
        
    }

    void changed(ObservableValue observable, Object oldValue, Object newValue) {
        String result = "";
        if(newValue != null) 
             result = converter.call(newValue);
        destination.setValue(result);
    }
    
}

class TableFactory extends AbstractNodeFactory {
    private static EditingCallback defaultCellFactory = new EditingCallback();
    private static EnumEditingCallback enumCellFactory = new EnumEditingCallback();

    TableFactory(Class beanClass) {
        super(beanClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object result = super.newInstance(builder, name, value, attributes);
        if(TableColumn.isAssignableFrom(beanClass) && value != null) {
            result.text = value.toString()
        }
        result
    }
    
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if(node instanceof TableView) {
            
            // Handle selection attributes
            def selectionMode= attributes.remove("selectionMode");
            if(selectionMode != null) {
                
                def mode = selectionMode instanceof SelectionMode ?
                            selectionMode : SelectionMode.valueOf(selectionMode.toUpperCase());
                node.getSelectionModel().selectionMode = mode;
            }
            def cellSelectionEnabled = attributes.remove("cellSelectionEnabled");
            if(cellSelectionEnabled != null) {
                node.getSelectionModel().cellSelectionEnabled = cellSelectionEnabled;
            }
            def selectedIndex = attributes.remove("selectedIndex");
            if(selectedIndex != null) {
                node.getSelectionModel().selectedIndex = selectedIndex;
            }
            def selectedItem = attributes.remove("selectedItem");
            if(selectedItem != null) {
                node.getSelectionModel().selectedItem = selectedItem;
            }
        }else { // TableColumn
            def converter = attributes.remove("converter");
            //TODO what how to do conversion like date-String etc.
            def type = attributes.remove("type");
            def property = attributes.remove("property");
            if(property != null) {
                if(converter != null) {
                    node.cellValueFactory  = new ConverterPropertyValueFactory(property, converter)
                }else {
                    node.cellValueFactory = new PropertyValueFactory(property);
                }
            }
            
            
            // class type for the field, default is String
            
            def editable = attributes.get("editable"); // leave for superclass processing
            def hasCellFactory = attributes.containsKey("cellFactory"); // leave for superclass processing
            if(editable && !hasCellFactory) {
                def cellFactory = null;
                if(type != null && Enum.class.isAssignableFrom(type)) {
                    cellFactory = new EnumEditingCallback(type);
                }
                /** TODO if necessary build a map of cellFactories.
                if(type != null)
                    cellFactory = cellFactories.get(type);
                **/
                if(cellFactory == null)
                    cellFactory = defaultCellFactory;
                node.setCellFactory(cellFactory);
            }
        }

        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child ) {
        if((parent instanceof TableView || parent instanceof TableColumn) && child instanceof TableColumn) {
            parent.columns.add(child);
        }else if(child instanceof GroovyCallback) {
            if(parent instanceof TableView && child.property == "onSelect") {
                   parent.selectionModel.selectedItemProperty().addListener(new ChangeListener() {
                        void changed(final ObservableValue observable, final Object oldValue, final Object newValue) {
                            builder.defer({child.closure.call(parent, oldValue, newValue);});
                        }
                    });      
            }else if(parent instanceof TableColumn ) {
                InvokerHelper.setProperty(node, child.property, child);
            }
        }else {
            super.setChild(builder, parent, child)
        }
    }
}
