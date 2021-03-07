/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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
class StrokePolylineOperation implements CanvasOperation {
    double[] xPoints
    double[] yPoints
    int nPoints

    void initParams(Object val) {
       if(val[0] instanceof Number) {
            xPoints = new double[1];
            xPoints[0] = val[0]
        }else if(val[0] instanceof List) {
            xPoints = new double[val[0].size()];
            for(int i = 0; i < val[0].size(); i++)
                xPoints[i] = val[0][i];
        }
        if(val[1] instanceof Number) {
            yPoints = new double[1];
            yPoints[1] = val[1]
        }else if(val[0] instanceof List) {
            yPoints = new double[val[0].size()];
            for(int i = 0; i < val[1].size(); i++)
                yPoints[i] = val[1][i];
        }
        nPoints = val[3]; 
    }

    void execute(GraphicsContext gc) {
        gc.strokePolyline(xPoints, yPoints, nPoints);
    }
}

