#!/usr/bin/ksh

operation=DEPLOYPORTAL
. $(dirname $0)/scripts/functions.sh 1>/dev/null 2>/dev/null
. $(dirname $0)/scripts/autoCommon.sh 1>/dev/null 2>/dev/null
setJava71Env

if      [[ ${#} -gt 1 ]] ; then
        autoDeploySystem=$2
fi

CLASSPATH=$CLASSPATH:$scriptDir/jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/lib
addAllJarsInFolderToClassPath $scriptDir/lib/MQ/7.5

portalDeploymentProperties=$scriptDir/args/systems/$autoDeploySystem/portalDeployment.properties

if [[ ${#} -lt 1 ]] ; then
        echo    zip file path parameter has to be supplied
        echo    script will exit now.
        exit    -1
elif    [[ ! -e $1 ]]; then
        echo $1 does not exist !
        echo    script will exit now.
        exit    -1
else
        zipFilePath=$1
fi
echo do cleanup
find $scriptDir/logs -mtime +60 -type f -name '*[0-9].err' -exec rm -fe  '{}' \;
find $scriptDir/autoDeployWorkFolder -mtime +60 -type f -name '*[0-9].ear' -exec rm -fe  '{}' \;

# debugArgs='-Xdebug -Xrunjdwp:transport=dt_socket,address=7890,server=y,suspend=y'
echo operation: $operation  system: $autoDeploySystem    sendMail: $sendMail   portalDeploymentProperties: $portalDeploymentProperties zipFilePath: $zipFilePath
java $debugArgs  $mainClass $operation $scriptDir/args/autoDeploy2.properties  $portalDeploymentProperties $zipFilePath &

# ./deployPortal.sh ./data/testDeployPortal/oztest.zip
# ./deployPortal.sh ./data/testDeployPortal/oztest.zip staging

