#!/usr/bin/ksh

. $(dirname $0)/functions.sh 1>/dev/null 2>/dev/null
setJava71Env

CLASSPATH=$CLASSPATH:$scriptDir/../jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/../lib
addAllJarsInFolderToClassPath $scriptDir/../lib/MQ/7.0.1


mainClass=oz.infra.fibi.gm.test.GmUtilsTest

echo main class: $mainClass 
echo CLASSPATH: $CLASSPATH
java $mainClass 
