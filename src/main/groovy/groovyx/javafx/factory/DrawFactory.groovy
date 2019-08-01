/*
 * Copyright 2011-2019 the original author or authors.
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

import groovyx.javafx.canvas.CanvasOperation
import groovyx.javafx.canvas.DrawOperations
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas

/**
 *
 * @author jimclarke
 */
class DrawFactory extends AbstractNodeFactory {
    private static final String DRAW_OPERATIONS_LIST_PROPERTY = "__drawOperationsList"

    DrawFactory() {
        super(DrawOperations)
    }

    DrawFactory(Class<DrawOperations> beanClass) {
        super(beanClass)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        DrawOperations operations = super.newInstance(builder, name, value, attributes)
        if (value instanceof Canvas)
            operations.canvas = value
        operations
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof CanvasOperation) {
            def operations = builder.parentContext.get(DRAW_OPERATIONS_LIST_PROPERTY, FXCollections.observableList([]))
            operations << child
        } else {
            super.setChild(builder, parent, child)
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        node.operations = builder.context.remove(DRAW_OPERATIONS_LIST_PROPERTY)
        if (node.canvas != null)
            node.draw()
        super.onNodeCompleted(builder, parent, node)
    }
}
