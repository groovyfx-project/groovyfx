/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.event

import javafx.util.Callback

/**
*
* @author jimclarke
*/
class GroovyCallback implements Callback {
    String property
    Closure closure
    
    public GroovyCallback() {
    }

    public GroovyCallback(String property) {
        this.property = property
    }

    @Override public Object call(Object config) {
        return closure.call(config)
    }

    public String toString() {
        "GroovyCallback: property = ${property}"
    }   
}

