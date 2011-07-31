/*
* Copyright 2011 the original author or authors.
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

package demo

import groovyx.javafx.beans.FXBindable;

/**
*
* @author jimclarke
*/
@FXBindable
class FXPerson {
    String firstName;
    String lastName;
    int age;
    String[] likes;	
}


FXPerson person = new FXPerson();

println("======================");
println(person.lastNameProperty());
println(person.firstNameProperty());
println(person.ageProperty());
println(person.likesProperty());
println("======================");

println("LastName: " + person.lastName);
println("FirstName: " + person.lastName);
println("Age: " + person.lastName);
println("Likes: " + person.likes);
for ( l in person.likes) 
    println("    " +  l );
println("======================");
person.lastName = 'Clarke';
person.firstName = 'Jim';
person.age = 17;  // I wish :-)
person.likes = [ 'JavaFX', 'Groovy'];
println("======================");
println("LastName: " + person.lastName);
println("FirstName: " + person.lastName);
println("Age: " + person.lastName);
println("Likes: " + person.likes);
for ( l in person.likes) 
    println("    " +  l );
println("======================");
println(person.lastNameProperty());
println(person.firstNameProperty());
println(person.ageProperty());
println(person.likesProperty());
println("======================");
