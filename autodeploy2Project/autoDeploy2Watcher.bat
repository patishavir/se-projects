@echo ON
call \\s6540001\SCRIPT\scripts\common\common.bat >nul
set CLASSPATH=%JREPath%\lib\rt.jar

call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib
call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib\MQ\7.0.1

set CLASSPATH=%CLASSPATH%;%~dp0jars\autoDeploy2.jar
@echo CLASSPATH: %CLASSPATH%
set logFileName=%~n0_%myTimeStamp%

set mainClass=oz.utils.autodeploy.AutoDeployMain
set logFilePath=%~dp0logs\%logFileName%.err

%JREPath%\bin\java.exe -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=%COMPUTERNAME% %mainClass% EARWATCHER %~dp0args\autoDeploy2.properties \\matafcc\public$\autoDeploy2\watch\  1>>%~dp0logs\%logFileName%.log