/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import groovyx.javafx.canvas.*
/**
 *
 * @author jimclarke
 */
class DrawFactory extends AbstractNodeFactory {
    private static final String DRAW_OPERATIONS_LIST_PROPERTY = "__drawOperationsList"
    
    DrawFactory() {
        super(DrawOperations);
    }
    
    DrawFactory(Class<DrawOperations> beanClass) {
        super(beanClass);
    }
    
    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        DrawOperations operations = super.newInstance(builder, name, value, attributes);
        if(value instanceof Canvas)
            operations.canvas = value;
        operations
    }
    
    @Override
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof CanvasOperation) {
            def operations = builder.parentContext.get(DRAW_OPERATIONS_LIST_PROPERTY, [])
            operations << child
        }else {
            super.setChild(builder, parent, child);
        }
    }
    
    
    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        node.operations = builder.context.remove(DRAW_OPERATIONS_LIST_PROPERTY)
        if(node.canvas != null)
            node.draw()
        super.onNodeCompleted(builder, parent, node)
    }
}

