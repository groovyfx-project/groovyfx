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
package groovyx.javafx;

import groovy.lang.Closure;
import javafx.application.Application;
import javafx.stage.Stage;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 *
 * @author jimclarke
 */
public class GroovyFX extends Application {
    public static Closure closure;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            InvokerHelper.invokeClosure(closure, new Object[] { primaryStage });
        } catch(RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    /**
     * @param c The closure to execute.
     */
     public static void start(Closure c) {
         closure = c;
         Application.launch(null);
     }
    
}
