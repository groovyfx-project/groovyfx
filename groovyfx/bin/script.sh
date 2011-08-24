# Attempt to set JAVA_HOME if it's not already set.
if [ -z "$JAVA_HOME" ] ; then
    if $darwin ; then
        [ -z "$JAVA_HOME" -a -d "/Library/Java/Home" ] && export JAVA_HOME="/Library/Java/Home"
        [ -z "$JAVA_HOME" -a -d "/System/Library/Frameworks/JavaVM.framework/Home" ] && export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Home"
    else
        javaExecutable="`which javac`"
        [ -z "$javaExecutable" -o "`expr \"$javaExecutable\" : '\([^ ]*\)'`" = "no" ] && die "JAVA_HOME not set and cannot find javac to deduce location, please set JAVA_HOME."
        # readlink(1) is not available as standard on Solaris 10.
        readLink=`which readlink`
        [ `expr "$readLink" : '\([^ ]*\)'` = "no" ] && die "JAVA_HOME not set and readlink not available, please set JAVA_HOME."
        javaExecutable="`readlink -f \"$javaExecutable\"`"
        javaHome="`dirname \"$javaExecutable\"`"
        javaHome=`expr "$javaHome" : '\(.*\)/bin'`
        JAVA_HOME="$javaHome"
        export JAVA_HOME

    fi
fi


# Determine the Java command to use to start the JVM.
if [ -z "$JAVACMD" ] ; then
    if [ -n "$JAVA_HOME" ] ; then
        if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
            # IBM's JDK on AIX uses strange locations for the executables
            JAVACMD="$JAVA_HOME/jre/sh/java"
        else
            JAVACMD="$JAVA_HOME/bin/java"
        fi
    else
        JAVACMD="java"
    fi
fi
if [ ! -x "$JAVACMD" ] ; then
    die "JAVA_HOME is not defined correctly, can not execute: $JAVACMD"
fi
if [ -z "$JAVA_HOME" ] ; then
    warn "JAVA_HOME environment variable is not set"
fi

TOOLS_JAR="$JAVA_HOME/lib/tools.jar"


if [ -z "$JAVAFX_HOME" ] ; then
    JAVAFX_HOME=/Users/jimclarke/SRC/JavaFX20
fi

if [ -z "$GROOVY_HOME" ] ; then
    GROOVY_HOME=/Users/jimclarke/src/groovy-1.8.0
    export GROOVY_HOME
fi

# resolve links - $0 may be a soft-link
PRG="$0"

while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done

DIRNAME=`dirname "$PRG"`


JAVAFX_RT=$JAVAFX_HOME/rt
JAVAFX_LIB=$JAVAFX_RT/lib

set -x
java -Dgroovy.home=$GROOVY_HOME -Dfile.encoding=UTF-8 -classpath $JAVAFX_HOME//rt/lib/jfxrt.jar:$GROOVY_HOME/embeddable/groovy-all-1.8.0.jar:./../dist/GroovyFX.jar -Dgroovy.starter.conf=$GROOVY_HOME/conf/groovy-starter.conf  -Dtools.jar=$TOOLS_JAR org.codehaus.groovy.tools.GroovyStarter --main groovy.ui.GroovyMain --conf /Users/jimclarke/src/groovy-1.8.0/conf/groovy-starter.conf --classpath . $*
