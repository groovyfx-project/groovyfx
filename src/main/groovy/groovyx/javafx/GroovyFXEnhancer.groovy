/*
 * Copyright 2011-2016 the original author or authors.
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
package groovyx.javafx

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.stage.Stage
import javafx.util.Callback
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * @author jimclarke
 * @author Andres Almiray
 */
class GroovyFXEnhancer {
    private static final Object[] EMPTY_ARGUMENTS = {};
    
    static void enhanceClasses() {
        ExpandoMetaClass.enableGlobally()
        
        def origListAsType = ArrayList.metaClass.getMetaMethod("asType", [Class] as Class[])
        ArrayList.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableList) {
                    FXCollections.observableList(delegate);
                } else if(clazz == ObservableSet) {
                    FXCollections.observableSet(delegate as Set);
                }else {
                    origListAsType.invoke(delegate, clazz)
                }
            }
        }
        def origMapAsType = Map.metaClass.getMetaMethod("asType", [Class] as Class[])
        Map.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableMap) {
                    FXCollections.observableMap(delegate);
                }else {
                    origMapAsType.invoke(delegate, clazz)
                }
            }
        }
        def origSetAsType = Set.metaClass.getMetaMethod("asType", [Class] as Class[])
        Set.metaClass {
            asType << {Class clazz ->
                if(clazz == ObservableSet) {
                    FXCollections.observableSet(delegate);
                }else if(origSetAsType != null){
                    origSetAsType.invoke(delegate, clazz)
                }
            }
        }

        Node.metaClass {
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Node.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }
        
        Scene.metaClass {
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Scene.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }
        
        Stage.metaClass {
            // Handle short cut for javafx properties where xxxxProperty() is same as xxxx()
            methodMissing = {  name, args ->
                def fxName = "${name}Property"
                if(delegate.metaClass.respondsTo(delegate, fxName, InvokerHelper.EMPTY_ARGUMENTS)) {
                    def meth =  {Object[] varargs ->
                        delegate."${name}Property"();
                    }
                    Scene.metaClass."$name" = meth;
                    return meth(args)
                } else {
                    throw new MissingMethodException(name, delegate.class, args)
                }
            }
        }

        if( System.properties[ 'javafx.platform' ] != 'eglfb' ) {
            WebEngine.metaClass {
                confirmHandler << { Closure closure -> delegate.setConfirmHandler(closure as Callback)}
                createPopupHandler << { Closure closure -> delegate.setCreatePopupHandler(closure as Callback)}
                promptHandler << { Closure closure -> delegate.setPromptHandler(closure as Callback)}

                onAlert << { Closure closure -> delegate.setOnAlert(closure as EventHandler)}
                onResized << { Closure closure -> delegate.setOnResized(closure as EventHandler)}
                onStatusChanged << { Closure closure -> delegate.setOnStatusChanged(closure as EventHandler)}
                onVisibilityChanged << { Closure closure -> delegate.setOnVisibilityChanged(closure as EventHandler)}
            }
        }
    }
}
