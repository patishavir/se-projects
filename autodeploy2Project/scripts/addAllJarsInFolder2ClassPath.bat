set folder=%1%
FOR  %%i IN (%folder%\*.jar) DO call :add2ClassPath %%i
GOTO :EOF

:add2ClassPath
set CLASSPATH=%CLASSPATH%;%1