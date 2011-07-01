/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.Node;

/**
 *
 * @author jimclarke
 */
class TabFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Tab tab;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Tab.class)) {
            tab = value
        } else {
            tab = new Tab();
        }
        return tab;
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Tooltip)
            parent.tooltip = child;
        else if(child instanceof Node)
            parent.content = child
            
        // graphic is set in the GraphicFactory.
    }
}

