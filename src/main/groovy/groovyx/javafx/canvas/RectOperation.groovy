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

package groovyx.javafx.canvas

import groovyx.javafx.beans.FXBindable
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author jimclarke
 */
@FXBindable
class RectOperation implements CanvasOperation {
    double x
    double y
    double w
    double h
    
    public void initParams(Object val) {
        x         = val[0]
        y         = val[1]
        w         = val[2]
        h         = val[3]
    }

    public void execute(GraphicsContext gc) {
        gc.rect(x, y, w, h);
    }
}

