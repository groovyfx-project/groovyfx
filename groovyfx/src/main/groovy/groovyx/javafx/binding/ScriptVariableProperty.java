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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;

/**
 * Wraps a groovy script variable in a JavaFX Property
 * 
 * @author jimclarke
 */
public class ScriptVariableProperty implements MapChangeListener<String, Object>{
    private static ScriptVariableProperty instance = new ScriptVariableProperty();
    
    private static final String SCRIPT_VAR = "__script__";
    
    // store the properties for each script instance
    // so that we reuse the same property for each script varible.
    private static Map<Script, Map<String, Property>> propertyMap =
            new HashMap<Script, Map<String, Property>>();
    
    public static Property getProperty(Script script, String propertyName){
        Map<String, Property> instanceMap = propertyMap.get(script);
        if(instanceMap == null) {
            // we have not seen this script before.
            instanceMap = new HashMap<String, Property>();
            propertyMap.put(script, instanceMap);
            
            // intercept the binding on the script so we can
            // be notified of changes.
            Map originalVMap = script.getBinding().getVariables(); 
            originalVMap.put(SCRIPT_VAR, script);
            ObservableMap obsVariables = FXCollections.observableMap(originalVMap);
            obsVariables.addListener(instance);
            Binding newBinding = new Binding(obsVariables);
            script.setBinding(newBinding);
        }
        Property property = instanceMap.get(propertyName);
        if(property == null) {
            // create a new Property
            Class type = script.getProperty(propertyName).getClass();
            if(type == Boolean.class || type == Boolean.TYPE) {
                property = new ScriptVariableBooleanProperty(script, propertyName);
            } else if(type == BigDecimal.class || type == Double.class || type == Double.TYPE) {
                property = new ScriptVariableDoubleProperty(script, propertyName);
            } else if(type == Float.class || type == Float.TYPE) {
                property = new ScriptVariableFloatProperty(script, propertyName);
            } else if(type == Byte.class || type == Byte.TYPE ||
                    type == Short.class || type == Short.TYPE ||
                    type == Integer.class || type == Integer.TYPE  ) { 
                property = new ScriptVariableIntegerProperty(script, propertyName);
            } else if(type == BigInteger.class || type == Long.class || type == Long.TYPE  ) { 
                property = new ScriptVariableLongProperty(script, propertyName);
            } else if(type == String.class  ) { 
                property = new ScriptVariableStringProperty(script, propertyName);
            } else  { 
                property = new ScriptVariableObjectProperty(script, propertyName);
            }
            instanceMap.put(propertyName, property);
        }
        return property;
    }

    private ScriptVariableProperty() {
        
    }
    @Override
    public void onChanged(Change<? extends String, ? extends Object> change) {
        ObservableMap<? extends String, ? extends Object> map = change.getMap();
        // get the script instance
        Script script = (Script)map.get(SCRIPT_VAR);
        if(script != null) {
            //get the instanceMap so we can locate the JavaFX Property, if it exists.
            Map<String, Property> instanceMap = propertyMap.get(script);
            if(instanceMap != null) {
                String variable = change.getKey();
                Property property = instanceMap.get(variable);
                if(property != null) { 
                    // we have a property installed for ths variable,
                    // so set it's value from the script variable.
                    property.setValue(map.get(variable));
                }
            }
        }
        
    }
    
}
