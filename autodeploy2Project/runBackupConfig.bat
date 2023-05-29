@echo ON
call \\s6540001\SCRIPT\scripts\common\common.bat >nul

set CLASSPATH=%JREPath%\lib\rt.jar

call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib > nul
call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib\MQ\7.0.1 > nul

set CLASSPATH=%CLASSPATH%;%~dp0jars\autoDeploy2.jar
@echo CLASSPATH: %CLASSPATH%
set logFileName=%~n0_%myTimeStamp%

set mainClass=oz.utils.autodeploy.AutoDeployMain
set portalDeploymentProperties=%~dp0args\systems\myTestPortal\portalDeployment.properties

set vmArgs="-DmyDebug=YES"

set system=suswastest2
%JREPath%\bin\java.exe %vmArgs% %mainClass% BACKUPCONFIG %~dp0args\autoDeploy2.properties %system% /tmp/autodeploy_backupConfig.zip -nostop 1>>%~dp0logs\%logFileName%.log 2>>%~dp0logs\%logFileName%.err
