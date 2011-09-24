/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayerBuilder;
/**
 *
 * @author jimclarke
 */
class MediaPlayerFactory extends AbstractGroovyFXFactory{
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object mediaPlayer;
        if (value != null && value instanceof MediaPlayer) {
            mediaPlayer = value
        } else {
            mediaPlayer = new MediaPlayerBuilder();
            if(value != null) {
                if(value instanceof Media)
                    mediaPlayer.media(value);
                else
                    mediaPlayer.media(new Media(value.toString()))
            }
            // Need to this here so that we are sure to return a MediaPlayer, not the builder.
            handleMediaPlayerAttributes(mediaPlayer, attributes);
            return mediaPlayer.build();
        }
    }
    
    private void handleMediaPlayerAttributes(Object node, Map attributes) {

        MediaPlayerBuilder mpb = node as MediaPlayerBuilder
        

        def attr = attributes.remove("audioSpectrumInterval");
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

             
        attr = attributes.remove("onPlaying");
        if(attr != null)
             mpb.onPlaying(attr) 

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
        
        
        // source is alias for media
        attr = attributes.remove("source");
        if(attr == null)
            attr = attributes.remove("media");
        if(attr != null) {
            if(attr instanceof Media)
                mpb.media(attr);
            else
                mpb.media(new Media(attr.toString()));
        }
    }
    
}

