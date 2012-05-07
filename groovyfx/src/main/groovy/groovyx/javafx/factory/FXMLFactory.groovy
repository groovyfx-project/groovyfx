/*
* Copyright 2012 the original author or authors.
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

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.fxml.FXMLLoader;
import java.net.URI;
import java.net.URL;
import javafx.scene.Parent;

/**
 *
 * @author jimclarke
 */
class FXMLFactory extends AbstractNodeFactory {
    private static final String LOCATION_PROPERTY = "__fxml_location"
    private static final String XML_PROPERTY = "__fxml_xml"
    
    FXMLFactory() {
        super(Node);
    }
    
    FXMLFactory(Class<Node> beanClass) {
        super(beanClass);
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def fxml = new FXMLLoader();
        Node result = null;
        if(value != null) {
            result = processValue(value);
            if(result == null)
                throw new Exception("In $name value must be an instanceof InputStream or one of its subclasses, java.net.URL, java.net.URI or a String  to be used as embedded content.")
        } else if(attributes.contains("location") || attributes.contains("url")){
            def location = attributes.remove("location");
            if(location == null) {
                location = attributes.remove("url");
            }
            if(location instanceof String) 
                location = new URL(location);
            result = FXMLLoader.load(location);
        } else if(attributes.contains("uri")){
            def uri = attributes.remove("uri");
            if(uri instanceof String)
                uri = new URI(uri);
            result = FXMLLoader.load(uri.toURL());
        } else if(attributes.contains("xml")) {
            def xml = attributes.remove("xml");
            result = loadXML(xml)
        } else if(attributes.contains("input")) {
            def input = attributes.remove("input");
            result = loadInput(input);
        } else { // default case
            return new Group();
        }
        
        result
        
        
    }
    
    private Node processValue(Object value) {
        Node result = null;
        switch(value) {
            case Node:
                result = value;
                break;
            case CharSequence:
                try {
                    URL url = new URL(value.toString());
                    result = FXMLLoader.load(url);
                }catch(MalformedURLException mfe) {
                    result = loadXML(value.toString());
                }
                break
            case InputStream:
                result = loadInput(value);
                break
            case URL:
                result = FXMLLoader.load(value);
                break
            case URI:
                result = FXMLLoader.load(value.toURL());
                break;
        }
        result;
    }
    
    
    private Object loadXML(String xml) {
        def loader = new FXMLLoader();
        def ins = new ByteArrayInputStream(xml.getBytes());
        try {
            return loader.load(ins);
        }finally {
            ins.close();
        }
    }
    
    private Object loadInput(input) {
        def loader = new FXMLLoader();
        return loader.load(input);
    }
    
    @Override
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        Node childNode = processValue(child);
        if(childNode != null) {
            parent.children.add(childNode);
        }else {
            super.setChild(builder, parent, child);
        }
    }
}

