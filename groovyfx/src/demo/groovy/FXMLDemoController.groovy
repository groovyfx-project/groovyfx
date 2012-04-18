/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label

/**
*
* @author jimclarke
*/
class FXMLDemoController implements Initializable {
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        println "You clicked me!" ;
        label.text ="Hello World!";
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}

