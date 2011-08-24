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
class MenuFactory extends NodeFactory {
    
    private static def menuBuilder = [
        "menuBar": {  builder, name, value, attributes ->  return new MenuBar()  },
        "contextMenu": { builder, name, value, attributes -> return new ContextMenu() },
        "menuButton": {  builder, name, value, attributes -> return new MenuButton(value) },
        "splitMenuButton" : {  builder, name, value, attributes -> 
            def smi = new SplitMenuButton() 
            smi.text = value;
            return smi;
        }
    ]
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object menu;
        if (value instanceof Control || value instanceof ContextMenu ) {
            menu = value
        } else {
            def creator = menuBuilder[name];
            if(creator != null)
                menu = creator(builder, name, value, attributes);
        }
        return menu;
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

