/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
/**
*
* @author jimclarke
*/
class TitledContent {
    public TitledPane pane;
    public Node node;
    
    public void setNode(Node node) {
        this.node = node;
        pane.content = node
    }
}

