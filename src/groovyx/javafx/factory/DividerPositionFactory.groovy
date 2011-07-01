/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

/**
 *
 * @author jimclarke
 */
class DividerPositionFactory extends AbstractFactory {
	
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        DividerPosition dp = new DividerPosition();
        return dp;
    }
}

