/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import groovyx.javafx.ClosureChangeListener
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 *
 * @author jimclarke
 */
class ChangeFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        ClosureChangeListener cl = new ClosureChangeListener();
        
        def closure = attributes.remove("changed");
        cl.closure = closure;
        
        return cl;
    }
    
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(parent instanceof SceneWrapper) {
            parent.addChangeListener(node);
        }else {
            try {
                def property = InvokerHelper.invokeMethod(parent,node.property + "Property", null);
                InvokerHelper.invokeMethod(property, "addListener", node );
            }catch(MissingMethodException ex) {
                println("No JavaFX property '" + node.property + "' for class " + parent.class);
            }
        }
    }
}

