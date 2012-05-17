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

import groovyx.javafx.event.*
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
public abstract class AbstractNodeFactory extends AbstractFXBeanFactory {
    
    public static def nodeEvents = [
        'onContextMenuRequested',
        'onDragDetected',
        'onDragDone',
        'onDragDropped',
        'onDragEntered',
        'onDragExited',
        'onDragOver',
        'onInputMethodTextChanged',
        'onKeyPressed',
        'onKeyReleased',
        'onKeyTyped',
        'onMouseClicked',
        'onMouseDragEntered',
        'onMouseDragExited',
        'onMouseDragged',
        'onMouseDragOver',
        'onMouseDragReleased',
        'onMouseEntered',
        'onMouseExited',
        'onMouseMoved',
        'onMousePressed',
        'onMouseReleased',
        'onScroll'
        
    ]
    
    public AbstractNodeFactory(Class beanClass) {
        super(beanClass)
    }
    public AbstractNodeFactory(Class beanClass, boolean leaf) {
        super(beanClass, leaf)
    }
    
    


     public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        for(v in nodeEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    FXHelper.setPropertyOrMethod(node, v, val as EventHandler)
/*****
                    def handler = new GroovyMouseHandler(v);
                    handler.setClosure((Closure)val);
****/
                }else if(val instanceof EventHandler) {
                    FXHelper.setPropertyOrMethod(node, v, val)
                }
            }
        }
        def parent = builder.context.get(FactoryBuilderSupport.CURRENT_NODE);
        handleLayoutConstraints(parent, node, attributes);
        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        switch(child) {
            case GroovyEventHandler:
                FXHelper.setPropertyOrMethod(parent, child.property, child)
                break;
            case Transform:
                parent.transforms.add(child);
                break;
            case Image:
                if(parent instanceof ImageView) {
                    parent.image = child;     
                }
                break;
            case GroovyChangeListener:
            case GroovyInvalidationListener:
                if(parent.metaClass.respondsTo(parent, child.property + "Property"))
                    parent."${child.property}Property"().addListener(child);
                break;
        }
    }
    
    public static def attributeDelegate = { FactoryBuilderSupport builder, def node, def attributes ->
        //FXHelper.fxAttributes(node, attributes);
    }

    private static def doEnum  = { Class cls, value ->
        value = FXHelper.getValue(value);
        if(!value.getClass().isEnum()) {
            value = Enum.valueOf(cls,value.toString().trim().toUpperCase())
        }
        return value;
    }

    private static def doInsets = {  value ->
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
    }

    private handleLayoutConstraints(Object parent, Object node, Map attributes) {
        if(parent != null && parent instanceof Pane) {
            // Halignment
            def val = attributes.remove("halignment");
            if(val && parent.metaClass.respondsTo(parent, "setHalignment"))
                parent.setHalignment(node, doEnum(HPos, val));
            
            // Valignment
            val = attributes.remove("valignment");
            if(val && parent.metaClass.respondsTo(parent, "setValignment"))
                parent.setValignment(node, doEnum(VPos, val));
                
            // column or columnIndex
            val = attributes.remove("column") ?: attributes.remove("columnIndex");
            if(val && parent.metaClass.respondsTo(parent, "setColumnIndex"))
                parent.setColumnIndex(node, val);

            // row or rowIndex
            val = attributes.remove("row") ?: attributes.remove("rowIndex");
            if(val && parent.metaClass.respondsTo(parent, "setRowIndex"))
                parent.setRowIndex(node, val);
            
            // rowSpan
            val = attributes.remove("rowSpan")
            if(val && parent.metaClass.respondsTo(parent, "setRowSpan"))
                parent.setRowSpan(node, val);

            // columnSpan
            val = attributes.remove("columnSpan")
            if(val && parent.metaClass.respondsTo(parent, "setColumnSpan"))
                parent.setColumnSpan(node, val);

            // Hgrow
            val = attributes.remove("hgrow");
            if(val && parent.metaClass.respondsTo(parent, "setHgrow"))
                parent.setHgrow(node, doEnum(Priority, val));

            // Vgrow
            val = attributes.remove("vgrow");
            if(val && parent.metaClass.respondsTo(parent, "setVgrow"))
                parent.setVgrow(node, doEnum(Priority, val));
            
            // Margin
            val = attributes.remove("margin");
            if(val && parent.metaClass.respondsTo(parent, "setMargin"))
                parent.setMargin(node, doInsets(val));

            // Alignment
            val = attributes.remove("alignment");
            if(val && parent.metaClass.respondsTo(parent, "setAlignment"))
                parent.setAlignment(node, doEnum(Pos, val));
        }
    }
}

