/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.canvas

import groovyx.javafx.beans.FXBindable
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

/**
 *
 * @author jimclarke
 */
@FXBindable
class DrawOperations implements CanvasOperation {
    List<CanvasOperation> operations = [];
    Canvas canvas
    
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

