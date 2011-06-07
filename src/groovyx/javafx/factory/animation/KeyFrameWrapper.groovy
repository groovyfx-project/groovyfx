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

package groovyx.javafx.factory.animation

import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
/**
 *
 * @author jimclarke
 */
class KeyFrameWrapper {
    public Duration time;
    public EventHandler<ActionEvent> onFinished = null;
    public Collection<KeyValue> values = new ArrayList<KeyValue>();

    public KeyFrame createKeyFrame() {
        return new KeyFrame(time, (String)null, onFinished, (Collection<KeyValue>)values);
    }
}

