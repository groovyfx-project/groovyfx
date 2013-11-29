_This documentation pertains to GroovyFX version 0.3.1, which includes support for JavaFX 2.2_

# Build instructions


Download JavaFX from http://javafx.com and follow the install instructions.


## Building with Gradle

* Set your environment variable JAVAFX_HOME to the directory that contains rt/lib/jfxrt.jar.
* cd groovyfx
* gradlew build

To run any specific demo, e.g. the AccordionDemo, you can just call:

> gradlew AccordionDemo

To see an executable overview of all build tasks including all demos:

> gradlew --gui


## Using GroovyFX from Maven Central

Having GroovyFX in Maven Central (thanks to Sonatype's OSS hosting!) makes it simple to use GroovyFX in
everything from simple test scripts to larger projects.  The Maven coordinates are as follows:

* _groupId_: org.codehaus.groovyfx
* _artifactId_: groovyfx
* _version_: 0.3.1

GroovyFX is simple to include in Groovy scripts thanks to Groovy's Grab annotation, a part of the Grape
system.  Just include the following line at the top of your script:

> @Grab('org.codehaus.groovyfx:groovyfx:0.3.1')

When you start such a script, make sure that an explicit reference to your jfxrt.jar is declared in your classpath
(even if you use Java 7, which includes JavaFX), e.g. like so:

> groovy -cp $JAVA_HOME/jre/lib/jfxrt.jar myScript.groovy


## Creating a GroovyFX-Based Project with Gradle

It is also simple to set up your own GroovyFX-based project using Gradle as the build system.  This sample
[build.gradle](https://gist.github.com/2712927) script will get you started.

Just create a new directory for your project and place the Gradle script into it.  Then simply execute the
following command:

> gradle makeDirs

to set up the rest of your project's directory structure.  You will automatically have a dependency on both
Groovy and GroovyFX.


## Building with IntelliJ IDEA

GroovyFX's build script is capable of generating all of the project files necessary to build the project
with IntelliJ IDEA.  Just run the following command from the project's root directory:

> gradlew idea

This will generate a groovyfx.ipr file.  From IntelliJ IDEA, select File -> Open Project and navigate to the
directory containing the groovyfx.ipr file and open it.  You should now be able to build the library and run
the demos with IntelliJ IDEA.


## Building with NetBeans

The NetBeans project files are included in the code repository.  You must customize the project files by
following these steps

1. Add the <JAVAFX_SDK>/rt/lib/jfxrt.jar lib to the project libraries.
2. In NetBeans, open the nbproject/project.properties file and modify the following property to point to your jfxrt.jar file:

> file.reference.jfxrt.jar=<path_to_JavaFX_SDK>/rt/lib/jfxrt.jar

You should now be able to build the library and run the demos with NetBeans.
