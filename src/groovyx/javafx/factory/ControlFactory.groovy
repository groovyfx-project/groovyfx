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
        }else {
            super.setChild(builder, parent, child);
        }
    }
}

