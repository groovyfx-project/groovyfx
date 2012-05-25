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

import javafx.scene.control.*
import javafx.scene.Node
import groovyx.javafx.SceneGraphBuilder;

import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.util.Callback;
import groovyx.javafx.event.*;


/**
 *
 * @author jimclarke
 */
class ControlFactory extends AbstractNodeFactory {

    public ControlFactory(Class beanClass) {
        super(beanClass);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Control control = super.newInstance(builder, name, value, attributes);
        if(value != null) {
              control.text = value.toString();
        }
        if(SplitPane.isAssignableFrom(beanClass)) {
             handleSplitPane(builder)
        }
        control;
    }
    
    private void handleSplitPane(builder) {
        def cntx = builder.getContext();
        List dividers = cntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
        if (dividers == null) {
            cntx.put(SceneGraphBuilder.CONTEXT_DIVIDER_KEY, new ArrayList());
        } else if (!dividers.isEmpty()) {
            dividers.clear();
        }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        switch(child) {
            case Tooltip:
                parent.setTooltip(child);
                break;
            case GroovyCallback:
                if(child.property == "onSelect") {
                    switch(parent) {
                        case ListView:
                        case ComboBox:
                        case TabPane:
                        case TreeView:
                            parent.selectionModel.selectedItemProperty().addListener(new ChangeListener() {
                                public void changed(final ObservableValue observable, final Object oldValue, final Object newValue) {
                                    builder.defer({child.closure.call(parent, newValue);});
                                }
                            }); 
                            break;
                        default:
                            break;
                    }
                 } else if(child.property == "cellFactory") {
                     switch(parent) {
                         case ListView:
                         case ComboBox:
                         case TreeView:
                            parent.cellFactory = child
                            break;
                         default:
                            break;
                     }
                 }
                 break;
            case ContextMenu:
                parent.contextMenu = contextMenu;
                break;
            default:
                switch(parent) {
                    case ScrollPane:
                        parent.content = child;
                        break;
                    case TableView:
                        if(child instanceof List)
                            parent.columns.addAll(child);
                        else if (child instanceof TableColumn)
                            parent.columns.add(child);
                        break;
                    case Accordion:
                        if (child instanceof List)
                            parent.panes.addAll(child);
                        else if (child instanceof TitledPane)
                            parent.panes.add(child);
                        break;
                    case TitledPane:
                        switch(child) {
                            case Node:
                                parent.content = child;
                                break
                            case TitledNode:
                                child.pane = parent
                                break;
                            case TitledContent:
                                child.pane = parent
                                break
                        }   
                        break;
                    case SplitPane:
                        if (child instanceof Node) {
                            parent.items.add(child);
                        } else if (child instanceof List) {
                            parent.items.addAll(child);
                        } else if (child instanceof DividerPosition) {
                            List dividers = builder.parentContext.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
                            dividers.add(child);
                        }
                        break;
                    case ToolBar:
                        if (child instanceof List)
                            parent.items.addAll(child);
                        else if (child instanceof Node)
                            parent.items.add(child);
                        break;
                    case TabPane:
                        if(child instanceof Tab) {
                            parent.tabs.add(child);
                        }else {
                            super.setChild(builder, parent, child);
                        }
                        break;
                    case TreeView:
                        if (child instanceof TreeItem) {
                            parent.root = child
                        } else if (child instanceof GroovyEventHandler) {
                            FXHelper.setPropertyOrMethod(parent, child.property, child)
                        }
                        break;
                    default:
                        super.setChild(builder, parent, child);
                        break;
                }
                break
        }
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof SplitPane) {
            def cntx = builder.getContext();
            List dividers = cntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
            dividers.each {div ->
                child.setDividerPosition(div.index, div.position);
            }
            dividers.clear();
        }
    }
}

