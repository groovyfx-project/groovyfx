/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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

import groovyx.javafx.event.GroovyCallback
import groovyx.javafx.event.GroovyEventHandler
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker.State
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.codehaus.groovy.runtime.InvokerHelper

/**
 *
 * @author jimclarke
 */
class WebFactory extends AbstractNodeFactory {
    WebFactory(Class beanClass) {
        super(beanClass)
    }
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object instance = super.newInstance(builder, name, value, attributes)

        if(WebView.isAssignableFrom(beanClass)) {
            def location = value.toString();
            if(location == null)
                location = attributes.remove("location");
            if(location != null)
                instance.engine.load(location.toString());
        }else if(HTMLEditor.isAssignableFrom(beanClass)) {
            if(value != null)
                instance.htmlText = value.toString()
        }

        instance;
    }
    
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof WebView) {
            switch(child) {
                case GroovyEventHandler:
                case GroovyCallback:
                    if(child.property == "onLoad") {
                        def listener = new ChangeListener<State>() {
                                @Override
                                public void changed(ObservableValue<? extends State> observable, 
                                            State oldState, State newState) {
                                    switch(newState) {
                                        case State.SUCCEEDED:
                                            child.closure.call(parent);
                                            break;
                                        default:
                                            break;

                                    }
                                }
                        };
                        parent.engine.loadWorker.stateProperty().addListener(listener);

                    }else if(child.property == "onError") {
                        def listener = new ChangeListener<State>() {
                                @Override
                                public void changed(ObservableValue<? extends State> observable, 
                                            State oldState, State newState) {
                                    switch(newState) {
                                        case State.FAILED:
                                            child.closure.call(parent.engine.loadWorker.message, parent.engine.loadWorker.exception);
                                            break;
                                        default:
                                            break;

                                    }
                                }
                        };
                        parent.engine.loadWorker.stateProperty().addListener(listener); 
                    }else {
                        InvokerHelper.setProperty(parent.engine, child.property, child);
                    }
                    break;

            }
        }else if(parent instanceof HTMLEditor && child instanceof String) {
            parent.htmlText = value.toString()
        }else {
            super.setChild(builder, parent, child)
        }
    }
}

