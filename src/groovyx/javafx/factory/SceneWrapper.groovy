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
import javafx.builders.SceneBuilder;
/**
 *
 * @author jimclarke
 */
class SceneWrapper extends SceneBuilder {
    public List<ClosureChangeListener> changeListeners = new ArrayList<ClosureChangeListener>();
    public Parent sceneRoot;
    
    
    public SceneWrapper() {
        super();
    }   
    public addChangeListener(ClosureChangeListener listener) {
        changeListeners.add(listener);
    }
    
    @Override
    public SceneBuilder root(Parent x) {
        if(x != null) {
            sceneRoot = x;
            super.root(x);
        }
        return this;
    }

    public Scene build() {
        Scene scene =  super.build();
        changeListeners.each { listener ->
            def property = InvokerHelper.invokeMethod(scene, listener.property + "Property", null);
            InvokerHelper.invokeMethod(property, "addListener", listener );
        }
        return scene;
    }
    
    public void addInputHandler(String type, EventHandler handler) {
        InvokerHelper.invokeMethod(this, type, [handler]);
    }
    
 
}

