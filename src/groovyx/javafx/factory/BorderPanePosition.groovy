/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Group;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author jimclarke
 */
class BorderPanePosition {
    public Pos pos;
    public Pos align;
    public Insets margin;
    public List<Node> nodes = new ArrayList<Node>();
        
    public void addNode(Node node) {
        nodes.add(node);
    }
    public Node getNode() {
        switch(nodes.size()) {
            case 0:
                return null;
            case 1:
                return nodes.get(0);
            default:
                Group grp = new Group();
                grp.getChildren().setAll(nodes);
                return grp;
        }
    }
}

