/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License")
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

package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import javafx.beans.property.StringProperty
import groovyx.javafx.beans.FXBindable
import groovy.transform.Canonical

//@Canonical
class Person {
    @FXBindable firstName
    @FXBindable lastName
    @FXBindable city
    @FXBindable state

    public Person(String first, String last, String city, String state) {
        setFirstName(first)
        setLastName(last)
        setCity(city)
        setState(state)
    }
    
//     private StringProperty firstName
//     public void setFirstName(String value) { firstNameProperty().set(value) }
//     public String getFirstName() { return firstName == null? "" : firstName.get() }
//     public StringProperty firstNameProperty() {
//         if (firstName == null) firstName = new StringProperty()
//         return firstName
//     }
//
//     private StringProperty lastName
//     public void setLastName(String value) { lastNameProperty().set(value) }
//     public String getLastName() { return lastName == null? "" : lastName.get() }
//     public StringProperty lastNameProperty() {
//         if (lastName == null) lastName = new StringProperty()
//         return lastName
//     }
//
//     private StringProperty city
//     public void setCity(String value) { cityProperty().set(value) }
//     public String getCity() { return city == null? "" : city.get() }
//     public StringProperty cityProperty() {
//         if (city == null) city = new StringProperty()
//         return city
//     }
//
//     private StringProperty state
//     public void setState(String value) { stateProperty().set(value) }
//     public String getState() { return state == null? "" : state.get() }
//     public StringProperty stateProperty() {
//         if (state == null) state = new StringProperty()
//         return state
//     }
}

def data = [
    new Person('Jim', 'Clarke', 'Orlando', 'Fl'),
    new Person('Jim', 'Connors', 'Long Island', 'NY'),
    new Person('Eric', 'Bruno', 'Long Island', 'NY'),
]

//todo cell factory

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX TableView Demo", width: 650, height:450, visible: true) {
         scene(fill: groovyblue) {
             tableView(items: data) {
                 tableColumn(text: "First Name", property: 'firstName')
                 tableColumn(text: "Last Name", property: 'lastName') 
                 tableColumn(text: "City", property: 'city')
                 tableColumn(text: "State", property: 'state')
             }
         }
    }
}
