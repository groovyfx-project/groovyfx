/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import groovyx.javafx.event.*
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener

/**
*
* @author jimclarke
*/
class ChangeFactory extends AbstractFXBeanFactory {
    
    ChangeFactory(Class beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
         def listener = null;
         if(ChangeListener.isAssignableFrom(beanClass)) {
             listener = new GroovyChangeListener(value?value:name)
         }else {
             listener = new GroovyInvalidationListener(value?value:name)
         }   
         listener
    }
    
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        node.closure = childContent
        return false
    }
	
}

