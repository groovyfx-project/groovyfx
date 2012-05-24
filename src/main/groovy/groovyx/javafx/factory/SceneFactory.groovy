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

import org.codehaus.groovy.runtime.InvokerHelper

import groovyx.javafx.event.GroovyEventHandler
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Node
import javafx.scene.Scene

import groovyx.javafx.event.*

import javafx.scene.NodeBuilder
import org.codehaus.groovy.runtime.InvokerHelper

/**
 *
 * @author jimclarke
 */
class SceneFactory extends AbstractFXBeanFactory {
    private static final String BUILDER_LIST_PROPERTY = "__builderList"
    
    boolean syntheticRoot = false;
    
    SceneFactory() {
        super(Scene);
    }
    
    SceneFactory(Class<Scene> beanClass) {
        super(beanClass);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Scene scene;
        if (checkValue(name, value)) {
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
        def id = attributes.remove("id");
        if(id != null)
            builder.getVariables().put(id, scene);
        return scene;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        Scene scene = (Scene)parent

        // If we have a synthetic root, then the first child Node either becomes
        // the root (if it's a Parent) or becomes a child of the synthetic root.
        // Either way, our synthetic root is no longer synthetic.
        if(syntheticRoot && child instanceof Node) {
            syntheticRoot = false;
            if(child instanceof Parent ) {
                scene.root = child;
                return
            }
        }
        
        switch(child) {
            case Node:
                scene.root.children.add(child)
                break;
            case String:
                scene.stylesheets.add(child);
                break;
            case List:
                scene.stylesheets.addAll(child.collect {it.toString()})
                break;
            case GroovyEventHandler:
                InvokerHelper.setProperty(scene, child.property, child);
                break;
            case NodeBuilder:
                def builderList = builder.parentContext.get(BUILDER_LIST_PROPERTY, [])
                builderList << child
                break;
            case GroovyChangeListener:
            case GroovyInvalidationListener:
                if(parent.metaClass.respondsTo(parent, child.property + "Property"))
                    parent."${child.property}Property"().addListener(child);
                break;
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
        for(v in AbstractNodeFactory.nodeEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    FXHelper.setPropertyOrMethod(node, v, val as EventHandler)
/*****
                    def handler = new GroovyEventHandler(v);
                    handler.setClosure((Closure)val);
****/
                }else if(val instanceof EventHandler) {
                    FXHelper.setPropertyOrMethod(node, v, val)
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
            node.root.children.add(it.build());
        }
    }
}


