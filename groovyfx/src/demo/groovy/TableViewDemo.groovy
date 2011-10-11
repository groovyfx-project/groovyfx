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
import java.text.SimpleDateFormat

enum Gender { MALE, FEMALE }

@Canonical
class Person {
  @FXBindable String name
  @FXBindable int age
  @FXBindable Gender gender
  @FXBindable Date dob
}

def persons = [
  new Person(name: "Jim Clarke", age: 29, gender: Gender.MALE, dob: new Date()),
  new Person(name: "Dean Iverson", age: 30, gender: Gender.MALE, dob: new Date()),
  new Person(name: "Angelina Jolie", age: 36, gender: Gender.FEMALE, dob: new Date())
]

def dateFormat = new SimpleDateFormat("yyyy-MM-dd")

GroovyFX.start { primaryStage ->
  def sg = new SceneGraphBuilder(primaryStage)

  sg.stage(title: "GroovyFX Table Demo", width: 500, height: 200, visible: true) {
    scene(fill: groovyblue) {
      tableView(selectionMode: "single", cellSelectionEnabled: true, editable: true, items: persons) {
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
                      item.gender = event.newValue
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

