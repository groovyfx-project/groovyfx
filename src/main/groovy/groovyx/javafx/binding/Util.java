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

import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import groovy.lang.Script;
import javafx.beans.property.*;
import javafx.beans.property.adapter.*;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 *
 * @author jimclarke
 */
public class Util {
    
    private static final Object[] EMPTY_OBJECT_ARRAY = {};
    
    static public ReadOnlyProperty getJavaFXProperty(Object instance, String propertyName) {
        MetaClass mc = InvokerHelper.getMetaClass(instance);
        String fxPropertyAccessor = propertyName + "Property";
        if (!mc.respondsTo(instance, fxPropertyAccessor, null).isEmpty()) {
            return (ReadOnlyProperty)InvokerHelper.invokeMethod(instance, fxPropertyAccessor, null);
        }else {
            return null;
        }
    }
    
    static public Property wrapValueInProperty(Object instance) {
        if(instance instanceof Property)
            return (Property)instance;
        Class type = instance.getClass();
        if(type == Boolean.class || type == Boolean.TYPE) {
            return new SimpleBooleanProperty((Boolean)instance);
        } else if(type == Double.class || type == Double.TYPE) {
            return new SimpleDoubleProperty((Double)instance);
        } else if(type == Float.class || type == Float.TYPE) {
            return new SimpleFloatProperty((Float)instance);
        } else if(type == Byte.class || type == Byte.TYPE ||
                type == Short.class || type == Short.TYPE ||
                type == Integer.class || type == Integer.TYPE  ) { 
            return new SimpleIntegerProperty(((Number)instance).intValue());
        } else if(type == Long.class || type == Long.TYPE  ) { 
            return new SimpleLongProperty((Long)instance);
        } else if(type == String.class  ) { 
            return new SimpleStringProperty((String)instance);
        } else  { 
            return new SimpleObjectProperty(instance);
        }
    }
    
    static public boolean isJavaBeanPropertyWritable(Object instance, String propertyName) {
        
        MetaClass mc = InvokerHelper.getMetaClass(instance);
        MetaProperty metaProperty = mc.getMetaProperty(propertyName);
        if(metaProperty != null) {
            String setterName = MetaProperty.getSetterName(propertyName);
            return !mc.respondsTo(instance, setterName, new Class[]{metaProperty.getType()}).isEmpty();
        } else if(instance instanceof Script) {
            return ((Script)instance).getProperty(propertyName) != null;
        }
        return false;
    }
    
    static public ReadOnlyProperty getJavaBeanFXProperty(Object instance, String propertyName) throws NoSuchMethodException {
        if(isJavaBeanPropertyWritable(instance, propertyName)) {
            return getJavaBeanFXWritableProperty(instance, propertyName);
        }else {
            return getJavaBeanFXReadOnlyProperty(instance, propertyName);
        }
    }
    
    static public ReadOnlyProperty getJavaBeanFXReadOnlyProperty(Object instance, String propertyName) throws NoSuchMethodException {
        MetaClass mc = InvokerHelper.getMetaClass(instance);
        MetaProperty metaProperty = mc.getMetaProperty(propertyName);
        if(metaProperty != null) {
            Class type = metaProperty.getType();
            if(type == Boolean.class || type == Boolean.TYPE) {
                ReadOnlyJavaBeanBooleanPropertyBuilder builder = ReadOnlyJavaBeanBooleanPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Double.class || type == Double.TYPE) {
                ReadOnlyJavaBeanDoublePropertyBuilder builder = ReadOnlyJavaBeanDoublePropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Float.class || type == Float.TYPE) {
                ReadOnlyJavaBeanFloatPropertyBuilder builder = ReadOnlyJavaBeanFloatPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Byte.class || type == Byte.TYPE ||
                    type == Short.class || type == Short.TYPE ||
                    type == Integer.class || type == Integer.TYPE  ) { 
                ReadOnlyJavaBeanIntegerPropertyBuilder builder = ReadOnlyJavaBeanIntegerPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Long.class || type == Long.TYPE  ) { 
                ReadOnlyJavaBeanLongPropertyBuilder builder = ReadOnlyJavaBeanLongPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build(); 
            } else if(type == String.class  ) { 
                ReadOnlyJavaBeanStringPropertyBuilder builder = ReadOnlyJavaBeanStringPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else  { 
                ReadOnlyJavaBeanObjectPropertyBuilder builder = ReadOnlyJavaBeanObjectPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            }
        }else {
            return null;
        }

    }
    static public Property getJavaBeanFXWritableProperty(Object instance, String propertyName) throws NoSuchMethodException {
        MetaClass mc = InvokerHelper.getMetaClass(instance);
        //Object a = mc.getAttribute(instance, propertyName);
        MetaProperty metaProperty = mc.getMetaProperty(propertyName);
        if(metaProperty != null) {
            Class type = metaProperty.getType();
            if(type == Boolean.class || type == Boolean.TYPE) {
                JavaBeanBooleanPropertyBuilder builder = JavaBeanBooleanPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Double.class || type == Double.TYPE) {
                JavaBeanDoublePropertyBuilder builder = JavaBeanDoublePropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Float.class || type == Float.TYPE) {
                JavaBeanFloatPropertyBuilder builder = JavaBeanFloatPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Byte.class || type == Byte.TYPE ||
                    type == Short.class || type == Short.TYPE ||
                    type == Integer.class || type == Integer.TYPE  ) { 
                JavaBeanIntegerPropertyBuilder builder = JavaBeanIntegerPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else if(type == Long.class || type == Long.TYPE  ) { 
                JavaBeanLongPropertyBuilder builder = JavaBeanLongPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build(); 
            } else if(type == String.class  ) { 
                JavaBeanStringPropertyBuilder builder = JavaBeanStringPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            } else  { 
                JavaBeanObjectPropertyBuilder builder = JavaBeanObjectPropertyBuilder.create();
                builder.bean(instance);
                builder.name(propertyName);
                builder.beanClass(instance.getClass());
                return builder.build();
            }
        } else if (instance instanceof Script) {
            Script script = (Script)instance;
            if(script.getBinding().hasVariable(propertyName)) {
                return ScriptVariableProperty.getProperty(script, propertyName);
            } else
                return null;
        }else {
            return null;
        }

    }
}
