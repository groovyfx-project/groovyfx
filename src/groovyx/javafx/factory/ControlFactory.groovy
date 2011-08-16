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
import groovyx.javafx.ClosureEventHandler

/**
 *
 * @author jimclarke
 */
class ControlFactory extends NodeFactory {

    private static def controlBuilder = [
            "scrollBar": { builder, name, value, attributes -> return new ScrollBar() },
            "slider": { builder, name, value, attributes -> return new Slider() },
            "separator": { builder, name, value, attributes -> return new Separator() },
            "listView": { builder, name, value, attributes -> return new ListView() },
            "passwordField": { builder, name, value, attributes -> return new PasswordField() },
            "textArea": { builder, name, value, attributes -> return new TextArea() },
            "textField": { builder, name, value, attributes-> return new TextField() },
            "progressBar": { builder, name, value, attributes -> return new ProgressBar() },
            "progessIndicator": { builder, name, value, attributes -> return new ProgressIndicator() },
            "scrollPane": { builder, name, value, attributes -> return new ScrollPane() },
            "tableView": { builder, name, value, attributes -> return new TableView() },
            "treeView": { builder, name, value, attributes -> return new TreeView() },
            "accordion": { builder, name, value, attributes -> return new Accordion() },
            "titledPane": { builder, name, value, attributes -> return new TitledPane() },
            "toolBar": { builder, name, value, attributes -> return new ToolBar() },
            "tabPane": { builder, name, value, attributes -> return new TabPane() },
            "splitPane": {builder, name, value, attributes ->
                def control = new SplitPane();
                def cntx = builder.getContext();
                List dividers = cntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
                if (dividers == null) {
                    cntx.put(SceneGraphBuilder.CONTEXT_DIVIDER_KEY, new ArrayList());
                } else if (!dividers.isEmpty()) {
                    dividers.clear();
                }
                return control;
            }
    ]

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Control control;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Control.class)) {
            control = value
        } else {
            def creator = controlBuilder[name];
            if(creator != null)
                control = creator(builder, name, value, attributes);
        }
        return control;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Tooltip) {
            ((Control) parent).setTooltip(child);
        }else if(child instanceof ContextMenu) {
            child.contextMenu = contextMenu;
        } else if (parent instanceof ScrollPane) {
            ((ScrollPane)parent).setContent((Node)child);
        } else if (parent instanceof TableView) {
            TableView table = (TableView) parent;
            if(child instanceof List)
                table.getColumns().addAll(child);
            else
                table.getColumns().add(child);
        } else if (parent instanceof Accordion) {
            if (child instanceof List)
                parent.getPanes.addAll(child);
            else
                parent.getPanes().add(child);
        } else if (parent instanceof TitledPane) {
            if (child instanceof Node) {
                parent.content = child;
            } else {
                child.pane = parent;
            }
        } else if (parent instanceof SplitPane) {
            if (child instanceof Node) {
                parent.items.add(child);
            } else if (child instanceof List) {
                parent.items.addAll(child);
            }
            else { // todo this should be saved and set upon node complete
                // so that all the items are saved first.
                def pcntx = builder.getParentContext();
                List dividers = pcntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
                dividers.add(child);

            }
        } else if (parent instanceof ToolBar) {
            if (child instanceof List)
                parent.items.addAll(child);
            else
                parent.items.add(child);
        } else if (parent instanceof TabPane && child instanceof Tab) {
            parent.tabs.add(child);
        } else if (parent instanceof TreeView) {
            if (child instanceof TreeItem) {
                parent.root = child
            } else if (child instanceof ClosureEventHandler) {
                switch (child.name) {
                    case 'onEditCancel':
                        parent.onEditCancel = child.action;
                        break;
                    case 'onEditCommit':
                        parent.onEditCommit = child.action;
                        break;
                    case 'onEditStart':
                        parent.onEditStart = child.action;
                        break;
                }
            }
        } else {
            super.setChild(builder, parent, child);
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

