/*
 * Copyright 2011-2018 the original author or authors.
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.canvas

import groovyx.javafx.beans.FXBindable
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.collections.FXCollections

/**
 *
 * @author jimclarke
 */
@FXBindable
class DrawOperations implements CanvasOperation {
    private List<CanvasOperation> operations = [];
    Canvas canvas
    
    public void setOperations(List ops)
    {
        this.operations = FXCollections.observableArrayList(ops)
    }

    public void add(CanvasOperation opertion) {
        operations << operation;
    }
    
    public void clear() {
        operations.clear()
    }
    
    public void remove(CanvasOperation operation) {
        operations.remove(operation)
    }
    
    public void draw() {
        execute(canvas.graphicsContext2D)
    }
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        draw();
    }
    
    public void initParams(Object obj){
        if(obj instanceof Canvas) {
            canvas = obj;
        }else {
            if(obj.size() == 2 && obj[1] instanceof Canvas) {
                operations = obj[0];
                canvas = obj[1];
            }else {
                operations = obj
            }
        }
    }
    public void execute(GraphicsContext gc) {
        
        operations?.each {
            it.execute(gc);
        }
    }
}

