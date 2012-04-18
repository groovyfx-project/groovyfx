/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.event

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

/**
*
* @author jimclarke
*/
class GroovyChangeListener extends AbstractClosureProperty implements ChangeListener {
    
    public GroovyChangeListener() {
        super();
    }
    
    public GroovyChangeListener(String property) {
        super(property);
    }
    public GroovyChangeListener(String property, Closure closure) {
        super(property, closure);
    }
    
    void changed(ObservableValue observable, Object oldValue, Object newValue) {

        this.closure.call(observable, oldValue, newValue);
    }
	
}	

