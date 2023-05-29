#!/usr/bin/ksh

. $(dirname $0)/scripts/functions.sh 1>/dev/null 2>/dev/null
setJava71Env

CLASSPATH=$CLASSPATH:$scriptDir/jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/lib
addAllJarsInFolderToClassPath $scriptDir/lib/MQ/7.0.1

logFile=$scriptDir/logs/$(hostname).$timeStamp
earFilePath=$scriptDir/test/snifit/DBTest.ear
# earFilePath=$scriptDir/test/suswastest2/DBTestEAR.ear
ls -l  $earFilePath

mainClass=oz.utils.was.autodeploy.AutoDeployEarMain
vmArgs="-DmyDebug=YES"
java $vmArgs $mainClass $scriptDir/args/autoDeploy2.properties $earFilePath 1>>$logFile.log 2>>$logFile.err
