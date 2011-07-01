/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.scene.Node;

/**
 *
 * @author jimclarke
 */
class TitledFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Titled titled;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Titled.class)) {
            titled = value
        } else {
            titled = new Titled();
            titled.isTitle = name == "title";
        }
        // pane is set to the titledPane in the parent setChild.
        return titled;
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(parent.isTitle) {
            parent.pane.title = child;
        }else {
            parent.pane.content = child;
        }
    }
}

