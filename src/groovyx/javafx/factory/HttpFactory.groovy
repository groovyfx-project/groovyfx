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

package groovyx.javafx.factory

import javafx.io.http.*;
import javafx.event.EventHandler;
import javafx.event.Event;
import org.codehaus.groovy.runtime.InvokerHelper;
/**
 *
 * @author jimclarke
 */
class HttpFactory extends AbstractFactory {
    private static final String[] httpHandlers = [
        "onStarted",
        "onConnecting",
        "onDoneConnect",
        "onReadingHeaders",
        "onDoneHeaders",
        "onResponseCode",
        "onResponseMessage",
        "onResponseHeaders",
        "onResponseBody",
        "onRequestBody",
        "onError",
        "onReading",
        "onToRead",
        "onRead",
        "onInput",
        "onException",
        "onDoneRead",
        "onDone",
        "onOutput",
        "onWriting",
        "onWritten",
        "onDoneWrite",
        "onToWrite"
    ];
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object instance;

        switch(name) {
            case "httpRequest":
                instance = new HttpRequest();
                def method = attributes.get("method");
                if(method != null) {
                    if(method instanceof String) {
                        switch(method.toString().toLowerCase()) {
                            case 'get':
                                method = HttpRequest.GET;
                                break;
                            case 'post':
                                method = HttpRequest.POST;
                                break;
                            case 'delete':
                                method = HttpRequest.DELETE;
                                break;
                            case 'put':
                                method = HttpRequest.PUT;
                                break;
                        }
                        attributes.put("method", method);
                    }
                }

                
                for(int i =0; i < httpHandlers.length; i++) {
                    def func = attributes.remove(httpHandlers[i]);
                    if(func != null) {
                        def ce = new ClosureEventHandler( closure: func );
                        InvokerHelper.setProperty(instance, httpHandlers[i], ce);
                    }
                }
                break;
            case 'httpHeader':
                instance = new HttpHeader();
                break;
        }

        return instance;
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof HttpHeader) {
            HttpHeader h = (HttpHeader)child;
            ((HttpRequest)parent).setHeader(h.name, h.value)
        }
    }
}

