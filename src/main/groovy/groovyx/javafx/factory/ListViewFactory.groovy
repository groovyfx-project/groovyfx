/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.ListView

/**
 *
 * @author jimclarke
 */
class ListViewFactory extends AbstractNodeFactory {
    
    ListViewFactory() {
        super(ListView)
    }
    
    ListViewFactory(Class<ListView> beanClass) {
        super(beanClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        ListView listView = super.newInstance(builder, name, value, attributes);
        def items = attributes.remove("items");
        if(items != null) {
            if(listView.getItems() == null) // bug in ListView?
                listView.setItems(FXCollections.observableArrayList());
            listView.getItems().addAll(items);
        }
        final def onSelect = attritues.remove("onSelect");
        if(onSelect != null) {
             listView.selectionModel.selectedItemProperty().addChangeListener(new ChangeListener() {
                void changed(final ObservableValue observable, final Object oldValue, final Object newValue) {
                    builder.defer({onSelect.call(newValue);});
                }
             });
        }
            
        listView;
    }

}

