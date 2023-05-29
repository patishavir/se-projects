@echo ON
call \\s6540001\SCRIPT\scripts\common\common.bat >nul

call %GUIGeneratorPath% %~dp0args\XML\%~n0.xml
set CLASSPATH=%JREPath%\lib\rt.jar

call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib
call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib\MQ\7.0.1

set CLASSPATH=%CLASSPATH%;%~dp0jars\%~n0.jar
@echo CLASSPATH: %CLASSPATH%
set logFileName=%~n0_%myTimeStamp%

set mainClass=oz.utils.was.autodeploy.AutoDeployEarMain

set vmArgs="-DmyDebug=YES"
%JREPath%\bin\java.exe %vmArgs% %mainClass% %~dp0args\%~n0.properties %earFilePath% 1>>%~dp0logs\%logFileName%.log 2>>%~dp0logs\%logFileName%.err