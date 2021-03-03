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
/**
 * Show how to embed an FXML file that was created with SceneBuilder and
 * get access to the components by id.
 * @author Dierk Koenig
 */

import static groovyx.javafx.GroovyFX.start

start {
    stage(title: "GroovyFX FXML Demo", visible: true) {
        scene(fill: GROOVYBLUE, width: 640, height: 500) {
            fxml(new File( getClass().getResource('/demo/buildertry.fxml').toURI() ).text)
        }
    }
    
    // println getClass().getResource("/buildertry.fxml")
}




