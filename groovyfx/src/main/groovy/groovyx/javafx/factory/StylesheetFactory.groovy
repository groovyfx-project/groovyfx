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

import java.util.*;

/**
 *
 * @author jimclarke
 */
class StylesheetFactory extends AbstractFXBeanFactory {
    
    StylesheetFactory() {
        super(List, true)
    }
    StylesheetFactory(Class<List> beanClass) {
        super(beanClass, true)
    }
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
        throws InstantiationException, IllegalAccessException {
        if (checkValue(name, value)) {
            return value
        }
        
        def result = [];
        switch(value) {
            case GString:
                result << value as String
                break;
            case String:
                result << value
                break;
            case URL:
                result << value.toExternalForm()
                break;
            case URI:
                result << value.toURL().toExternalForm();
                break;
            default:
                throw new RuntimeException("In $name value argument must be a List of Strings, a String, a URL, or a URI class");
        }
        result
    }
}

