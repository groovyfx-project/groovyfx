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

import groovyx.javafx.appsupport.Action
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 *
 * @author Andres Almiray
 */
class ActionFactory extends AbstractFXBeanFactory {
    ActionFactory(C) {
        super(Action, false)
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        attributes.remove('id')
        return super.onHandleNodeAttributes(builder, node, attributes)
    }

    static Map extractActionParams(Map attributes) {
        Map actionParams = [:]

        actionParams.skipName = attributes.remove('skipName')
        actionParams.skipIcon = attributes.remove('skipIcon')

        actionParams
    }

    static void applyAction(control, Action action, Map actionParams) {
        MetaClass mc = control.metaClass

        if (mc.respondsTo(control, "onActionProperty")) {
            control.onActionProperty().bind(action.onActionProperty())
        }
        if (!actionParams.skipName && mc.respondsTo(control, "textProperty")) {
            control.textProperty().bind(action.nameProperty())
        }
        if (mc.respondsTo(control, "graphicProperty")) {
            if (!actionParams.skipIcon) {
                action.iconProperty().addListener(new ChangeListener() {
                    void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                        ActionFactory.setIcon(control, newValue)
                    }
                })
                if (action.icon != null) ActionFactory.setIcon(control, action.icon)
            }
        }
        if (mc.respondsTo(control, "selectedProperty")) {
            control.selectedProperty().bind(action.selectedProperty())
        }
        if (mc.respondsTo(control, "disableProperty")) {
            control.disableProperty().bind(action.enabledProperty().not())
        }
    }

    static void setIcon(node, String iconUrl) {
        Image image = new Image(Thread.currentThread().getContextClassLoader().getResource(iconUrl).toString())
        node.graphicProperty().set(new ImageView(image: image))
    }
}
