

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
/**
 *
 * @author jimclarke
 */

GroovyFX.start({
def sg = new SceneGraphBuilder(it);

sg.stage(
    title: "SplitPane Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        splitPane ( orientation: "horizontal") {
            dividerPosition(index: 0, position: 0.25)
            dividerPosition(index: 1, position: 0.50)
            dividerPosition(index: 2, position: 1.0)
            label(text: "This is Label 1\n\nAnd there were a few empty lines just there!") // left or top
            label(text: "This is Label 2\n\nAnd there were a few empty lines just there!") // right or bottom
            label(text: "This is Label 3\n\nAnd there were a few empty lines just there!")
            
        }
    }
}

});


