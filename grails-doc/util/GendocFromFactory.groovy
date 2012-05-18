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


import groovyx.javafx.factory.*
import java.io.*
import static groovy.io.FileType.*

def outputDir = "./grails-doc/util/tmp";
def demoDir = "./src/demo/groovy";

for(int i = 0; i < args.length; i++) {
    if(args[i].startsWith("--out=")) {
        outputDir = args[i].substring(6)
    }else if(args[i].startsWith("--demo=")) {
        demoDir = args[i].substring(7)
    }
}
def ofile = new File(outputDir);
if(!ofile.exists()) {
    ofile.mkdirs();
}
def demoFile = new File(demoDir);
def sgb = new SceneGraphBuilder();

def grepDemos = { String groovyfx ->
    def pattern = /^.*$groovyfx.*$/ 
    files = [];
    demoFile.eachFileMatch FILES, ~/.*\.groovy/, { File groovy -> 
        if(groovy.text.contains(groovyfx)) {
            files << groovy
        }
    }
    files
}

def genDoc = { String factory, String groovyfx, Class beanClass ->
    File dir = new File(outputDir, factory);
    if(!dir.exists()) {
        dir.mkdir();
    }
    File gdocFile = new File(dir, "${groovyfx}.gdoc");
    PrintWriter out = new PrintWriter(gdocFile);
    try {
        out.println(String.format(".h4 %s", groovyfx))
        if(beanClass != null && beanClass.name.startsWith("javafx.")) {
            out.println(String.format("Represents the JavaFX class, %s", beanClass.name))
            String javadoc = beanClass.name.replace('.','/');
            out.println(String.format("see [%s|http://docs.oracle.com/javafx/2/api/%s.html]\n", beanClass.name, javadoc))
            
            def demoFiles = grepDemos(groovyfx)
            if(demoFiles.size() > 0) {
                out.println ""
                out.println "{code}" ;
                // strip copyright
                out.println demoFiles[0].text.substring(601);
                out.println "{code}";
                out.println ""
                out.println ".h5 See Also\n"
                demoFiles.each {File demo ->
                    out.println demo.name
                }
                
            }
            
        }
        out.println();
    }finally {
        out.close();
    }
    
}





def factories = sgb.factories;
factories.each {
    if(it.value instanceof AbstractFXBeanFactory) {
        String factoryClassName = it.value.class.name;
        int ndx = factoryClassName.lastIndexOf('.');
        String factory = factoryClassName.substring(ndx+1, factoryClassName.length()-7);
        switch(it.value.class) {
            case ClosureHandlerFactory:
                genDoc("Event", it.key, null)
                break;
            default:
                genDoc(factory, it.key, it.value.beanClass)
                break;
        }
        
        
    }
}
