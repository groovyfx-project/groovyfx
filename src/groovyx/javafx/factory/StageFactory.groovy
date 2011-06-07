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

/**
 *
 * @author jimclarke
 */
class StageFactory extends AbstractFactory {
    SceneWrapper sceneWrapper;

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Stage stage

        def style = attributes.remove("style")
        if(style == null) {
            style = StageStyle.DECORATED;
        }
        if(style instanceof String) {
            style = StageStyle.valueOf(style.toUpperCase())
        }
        
        

        if (FactoryBuilderSupport.checkValueIsType(value, name, Stage.class)) {
            stage = value
        } else if (builder.stage != null) {
            stage = builder.stage;
            stage.style = style;
        } else {
            stage = new Stage(style)
        }
        
        def onHidden = attributes.remove("onHidden");
        if(onHidden != null) {
            def handler = new ClosureEventHandler(closure: onHidden);
            stage.onHidden = handler;
        }
        def onHidding = attributes.remove("onHidding");
        if(onHidding != null) {
            def handler = new ClosureEventHandler(closure: onHidding);
            stage.onHidding = handler;
        }
        def onShowing = attributes.remove("onShowing");
        if(onShowing != null) {
            def handler = new ClosureEventHandler(closure: onShowing);
            stage.onShowing = handler;
        }
        def onShown = attributes.remove("onShown");
        if(onShown != null) {
            def handler = new ClosureEventHandler(closure: onShown);
            stage.onShown = handler;
        }

        //FXHelper.fxAttributes(stage, attributes);
        return stage;
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if(child instanceof SceneWrapper) {
            sceneWrapper = child;
        }
    }

   

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        node.setScene(sceneWrapper.createScene());
        if(node.getWidth() == -1)
            node.sizeToScene();
        if (builder.context.show) {
            node.visible = true
        }

    }

}

