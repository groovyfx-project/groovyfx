/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package groovyx.javafx.factory

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Camera;
import javafx.scene.paint.*;
import javafx.scene.Cursor;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.input.*;
import javafx.event.EventHandler;
import groovyx.javafx.ClosureChangeListener
import org.codehaus.groovy.runtime.InvokerHelper;
/**
 *
 * @author jimclarke
 */
class SceneWrapper {
    public Parent root;
    public double width = -1;
    public double height = -1;
    public Camera camera;
    public Cursor cursor;
    public Paint fill = Color.WHITE;
    public List<String> stylesheets;
    public List<ClosureChangeListener> changeListeners = new ArrayList<ClosureChangeListener>();
    
    public Map<String, EventHandler<MouseEvent>> mouseEvents =
        new HashMap<String, EventHandler<MouseEvent>>();
    public Map<String, EventHandler<KeyEvent>> keyEvents=
        new HashMap<String, EventHandler<KeyEvent>>();
        
    public addChangeListener(ClosureChangeListener listener) {
        changeListeners.add(listener);
    }
    
    public addMouseHandler(String type, EventHandler<MouseEvent> handler) {
        mouseEvents.put(type, handler);
    }
    
    public addKeyHandler(String type, EventHandler<KeyEvent> handler) {
        keyEvents.put(type, handler);
    }

    public Scene createScene() {
        Scene scene =  new Scene(root, width, height, fill);
        if(camera != null)
                scene.setCamera(camera);
        if(cursor != null)
                scene.setCursor(cursor);
        if(stylesheets != null && !stylesheets.isEmpty())
            scene.getStylesheets().setAll(stylesheets);
        mouseEvents.each { type, handler -> 
            switch(type) {
                case 'onMouseClicked':
                    scene.setOnMouseClicked(handler);
                    break;
                case 'onMouseDragged':
                    scene.setOnMouseDragged(handler);
                    break;
                case 'onMouseEntered':
                    scene.setOnMouseEntered(handler);
                    break;
                case 'onMouseExited':
                    scene.setOnMouseExited(handler);
                    break;
                case 'onMouseMoved':
                    scene.setOnMouseMoved(handler);
                    break;
                case 'onMousePressed':
                    scene.setOnMousePressed(handler);
                    break;
                case 'onMouseReleased':
                    scene.setOnMouseReleased(handler);
                    break;
                case 'onMouseWheelMoved':
                    scene.setOnMouseWheelMoved(handler);
                    break;
            }
        }
        
        keyEvents.each { type, handler -> 
            switch(type) {
                case 'onKeyPressed':
                    scene.setOnKeyPressed(handler);
                    break;
                case 'onKeyReleased':
                    scene.setOnKeyReleased(handler);
                    break;
                case 'onKeyTyped':
                    scene.setOnKeyTyped(handler);
                    break;
            }
        }
        
        changeListeners.each { listener ->
            def property = InvokerHelper.invokeMethod(scene, listener.property + "Property", null);
            InvokerHelper.invokeMethod(property, "addListener", listener );
        }
        return scene;
    }
}

