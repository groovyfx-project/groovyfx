/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory

/**
*
* @author jimclarke
*/
class NodeFactory extends AbstractNodeFactory {
    public NodeFactory(Class beanClass) {
        super(beanClass)
    }
    public NodeFactory(Class beanClass, boolean leaf) {
        super(beanClass, leaf)
    }
}

