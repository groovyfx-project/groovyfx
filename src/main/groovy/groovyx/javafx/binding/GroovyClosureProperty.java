/*
* Copyright 2011-2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package groovyx.javafx.binding;

import groovy.lang.*;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.property.adapter.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.codehaus.groovy.runtime.InvokerHelper;


/**
 *
 * @author jimclarke
 */
public class GroovyClosureProperty implements ReadOnlyProperty,  ChangeListener, InvalidationListener{
    private  final GroovyClosureProperty.DeadEndObject DEAD_END = new GroovyClosureProperty.DeadEndObject();
    
    private Closure closure;
    private Object bean;
    private String name;
    
    private Object oldValue = null;
    private Object newValue = null;
    private boolean valueDirty = true;

    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private List<InvalidationListener> invalidationListeners = new ArrayList<InvalidationListener>();
    
    public GroovyClosureProperty() {}
    
    public GroovyClosureProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name;
    }
    
    public GroovyClosureProperty(Closure closure) {
        setClosure(closure);
    }
    
    public GroovyClosureProperty(Object bean, String name, Closure closure) {
        this.bean = bean;
        this.name = name;
        setClosure(closure);
    }

    public final void setClosure(Closure closure) {
        this.closure = closure;
        createBindings(closure);
    }

    @Override
    public final Object getValue() {
        Object result = null;
        if(closure != null) {
            result = closure.call();
            if(result instanceof ReadOnlyProperty) {
                result = ((ReadOnlyProperty)result).getValue();
            }
        }
        return result;
    }
    
    @Override
    public final Object getBean() {
        return bean;
    }

    @Override
    public final String getName() {
        return name;
    }
    
        /**
     * @return the closure
     */
    public final Closure getClosure() {
        return closure;
    }

    /**
     * @param bean the bean to set
     */
    public final void setBean(Object bean) {
        this.bean = bean;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    @Override
    public void addListener(ChangeListener listener) {
        changeListeners.add(listener);
    }


    @Override
    public void removeListener(ChangeListener  listener) {
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
        
    private void createBindings(Closure closure) {
        
        final Snooper delegate = new Snooper();
        
        try {
            final Class closureClass = closure.getClass();
            // do in privileged block since we may be looking at private stuff
            Closure closureLocalCopy = java.security.AccessController.doPrivileged(new PrivilegedAction<Closure>() {
                @Override
                public Closure run() {
                    Constructor constructor = closureClass.getConstructors()[0];
                    int paramCount = constructor.getParameterTypes().length;
                    Object[] args = new Object[paramCount];
                    args[0] = delegate;
                    for (int i = 1; i < paramCount; i++) {
                        args[i] = new Reference(new Snooper());
                    }
                    try {
                        boolean acc = constructor.isAccessible();
                        constructor.setAccessible(true);
                        Closure localCopy = (Closure) constructor.newInstance(args);
                        if (!acc) { constructor.setAccessible(false); }
                        localCopy.setResolveStrategy(Closure.DELEGATE_ONLY);
                        for (Field f:closureClass.getDeclaredFields()) {
                            acc = f.isAccessible();
                            f.setAccessible(true);
                            if (f.getType() == Reference.class) {
                                delegate.getFields().put(f.getName(), (Snooper)((Reference) f.get(localCopy)).get());
                            }
                            if (!acc) { f.setAccessible(false); }
                        }
                        return localCopy;
                    } catch (Exception e) {
                        throw new RuntimeException("Error snooping closure", e);
                    }
                }
            });
            try {
                closureLocalCopy.call();
            } catch (DeadEndException e) {
                // we want this exception exposed.
                throw e;
            } catch (Exception e) {
                //LOGME
                // ignore it, likely failing because we are faking out properties
                // such as a call to Math.min(int, BindPathSnooper)
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("A closure expression binding could not be created because of " + e.getClass().getName() + ":\n\t" + e.getMessage());
        }
        List<BindPath> rootPaths = new ArrayList<BindPath>();
        for (Map.Entry<String, Snooper> entry : delegate.getFields().entrySet()) {
            BindPath bp = new BindPath(entry.getKey(), entry.getValue());
            bp.setCurrentObject(closure);
            bp.bind();
            rootPaths.add(bp);
        }
        update();
        
    }
    
    private void update() {
        this.oldValue = this.newValue;
        this.newValue = closure.call();
        valueDirty = false;
    }
    
    private void fireChanged() {
        valueDirty = true;
        update();
        ChangeListener[] cListeners = changeListeners.toArray(new ChangeListener[changeListeners.size()]);
        for(int i = 0; i < cListeners.length; i++) {
            cListeners[i].changed(this, oldValue, newValue);
        }
    }
    private void fireInvalidated() {
        valueDirty = true;
        InvalidationListener[] listeners = invalidationListeners.toArray(new InvalidationListener[invalidationListeners.size()]);
        for(int i = 0; i < listeners.length; i++) {
            listeners[i].invalidated(this);
        }
    }
    
    @Override
    public void changed(ObservableValue obsv, Object oldValue, Object newValue) {
        fireChanged();
    }

    @Override
    public void invalidated(Observable obs) {
        fireInvalidated();
    }



    
    
    
    
    class Snooper extends GroovyObjectSupport {

        private Map<String,Snooper> fields = new HashMap<String,Snooper>();

        @Override
        public Object getProperty(String property) {
            if (getFields().containsKey(property)) {
                return getFields().get(property);
            } else {
                Snooper snooper = new Snooper();
                getFields().put(property, snooper);
                return snooper;
            }
        }

        @Override
        public Object invokeMethod(String name, Object args) {
            return DEAD_END;
        }

        /**
         * @return the fields
         */
        public Map<String,Snooper> getFields() {
            return fields;
        }
    }
    class DeadEndException extends RuntimeException {
        DeadEndException(String message) { super(message); }
    }
    class DeadEndObject {
        public Object getProperty(String property) {
            throw new DeadEndException("Cannot bind to a property on the return value of a method call");
        }
        public Object invokeMethod(String name, Object args) {
            return this;
        }
    }
    private static final Class[] NAME_PARAMS = {String.class, PropertyChangeListener.class};
    private static final Class[] GLOBAL_PARAMS = {PropertyChangeListener.class};
    private static final Class[] FX_PARAMS = {ChangeListener.class};
    
    class BindPath {
        private String propertyName;
        private Object originalObject;
        private Object currentObject;
        private List<BindPath> children = new ArrayList<BindPath>();
        
        private ReadOnlyProperty property;
        
        public BindPath() {
            
        }
        public BindPath(String propertyName) {
            this.propertyName = propertyName;
        }
        
        public BindPath(String propertyName, Snooper snooper) {
            this.propertyName = propertyName;
            createChildren(snooper);
        }

        public final void createChildren(Snooper snooper) {
            for (Map.Entry<String, Snooper> entry : snooper.getFields().entrySet()) {
                BindPath bp = new BindPath(entry.getKey());
                bp.createChildren(entry.getValue());
                children.add(bp);
            }
        }
        
        public final void unbind() {
            property.removeListener((ChangeListener)GroovyClosureProperty.this);
            property.removeListener((InvalidationListener)GroovyClosureProperty.this);
        }

        public final void bind() {
            MetaClass mc = InvokerHelper.getMetaClass(currentObject);
            property = Util.getJavaFXProperty(currentObject, propertyName);
            if(property == null) {
                try {
                if( ! (currentObject instanceof Closure))  // avoid java.lang.reflect.UndeclaredThrowableException
                    property = Util.getJavaBeanFXProperty(currentObject, propertyName);
                if(property == null) {
                    Object attribute = null;
                    if(originalObject != null && originalObject instanceof Closure) {
                        attribute = mc.getAttribute(originalObject, propertyName);
                    }else {
                        attribute = mc.getAttribute(currentObject, propertyName);
                        if(attribute instanceof Reference)
                            attribute = ((Reference)attribute).get();
                    }
                    property = Util.wrapValueInProperty(attribute);
                }
                //property.addListener((ChangeListener)GroovyClosureProperty.this);
                //property.addListener((InvalidationListener)GroovyClosureProperty.this);
                //updateSet.add(property);
                }catch(NoSuchMethodException shouldNotHappen) {
                    shouldNotHappen.printStackTrace();
                }
            }
            property.addListener((ChangeListener)GroovyClosureProperty.this);
            property.addListener((InvalidationListener)GroovyClosureProperty.this);
            Object  propertyInstance = property.getValue();
            for(BindPath bp : children) {
                bp.setCurrentObject(propertyInstance);
                bp.bind();
            }
        }
        

        /**
         * @return the propertyName
         */
        public final String getPropertyName() {
            return propertyName;
        }

        /**
         * @param propertyName the propertyName to set
         */
        public final void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * @return the currentObject
         */
        public final Object getCurrentObject() {
            return currentObject;
        }

        /**
         * @param currentObject the currentObject to set
         */
        public final void setCurrentObject(Object currentObject) {
            //if(currentObject instanceof Closure) {
            //    originalObject = currentObject;
            //    this.currentObject = ((Closure)currentObject).getThisObject();
            //}else {
                this.currentObject = currentObject;
            //}
        }

        /**
         * @return the children
         */
        public List<BindPath> getChildren() {
            return children;
        }
    }

}
