/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo

import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.GroovyFX;
import javafx.embed.swing.JFXPanel
import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
import java.awt.Dimension
import javafx.application.Platform;

def DEFAULT_URL = "http://www.yahoo.com"
def swing = new SwingBuilder();
def fxPanel = new JFXPanel(preferredSize: new Dimension(800,400));

def setUrl = { url ->
    Platform.runLater {
        webEngine.load(url)
    }
}

swing.edt {
    frame(title:'Swing Embedded Browser', size:[800,500], show: true) {
        borderLayout()
        panel(constraints: BL.NORTH) {
            textField(id: "urlField", actionPerformed: {setUrl(urlField.text)}, columns: 40)
            button(text: "Go", actionPerformed: {setUrl(urlField.text)})
        }
        widget(fxPanel, constraints: BL.CENTER)
    }
}

GroovyFX.start {
     def sg = new SceneGraphBuilder(it);
     fxPanel.scene = sg.scene(width: 800, height: 400) {
         webEngine = webEngine(location: DEFAULT_URL)
         webView(engine: webEngine)
     }
}