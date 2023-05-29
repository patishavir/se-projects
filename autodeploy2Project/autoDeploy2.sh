#!/usr/bin/ksh

operation=DEPLOYEAR
. $(dirname $0)/scripts/functions.sh 1>/dev/null 2>/dev/null
. $(dirname $0)/scripts/autoCommon.sh 1>/dev/null 2>/dev/null
setJava71Env

CLASSPATH=$CLASSPATH:$scriptDir/jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/lib
addAllJarsInFolderToClassPath $scriptDir/lib/MQ/7.5

earFilePath=$1

vmArgs="-DmyDebug=YES"
echo system: $system    sendMail: $sendMail operation: $operation earFilePath: $earFilePath
java $vmArgs $mainClass $operation $scriptDir/args/autoDeploy2.properties $earFilePath &

# ./autoDeploy2.sh ./data/test/myTestPortal/connectivityTest.ear
# ./autoDeploy2.sh ./data/test/suswastest2/connectivityTest.ear
# ./autoDeploy2.sh ./data/test/internalProdPortal/connectivityTest.ear
# ./autoDeploy2.sh ./data/test/externalProdPortal/connectivityTest.ear
