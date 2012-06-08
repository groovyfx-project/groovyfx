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
import javafx.scene.shape.ArcType;

/**
 *
 * @author jimclarke
 */
@FXBindable
class FillArcOperation implements CanvasOperation {
    double x;
    double y;
    double w;
    double h;
    double startAngle;
    double arcExtent;
    ArcType closure;
    
    public void initParams(Object val) {
        x          = val[0]
        y          = val[1]
        w          = val[2]
        h          = val[3]
        startAngle = val[4]
        arcExtent  = val[5]
        if(val[6] instanceof ArcType) {
            closure = val[6];
        }else {
            closure = Enum.valueOf(ArcType,val[6].toString().trim().toUpperCase())
        }
        
    }

    public void execute(GraphicsContext gc) {
        gc.fillArc(x, y, w, h, startAngle, arcExtent, closure);
    }
}

