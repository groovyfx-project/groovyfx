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

/**
 *
 * @author jimclarke
 */
class ControlFactory extends NodeFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Control control;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Control.class)) {
            control = value
        } else {
            switch(name) {
                case 'scrollBar':
                    control = new ScrollBar();
                    break;
                case 'slider':
                    control = new Slider();
                    break;
                case 'separator':
                    control = new Separator();
                    break;
                case 'listView':
                    control = new ListView();
                    break;
                case 'passwordBox':
                    control = new PasswordBox();
                    break;
                case 'textBox':
                    control = new TextBox();
                    break;
                case 'progressBar':
                    control = new ProgressBar();
                    break;
                case 'progessIndicator':
                    control = new ProgressIndicator();
                    break;
                case 'scrollPane':
                    control = new ScrollPane();
                    break;
                case 'tableView':
                    control = new TableView();
                    break;
                case 'accordion':
                    control = new Accordion();
                    break;
                case 'titledPane':
                    control = new TitledPane();
                    break;
                case 'splitPane':
                    control = new SplitPane();
                    def cntx = builder.getContext();
                    List dividers = cntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
                    if(dividers == null) {
                        cntx.put(SceneGraphBuilder.CONTEXT_DIVIDER_KEY, new ArrayList());
                    }else if(!dividers.isEmpty()){
                        dividers.clear();
                    }    
                    break;
                case 'toolBar':
                    control = new ToolBar();
                    break;
                case 'tabPane' :
                    control = new TabPane();
                    break;
            }
        }
        return control;
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Tooltip) {
            ((Control)parent).setTooltip(child);
        }else if(parent instanceof ScrollPane) {
            ((ScrollPane)parent).setNode((Node)child);
        } else if(parent instanceof TableView) {
            TableView table = (TableView)parent;
            //if(child instanceof TableModel) {
            //    table.setModel(child);
            //}else if(child instanceof TableColumn) {
                table.getColumns().add(child);
            //}
        } else if(parent instanceof Accordion) {
            parent.getPanes().add(child);
        } else if(parent instanceof TitledPane) {
            if(child instanceof Node) {
                parent.content = child;
            }else {
                child.pane = parent;
            }
        }else if(parent instanceof SplitPane) {
            if(child instanceof Node)
                parent.items.add(child);
            else { // todo this should be saved and set upon node complete
                // so that all the items are saved first.
                def pcntx = builder.getParentContext();
                List dividers = pcntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
                dividers.add(child);
                
            }
        }else if(parent instanceof ToolBar) {
            parent.items.add(child);
        } else if(parent instanceof TabPane && child instanceof Tab) {
            parent.tabs.add(child);
        }else {
            super.setChild(builder, parent, child);
        }
    }
    
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof SplitPane) {
            def cntx = builder.getContext();
            List dividers = cntx.get(SceneGraphBuilder.CONTEXT_DIVIDER_KEY);
            dividers.each{div ->
                child.setDividerPosition(div.index, div.position);
            }
            dividers.clear();
        }
    }
    
    
}

