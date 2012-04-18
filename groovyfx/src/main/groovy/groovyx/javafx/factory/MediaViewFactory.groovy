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
class MediaViewFactory extends AbstractNodeFactory {
    
    MediaViewFactory() {
        super(MediaView)
    }
    MediaViewFactory(Class<MediaView> beanClass) {
        super(beanClass);
    }
    
}

