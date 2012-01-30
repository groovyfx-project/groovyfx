/*
* Copyright 2011-2012 the original author or authors.
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
package groovyx.javafx;

import groovy.lang.Closure;
import javafx.application.Application;
import javafx.stage.Stage;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * General starter application that displays a stage.
 * @author jimclarke
 * @author Dierk Koenig added the default delegate
 */
public class GroovyFX extends Application {
    public static Closure closure;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            closure.setDelegate(new SceneGraphBuilder(primaryStage));
            InvokerHelper.invokeClosure(closure, new Object[] { this });
        } catch(RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    /**
     * @param buildMe The code that is to be built in the context of a SceneGraphBuilder for the primary
     *                stage and started
     */
     public static void start(Closure buildMe) {
         closure = buildMe;
         Application.launch();
     }
}
