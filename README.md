_GroovyFX is currently under development and the 0.1-SNAPSHOT version must be considered experimental_.

Build instructions
==================
Download JavaFX from http://javafx.com and follow the install instructions.

Building with Gradle
--------------------
* Set your environment variable JAVAFX_HOME to the directory that contains rt/lib/jfxrt.jar.
* cd groovyfx
* gradlew build

To run any specific demo, you can just call
* gradlew AccordionDemo

To see an executable overview of all build tasks including all demos
* gradlew --gui

Building with IntellijIdea
--------------------------
* use the Groovyfx.iml module
* make sure to have libraries named "JavaFX 2.0 Beta" and "groovy-1.8.4"

Building with NetBeans
----------------------
Add the <JAVAFX_SDK>/rt/lib/jfxrt.jar lib to the project libraries.
In Netbeans modify the nbproject/project.properties file modify the following property:

file.reference.jfxrt.jar=/Users/jimclarke/SRC/JavaFX20/rt/lib/jfxrt.jar

