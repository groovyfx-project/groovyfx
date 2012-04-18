/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.stage.FileChooser;

/**
 *
 * @author jimclarke
 */
class FilterFactory extends AbstractFXBeanFactory {
    
    FilterFactory() {
        super(null, true)
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
       def extName = value;
       if(extName == null) {
           extName = attributes.remove("description");
       }
       def extensions = attributes.remove("extensions");
       FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(extName, extensions);
       return filter;
    }
    
	
}

