#!/usr/bin/ksh

operation=BACKUPCONFIG
. $(dirname $0)/scripts/functions.sh  1>/dev/null 2>/dev/null
. $(dirname $0)/scripts/autoCommon.sh 1>/dev/null 2>/dev/null
setJava71Env

CLASSPATH=$CLASSPATH:$scriptDir/jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/lib
addAllJarsInFolderToClassPath $scriptDir/lib/MQ/7.5

vmArgs="-DmyDebug=YES"
echo system: $system logFilePath: $logFilePath mainClass: $mainClass sendMail: $sendMail operation: $operation
java $vmArgs $mainClass $operation $scriptDir/args/autoDeploy2.properties ${autoDeploySystem} /tmp/autodeploy_backupConfig.zip -nostop &
