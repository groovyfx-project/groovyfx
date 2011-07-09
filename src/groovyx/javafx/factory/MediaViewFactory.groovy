/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory
import javafx.scene.media.MediaView;

/**
 *
 * @author jimclarke
 */
class MediaViewFactory extends NodeFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        MediaView mediaView;
        if (FactoryBuilderSupport.checkValueIsType(value, name, MediaView.class)) {
            mediaView = value
        } else {
            mediaView = new MediaView();
        }
    }
    
}

