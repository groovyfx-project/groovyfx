package structure

/*
   This example is intentionally baking many different aspects of UI generation in one class
   in order to serve as a counter-example of good structuring.
   @author Dierk Koenig
*/

import static groovyx.javafx.GroovyFX.start
import javafx.scene.layout.GridPane
import groovyx.javafx.beans.FXBindable

class Email1 {
    @FXBindable String name, address, feedback
    String toString() { "<$name> $address : $feedback" }
}

start { app ->
    def email = new Email1()
    stage title: "All-in-one app demo", visible: true, {
        scene {
            gridPane hgap: 5, vgap: 10, padding: 25, alignment: "top_center",
                     style: "-fx-background-color: groovyblue", {
                columnConstraints minWidth: 50,   halignment: "right"
                columnConstraints prefWidth: 250, hgrow: 'always'

                effect innerShadow()

                label "Please Send Us Your Feedback", style: "-fx-font-size: 18px;",  row: 0, textFill: white,
                      columnSpan: GridPane.REMAINING, halignment: "center", margin: [0, 0, 10], {
                    onMouseEntered { e -> e.source.parent.gridLinesVisible = true  }
                    onMouseExited  { e -> e.source.parent.gridLinesVisible = false }
                }

                label "Name", hgrow: "never", row: 1, column: 0, textFill: white
                textField id: 'name', row: 1, column: 1

                label "Email", row: 2, column: 0, textFill: white
                textField id: 'address', row: 2, column: 1

                label "Message", row: 3, column: 0, valignment: "baseline", textFill: white
                textArea id: 'feedback', prefRowCount: 8, row: 3, column: 1, vgrow: 'always'

                button "Send Message", row: 4, column: 1, halignment: "right", {
                    onAction {
                        println "preparing and sending the mail: $email"
    }   }   }   }   }
    email.nameProperty().bind(name.textProperty())
    email.addressProperty().bind(address.textProperty())
    email.feedbackProperty().bind(feedback.textProperty())
}