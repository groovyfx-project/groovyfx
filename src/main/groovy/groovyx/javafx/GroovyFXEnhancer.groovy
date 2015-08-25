/*
* Copyright 2011-2012 the original author or authors.
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
package groovyx.javafx

import org.codehaus.groovy.runtime.InvokerHelper
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableNumberValue
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine
import javafx.stage.Window
import javafx.util.Callback
import javafx.util.Duration
import javafx.beans.binding.*
import javafx.beans.property.*
import javafx.scene.control.*

import javafx.collections.*;

/**
 * @author jimclarke
 * @author Andres Almiray
 */
class GroovyFXEnhancer {
    private static final Object[] EMPTY_ARGUMENTS = {};
    
    static void enhanceClasses() {
        ExpandoMetaClass.enableGlobally()
        
        def origListAsType = ArrayList.metaClass.getMetaMethod("asType", [Class] as Class[])
        ArrayList.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableList) {
                    FXCollections.observableList(delegate);
                } else if(clazz == ObservableSet) {
                    FXCollections.observableSet(delegate as Set);
                }else {
                    origListAsType.invoke(delegate, clazz)
                }
            }
        }
        def origMapAsType = Map.metaClass.getMetaMethod("asType", [Class] as Class[])
        Map.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableMap) {
                    FXCollections.observableMap(delegate);
                }else {
                    origMapAsType.invoke(delegate, clazz)
                }
            }
        }
        def origSetAsType = Set.metaClass.getMetaMethod("asType", [Class] as Class[])
        Set.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableSet) {
                    FXCollections.observableSet(delegate);
                }else if(origSetAsType != null){
                    origSetAsType.invoke(delegate, clazz)
                }
            }
        }

        Number.metaClass {
            getM = {-> Duration.minutes(delegate)}
            getS = {-> Duration.seconds(delegate)}
            getMs = {-> Duration.millis(delegate)}
            getH = {-> Duration.hours(delegate)}

            // FX Properties
            plus << { ObservableNumberValue operand -> operand.add(delegate)}
            minus << { ObservableNumberValue operand ->
                new SimpleDoubleProperty(delegate).subtract(operand)
            }
            multiply << { ObservableNumberValue operand -> operand.multiply(delegate)}
            div << { ObservableNumberValue operand ->
                new SimpleDoubleProperty(delegate).divide(operand)
            }
        }

        ReadOnlyDoubleProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}

            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}

            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}

            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}

            negative << { delegate.negate() }

            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}

            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}

            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}

            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}

            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }

        }

        ReadOnlyFloatProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}

            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}

            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}

            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}

            negative << { delegate.negate() }

            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}

            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}

            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}

            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}

            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }

        }


        ReadOnlyIntegerProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}

            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}

            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}

            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}

            negative << { delegate.negate() }

            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}

            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}

            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}

            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}

            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }

        }


        ReadOnlyLongProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}

            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}

            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}

            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}

            negative << { delegate.negate() }

            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}

            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}

            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}

            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}

            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }

        }



        ReadOnlyBooleanProperty.metaClass {
            // or, and, and xor are already in the class groovy symbols | and &

            negative << { delegate.not() }

            eq << { Boolean operand ->
                delegate.isEqualTo(new SimpleBooleanProperty(operand))
            }
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Boolean operand ->
                delegate.isNotEqualTo(new SimpleBooleanProperty(operand))
            }
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            xor << { Boolean operand ->
                delegate.isNotEqualTo(new SimpleBooleanProperty(operand))
            }
            xor << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }
        }

        ReadOnlyObjectProperty.metaClass {
            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }
        }
        ReadOnlyListProperty.metaClass {
            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }
        }
        ReadOnlyMapProperty.metaClass {
            onChange { Closure closure ->
                delegate.addListener(closure as ChangeListener);
            }

            onInvalidate { Closure closure ->
                delegate.addListener(closure as InvalidationListener);
            }
        }

        NumberBinding.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { 
                ObservableNumberValue operand -> delegate.add(operand)
            }

            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}

            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}

            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}

            negative << { delegate.negate() }

            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}

            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}

            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}

            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}

            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
        }

        
        BooleanBinding.metaClass {
            // or, and, and xor are already in the class groovy symbols | and &

            negative << { delegate.not() }

            eq << { Boolean operand ->
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isEqualTo(prop)
            }
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}

            ne << { Boolean operand ->
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isNotEqualTo(prop)
            }
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

            xor << { Boolean operand ->
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isNotEqualTo(prop)
            }
            xor << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}

        }

        Node.metaClass {

            onDragDetected << { Closure closure -> delegate.setOnDragDetected(closure as EventHandler)}
            onDragDone << { Closure closure -> delegate.setOnDragDone(closure as EventHandler)}
            onDragDropped << { Closure closure -> delegate.setOnDragDropped(closure as EventHandler)}
            onDragEntered << { Closure closure -> delegate.setOnDragEntered(closure as EventHandler)}
            onDragExited << { Closure closure -> delegate.setOnDragExited(closure as EventHandler)}
            onDragOver << { Closure closure -> delegate.setOnDragOver(closure as EventHandler)}
            onInputMethodTextChanged << { Closure closure -> delegate.setOnInputMethodTextChanged(closure as EventHandler)}
            onKeyPressed << { Closure closure -> delegate.setOnKeyPressed(closure as EventHandler)}
            onKeyReleased << { Closure closure -> delegate.setOnKeyReleased(closure as EventHandler)}
            onKeyTyped << { Closure closure -> delegate.setOnKeyTyped(closure as EventHandler)}
            onMouseDragEntered << { Closure closure -> delegate.setOnMouseDragEntered(closure as EventHandler)}
            onMouseClicked << { Closure closure -> delegate.setOnMouseClicked(closure as EventHandler)}
            onMouseDragExited << { Closure closure -> delegate.setOnMouseDragExited(closure as EventHandler)}
            onMouseDragged << { Closure closure -> delegate.setOnMouseDragged(closure as EventHandler)}
            onMouseDragOver << { Closure closure -> delegate.setOnMouseDragOver(closure as EventHandler)}
            onMouseDragReleased << { Closure closure -> delegate.setOnMouseDragReleased(closure as EventHandler)}
            onMouseEntered << { Closure closure -> delegate.setOnMouseEntered(closure as EventHandler)}
            onMouseExited << { Closure closure -> delegate.setOnMouseExited(closure as EventHandler)}
            onMouseMoved << { Closure closure -> delegate.setOnMouseMoved(closure as EventHandler)}
            onMousePressed << { Closure closure -> delegate.setOnMousePressed(closure as EventHandler)}
            onMouseReleased << { Closure closure -> delegate.setOnMouseReleased(closure as EventHandler)}
            onScroll << { Closure closure -> delegate.setOnScroll(closure as EventHandler)}
            
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Node.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }
        
        Scene.metaClass {
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Scene.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }
        
        Stage.metaClass {
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Scene.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }

        ComboBox.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
        }
        ChoiceBox.metaClass {
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
        }
        ButtonBase.metaClass {
            onAction << { Closure closure -> delegate.setOnAction(closure as EventHandler)}
        }
        ListView.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
        }
        TabPane.metaClass {
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
        }
        TableView.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            columnResizePolicy << { Closure closure -> delegate.setColumnResizePolicy(closure as Callback)}
            rowFactory << { Closure closure -> delegate.setRowFactory(closure as Callback)}
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
        }
        TableColumn.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            cellValueFactory << { Closure closure -> delegate.setCellValueFactory(closure as Callback)}
            onEditCancel << { Closure closure -> delegate.setOnEditCancel(closure as EventHandler)}
            onEditCommit << { Closure closure -> delegate.setOnEditCommit(closure as EventHandler)}
            onEditStart << { Closure closure -> delegate.setOnEditStart(closure as EventHandler)}
        }
        TreeView.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure ->
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);
            }
            onEditCancel << { Closure closure -> delegate.setOnEditCancel(closure as EventHandler)}
            onEditCommit << { Closure closure -> delegate.setOnEditCommit(closure as EventHandler)}
            onEditStart << { Closure closure -> delegate.setOnEditStart(closure as EventHandler)}
        }
        Tab.metaClass {
            onClose << { Closure closure -> delegate.setOnClose(closure as EventHandler)}
            onSelectionChanged << { Closure closure -> delegate.setOnSelectionChanged(closure as EventHandler)}
        }
        Menu.metaClass {
            onHidden << { Closure closure -> delegate.setOnHidden(closure as EventHandler)}
            onHiding << { Closure closure -> delegate.setOnHiding(closure as EventHandler)}
            onShowing << { Closure closure -> delegate.setOnShowing(closure as EventHandler)}
            onShown << { Closure closure -> delegate.setOnShown(closure as EventHandler)}
        }
        Window.metaClass {
            onHidden << { Closure closure -> delegate.setOnHidden(closure as EventHandler)}
            onHiding << { Closure closure -> delegate.setOnHiding(closure as EventHandler)}
            onShowing << { Closure closure -> delegate.setOnShowing(closure as EventHandler)}
            onShown << { Closure closure -> delegate.setOnShown(closure as EventHandler)}
            onCloseRequest << { Closure closure -> delegate.setOnCloseRequest(closure as EventHandler)}
        }
        MenuItem.metaClass {
            onAction << { Closure closure -> delegate.setOnAction(closure as EventHandler)}
        }
        if( System.properties[ 'javafx.platform' ] != 'eglfb' ) {
            WebEngine.metaClass {
                confirmHandler << { Closure closure -> delegate.setConfirmHandler(closure as Callback)}
                createPopupHandler << { Closure closure -> delegate.setCreatePopupHandler(closure as Callback)}
                promptHandler << { Closure closure -> delegate.setPromptHandler(closure as Callback)}

                onAlert << { Closure closure -> delegate.setOnAlert(closure as EventHandler)}
                onResized << { Closure closure -> delegate.setOnResized(closure as EventHandler)}
                onStatusChanged << { Closure closure -> delegate.setOnStatusChanged(closure as EventHandler)}
                onVisibilityChanged << { Closure closure -> delegate.setOnVisibilityChanged(closure as EventHandler)}
            }
        }
    }
}
