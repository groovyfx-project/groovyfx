/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.event

import javafx.beans.InvalidationListener
import javafx.beans.Observable

/**
*
* @author jimclarke
*/
class GroovyInvalidationListener extends AbstractClosureProperty implements InvalidationListener {
    
    public GroovyInvalidationListener() {
        super();
    }
    
    public GroovyInvalidationListener(String property) {
        super(property);
    }
    public GroovyInvalidationListener(String property, Closure closure) {
        super(property, closure);
    }
    
    public void invalidated(Observable observable) {
        this.closure.call(observable);
    }
	
}

