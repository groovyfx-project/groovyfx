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
import static groovyx.javafx.GroovyFX.start

start {
    final imageURL = "http://www.nasa.gov/images/content/611907main_image_2134_800-600.jpg";
    stage(title: "GroovyFX Image Demo", visible: true,) {
        scene {
            imageView(imageURL)
            // alternative ways to create an imageView
            //imageView(new URL(imageURL)), or
            //imageView(new URI(imageURL)), or
            //imageView () {
            //    image(imageURL), or
            //    image(new File("someFile.png")), or
            //    image(new URL(imageURL)), or
            //    image(new URI(imageURL)), or
            //}
            
        }
    }
}

