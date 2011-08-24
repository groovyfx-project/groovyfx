/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.builders.MediaPlayerBuilder;
/**
 *
 * @author jimclarke
 */
class MediaPlayerFactory extends AbstractGroovyFXFactory{
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object mediaPlayer;
        if (FactoryBuilderSupport.checkValueIsType(value, name, MediaPlayer.class)) {
            mediaPlayer = value
        } else {
            mediaPlayer = new MediaPlayerBuilder();
        }
    }
    
    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (node instanceof MediaPlayer)
            return false;

        MediaPlayerBuilder mpb = node as MediaPlayerBuilder
        

        def attr = attributes.get("audioSpectrumInterval");
        if(attr != null)
             mpg.audioSpectrumInterval(attr) 
        
        attr = attributes.remove("audioSpectrumListener");
        if(attr != null)
             mpb.audioSpectrumListener(attr) 
             
        attr = attributes.remove("audioSpectrumNumBands");
        if(attr != null)
             mpb.audioSpectrumNumBands(attr)

        attr = attributes.remove("audioSpectrumThreshold");
        if(attr != null)
             mpb.audioSpectrumThreshold(attr) 

        attr = attributes.remove("autoPlay");
        if(attr != null)
             mpb.autoPlay(attr) 

        attr = attributes.remove("balance");
        if(attr != null)
             mpb.balance(attr) 

        attr = attributes.remove("cycleCount");
        if(attr != null)
             mpb.cycleCount(attr)

        attr = attributes.remove("media");
        if(attr != null)
             mpb.media(attr) 

        attr = attributes.remove("mute");
        if(attr != null)
             mpb.mute(attr) 

        attr = attributes.remove("onEndOfMedia");
        if(attr != null)
             mpb.onEndOfMedia(attr) 

        attr = attributes.remove("onError");
        if(attr != null)
             mpb.onError(attr) 

        attr = attributes.remove("onHalted");
        if(attr != null)
             mpb.onHalted(attr) 

        attr = attributes.remove("onPaused");
        if(attr != null)
             mpb.onPaused(attr) 

        attr = attributes.remove("onPlay");
        if(attr != null)
             mpb.onPlay(attr) 

        attr = attributes.remove("onReady");
        if(attr != null)
             mpb.onReady(attr) 

        attr = attributes.remove("onRepeat");
        if(attr != null)
             mpb.onRepeat(attr) 

        attr = attributes.remove("onStalled");
        if(attr != null)
             mpb.onStalled(attr) 

        attr = attributes.remove("onStopped");
        if(attr != null)
             mpb.onStopped(attr) 

        attr = attributes.remove("rate");
        if(attr != null)
             mpb.rate(attr) 

        attr = attributes.remove("startTime");
        if(attr != null)
             mpb.startTime(attr) 

        attr = attributes.remove("stopTime");
        if(attr != null)
             mpb.stopTime(attr) 

        attr = attributes.remove("volume");
        if(attr != null)
             mpb.volume(attr) 
             
        attr = attributes.remove("source");
        if(attr != null) 
            mpb.media(new Media(attr));
        return super.onHandleNodeAttributes(builder, node, attributes);
    }
    
     protected Object postNodeCompletion(Object parent, Object node) {
         this.onNodeCompleted();
         if(node instanceof MediaPlayerBuilder) {
             node = node.build();
         }
         
        if(parent instanceof MediaView) {
            parent.mediaPlayer= node;
        }
        return super.postNodeCompletion(parent, node);
     }
}

