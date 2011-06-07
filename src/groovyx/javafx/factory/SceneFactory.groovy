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

import javafx.scene.Scene
import javafx.scene.paint.Color
import java.util.List;
import javafx.scene.Node;
import groovyx.javafx.SceneGraphBuilder;
import javafx.scene.Group;
import javafx.scene.Parent;

/**
 *
 * @author jimclarke
 */
class SceneFactory extends AbstractFactory {


    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        SceneWrapper scene;
        if (FactoryBuilderSupport.checkValueIsType(value, name, SceneWrapper.class)) {
            scene = value
        } else {
            def root = attributes.remove("parent");
            scene = new SceneWrapper(parent: root)
        }

        builder.getContext().put(SceneGraphBuilder.CONTEXT_SCENE_KEY, SceneWrapper)
        return scene;
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        SceneWrapper scene = (SceneWrapper)parent;
        if(scene.parent == null && child instanceof Node) {
            if(child instanceof Parent ) {
                scene.parent = child;
                return;
            } else {
                scene.parent = new Group();
            }
        }
        if(child instanceof Node)
            scene.parent.getChildren().add((Node) child);
        //TODO add stylesheets
        else if(child instanceof List) {
            scene.stylesheets = (List)child;
        }
    }

}


