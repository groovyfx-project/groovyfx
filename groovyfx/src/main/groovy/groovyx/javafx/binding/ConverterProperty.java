/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package groovyx.javafx.binding;

import groovy.lang.Closure;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author jimclarke
 */

public class ConverterProperty implements  ReadOnlyProperty, ChangeListener,  InvalidationListener{
    private ReadOnlyProperty baseProperty;
    private Closure converter;
    
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private List<InvalidationListener> invalidationListeners = new ArrayList<InvalidationListener>();
    
    private Object oldValue = null;
    private Object newValue = null;
    
    public ConverterProperty(ReadOnlyProperty baseProperty, Closure converter) {
        this.baseProperty = baseProperty;
        this.converter = converter;
        baseProperty.addListener((ChangeListener)this);
        baseProperty.addListener((InvalidationListener)this);
        newValue = getValue();
    }
    
    
    @Override
    public final Object getValue() {
        oldValue = newValue;
        newValue = converter.call(baseProperty.getValue());
        return newValue;
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return "ConverterProperty";
    }

    @Override
    public void addListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

    @Override
    public void changed(ObservableValue obs, Object oldVal, Object newVal) {
        getValue();
        ChangeListener[] l = changeListeners.toArray(new ChangeListener[invalidationListeners.size()]);
        for(int i = 0; i < l.length; i++) {
            l[i].changed(this, oldValue, newValue);
        }
    }

    @Override
    public void invalidated(Observable obs) {
        InvalidationListener[] l = invalidationListeners.toArray(new InvalidationListener[invalidationListeners.size()]);
        for(int i = 0; i < l.length; i++) {
            l[i].invalidated(this);
        }
    }
}
