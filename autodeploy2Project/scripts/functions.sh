#!/usr/bin/ksh
#
# runUntilSuccessful
#
runUntilSuccessful () {

if [[ $retryLimit != +([0-9]) ]] ; then
         retryLimit=4
fi
echo ++++ retryLimit is $retryLimit

typeset myScript=$1
shift
typeset RETRY=0
typeset rc=99
while [[ $RETRY -lt $retryLimit && $rc -ne 0 ]]
do
        $myScript $@
        rc=$?
        echo ++++ return code from $myScript $@ is $rc. Retry count: $RETRY
        if [ $rc -ne 0 ]
                then
                        RETRY=`expr $RETRY + 1`
                        sleep 5
        fi
done
echo ++++ ------------------ $(date +"%d/%m/%Y %T") ------------------ ++++
return $rc
}
#
# setTimeStamp
#
setTimeStamp () {
	export timeStamp=$(date +%Y%m%d.%H%M%S)
	yyyymmdd=$(date +%Y%m%d)
	weekday=$(date +%w)
	dom=$(date +%d)
if [[ $1 == 'verbose' ]] ; then
	echo "\n"
	echo timeStamp: $timeStamp     weekday: $weekday   yyyymmdd:  $yyyymmdd
fi
}
#
# setWebSpher61Variables
#
setWebSphere61Variables () {
        WAS_INSTALL_ROOT_61=/usr/IBM/WebSphere/AppServer
        if [[ -d /nd61/IBM/WebSphere/AppServer ]] ; then
                WAS_INSTALL_ROOT_61=/nd61/IBM/WebSphere/AppServer
        fi
	profileName_61=AppSrv01
	USER_INSTALL_ROOT_61=$WAS_INSTALL_ROOT_61/profiles/$profileName_61
	if [[ -d $WAS_INSTALL_ROOT_61/profiles/Custom01 ]] ; then
                USER_INSTALL_ROOT_61=$WAS_INSTALL_ROOT_61/profiles/Custom01
                profileName_61=Custom01
        fi
	DMGR_INSTALL_ROOT_61=$WAS_INSTALL_ROOT_61/profiles/Dmgr01
        IHS_INSTALL_ROOT_61=/usr/IBM/HTTPServer
        if [[ -d /nd61/IBM/HTTPServer ]] ; then
                IHS_INSTALL_ROOT_61=/nd61/IBM/HTTPServer
        fi
	DMGR_HOST=snif-q
	DMGR_PORT=8886
if [[ $1 == 'verbose' ]] ; then
	echo "\n"
	echo WAS_INSTALL_ROOT_61: $WAS_INSTALL_ROOT_61
	echo USER_INSTALL_ROOT_61: $USER_INSTALL_ROOT_61
	echo  DMGR_INSTALL_ROOT_61: $DMGR_INSTALL_ROOT_61
	echo  profileName_61: $profileName_61
fi
}
#
# WAS 7.0
#
setWebSphere7Variables () {
	WAS_INSTALL_ROOT_7=/usr/IBM/WebSphere/AppServer
	if [[ -d /nd7/IBM/WebSphere/AppServer ]] ; then
		WAS_INSTALL_ROOT_7=/nd7/IBM/WebSphere/AppServer
	fi
	profileName_7=AppSrv01
	USER_INSTALL_ROOT_7=$WAS_INSTALL_ROOT_7/profiles/$profileName_7
	if [[ -d $WAS_INSTALL_ROOT_7/profiles/Custom01 ]] ; then
                USER_INSTALL_ROOT_7=$WAS_INSTALL_ROOT_7/profiles/Custom01
		profileName_7=Custom01
        fi
	DMGR_INSTALL_ROOT_7=$WAS_INSTALL_ROOT_7/profiles/Dmgr01
	IHS_INSTALL_ROOT_7=/usr/IBM/HTTPServer
	if [[ -d /nd7/IBM/HTTPServer ]] ; then
		IHS_INSTALL_ROOT_7=/nd7/IBM/HTTPServer
	fi
	DMGR_HOST_7=snif-q
	DMGR_PORT_7=8881

if [[ $1 == 'verbose' ]] ; then
	echo "\n"
	echo WAS_INSTALL_ROOT_7: $WAS_INSTALL_ROOT_7
	echo USER_INSTALL_ROOT_7: $USER_INSTALL_ROOT_7
	echo IHS_INSTALL_ROOT_7: $IHS_INSTALL_ROOT_7
	echo  DMGR_INSTALL_ROOT_7: $DMGR_INSTALL_ROOT_7
	echo  profileName_7: $profileName_7
	echo  DMGR_HOST: $DMGR_HOST
	echo  DMGR_PORT: $DMGR_PORT
fi
}
#
# WAS 8.0
#
setWebSphere8Variables () {
	WAS_INSTALL_ROOT_8=/nd8/IBM/WebSphere/AppServer
	profileName_8=AppSrv01
	USER_INSTALL_ROOT_8=$WAS_INSTALL_ROOT_8/profiles/$profileName_8
	if [[ -d $WAS_INSTALL_ROOT_8/profiles/Custom01 ]] ; then
                USER_INSTALL_ROOT_8=$WAS_INSTALL_ROOT_8/profiles/Custom01
		profileName_8=Custom01
        fi
	DMGR_INSTALL_ROOT_8=$WAS_INSTALL_ROOT_8/profiles/Dmgr01
	IHS_INSTALL_ROOT_8=/nd8/IBM/HTTPServer
	DMGR_HOST_8=WAS8TEST
	DMGR_PORT_8=8879

if [[ $1 == 'verbose' ]] ; then
	echo "\n"
	echo WAS_INSTALL_ROOT_8: $WAS_INSTALL_ROOT_8
	echo USER_INSTALL_ROOT_8: $USER_INSTALL_ROOT_8
	echo IHS_INSTALL_ROOT_8: $IHS_INSTALL_ROOT_8
	echo  DMGR_INSTALL_ROOT_8: $DMGR_INSTALL_ROOT_8
	echo  profileName_8: $profileName_8
	echo  DMGR_HOST_8: $DMGR_HOST
	echo  DMGR_PORT_8: $DMGR_PORT
fi
}
#
# Was 8.5
#
setWebSphere85Variables () {
        WAS_INSTALL_ROOT_85=/nd85/IBM/WebSphere/AppServer
        profileName_85=AppSrv01
        USER_INSTALL_ROOT_85=$WAS_INSTALL_ROOT_85/profiles/$profileName_85
        if [[ -d $WAS_INSTALL_ROOT_85/profiles/Custom01 ]] ; then
                USER_INSTALL_ROOT_85=$WAS_INSTALL_ROOT_85/profiles/Custom01
                profileName_85=Custom01
        fi
        DMGR_INSTALL_ROOT_85=$WAS_INSTALL_ROOT_85/profiles/Dmgr01
        IHS_INSTALL_ROOT_85=/nd85/IBM/HTTPServer
        DMGR_HOST_85=snif-q
        DMGR_PORT_85=8885

if [[ $1 == 'verbose' ]] ; then
        echo "\n"
        echo WAS_INSTALL_ROOT_85: $WAS_INSTALL_ROOT_85
        echo USER_INSTALL_ROOT_85: $USER_INSTALL_ROOT_85
        echo IHS_INSTALL_ROOT_85: $IHS_INSTALL_ROOT_85
        echo  DMGR_INSTALL_ROOT_85: $DMGR_INSTALL_ROOT_85
        echo  profileName_85: $profileName_85
        echo  DMGR_HOST_85: $DMGR_HOST
        echo  DMGR_PORT_85: $DMGR_PORT
fi
}
#
# getScriptDir
#
getScriptDir () {
	scriptPath="$(whence ${0})"
	scriptFolder="${scriptPath%/*}"
	scriptDir=$scriptFolder
	scriptName=`basename $scriptPath`

if [[ $1 == 'verbose' ]] ; then
	echo "\n"
	echo scriptPath=$scriptPath
	echo scriptDir=$scriptDir
	echo scriptFolder=$scriptDir
	echo scriptName=$scriptName
fi
}
#
# setJavaEnv
#
setJavaEnv () {
export CLASSPATH=/usr/java6_64/jre/lib/rt.jar
export PATH=/usr/java6_64/jre/bin/:$PATH
export JAVA_HOME=/usr/java6_64/jre
export JAVA7_HOME=/usr/java7_64/jre
export jarContainerPath=/snifit/scripts/java/JarContainer
export guiGeneratorPath=/snifit/scripts/java/GUIGenerator/GUIGenerator.sh
if [[ $1 == 'verbose' ]] ; then
	echo CLASSPATH=$CLASSPATH
	echo JAVA_HOME=$JAVA_HOME
	echo PATH=$PATH
	echo jarContainerPath=$jarContainerPath
	$JAVA_HOME/bin/java -version
fi
}
#
# setJava7Env
#
setJava7Env () {
export CLASSPATH=/usr/java7_64/jre/lib/rt.jar
export PATH=/usr/java7_64/jre/bin/:$PATH
export JAVA_HOME=/usr/java7_64/jre
export jarContainerPath=/snifit/scripts/java/JarContainer
export guiGeneratorPath=/snifit/scripts/java/GUIGenerator/GUIGenerator.sh
if [[ $1 == 'verbose' ]] ; then
	echo CLASSPATH=$CLASSPATH
	echo JAVA_HOME=$JAVA_HOME
	echo PATH=$PATH
	echo jarContainerPath=$jarContainerPath
	$JAVA_HOME/bin/java -version
fi
}
#
# setJava71Env
#
setJava71Env () {
export CLASSPATH=/usr/java71_64/jre/lib/rt.jar
export PATH=/usr/java71_64/jre/bin/:$PATH
export JAVA_HOME=/usr/java71_64/jre
export jarContainerPath=/snifit/scripts/java/JarContainer
export guiGeneratorPath=/snifit/scripts/java/GUIGenerator/GUIGenerator.sh
if [[ $1 == 'verbose' ]] ; then
	echo CLASSPATH=$CLASSPATH
	echo JAVA_HOME=$JAVA_HOME
	echo PATH=$PATH
	echo jarContainerPath=$jarContainerPath
	$JAVA_HOME/bin/java -version
fi
}
#
# addAllJarsInFolderToClassPath
#
addAllJarsInFolderToClassPath () {
for jarFile in $1/*.jar
	do
		# echo $jarFile
		CLASSPATH=$CLASSPATH:$jarFile
	done
	# echo $CLASSPATH
}
#
# replaceString
#
replaceString () {
filePath=$1
sourceString=$2
targetString=$3
nparams=3
rc=0

echo filePath: $filePath   sourceString: $sourceString  targetString: $targetString

if [[ $# -ne $nparams ]] ; then
        echo please specify $nparams parameters
        rc=1

        elif  [[ ! -f $filePath ]] ; then
                echo $filePath does not exist ! operation failed.
                rc=1
                else
                        tempFilePath=$filePath.$timeStamp
                        echo $tempFilePath
                        mv $filePath $tempFilePath
                        sed "s/$sourceString/$targetString/" < $tempFilePath > $filePath
                        cat $filePath
			rm -f  $tempFilePath
fi

return $rc
}

HOSTNAME=`hostname`
setWebSphere61Variables
setWebSphere7Variables
setWebSphere8Variables
setWebSphere85Variables

setTimeStamp

getScriptDir
