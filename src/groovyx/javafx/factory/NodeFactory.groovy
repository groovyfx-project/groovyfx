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

import groovyx.javafx.input.GroovyKeyHandler
import groovyx.javafx.input.GroovyMouseHandler
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Transform

import org.codehaus.groovy.runtime.InvokerHelper;
/**
 *
 * @author jimclarke
 */
public class NodeFactory extends AbstractFactory {
    
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
                    addInputHandler((Node)node, v, handler);
                }else if(val instanceof EventHandler) {
                    addInputHandler((Node)node, v, (EventHandler)val);
                }
            }
        }
        for(v in keyEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new GroovyKeyHandler(v);
                    handler.setClosure((Closure)val);
                    addInputHandler((Node)node, v, handler);
                }else if(val instanceof EventHandler) {
                    addInputHandler((Node)node, v, (EventHandler)val);
                }
            }
        }
        return true;
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof GroovyMouseHandler) {
            addInputHandler((Node)parent, ((GroovyMouseHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof GroovyKeyHandler) {
            addInputHandler((Node)parent, ((GroovyKeyHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof Effect) {
            ((Node)parent).setEffect((Effect)child);
        } else if(child instanceof Transform) {
            ((Node)parent).getTransforms().add((Transform)child);
        } else if(child instanceof Image && parent instanceof ImageView) {
            ((ImageView)parent).setImage((Image)child);
        }
    }
    
    public void addInputHandler(Object node, String type, EventHandler handler) {
        FXHelper.setPropertyOrMethod(node, type, handler)
    }

    
    public bindingAttributeDelegate(FactoryBuilderSupport builder, def node, def attributes) {
        FXHelper.fxAttributes(node, attributes);
    }
}

