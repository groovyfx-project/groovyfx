/*
 * Copyright 2011-2015 the original author or authors.
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
package idesupport

/**
Extract the names, types, and more info of all known nodes from the GDSL file.
@author Dierk Koenig
*/


File gdsl = new File("src/main/groovy/idesupport/groovyfx.gdsl")
if (! gdsl.exists()) {
    println "cannot find file for reading: $gdsl.absolutePath"
    return
}

def methods    = []
def properties = []

method   = { Map args -> methods    << args }
property = { Map args -> properties << args }
context  = { Map args -> this }
contributor   = { arg, closure -> arg.with closure }
enclosingCall = { true }
closureScope  = { null }

evaluate gdsl

methods.unique{it.name}.each {
    def type = it.type ?: ''
    def params = it.params ?: Collections.emptyMap()
    println "${it.name.padRight(22)} ${type.padRight(42)} (${params.keySet().join(', ')})"
}
println properties*.name
