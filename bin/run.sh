if [ -z "$JAVAFX_HOME" ] ; then
JAVAFX_HOME=/Users/jimclarke/SRC/JavaFX20
fi
if [ -z "$GROOVY_HOME" ] ; then
GROOVY_HOME=/Users/jimclarke/src/groovy-1.8.0
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
java -Dfile.encoding=UTF-8 -classpath $JAVAFX_LIB/jfxrt.jar:$GROOVY_HOME/embeddable/groovy-all-1.8.0.jar:$DIRNAME/../dist/GroovyFX.jar $*

