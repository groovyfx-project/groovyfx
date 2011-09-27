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

import groovyx.javafx.SceneGraphBuilder

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

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

/**
 * @author dean
 */
GroovyFX.start { primaryStage ->
    def sg = new SceneGraphBuilder(primaryStage);

    sg.stage(title: "GroovyFX Choice Box Demo", width: 400, height:200, visible: true) {
         scene(fill: groovyblue) {
             vbox(padding: 10, spacing: 5) {
                 choiceBox(items: ['blue', 'green', 'red'])
             }
         }
    }
}

