/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
*
* @author jimclarke
*/
class ImageViewFactory extends AbstractNodeFactory {
    
    public ImageViewFactory() {
        super(ImageView);
    }
    
    public ImageViewFactory(Class<ImageView> beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        ImageView iv = super.newInstance(builder, name, value, attributes);
        if(value != null) {
            switch(value) {
                case Image:
                    iv.image = value;
                    break;
                default:
                    iv.image = new Image(value.toString());
                    break;
            }
        }
        iv
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Image) {
            parent.setImage = child;
        }else {
            super.setChild(builder, parent, child);
        }
    }
	
}

