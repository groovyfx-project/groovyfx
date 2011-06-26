

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
    title: "ToolBar Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        toolBar ( orientation: "vertical") {
            button(text: "button 1") 
            button(text: "button 2")
            button(text: "button 3")
            button(text: "button 4")
            button(text: "button 5")
            button(text: "button 6")
            button(text: "button 7")
            button(text: "button 8")
            button(text: "button 9")
            button(text: "button 10")
            
        }
    }
}

});


