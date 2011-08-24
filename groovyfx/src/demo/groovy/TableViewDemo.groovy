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



import groovy.transform.Canonical
import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder
import groovyx.javafx.beans.FXBindable

@Canonical
class Person {
    @FXBindable String firstName
    @FXBindable String lastName
    @FXBindable String city
    @FXBindable String state
}

def data = [
    new Person('Jim', 'Clarke', 'Orlando', 'Fl'),
    new Person('Jim', 'Connors', 'Long Island', 'NY'),
    new Person('Eric', 'Bruno', 'Long Island', 'NY'),
    new Person('Jim', 'Weaver', 'Marion', 'IN'),
    new Person('Weiqi', 'Gao', 'Ballwin', 'MO'),
    new Person('Stephen', 'Chin', 'Belmont', 'CA'),
    new Person('Dean', 'Iverson', 'Fort Collins', 'CO'),
]

//todo cell factory

GroovyFX.start {
    def sg = new SceneGraphBuilder()

    sg.stage(title: "GroovyFX TableView Demo", visible: true) {
        scene(fill: groovyblue, width: 650, height: 450) {
            stackPane(padding: 20) {
                tableView(items: data) {
                    tableColumn(text: "First Name", property: 'firstName')
                    tableColumn(text: "Last Name", property: 'lastName')
                    tableColumn(text: "City", property: 'city')
                    tableColumn(text: "State", property: 'state')
                }
            }
        }
    }
}
