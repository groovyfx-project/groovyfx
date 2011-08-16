package groovyx.javafx.event

import javafx.event.ActionEvent

/**
 * @author dean
 */
class GroovyActionHandler extends GroovyEventHandler<ActionEvent> {
    GroovyActionHandler(String type) {
        super(type)
    }
}
