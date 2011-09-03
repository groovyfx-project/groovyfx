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
import javafx.scene.Node

import javafx.scene.layout.Region
import javafx.scene.NodeBuilder

/**
 *
 * @author jimclarke
 */
class SceneFactory extends AbstractGroovyFXFactory {
    private static final String BUILDER_LIST_PROPERTY = "__builderList"

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        SceneWrapper scene;
        if (FactoryBuilderSupport.checkValueIsType(value, name, SceneWrapper.class)) {
            scene = value
        } else {
            
            scene = new SceneWrapper()
            def root = attributes.remove("root");
            if(root != null) {
                scene.root(root);
            }
        }

        builder.getContext().put(SceneGraphBuilder.CONTEXT_SCENE_KEY, SceneWrapper)
        return scene;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        SceneWrapper sceneWrapper = (SceneWrapper)parent;
        
        if(sceneWrapper.sceneRoot == null  && child instanceof Node) {
            if(child instanceof Group || child instanceof Region ) {
                sceneWrapper.root(child);
                return;
            } else {
                sceneWrapper.root(new Group());
            }
        }

        if(child instanceof Node) {
            sceneWrapper.sceneRoot.getChildren().add((Node) child);
        } else if(child instanceof List) {
            sceneWrapper.stylesheets((List)child);
        } else if(child instanceof GroovyMouseHandler) {
            sceneWrapper.addInputHandler(((GroovyMouseHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof GroovyKeyHandler) {
            sceneWrapper.addInputHandler(((GroovyKeyHandler)child).getType(), (EventHandler)child);
        } else if (child instanceof NodeBuilder) {
            def builderList = builder.parentContext.get(BUILDER_LIST_PROPERTY, [])
            builderList << child
        }
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        def scene = (SceneWrapper)node
        def attr = attributes.remove("fill")
        if(attr) {
            scene.fill(ColorFactory.get(attr))
        }
        attr = attributes.remove("camera")
        if(attr) {
            scene.camera(attr)
        }
        attr = attributes.remove("cursor")
        if(attr) {
            scene.cursor(attr)
        }
        attr = attributes.remove("height")
        if(attr) {
            scene.height(attr)
        }
        attr = attributes.remove("root")
        if(attr) {
            scene.root(root)
        }
        attr = attributes.remove("stylesheets")
        if(attr) {
            scene.stylesheets(attr)
        }
        attr = attributes.remove("width")
        if(attr) {
            scene.width(attr)
        }
        for(v in NodeFactory.mouseEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v)
                if(val instanceof Closure) {
                    def handler = new GroovyMouseHandler(v)
                    handler.setClosure((Closure)val)
                    scene.addInputHandler(v, handler)
                }else if(val instanceof EventHandler) {
                    scene.addInputHandler(v, (EventHandler)val)
                }
            }
        }
        // onMouseWheelRotated is not defined on node??
        if(attributes.containsKey("onMouseWheelRotated")) {
            def val = attributes.remove("onMouseWheelRotated")
            if(val instanceof Closure) {
                def handler = new GroovyMouseHandler("onMouseWheelRotated")
                handler.setClosure((Closure)val)
                scene.addInputHandler("onMouseWheelRotated", handler)
            }else if(val instanceof EventHandler) {
                scene.addInputHandler("onMouseWheelRotated", (EventHandler)val)
            }
        }
        for(v in NodeFactory.keyEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v)
                if(val instanceof Closure) {
                    def handler = new GroovyKeyHandler(v)
                    handler.setClosure((Closure)val)
                    scene.addInputHandler(v, handler)
                }else if(val instanceof EventHandler) {
                    scene.addInputHandler(v, (EventHandler)val)
                }
            }
        }
        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (node instanceof SceneWrapper && node.sceneRoot == null) {
            node.root(new Group())
        }
        
        def builderList = builder.context.remove(BUILDER_LIST_PROPERTY)
        builderList?.each {
            ((SceneWrapper)node).sceneRoot.getChildren().add(it.build());
        }
    }
}


