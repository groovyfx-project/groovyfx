/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
/**
 *
 * @author jimclarke
 */

GroovyFX.start({
def sg = new SceneGraphBuilder(it);

def popup = sg.popup(autoHide: true) {
    group() {
        rectangle(width: 200, height: 200, fill: blue)
        button( layoutX: 10, layoutY: 20, text: "foo")
    }
    
}

sg.stage(
    title: "TreeView Example",
    x: 100, y: 100, width: 400, height:400,
    visible: true,
    style: "decorated",
) {

    scene(fill: hsb(128, 0.5, 0.5, 0.5) ) {
        treeView ( showRoot: false) {
            //onEditCancel()
            //onEditStart()
            //onEditCommit()
            treeItem(expanded: true, value: "Root") {
                treeItem(value: "one") {
                    branchCollapse(onEvent: {popup.show(it, 100, 100);})
                    branchExpand(onEvent: {println "one expand"})
                    treeItem(value: "one.one")
                    treeItem(value: "one.two")
                    treeItem(value: "one.three")
                    graphic() {
                        rectangle(width:20, height: 20, fill: red)
                    }
                }
                treeItem(value: "two") {
                    treeItem(value: "two.one")
                    treeItem(value: "two.two")
                    treeItem(value: "two.three")
                    graphic() {
                        rectangle(width:20, height: 20, fill: green)
                    }
                }
                treeItem(value: "three") {
                    treeItem(value: "three.one")
                    treeItem(value: "three.two")
                    treeItem(value: "three.three")
                    graphic() {
                        rectangle(width:20, height: 20, fill: blue)
                    }
                }
                treeItem(value: "four") {
                    treeItem(value: "four.one")
                    treeItem(value: "four.two")
                    treeItem(value: "four.three")
                    graphic() {
                        rectangle(width:20, height: 20, fill: orange)
                    }
                }
                treeItem(value: "five") {
                    treeItem(value: "five.one")
                    treeItem(value: "five.two")
                    treeItem(value: "five.three")
                    graphic() {
                        rectangle(width:20, height: 20, fill: yellow)
                    }
                }
            }
        }
    }
}

});




