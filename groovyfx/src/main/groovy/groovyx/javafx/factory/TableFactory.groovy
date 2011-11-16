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


import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.TableColumn.*;
import java.lang.reflect.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.beans.property.*;
import javafx.collections.*;


/**
 *
 * @author jimclarke
 */

class EditingCallback implements javafx.util.Callback {
    public Object call(Object column) {
        return new EditingCell();
    }
}

class EditingCell extends TableCell implements EventHandler {
    private TextField textField;

    public EditingCell() {
        this.setEditable(true);
    }


    @Override
    public void startEdit() {
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
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(Object t) {
        super.commitEdit(t);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            if (textField != null) {
                textField.setText(item.toString());
            }
            setText(item.toString());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }
    
    public void handle(Event t) { 
        if (t.getCode() == KeyCode.ENTER) {
            commitEdit(textField.getText());
        } else if (t.getCode() == KeyCode.ESCAPE) {
            cancelEdit();
        }
    }

}

class EnumEditingCallback implements javafx.util.Callback {
    public Class enumClass;
    
    public EnumEditingCallback(Class enumClass) {
        this.enumClass = enumClass;
    }
    
    public Object call(Object column) {
        return new EnumEditingCell(enumClass);
    }
}


// Generic enum choicebox editor.
class EnumEditingCell extends TableCell implements ChangeListener {
    private ChoiceBox choiceBox;
    public Class enumClass;

    public EnumEditingCell(Class enumClass) {
        this.setEditable(true);
        this.enumClass = enumClass;
    }

    @Override
    public void startEdit() {
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
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(Object t) {
        super.commitEdit(t);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Object item, boolean empty) {
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
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PatientView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PatientView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PatientView.class.getName()).log(Level.SEVERE, null, ex);
        }catch (InvocationTargetException ex) {
            Logger.getLogger(PatientView.class.getName()).log(Level.SEVERE, null, ex);
        }


        choiceBox.setItems(items);
        choiceBox.getSelectionModel().select(getItem());
        choiceBox.getSelectionModel().selectedItemProperty().addListener(this);

    }  
    
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        commitEdit(newValue); 
    }
}

// creates a mapping between two different types of ObservableValues
// For example String and Date
class ConverterPropertyValueFactory extends PropertyValueFactory implements ChangeListener {
    public Closure converter;
    private StringProperty destination;
    
    public ConverterPropertyValueFactory(String property, Closure converter) {
        super(property);
        this.converter = converter; 
    }
    
    public ObservableValue call(TableColumn.CellDataFeatures param) {
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
    
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        String result = "";
        if(newValue != null) 
             result = converter.call(newValue);
        destination.setValue(result);
    }
    
}

class TableFactory extends AbstractGroovyFXFactory {
    private static EditingCallback defaultCellFactory = new EditingCallback();
    private static EnumEditingCallback enumCellFactory = new EnumEditingCallback();
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object result = null;
        switch(name) {
            case 'tableView':
                result = new TableView();
                break;
            case 'tableColumn':
                final colName = value instanceof String ? value : ''
                result = new TableColumn(colName);
                break;
        }
        return result;
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
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(parent instanceof TableView && child instanceof TableColumn) {
            parent.columns.add(child);
        }
    }
}
