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

import javafx.stage.Stage
import javafx.scene.Scene
import javafx.stage.StageStyle
import javafx.stage.Popup;

/**
 *
 * @author jimclarke
 */
class StageFactory extends AbstractFactory {
    SceneWrapper sceneWrapper;

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def window = null;
        if(name == "stage") {

            def style = attributes.remove("style")
            if(style == null) {
                style = StageStyle.DECORATED;
            }
            if(style instanceof String) {
                style = StageStyle.valueOf(style.toUpperCase())
            }


            def primary = attributes.remove("primary")
            if(primary == null) {
                primary = true;
            }
            if (FactoryBuilderSupport.checkValueIsType(value, name, Stage.class)) {
                window = value
            } else if (primary && builder.stage != null) {
                window = builder.stage;
                window.style = style;
            } else {
                window = new Stage(style)
                if(primary)
                    builder.stage = window;
            }
        }else if(name == "popup") {
            window = new Popup();
        }
        def onHidden = attributes.remove("onHidden");
        if(onHidden != null) {
            def handler = new ClosureEventHandler(closure: onHidden);
            window.onHidden = handler;
        }
        def onHidding = attributes.remove("onHidding");
        if(onHidding != null) {
            def handler = new ClosureEventHandler(closure: onHidding);
            window.onHidding = handler;
        }
        def onShowing = attributes.remove("onShowing");
        if(onShowing != null) {
            def handler = new ClosureEventHandler(closure: onShowing);
            window.onShowing = handler;
        }
        def onShown = attributes.remove("onShown");
        if(onShown != null) {
            def handler = new ClosureEventHandler(closure: onShown);
            window.onShown = handler;
        }

        return window;
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if(child instanceof SceneWrapper) {
            sceneWrapper = child;
        }else if(parent instanceof Popup) {
            parent.content.add(child);
        }
    }

   

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(node instanceof Stage) {
            if(sceneWrapper != null)
                node.scene = sceneWrapper.build();
            if(node.getWidth() == -1)
                node.sizeToScene();
            if (builder.context.show) {
                node.visible = true
            }
        }

    }

}

