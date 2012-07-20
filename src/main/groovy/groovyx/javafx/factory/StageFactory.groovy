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

import javafx.stage.FileChooser
import javafx.stage.Popup
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.event.EventHandler;
import groovyx.javafx.event.*

/**
 *
 * @author jimclarke
 */
class StageFactory extends AbstractFXBeanFactory {
    
    public StageFactory(Class beanClass) {
        super(beanClass)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
                if(Stage.isAssignableFrom(beanClass)) {
                    return handleStage(builder, name, value, attributes)
                }
                def window = super.newInstance(builder, name, value, attributes)
                def id = attributes.remove("id");
                if(id != null) {
                    if(id != null)
                        builder.getVariables().put(id, window);
                }
                if( ! FileChooser.isAssignableFrom(beanClass)) {
                    def onHidden = attributes.remove("onHidden");
                    if(onHidden != null) {
                        window.onHidden = onHidden as EventHandler;
                    }
                    def onHidding = attributes.remove("onHidding");
                    if(onHidding != null) {
                        window.onHidding = onHidding as EventHandler;
                    }
                    def onShowing = attributes.remove("onShowing");
                    if(onShowing != null) {
                        window.onShowing = onShowing as EventHandler;
                    }
                    def onShown = attributes.remove("onShown");
                    if(onShown != null) {
                        window.onShown = onShown as EventHandler;
                    }
                }

        return window;
    }
    
    private Stage handleStage(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        def window = null;
        def style = attributes.remove("style")
        if(style == null) {
            style = StageStyle.DECORATED;
        }
        if(style instanceof String) {
            style = StageStyle.valueOf(style.toUpperCase())
        }
        def centerOnScreen = attributes.remove("centerOnScreen");
        builder.context.put("centerOnScreen", centerOnScreen);

        def show = attributes.remove("show");
        if(show == null)
            show = attributes.remove("visible");
        builder.context.put("show", show);

        def primary = attributes.remove("primary");
        if(primary == null)
            primary = true;

        if (checkValue(name, value)) {
            window = value
        } else if (primary && builder.variables.primaryStage != null) {
            window = builder.variables.primaryStage;
            window.initStyle(style)
        } else {
            window = new Stage(style)
            if(primary)
                builder.variables.primaryStage = window;
        }
        
        def id = attributes.remove("id");
        if(id != null) {
            builder.getVariables().put(id, window);
        }
        window;
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if(parent instanceof Popup) {
            parent.content.add(child);
        }else if(parent instanceof FileChooser && child instanceof FileChooser.ExtensionFilter) {
            parent.getExtensionFilters().add(child);
        }else if (child instanceof GroovyEventHandler) {
            FXHelper.setPropertyOrMethod(parent, child.property, child) 
        }
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(node instanceof Stage) {
            if(node.getWidth() == -1)
                node.sizeToScene();
            if(builder.context.centerOnScreen) {
                node.centerOnScreen();
            }
            if (builder.context.show) {
                node.show();
            }
        }

    }

}

