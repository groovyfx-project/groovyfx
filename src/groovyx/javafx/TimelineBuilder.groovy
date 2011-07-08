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

package groovyx.javafx

import java.util.logging.Logger
import groovyx.javafx.factory.animation.*
import javafx.util.Duration;
import javafx.animation.*;
/**
 *
 * @author jimclarke
 */
class TimelineBuilder extends FactoryBuilderSupport {
    // local fields
    private static final Logger LOG = Logger.getLogger(TimelineBuilder.name)
    private static boolean headless = false

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    public TimelineBuilder(boolean init = true) {
        super(init)
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
        ExpandoMetaClass.enableGlobally()
        Number.metaClass.getM = { -> new Duration(delegate*1000.0*60.0)};
        Number.metaClass.getS = { -> new Duration(delegate*1000.0)};
        Number.metaClass.getMs = { -> new Duration(delegate)};
        Number.metaClass.getH = { -> new Duration(delegate*1000.0*60.0*60.0)};

    }

    public def registerTimelines() {
        registerFactory("timeline", new TimelineFactory())

        registerFactory("at", new KeyFrameFactory())
        registerFactory("action", new KeyFrameActionFactory())
        registerFactory("change", new KeyValueFactory())
        registerFactory("to", new KeyValueSubFactory())
        registerFactory("tween", new KeyValueSubFactory())
        
    }
    
    def propertyMissing(String name) {
        switch(name.toLowerCase()) {
            case "ease_both":
            case "easeboth":
                return Interpolator.EASE_BOTH;
            case "ease_in":
            case "easein":
                return Interpolator.EASE_IN;
            case "ease_out":
            case "easeout":
                return Interpolator.EASE_OUT;
            case "discreate":
                return Interpolator.DISCRETE;
            case "linear":
                return Interpolator.LINEAR;
        }
        throw new MissingPropertyException("Unrecognized property: ${name}", name, this.class);
    }
    
}

