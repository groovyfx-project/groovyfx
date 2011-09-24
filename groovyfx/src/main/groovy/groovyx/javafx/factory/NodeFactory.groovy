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

import groovyx.javafx.event.GroovyEventHandler
import groovyx.javafx.event.GroovyKeyHandler
import groovyx.javafx.event.GroovyMouseHandler
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.transform.Transform
import javafx.scene.layout.*;
import javafx.geometry.*;

/**
 *
 * @author jimclarke
 */
public class NodeFactory extends AbstractGroovyFXFactory {
    
    public static def mouseEvents = [
        'onMouseClicked',
        'onMouseDragged',
        'onMouseEntered',
        'onMouseExited',
        'onMouseMoved',
        'onMousePressed',
        'onMouseReleased',
        'onMouseWheelMoved',   
        'onDragDetected',
        'onDragDone',
        'onDragEntered',
        'onDragExited',
        'onDragOver',
        'onDragDropped'
    ]

    public static def keyEvents = [
        'onKeyPressed',
        'onKeyReleased',
        'onKeyTyped',
    ]

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        Object node;
        if (value != null && value instanceof Node) {
            node = value
        } else {
            switch(name) {
                case 'imageView':
                    node = new ImageView();
                    if(value != null && value instanceof Image)
                        node.image = value;
                    break;
            }
        }
        
        return node;
    }

     public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        for(v in mouseEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new GroovyMouseHandler(v);
                    handler.setClosure((Closure)val);
                    addEventHandler((Node)node, v, handler);
                }else if(val instanceof EventHandler) {
                    addEventHandler((Node)node, v, (EventHandler)val);
                }
            }
        }
        for(v in keyEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new GroovyKeyHandler(v);
                    handler.setClosure((Closure)val);
                    addEventHandler((Node)node, v, handler);
                }else if(val instanceof EventHandler) {
                    addEventHandler((Node)node, v, (EventHandler)val);
                }
            }
        }
        def parent = builder.context.get(FactoryBuilderSupport.CURRENT_NODE);
        handleLayoutConstraints(parent, node, attributes);
         return super.onHandleNodeAttributes(builder, node, attributes);
    }

    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof GroovyEventHandler) {
            addEventHandler((Node)parent, ((GroovyEventHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof Transform) {
            ((Node)parent).getTransforms().add((Transform)child);
        } else if(child instanceof Image && parent instanceof ImageView) {
            ((ImageView)parent).setImage((Image)child);
        }
    }
    
    public void addEventHandler(Object node, String type, EventHandler handler) {
        FXHelper.setPropertyOrMethod(node, type, handler)
    }

    public static def attributeDelegate = { FactoryBuilderSupport builder, def node, def attributes ->
        FXHelper.fxAttributes(node, attributes);
    }
    private static def doEnum  = { Class cls, value ->
        value = FXHelper.getValue(value);
        if(!value.getClass().isEnum()) {
            value = Enum.valueOf(cls,value.toString().trim().toUpperCase())
        }
        return value;
    };
    private static def doInsets = {  value ->
        value = getValue(value);
        if (Number.class.isAssignableFrom(value.getClass())) {
            value = new Insets(value, value, value, value)
        } else if(List.class.isAssignableFrom(value.getClass())) {
            switch (value.size()) {
                case 0:
                    value = Insets.EMPTY
                    break
                case 1:
                    //top, right,bottom, left
                    value = new Insets(value[0], value[0], value[0], value[0])
                    break
                case 2:
                    value = new Insets(value[0], value[1], value[0], value[1])
                    break
                case 3:
                    value = new Insets(value[0], value[1], value[2], value[1])
                    break
                default:
                    value = new Insets(value[0], value[1], value[2], value[3])
                    break
            }
        } else if(value.toString().toUpperCase() == 'EMPTY') {
            value = Insets.EMPTY;
        }
        return value;    
    };
    private handleLayoutConstraints(Object parent, Object node, Map attributes) {
        if(parent != null && parent instanceof Pane) {
            if(parent instanceof GridPane) {
                //Halignment
                def val = attributes.remove("halignment");
                if(val)
                    GridPane.setHalignment(node, doEnum(HPos, val));
                //Valignment
                val = attributes.remove("valignment");
                if(val)
                    GridPane.setValignment(node, doEnum(VPos, val));
                
                //columnIndex
                val = attributes.remove("columnIndex")
                if(val == null)
                    val = attributes.remove("column");
                if(val)
                    GridPane.setColumnIndex(node, val);
                //rowIndex
                val = attributes.remove("rowIndex");
                if(val == null)
                    val = attributes.remove("row");
                if(val)
                    GridPane.setRowIndex(node, val);
                //rowSpan
                val = attributes.remove("rowSpan")
                if(val)
                    GridPane.setRowSpan(node, val);
                //columnSpan
                val = attributes.remove("columnSpan")
                if(val)
                    GridPane.setColumnSpan(node, val);
                //Hgrow
                val = attributes.remove("hgrow");
                if(val)
                    GridPane.setHgrow(node, doEnum(Priority, val));
                //Vgrow
                val = attributes.remove("vgrow");
                if(val)
                    GridPane.setVgrow(node, doEnum(Priority, val));
                //Margin
                val = attributes.remove("margin");
                if(val)
                    GridPane.setMargin(node, doInsets(val));
            }else if(parent instanceof HBox) {
                //Hgrow
                def val = attributes.remove("hgrow");
                if(val)
                    HBox.setHgrow(node, doEnum(Priority, val));
                //Margin
                val = attributes.remove("margin");
                if(val)
                    HBox.setMargin(node, doInsets(val));
            }else if(parent instanceof VBox) {
                //Vgrow
                def val = attributes.remove("vgrow");
                if(val)
                    VBox.setVgrow(node, doEnum(Priority, val));
                //Margin
                val = attributes.remove("margin");
                if(val)
                    VBox.setMargin(node, doInsets(val));
            }else if(parent instanceof BorderPane) {
                //BorderPane.setAlignment
                def val = attributes.remove("alignment");
                if(val)
                    BorderPane.setAlignment(node, doEnum(Pos, val));
                //BorderPane.setMargin
                val = attributes.remove("margin");
                if(val)
                    BorderPane.setMargin(node, doInsets(val));
            }else if(parent instanceof FlowPane) {
                //FlowPane.setMargin
                val = attributes.remove("margin");
                if(val)
                    FlowPane.setMargin(node, doInsets(val));
            }else if(parent instanceof StackPane) {
                //StackPane.setAlignment
                 def val = attributes.remove("alignment");
                if(val)
                    StackPane.setAlignment(node, doEnum(Pos, val));
                //StackPane.setMargin
                val = attributes.remove("margin");
                if(val)
                    StackPane.setMargin(node, doInsets(val));
            }else if(parent instanceof TilePane) {
                //TilePane.setAlignment
                def val = attributes.remove("alignment");
                if(val)
                    TilePane.setAlignment(node, doEnum(Pos, val));
                //TilePane.setMargin
                val = attributes.remove("margin");
                if(val)
                    TilePane.setMargin(node, doInsets(val));
            }
        }
    }
}

