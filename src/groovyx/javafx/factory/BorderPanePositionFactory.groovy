/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
/**
 *
 * @author jimclarke
 */
class BorderPanePositionFactory extends AbstractFactory {
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
       BorderPanePosition bpp = new BorderPanePosition();
       switch(name) {
           case 'top':
                bpp.pos = Pos.TOP_CENTER;
                break;
           case 'bottom':
                bpp.pos = Pos.BOTTOM_CENTER;
                break;
           case 'left':
                bpp.pos = Pos.CENTER_LEFT;
                break;
           case 'center':
                bpp.pos = Pos.CENTER;
                break;
           case 'right':
                bpp.pos = Pos.CENTER_RIGHT;
                break;
       }
       
       return bpp;
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
         parent.addNode(child);
    }
    
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object bpp) {
        Node node = bpp.getNode();
        if(bpp.pos == Pos.TOP_CENTER) {
            parent.setTop(node);
        }else if(bpp.pos == Pos.BOTTOM_CENTER) {
            parent.setBottom(node);
        }else if(bpp.pos == Pos.CENTER_LEFT) {
            parent.setLeft(node);
        } else if(bpp.pos == Pos.CENTER) {
            parent.setCenter(node);
        }else if(bpp.pos == Pos.CENTER_RIGHT) {
            parent.setRight(node);
        }
        if(bpp.align != null) {
            BorderPane.setAlignment(node, bpp.align);
        }
        if(bpp.margin != null) {
            BorderPane.setMargin(node, bpp.margin);
        }
    }
    
}

