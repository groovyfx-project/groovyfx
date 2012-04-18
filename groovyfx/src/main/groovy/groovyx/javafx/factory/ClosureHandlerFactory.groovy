/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import groovyx.javafx.event.GroovyEventHandler
import javafx.event.EventHandler

/**
*
* @author jimclarke
*/
class ClosureHandlerFactory extends AbstractFXBeanFactory {
    
    
    public ClosureHandlerFactory(Class beanClass) {
        super(beanClass)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        GroovyEventHandler eh = super.newInstance(builder, name, value, attributes);
        eh.property = name.toString();
        if(value instanceof Closure) {
             eh.closure = value;
        }
        eh
    }
    
    @Override
    boolean isHandlesNodeChildren() { true }

    @Override
    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        if(childContent)
            node.closure = childContent

        return false
    }
	
}

