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

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane

/**
 *
 * @author jimclarke
 */
class ContainerFactory extends AbstractNodeFactory {
    private static final String BUILDER_LIST_PROPERTY = "__builderList"
    
    ContainerFactory(Class beanClass) {
        super(beanClass);
    }


    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Node) {
            if (parent instanceof BorderPane) {
                parent.setCenter(child)
            } else {
                ((Parent) parent).getChildren().add((Node) child)
            }
        } else if (child instanceof List) {
            ((Parent) parent).getChildren().addAll((List) child)

        } else if (parent instanceof GridPane && child instanceof RowColumnInfo) {
            GridPane grid = (GridPane) parent
            RowColumnInfo rci = (RowColumnInfo) child
            if (rci.rowInfo != null) {
                if (rci.range != null) {
                    for (i in rci.range) {
                        grid.getRowInfo().add(i, rci.rowInfo)
                    }
                } else {
                    grid.getRowInfo().add(rci.rowInfo)
                }
            } else if (rci.columnInfo != null) {
                if (rci.range != null) {
                    for (i in rci.range) {
                        grid.getColumnInfo().add(i, rci.columnInfo)
                    }
                } else {
                    grid.getColumnInfo().add(rci.columnInfo)
                }
            }
        } /*else if (child instanceof NodeBuilder) {
            def builderList = builder.parentContext.get(BUILDER_LIST_PROPERTY, [])
            builderList << child
        } */else {
            super.setChild(builder, parent, child)
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        def builderList = builder.context.remove(BUILDER_LIST_PROPERTY)
        builderList?.each {
            ((Parent) node).getChildren().add(it.build())
        }
    }
}

