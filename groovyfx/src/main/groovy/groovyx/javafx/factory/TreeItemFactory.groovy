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
import javafx.scene.Node;
import javafx.event.EventHandler;
import groovyx.javafx.ClosureEventHandler;

/**
 *
 * @author jimclarke
 */
class TreeItemFactory extends AbstractGroovyFXFactory {
    
    private static def treeItemEvents = [
        "branchCollapse",
        "branchExpand",
        "childrenModification",
        "graphicChanged",
        "treeItemCountChange",
        "treeNotification",
        "valueChanged"
    ];
    
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        TreeItem item = new TreeItem();
        if(value != null) {
            item.value = value
        }
        return item;
    }
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof TreeItem) {
            parent.children.add(child);
        } else if (child instanceof Graphic) {
            parent.graphic = child.node
        } else if(child instanceof Node) {
            parent.graphic = child;
        } else if(child instanceof ClosureEventHandler) {
            setEventHandler((TreeItem)parent, (ClosureEventHandler)child);
        } else {
            println("Not gou")
        }
    }
    
    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node,
            Map attributes ) {
        for(v in treeItemEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    def handler = new ClosureEventHandler(v);
                    handler.action = val
                    setEventHandler(node, handler);
                }else if(val instanceof EventHandler) {
                    setEventHandler(node, val);
                }
            }
        }
        return super.onHandleNodeAttributes(builder, node, attributes);
    }
    
    void setEventHandler(TreeItem item, EventHandler handler) {
        def eventType = null;
        switch(handler.name) {
            case "branchCollapse":
                eventType = TreeItem.BRANCH_COLLAPSED_EVENT;
                break;
            case "branchExpand":
                eventType = TreeItem.BRANCH_EXPANDED_EVENT
                break;
            case "childrenModification":
                eventType = TreeItem.CHILDREN_MODIFICATION_EVENT
                break;
            case "graphicChanged":
                eventType = TreeItem.GRAPHIC_CHANGED_EVENT
                break;
            case "treeItemCountChange":
                eventType = TreeItem.TREE_ITEM_COUNT_CHANGE_EVENT
                break;
            case "treeNotification":
                eventType = TreeItem.TREE_NOTIFICATION_EVENT
                break;
            case "valueChanged":
                eventType = TreeItem.VALUE_CHANGED_EVENT
                break;
        }
        
        item.addEventHandler(eventType, handler)
    }

}

