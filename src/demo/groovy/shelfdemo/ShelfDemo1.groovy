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

import javafx.scene.layout.Region

accordion(maxWidth: Region.USE_PREF_SIZE, maxHeight: 300, id: 'Accordion') {
    titledPane(id: "t1", text: "Label 1") {
        content {
            label(text: "This is Label 1\n\nAnd there were a few empty lines just there!")
        }
    }
    titledPane(id: "t2", text: "Label 2") {
        content {
            label(text: "This is Label 2\n\nAnd there were a few empty lines just there!")
        }
    }
    titledPane(id: "t3", text: "Label 3") {
        // this is content
        label(text: "This is Label 3\n\nAnd there were a few empty lines just there!")
    }
}
