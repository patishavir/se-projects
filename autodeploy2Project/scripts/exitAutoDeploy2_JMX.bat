@echo ON
call \\s6540001\SCRIPT\scripts\common\common.bat >nul
set CLASSPATH=%JREPath%\lib\rt.jar
set projectName=autoDeploy2
set CLASSPATH=%CLASSPATH%;%~dp0..\jars\%projectName%.jar
@echo CLASSPATH: %CLASSPATH%

set logFileName=%~n0_%mm%

set mainClass=oz.utils.autodeploy.AutoDeployMain

set jmxPort=9999
set host=S5380440

%JREPath%\bin\java.exe %mainClass% EXITWATCHER %~dp0..\args\autoDeploy2.properties %host% %jmxPort%  1>>%~dp0..\logs\%logFileName%.log 2>>%~dp0..\logs\%logFileName%.err
explorer.exe %~dp0..\logs\
pause