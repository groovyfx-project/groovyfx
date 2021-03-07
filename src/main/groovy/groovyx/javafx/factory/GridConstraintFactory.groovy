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

import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints

/**
 *
 * @author jimclarke
 */
class GridConstraintFactory extends AbstractFXBeanFactory {
    
    GridConstraintFactory(Class beanClass) {
        super(beanClass);
    }
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        def gc = super.newInstance(builder, name, value, attributes)
        FXHelper.fxAttributes(gc, attributes)
        gc;
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof GridPane) {
            if (child instanceof RowConstraints) {
                parent.rowConstraints.add(child)
            } else if (child instanceof ColumnConstraints) {
                parent.columnConstraints.add(child)
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (node instanceof GridConstraint) {
            node.node = parent
            node.updateConstraints()
        }
    }
}

