/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import java.net.URI;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Group;

/**
 *
 * @author jimclarke
 */
class FXMLFactory extends NodeFactory {
    private static final String LOCATION_PROPERTY = "__fxml_location"
    private static final String XML_PROPERTY = "__fxml_xml"
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def fxml = new FXMLLoader();
        def result = null;
        if(value != null) {
            if(value instanceof InputStream) {
                result = loadInput(value);
            } else if(value instanceof URL) {
                result = FXMLLoader.load(value);
            } else if(value instanceof URI) {
                result = FXMLLoader.load(value.toURL());
            }else {
                result = loadXML(value);
            }
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
        
        return result;
        
        
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
    
     public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
         if(child instanceof String) {
             parent.xml = child
         }
     }
     
	
}

