@echo OFF
call \\s6540001\SCRIPT\scripts\common\common.bat >nul

call %GUIGeneratorPath% %~dp0args\XML\%~n0.xml
set CLASSPATH=%JREPath%\lib\rt.jar

call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib
call %~dp0scripts\addAllJarsInFolder2ClassPath %~dp0lib\MQ\7.0.1

set CLASSPATH=%CLASSPATH%;%~dp0jars\autoDeploy2_2.jar
@echo CLASSPATH: %CLASSPATH%

set logFileName=%~n0_%myTimeStamp%
set logsFolder=%~dp0logs
set logFilePath=%logsFolder%\%logFileName%.err

set mainClass=oz.utils.autodeploy.AutoDeployMain

set vmArgs="-DmyDebug=YES"
REM set debugString=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8765,suspend=y

:set loadRulesFilePath=S:\dbload_bmb\TOZ\20160816\TOZ_TEST_LoadRules_6.03.01.zip

:set appId=TOZ
:set version=6.77.17

:set dbChangesDB=OZ77
:set dbChangesScriptsFolderPath=S:\dbChanges\OZ77\20160531\

:set dbChangesCommonDB=OZ77
:set commondbChangesScriptsFolderPath=S:\dbChanges\OZ77\20160531\

:set earFilePath=%~dp0test\snifit\DBTest.ear



@echo loadRulesFilePath: %loadRulesFilePath% appId: %appId%  version: %version%  dbChangesDB: %dbChangesDB% dbChangesScriptsFolderPath: %dbChangesScriptsFolderPath%
@echo dbChangesCommonDB: %dbChangesCommonDB%  commondbChangesScriptsFolderPath: %commondbChangesScriptsFolderPath% earFilePath: %earFilePath%

if defined loadRulesFilePath %JREPath%\bin\java.exe %vmArgs% %debugString% %mainClass% COPYLOADRULES %~dp0args\autoDeploy2.properties %system% %loadRulesFilePath% 1>>%logsFolder%\%logFileName%.log

if defined version %JREPath%\bin\java.exe %vmArgs% %debugString% %mainClass% MODIFYLOADRULESCFG %~dp0args\autoDeploy2.properties %system% %appId% 6.77.17 1>>%logsFolder%\%logFileName%.log

if defined dbChangesScriptsFolderPath call :runDbCanges %dbChangesDB% %dbChangesScriptsFolderPath%

if defined commondbChangesScriptsFolderPath call :runDbCanges %dbChangesCommonDB% %commondbChangesScriptsFolderPath%

if defined earFilePath call %~dp0autoDeployEar.bat %earFilePath%

pause
exit

:runDbCanges
	set databaseName=%1%
	set sqlScriptFolderPath=%2%
	call O:\Scripts\java\db2\RunSqlScriptsFromFolder\RunSqlScriptsFromFolder.bat