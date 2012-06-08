/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import groovyx.javafx.canvas.CanvasOperation

/**
 *
 * @author jimclarke
 */
class CanvasOperationFactory extends AbstractFXBeanFactory {
    
    CanvasOperationFactory(Class<CanvasOperation> beanClass) {
        super(beanClass);
    }
    
     public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
         Object result = super.newInstance(builder, name, value, attributes);
         if(value != null) {
             result.initParams(value);
         }
         result
     }
}

