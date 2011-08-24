/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyx.javafx.factory

import javafx.fxml.FXMLLoader;

/**
 *
 * @author jimclarke
 */
class FXMLLoaderBuilder {
    public String xml;
    
    public Object build() {
        def loader = new FXMLLoader();
        def ins = new ByteArrayInputStream(xml.getBytes());
        try {
            return loader.load(ins);
        }finally {
            ins.close();
        }
    }
	
}

