/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
import javafx.embed.swing.JFXPanel
import javafx.application.Platform;
import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
import java.awt.Dimension;

def swing = new SwingBuilder();




count = 0
def fxPanel = new JFXPanel(preferredSize: new Dimension(100,100));

def fr = null;
swing.edt {
  fr = frame(title:'Frame', size:[300,300], show: true) {
    borderLayout()
    textlabel = label(text:"Click the button!", constraints: BL.NORTH)
    widget(fxPanel, constraints: BL.CENTER)
    button(text:'Click Me',
         actionPerformed: {count++; textlabel.text = "Clicked ${count} time(s)."; println "clicked"},
         constraints:BL.SOUTH)
  }
}

GroovyFX.start {
     def sg = new SceneGraphBuilder(it);
     fxPanel.scene = sg.scene(width: 100, height: 100) {
         fill red
         rectangle(x: 30, y: 20, width: 10, height: 10, fill: green)
     }
}