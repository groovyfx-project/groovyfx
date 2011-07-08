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

import javafx.scene.Scene
import javafx.scene.paint.Color
import java.util.List;
import javafx.scene.Node;
import groovyx.javafx.SceneGraphBuilder;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.Parent;

import groovyx.javafx.input.*;
import javafx.scene.input.*;
import javafx.event.EventHandler;

/**
 *
 * @author jimclarke
 */
class SceneFactory extends AbstractFactory {

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
            // TODO add stylesheets
            sceneWrapper.stylesheets((List)child);
        } else if(child instanceof GroovyMouseHandler) {
            parent.addMouseHandler(((GroovyMouseHandler)child).getType(), (EventHandler)child);
        } else if(child instanceof GroovyKeyHandler) {
            parent.addKeyHandler(((GroovyKeyHandler)child).getType(), (EventHandler)child);
        }
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node,
            Map attributes ) {
        def attr = attributes.remove("fill");
        if(attr) {
            node.fill(ColorFactory.get(attr));
        }
        attr = attributes.remove("camera");
        if(attr) {
            node.camera(attr);
        }
        attr = attributes.remove("cursor");
        if(attr) {
            node.cursor(attr);
        }
        attr = attributes.remove("height");
        if(attr) {
            node.height(attr);
        }
        attr = attributes.remove("root");
        if(attr) {
            node.root(root);
        }
        attr = attributes.remove("stylesheets");
        if(attr) {
            node.stylesheets(attr);
        }
        attr = attributes.remove("width");
        if(attr) {
            node.width(attr);
        }
        for(v in NodeFactory.mouseEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new GroovyMouseHandler(v);
                    handler.setClosure((Closure)val);
                    node.addMouseHandler(v, handler);
                }else if(val instanceof EventHandler) {
                    node.addMouseHandler(v, (EventHandler)val);
                }
            }
        }
        for(v in NodeFactory.keyEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new GroovyKeyHandler(v);
                    handler.setClosure((Closure)val);
                    node.addKeyHandler(v, handler);
                }else if(val instanceof EventHandler) {
                    node.addKeyHandler(v, (EventHandler)val);
                }
            }
        }
        return true;
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (node instanceof SceneWrapper && node.sceneRoot == null) {
            node.root(new Group())
        }
    }
}


