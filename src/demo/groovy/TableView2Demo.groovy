/*
 * Copyright 2011-2018 the original author or authors.
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
import groovy.transform.Canonical
import groovyx.javafx.beans.FXBindable

import javafx.collections.FXCollections

import java.text.SimpleDateFormat

import static groovyx.javafx.GroovyFX.start

enum Gender {
    MALE, FEMALE
}

@Canonical
class Person {
    @FXBindable String name
    @FXBindable int age
    @FXBindable Gender gender
    @FXBindable Date dob
}

people = FXCollections.observableList([])

def random = new Random()
def createPerson = {
    int i = random.nextInt(30)
    new Person(
        name: "Person ${people.size()}",
        age: 30 + i,
        gender: i % 2 ? Gender.FEMALE : Gender.MALE,
        dob: new Date() - (30 + i)
    )
}

def dateFormat = new SimpleDateFormat("MMM dd, yyyy")

start {
    stage(title: "GroovyFX Table Demo", width: 500, height: 200, visible: true) {
        scene(fill: GROOVYBLUE) {
            gridPane(hgap: 5, vgap: 10, padding: 25, alignment: "top_center") {
                button(row: 1, column: 0, text: 'Add Person', onAction: {
                    people << createPerson()
                })
                tableView(items: people, selectionMode: "single", cellSelectionEnabled: true, editable: true, row: 2, column: 0) {
                    tableColumn(editable: true, property: "name", text: "Name", prefWidth: 150,
                        onEditCommit: { event ->
                            Person item = event.tableView.items.get(event.tablePosition.row)
                            item.name = event.newValue
                        }
                    )
                    tableColumn(editable: true, property: "age", text: "Age", prefWidth: 50, type: Integer,
                        onEditCommit: { event ->
                            Person item = event.tableView.items.get(event.tablePosition.row)
                            item.age = Integer.valueOf(event.newValue)
                        }
                    )
                    tableColumn(editable: true, property: "gender", text: "Gender", prefWidth: 150, type: Gender,
                        onEditCommit: { event ->
                            Person item = event.tableView.items.get(event.tablePosition.row)
                            item.gender = event.newValue;
                        }
                    )
                    tableColumn(editable: true, property: "dob", text: "Birth", prefWidth: 150, type: Date,
                        converter: { from ->
                            // convert date object to String
                            return dateFormat.format(from)
                        },
                        onEditCommit: { event ->
                            Person item = event.tableView.items.get(event.tablePosition.row)
                            // convert TextField string to a date object.
                            Date date = dateFormat.parse(event.newValue)
                            item.dob = date
                        }
                    )
                }
            }
        }
    }
}

