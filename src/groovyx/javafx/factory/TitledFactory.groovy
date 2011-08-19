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

import javafx.scene.Node;

/**
 *
 * @author jimclarke
 */
class TitledFactory extends AbstractGroovyFXFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Titled titled;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Titled.class)) {
            titled = value
        } else {
            titled = new Titled();
            titled.isTitle = name == "title";
        }
        // pane is set to the titledPane in the parent setChild.
        return titled;
    }
    
    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(parent.isTitle) {
            parent.pane.title = child;
        }else {
            parent.pane.content = child;
        }
    }
}

