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
        def operations = builder.context.remove(CANVAS_OPERATIONS_LIST_PROPERTY)
        def dop = new DrawOperations(operations: operations, canvas: node);
        dop.draw();
        node.userData = dop;
        super.onNodeCompleted(builder, parent, node)
    }
}

