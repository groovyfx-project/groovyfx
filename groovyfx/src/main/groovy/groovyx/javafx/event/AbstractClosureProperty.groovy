/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.event

/**
*
* @author jimclarke
*/
class AbstractClosureProperty {
    String property
    Closure closure
    
    public AbstractClosureProperty() {
    }

    public AbstractClosureProperty(String property) {
        this.property = property
    }
    public AbstractClosureProperty(String property, Closure closure) {
        this.property = property
        this.closure = closure
    }
    
    public String toString() {
        "${this.class.name}: property = ${property}"
    }
}

