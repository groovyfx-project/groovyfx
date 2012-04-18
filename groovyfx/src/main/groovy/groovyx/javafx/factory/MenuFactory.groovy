/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import javafx.scene.control.*;
import javafx.stage.Window;

/**
*
* @author jimclarke
*/
class MenuFactory extends AbstractNodeFactory {
    
    
    MenuFactory(Class beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object menu = super.newInstance(builder, name, value, attributes);
        if(value != null) {
             switch(menu) {
                 case MenuButton:
                 case SplitMenuButton:
                     menu.text = value.toString();
                     break;
             }
            
        }
        menu;
    }
    
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof MenuBar && child instanceof Menu) {
            parent.menus.add(child);
        }else if (child instanceof MenuItem) {
            parent.items.add(child);
        }else {
            super.setChild(builder, parent, child);
        }
    }
}

