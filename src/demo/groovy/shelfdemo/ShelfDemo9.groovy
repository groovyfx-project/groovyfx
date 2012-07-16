/*
* Copyright 2011-2012 the original author or authors.
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
package shelfdemo

/**
 * Showing how multiple effects can be chained via the 'input' property or through groups
 * @author Dierk Koenig
 */

pane(id: 'Chained Effects') {
    //simple
    rectangle x: 10, y: 10, width: 87.5, height: 87.5, fill: WHITESMOKE, {
        effect dropShadow(color: YELLOW, input: innerShadow(color: GROOVYBLUE))
    }
    // changing the sequence leads to different results
    rectangle x: 102.5, y: 10, width: 87.5, height: 87.5, fill: WHITESMOKE, {
        effect innerShadow(color: GROOVYBLUE, input: dropShadow(color: YELLOW))
    }
    // chaining by using groups
    group {
        rectangle x: 195, y: 10, width: 87.5, height: 87.5, fill: WHITESMOKE, {
            effect innerShadow(color: GROOVYBLUE)
        }
        effect dropShadow(color: YELLOW)
    }
    // changing the effects at runtime
    def inner = innerShadow(color: GROOVYBLUE)
    def outer = dropShadow(color: YELLOW)
    rectangle id: 'interactive', x: 287.5, y: 10, width: 87.5, height: 87.5, fill: WHITESMOKE, {
        effect outer
        onMouseEntered { outer.input = inner }
        onMouseExited { outer.input = null }
        onMouseClicked { interactive.effect = reflection(input: outer) }
    }
}


