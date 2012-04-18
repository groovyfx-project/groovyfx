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
class MenuItemFactory extends AbstractNodeFactory {
    MenuItemFactory(Class beanClass) {
        super(beanClass)
    }
    
    MenuItemFactory(Class beanClass, Closure instantiator) {
        super(beanClass, instantiator)
    }
        
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(Menu.isAssignableFrom(beanClass)) {
            return handleMenuNode(builder, name, value, attributes)
        }
      
        if(value == null) {
            return super.newInstance(builder, name, value, attributes)
        }
        
        
        MenuItem mi = null
        switch(value) {
            case CharSequence:
                mi = super.newInstance(builder, name, value, attributes)
                mi.text = value.toString()
                break
            case MenuItem:
                mi = super.newInstance(builder, name, value, attributes)
                mi.items.add(value);
                break
            case Node:
                mi = super.newInstance(builder, name, null, attributes)
                if(mi instanceof CustomMenuItem) {
                    mi.content = node
                } else {
                    mi.graphic = node
                }
                break
            default:
                throw new Exception("In $name value must be an instanceof MenuItem or one of its subclass, a String or a Node to be used as embedded content.")
        }
        mi
    }
    
     protected Menu handleMenuNode(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if(value == null) 
            return beanClass.newInstance("")

        Menu menu = null
        switch(value) {
            case Menu:
                menu = value
                break
            case CharSequence:
                menu = beanClass.newInstance(value.toString())
                break
            case Node:
                menu = beanClass.newInstance("")
                menu.graphic = value
                break
            default:
                throw new Exception("In $name value must be an instanceof Menu or one of its subclasses, a String or a Node to be used as graphic content.")
        }
        menu
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
        } else {
            super.setChild(builder, parent, child);
        }
    }
}

