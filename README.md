_This documentation pertains to GroovyFX version 0.4.0, which includes support for JavaFX 2.2 on Java 7 and
JavaFX 8 on Java 8_

# Build instructions

Download JavaFX from http://javafx.com and follow the install instructions.

Or if you are feeling "bleeding edge" clone the Git repository!


## Building with Gradle shell command line

* If you are using Java 8 or a recent Java 7 then there is nothing extra to do. If you are using an old Java
  7, you may find you have to set the environment variable JAVAFX_HOME to the directory that contains
  rt/lib/jfxrt.jar.
* cd to the GroovyFX directory
* gradlew build

To run any specific demo, e.g. the AccordionDemo, you can just call:

> gradlew AccordionDemo

To see an executable overview of all build tasks including all demos:

> gradlew --gui


## Using GroovyFX from Maven Central

Having GroovyFX in Maven Central (thanks to Sonatype's OSS hosting) makes it simple to use GroovyFX in
everything from simple test scripts to larger projects.  The Maven coordinates are as follows:

* _groupId_: org.codehaus.groovyfx
* _artifactId_: groovyfx
* _version_: 0.4.0

GroovyFX is simple to include in Groovy scripts thanks to Groovy's Grab annotation, a part of the Grape
system.  Just include the following line at the top of your script:

> @Grab('org.codehaus.groovyfx:groovyfx:0.4.0')

You may find that when you start such a script, you have to ensure that an explicit reference to your
jfxrt.jar is declared in your classpath. For Java 8:

> groovy -cp $JAVA_HOME/jre/lib/ext/jfxrt.jar myScript.groovy

and for Java 7:

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

Install the NetBeans Gradle Plugin (which should be in the list of plugins offered as standard, but failing
that there see http://plugins.netbeans.org/plugin/44510/gradle-support). Using the open a new project
dialogue, navigate to the GroovyFX project directory and you should see the Gradle logo indicating you can
open this as a Gradle project. You should now be able to build the library and run the demos with NetBeans.


## Building with Eclipse

As at 2014-05-07, GrEclipse appears not to support Groovy 2.3.0, so building with Eclipse has not been tried.
