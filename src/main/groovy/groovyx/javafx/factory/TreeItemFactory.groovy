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

import groovyx.javafx.JdkUtil
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.event.EventHandler;
import groovyx.javafx.event.GroovyEventHandler;

/**
 * @author jimclarke
 * minor adaptions by hackergarten
 */
class TreeItemFactory extends AbstractFXBeanFactory {

    public static def treeItemEvents
    static {
        treeItemEvents = [
                "onBranchCollapse": TreeItem.BRANCH_COLLAPSED_EVENT,
                "onBranchExpand" : TreeItem.BRANCH_EXPANDED_EVENT,
                "onChildrenModification" : TreeItem.CHILDREN_MODIFICATION_EVENT,
                "onGraphicChanged" : TreeItem.GRAPHIC_CHANGED_EVENT,
                "onTreeNotification" : TreeItem.TREE_NOTIFICATION_EVENT,
                "onValueChanged" : TreeItem.VALUE_CHANGED_EVENT
            ]
        if (JdkUtil.jdkIsBefore8()) {
            String constName = "TREE_ITEM_COUNT_CHANGE_EVENT"
            treeItemEvents.onTreeItemCountChange = TreeItem."$constName"
        } else {
            String constName = "EXPANDED_ITEM_COUNT_CHANGE_EVENT"
            treeItemEvents.onExpandedItemCountChange = TreeItem."$constName"
        }
    }

    public TreeItemFactory() {
        super(TreeItem)
    }

    public TreeItemFactory(Class<TreeItem> beanClass) {
        super(beanClass)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        TreeItem item = super.newInstance(builder, name, value, attributes)
        if(!checkValue(name, value)) {
            item.value = value
        }
        item;
    }
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        switch(child) {
            case TreeItem:
                parent.children.add(child);
                break
            case Graphic:
                parent.graphic = child.node
                break
            case Node:
                parent.graphic = child;
                break
            case GroovyEventHandler:
                setEventHandler(parent, child.property, child);
                break
            default:
                throw new Exception("In a TreeItem, value must be an instanceof TreeItem, Node, or an event to be used as embedded content.")
        }
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node,
            Map attributes ) {
        for(v in treeItemEvents) {
            if(attributes.containsKey(v)) {
                def val = attributes.remove(v);
                if(val instanceof Closure) {
                    setEventHandler(node, v, val as EventHandler);
                }else if(val instanceof EventHandler) {
                    setEventHandler(node, v, val);
                }
            }
        }
        return super.onHandleNodeAttributes(builder, node, attributes);
    }

    void setEventHandler(TreeItem item, String property, EventHandler handler) {
        def eventType = treeItemEvents[property]
        item.addEventHandler(eventType, handler)
    }

}
