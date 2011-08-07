/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import javafx.scene.control.*;
import javafx.scene.Node;

/**
*
* @author jimclarke
*/
class MenuItemFactory extends AbstractFactory {
	private static def menuItemBuilder = [
            "menu": { builder, name, value, attributes -> return new Menu("") },
            "menuItem": { builder, name, value, attributes -> return new MenuItem() },
            "checkMenuItem": { builder, name, value, attributes -> return new CheckMenuItem() },
            "customMenuItem": { builder, name, value, attributes -> return new CustomMenuItem() },
            "separatorMenuItem": { builder, name, value, attributes -> return new SeparatorMenuItem() },
            "radioMenuItem": { builder, name, value, attributes -> return new RadioMenuItem() },
        ];
        
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        MenuItem mi;
        if (value instanceof MenuItem) {
            mi = value
        } else {
            def creator = menuItemBuilder[name];
            if(creator != null)
                mi = creator(builder, name, value, attributes);
            if(value instanceof String) {
                mi.text = value;
            } else if(value instanceof Node) {
                if(mi instanceof CustomMenuItem)
                     mi.content = node
                else
                    mi.graphic = value;    
            }
        }
        return mi;
    }
    
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof Menu && child instanceof MenuItem) {
            parent.items.add(child);        
        } else if(child instanceof Node) {
            if(parent instanceof CustomMenuItem)
                 parent.content = child
            else
                 parent.graphic = child;  
        } else if(child instanceof NodeBuilder) {
            parent.graphic = child.build();
        } 
    }
}

