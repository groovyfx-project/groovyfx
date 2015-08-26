/*
 * Copyright 2011-2015 the original author or authors.
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

import javafx.stage.Stage
import java.util.concurrent.CountDownLatch

public class GroovyFXTest extends GroovyTestCase {

    void testStartSetsDelegate() {
        Stage startParameter
        def latch = new CountDownLatch(1)
        com.sun.javafx.application.PlatformImpl.startup { // not an official API
            startParameter = new Stage()
            latch.countDown()
        }
        latch.await()
        def closure
        closure = { app ->
            assert primaryStage.is(startParameter)
            assert closure.delegate in SceneGraphBuilder
        }
        def application = new GroovyFX()
        application.closure = closure
        application.start(startParameter)
    }
}
