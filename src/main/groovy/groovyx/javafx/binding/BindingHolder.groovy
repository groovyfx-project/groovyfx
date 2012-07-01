/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.binding

import javafx.beans.value.ObservableValue
import javafx.beans.property.Property;

/**
 *
 * @author jimclarke
 */
class BindingHolder {
    BindingHolder parent;
    String propertyName;
    ObservableValue observable;
    Closure converter; 
    BindingHolder bindTo;
    
    // This is either the observable or the wrapper to handle the converter
    ObservableValue binding;
    // the bound value from any bindTo.
    ObservableValue boundValue;
    
    public BindingHolder(Object value) {
        this(null, value);
    }
    
    public BindingHolder(BindingHolder parent, Object value) {
        this.parent = parent;
        if(value instanceof ObservableValue) {
            observable  = value;
        }else if(value instanceof List) {
            def instance = value[0];
            def propertyName = value[1];
            observable = Util.getJavaFXProperty(instance, propertyName)
            if(observable == null)
                observable = Util.getJavaBeanFXProperty(instance, propertyName);
            if(observable == null) {
                println "Warning, could not locate neither a JavaFX property nor a JavaBean property for ${instance.class}, property: '${propertyName}'"
            }
        }else if (value instanceof Closure){ // closure
            observable = new GroovyClosureProperty((Closure)value);
        }else if(value instanceof String || value instanceof GString) {
            propertyName = value.toString();
        }
        
    }
    
    public BindingHolder bind() {
        unbind();
        if(bindTo) {
           boundValue = bindTo.binding;
           if(boundValue instanceof Property)
                observable.bindBidirectional(boundValue);
           else
                observable.bind(boundValue);
       }
       this
    }
    
    public BindingHolder rebind() {
        if(boundValue) {
            unbind();
            bind();
        }
        this
    }
    
    public BindingHolder unbind() {
        if(boundValue) {
            if(boundValue instanceof Property) {
                binding.unbindBidirectional(boundValue);
            }else {
                binding.unbind();
            }
            boundValue = null;
        }
        this
    }
    public ObservableValue getBinding() {
       if(binding == null) {
           bind();
           binding = converter == null ? observable :
             new ConverterProperty(observable, converter);
       }
       binding 
    }
    
    public boolean isResolved() {
        return observable != null;
    }
    
    public ObservableValue resolve(Object instance) {
        observable = Util.getJavaFXProperty(instance, propertyName);
        if(observable == null) 
            observable = Util.getJavaBeanFXProperty(instance, propertyName);
        if(observable == null) {
            println "Warning, could not locate neither a JavaFX property nor a JavaBean property for ${instance.class}, property: '${propertyName}'"
        }
        return observable;
    }
    
    public BindingHolder using(Closure converter) {
        BindingHolder bh = lastHolder();
        bh.converter = converter
        bh.binding = null;
        bh.binding;
        if(bh.parent)
            bh.parent.rebind();
        this
    }
    
    public BindingHolder to(Object value) {
        BindingHolder bh = lastHolder();
        bh.bindTo = new BindingHolder(bh, value);
        bh.bind();
        this
    }
    public BindingHolder to(Object instance, String prop) {
        BindingHolder bh = lastHolder();
        bh.bindTo = new BindingHolder(bh, [instance,prop]);
        bh.bind();
        this
    }
    
    private BindingHolder lastHolder() {
        BindingHolder bh
        for(bh = this;bh.bindTo != null; bh = bh.bindTo); 
        bh
    }
}

