/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2019 the original author or authors.
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
class ArcToOperation implements CanvasOperation {
    double x1;
    double y1;
    double x2;
    double y2;
    double radius
    
    public void initParams(Object val) {
        x1     = val[0]
        y1     = val[1]
        x2     = val[2]
        y2     = val[3]
        radius = val[4]
    }

    public void execute(GraphicsContext gc) {
        gc.arcTo(x1, y1, x2, y2, radius);
    }
}

