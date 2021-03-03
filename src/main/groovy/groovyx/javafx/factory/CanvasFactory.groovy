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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
class CanvasFactory extends AbstractNodeFactory {
    private static final String CANVAS_OPERATIONS_LIST_PROPERTY = "__canvasOperationsList"
    
    CanvasFactory() {
        super(Canvas);
    }
    
    CanvasFactory(Class<Canvas> beanClass) {
        super(beanClass);
    }
    
    @Override
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof CanvasOperation) {
            def operations = builder.parentContext.get(CANVAS_OPERATIONS_LIST_PROPERTY, [])
            operations << child
        }else {
            super.setChild(builder, parent, child);
        }
    }
    
    
    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        def operations = FXCollections.observableArrayList(builder.context.remove(CANVAS_OPERATIONS_LIST_PROPERTY))
        def dop = new DrawOperations(operations: operations, canvas: node);
        dop.draw();
        node.userData = dop;
        super.onNodeCompleted(builder, parent, node)
    }
}

