/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author jimclarke
 */
class ClosureChangeListener implements ChangeListener {
    public String property;
    public Closure closure;
    
    public ClosureEventHandler() {}
    
    public ClosureEventHandler(String property) {
        this.property = property;
    }
    
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(closure != null) {
            closure(observable, oldValue, newValue);
        }
    }

}

