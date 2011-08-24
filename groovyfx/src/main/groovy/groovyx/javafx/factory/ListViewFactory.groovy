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
import javafx.collections.FXCollections;

/**
 *
 * @author jimclarke
 */
class ListViewFactory extends NodeFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        ListView listView;
        if (FactoryBuilderSupport.checkValueIsType(value, name, ListView.class)) {
            listView = value
        } else {
            listView = new ListView();
        }
        def items = attributes.remove("items");
        if(items != null) {
            if(listView.getItems() == null) // bug in ListView?
                listView.setItems(FXCollections.observableArrayList());
            listView.getItems().addAll(items);
        }

        //FXHelper.fxAttributes(listView, attributes);
        return listView;
    }

}

