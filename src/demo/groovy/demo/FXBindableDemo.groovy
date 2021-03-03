/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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
import groovyx.javafx.beans.FXBindable
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet

import static groovyx.javafx.GroovyFX.start

/**
 *
 * @author jimclarke
 */
@FXBindable
class FXPerson {
    String firstName;
    String lastName;
    int age;
    ObservableList likes = []; 
    ObservableMap attributes = [:];
    ObservableSet aSet = [] as Set;
    
}
     
start {
FXPerson person = new FXPerson();

println("======================"); 
println(person.lastNameProperty);
println(person.firstNameProperty);
println(person.ageProperty);
println(person.likesProperty);
println("======================");

person.likes = ["GroovyFX"] as ObservableList
println("LastName: " + person.lastName);
println("FirstName: " + person.firstName);
println("Age: " + person.age);
println("Likes: " + person.likes);
for (l in person.likes)
    println("    " + l);
println("======================");
person.lastName = 'Clarke';
person.firstName = 'Jim';
person.age = 17;  // I wish :-)
println(person.getLikes()[0]);
person.likes << 'JavaFX';
person.likes << 'Java';
println("======================");
println("LastName: " + person.lastName);
println("FirstName: " + person.firstName);
println("Age: " + person.age);
println("Likes: " + person.likes);
for (l in person.likes) 
    println("    " + l);
println(person.likes[0]);
println("======================");
println(person.lastNameProperty);
println(person.firstNameProperty);
println(person.ageProperty);
println(person.likesProperty);
println("======================");

person.attributes = ['one':'two'] as ObservableMap
person.attributes.put('foo', 'bar');
println person.attributes
println person.attributes['foo'];

person.aSet = [0] as ObservableSet
person.aSet << 1
person.aSet << 2
println person.aSet
println person.aSet.size()
println person.aSet.contains(1)
Platform.exit();
}