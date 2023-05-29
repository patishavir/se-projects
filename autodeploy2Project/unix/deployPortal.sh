#!/usr/bin/ksh

. $(dirname $0)/scripts/functions.sh 1>/dev/null 2>/dev/null
setJava71Env

CLASSPATH=$CLASSPATH:$scriptDir/jars/autoDeploy2.jar

addAllJarsInFolderToClassPath $scriptDir/lib
addAllJarsInFolderToClassPath $scriptDir/lib/MQ/7.0.1

logsFolder=$scriptDir/logs

if [[ ! -d $logsFolder ]] ; then
	mkdir $logsFolder
fi

logFile=$logsFolder/$(hostname).$timeStamp

mainClass=oz.utils.was.autodeploy.portal.AutoDeployPortalMain

system=myTestPortal
hostname=$(hostname)
envCode=$( echo $hostname | cut -c 8-10)
# echo $envCode
if [[ $envCode = "l-i" ]] ; then
        system=internalProdPortal
	export sendMail=no
elif [[ $hostname = "supportl-stg" ]] ; then
        system=externalProdPortal
        export sendMail=no
fi

portalDeploymentProperties=$scriptDir/args/systems/$system/portalDeployment.properties
echo system: $system    sendMail: $sendMail   portalDeploymentProperties: $portalDeploymentProperties

# zipFilePath=$scriptDir/test/2016-04-10.zip
zipFilePath=$scriptDir/test/testdata

vmArgs="-DmyDebug=YES"
# debugArgs='-Xdebug -Xrunjdwp:transport=dt_socket,address=7890,server=y,suspend=y'

java $debugArgs $vmArgs $mainClass $scriptDir/args/autoDeploy2.properties  $portalDeploymentProperties $zipFilePath 1>>$logFile.log 2>>$logFile.err &
