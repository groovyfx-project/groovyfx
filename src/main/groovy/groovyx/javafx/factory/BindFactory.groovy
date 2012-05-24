/*
* Copyright 2011 the original author or authors.
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


package groovyx.javafx.factory

import javafx.beans.property.*;
import javafx.beans.property.adapter.*;
import javafx.beans.value.*;
import groovyx.javafx.binding.*

import org.codehaus.groovy.runtime.InvokerHelper;

/**
*
* @author jimclarke
*/
class BindFactory extends AbstractFXBeanFactory {
    
    public BindFactory() {
        super(ObservableValue)
    }
    
    public BindFactory(Class<ObservableValue> beanClass) {
        super(beanClass);
    }
    
    public boolean isHandlesNodeChildren() {
        return true;
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
                
          ObservableValue property = null;
          if(value instanceof ObservableValue) {
                property = value;
          }else if(value instanceof List) {
                def instance = value[0];
                def propertyName = value[1];
                property = Util.getJavaFXProperty(instance, propertyName)
                if(property == null)
                    property = Util.getJavaBeanFXProperty(instance, propertyName);
          }else {
                property = new GroovyClosureProperty();
          }
          
          def converter = attributes.remove("converter");
          if(converter) {
                property =  new ConverterProperty(property, converter);
          }
          property;
    }
    
    private ReadOnlyProperty getJavaFXProperty(instance, propertyName) {
        def fxProperty = null;
        try {
            fxProperty = (ReadOnlyProperty)InvokerHelper.invokeMethodSafe(instance,
                                            propertyName + "Property", null);
        } catch (MissingMethodException ignore) {
            
        }
        fxProperty                              
    }
    
    private ReadOnlyProperty buildJavaFXJavaBeanProperty(instance, propertyName) {
        def metaProperty = instance.getClass().metaClass.getMetaProperty(propertyName);
        def type = metaProperty.type;
        def builder = null;
        switch(type) {
            case Boolean:
            case Boolean.TYPE:
                builder = ReadOnlyJavaBeanBooleanPropertyBuilder.create();
                break;
            case Double:
            case Double.TYPE:
                builder = ReadOnlyJavaBeanDoublePropertyBuilder.create();
                break;
            case Float:
            case Float.TYPE:
                builder = ReadOnlyJavaBeanFloatPropertyBuilder.create();
                break;
            case Byte:
            case Byte.TYPE:
            case Short:
            case Short.TYPE:
            case Integer:
            case Integer.TYPE:
                builder = ReadOnlyJavaBeanIntegerPropertyBuilder.create();
                break;
            case Long:
            case Long.TYPE:
                builder = ReadOnlyJavaBeanLongPropertyBuilder.create();
                break;
            case String:
            case String.TYPE:
                builder = ReadOnlyJavaBeanStringPropertyBuilder.create();
                break;
            default:
                builder = ReadOnlyJavaBeanObjectPropertyBuilder.create();
                break;
        }
        builder.bean(instance);
        builder.name(propertyName);
        builder.beanClass(instance.class)
        return builder.build();
    }

    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        switch(node) {
            case GroovyClosureProperty:
                node.closure = childContent
                break;
        }
    }
	
}