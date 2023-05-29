@echo OFF
call \\s6540001\SCRIPT\scripts\common\common.bat >nul

if not defined earFilePath call %GUIGeneratorPath% %~dp0args\XML\%~n0.xml
set CLASSPATH=%JREPath%\lib\rt.jar

call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib
call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib\MQ\7.0.1

set CLASSPATH=%CLASSPATH%;%~dp0jars\autoDeploy2_2.jar
@echo CLASSPATH: %CLASSPATH%

set mainClass=oz.utils.autodeploy.AutoDeployMain
set vmArgs="-DmyDebug=YES"

Rem set earFilePath=%~dp0\test\intranetProd85\connectivityTest.ear
Rem set earFilePath=%~dp0\test\snifit\connectivityTest.ear
REM set earFilePath=&~dp0\test\intranetProd85\DBTestEAR.ear

IF NOT DEFINED logsFolder set logsFolder=%~dp0logs
IF NOT DEFINED logFileName set logFileName=%~n0_%myTimeStamp%
IF NOT DEFINED logFilePath set logFilePath=%logsFolder%\%logFileName%.err

%JREPath%\bin\java.exe %vmArgs% %mainClass% DEPLOYEAR %~dp0args\autoDeploy2.properties %earFilePath% 1>>%logsFolder%\%logFileName%.log