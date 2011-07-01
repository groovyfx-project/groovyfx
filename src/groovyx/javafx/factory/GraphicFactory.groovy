/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

/**
 *
 * @author jimclarke
 */
class GraphicFactory extends AbstractFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
       Graphic graphic = new Graphic();
       return graphic;
    }
    
     public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
         parent.node = child;
     }
    
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        parent.graphic = node.node;
    }
}

