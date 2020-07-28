#!/bin/bash

if [ -d "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
        JAVA_CMD="$JAVA_HOME/bin/java"
elif [ -x "/usr/bin/java" ]; then
        JAVA_CMD="/usr/bin/java"
else
        JAVA_CMD=java
fi

CONV_HOME=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

echo "run ConverterUI at $CONV_HOME"

$JAVA_CMD -Dfile.encoding=UTF-8 "$CONV_HOME/ConverterUI.jar"
