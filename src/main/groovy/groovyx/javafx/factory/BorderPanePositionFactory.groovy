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

import javafx.scene.layout.BorderPane
import org.codehaus.groovy.runtime.InvokerHelper

/**
 *
 * @author jimclarke
 */
class BorderPanePositionFactory extends AbstractFXBeanFactory {
    
    BorderPanePositionFactory() {
        super(BorderPanePosition)
    }
    BorderPanePositionFactory(Class<BorderPanePosition> beanClass) {
        super(beanClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
       BorderPanePosition bpp = super.newInstance(builder, name, value, attributes);
       bpp.property = name;
       bpp;
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child ) {
         if (child instanceof javafx.scene.Node) parent.addNode(child);
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object bpp) {
        InvokerHelper.setProperty(parent, bpp.property, bpp.node);
        if(bpp.align != null) {
            BorderPane.setAlignment(bpp.node, bpp.align);
        }
        if(bpp.margin != null) {
            BorderPane.setMargin(bpp.node, bpp.margin);
        }
    }
    
}

