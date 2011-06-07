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

import javafx.lang.*;
import groovyx.javafx.SceneGraphBuilder;
import javafx.io.http.HttpEvent;
import javafx.stage.Stage;
import groovyx.javafx.GroovyFX

//TODO: HttpRequest is deprecated in the Beta software
// not sure what is replacing it.
GroovyFX.start({
    def sg = new SceneGraphBuilder();

    def onInput = {
            event ->
            InputStream is = event.getInput();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for(String line = reader.readLine(); line != null;
                line = reader.readLine()) {
                println(line);
            }

        };

    def onStarted = {
            println("Started");
        }

    def request = sg.httpRequest(
        location: "http://api.flickr.com/services/rest/?method=flickr.test.echo&name=value",
        method: "get",
        onInput: onInput,
        onStarted: onStarted
    )
    request.start();
    println("Request = ${request}")

        Stage stage = new Stage();
        stage.setVisible(true);

});

