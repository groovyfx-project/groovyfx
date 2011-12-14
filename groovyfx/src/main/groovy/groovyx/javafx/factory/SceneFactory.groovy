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

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.event.GroovyKeyHandler
import groovyx.javafx.event.GroovyMouseHandler
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.StackPane;

import javafx.scene.layout.Region
import javafx.scene.NodeBuilder

/**
 *
 * @author jimclarke
 */
class SceneFactory extends AbstractGroovyFXFactory {
    private static final String BUILDER_LIST_PROPERTY = "__builderList"
    
    boolean syntheticRoot = false;

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Scene scene;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Scene.class)) {
            scene = value
        } else {
            def root = attributes.remove("root")
            def height = attributes.remove("height")
            def width = attributes.remove("width")
            def depthBuffer = attributes.remove("depthBuffer")
            
            if(root == null) {
                root = new Group()
                syntheticRoot = true
            }

            if(depthBuffer == null)
                depthBuffer = false
            
            if(width != null && height != null) {
                scene = new Scene(root, width, height, depthBuffer)
            } else {
                scene = new Scene(root)
            }
        }

        return scene;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        Scene scene = (Scene)parent

        // If we have a synthetic root, then the first child Node either becomes
        // the root (if it's a Parent) or becomes a child of the synthetic root.
        // Either way, our synthetic root is no longer synthetic.
        if(syntheticRoot && child instanceof Node) {
            if(child instanceof Parent ) {
                scene.root = child;
                return
            } 
            syntheticRoot = false;
        }

        if(child instanceof Node) {
            scene.root.children.add((Node) child)
        } else if(child instanceof List) {
            scene.stylesheets.addAll(child.collect {it.toString()})
        } else if(child instanceof GroovyMouseHandler) {
            InvokerHelper.setProperty(scene, ((GroovyMouseHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof GroovyKeyHandler) {
            InvokerHelper.setProperty(scene, ((GroovyKeyHandler)child).getType(), (EventHandler)child);
        } else if (child instanceof NodeBuilder) {
            def builderList = builder.parentContext.get(BUILDER_LIST_PROPERTY, [])
            builderList << child
        }
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        def scene = (Scene)node
        def attr = attributes.remove("stylesheets")
        if(attr) {
            if(attr instanceof List)
                scene.stylesheets.addAll(attr)
            else    
                scene.stylesheets.add(attr.toString())
        }
        for(v in NodeFactory.mouseEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v)
                if(val instanceof Closure) {
                    def handler = new GroovyMouseHandler(v)
                    handler.setClosure((Closure)val)
                    scene.addInputHandler(v, handler)
                    InvokerHelper.setProperty(scene, v, handler);
                }else if(val instanceof EventHandler) {
                    InvokerHelper.setProperty(scene, v, val);
                }
            }
        }
        // onMouseWheelRotated is not defined on Scene??
        /*********
        if(attributes.containsKey("onMouseWheelRotated")) {
            def val = attributes.remove("onMouseWheelRotated")
            if(val instanceof Closure) {
                def handler = new GroovyMouseHandler("onMouseWheelRotated")
                handler.setClosure((Closure)val)
                scene.addInputHandler("onMouseWheelRotated", handler)
            }else if(val instanceof EventHandler) {
                scene.addInputHandler("onMouseWheelRotated", (EventHandler)val)
            }
        }**********/
        for(v in NodeFactory.keyEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v)
                if(val instanceof Closure) {
                    def handler = new GroovyKeyHandler(v)
                    handler.setClosure((Closure)val)
                    InvokerHelper.setProperty(scene, v, handler);
                }else if(val instanceof EventHandler) {
                    InvokerHelper.setProperty(scene, v, val);
                }
            }
        }
        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (node instanceof Scene && node.root == null) {
            node.root = new Group();
        }
        
        def builderList = builder.context.remove(BUILDER_LIST_PROPERTY)
        builderList?.each {
            ((Scene)node).root.children.add(it.build());
        }
    }
}


