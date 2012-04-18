/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package groovyx.javafx.factory
import org.codehaus.groovyfx.javafx.binding.ClosureTriggerBinding

/**
*
* @author jimclarke
*/
class AbstractFXBeanFactory extends AbstractFactory {
    final Class beanClass
    final protected boolean leaf = false
    
    public AbstractFXBeanFactory(Class beanClass) {
        this(beanClass, false)
    }

    public AbstractFXBeanFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }
    

    @Override
    public boolean isLeaf() {
        return leaf
    }
    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (checkValue(name, value)) {
            return value
        }
        return beanClass.newInstance();
    }
    
    public boolean checkValue(Object name, Object value) {
        return value != null && beanClass.isAssignableFrom(value.class)
    }
    
/**
 *
 * If a factory overrides the onHandleNodeAttributes method then it should return super.onHandleNodeAttributes(...)
 * at the end of the method. This ensures that the remaining attributes are processed by the default handling code
 * in AbstractGroovyFX Factory.
 *
 * The only exception is when the overriding method processes all possible attributes itself and returns false
 * to indicate that no other processing is required.
 * 
 * @author Dean Iverson (relocated code from SceneGraphBuilder)
 */
    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        // set the properties
        // noinspection unchecked
        def removeList = [];
        attributes.each {
            String property = it.key.toString();
            Object value = it.value;
            if(value instanceof ClosureTriggerBinding) {
                def ctb = (ClosureTriggerBinding)value;
                value = ctb.getSourceValue();
            }
            if(FXHelper.fxAttribute(node, property, value))
                removeList << property
        }
        for(property in removeList) {
            attributes.remove(property)
        }
        super.onHandleNodeAttributes(builder, node, attributes);
        return true;
    }
}

