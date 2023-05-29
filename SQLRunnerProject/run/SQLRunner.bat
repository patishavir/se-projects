@echo on
set JAVA_HOME="c:\Program Files\Java\jre6"
set CLASSPATH=%JAVA_HOME%\lib\rt.jar
set jarsFolder=c:\oj\projects\JarContainer
set CLASSPATH=%CLASSPATH%;%jarsFolder%\jdom\jdom.jar
set CLASSPATH=%CLASSPATH%;%jarsFolder%\db2\db2java.zip;%jarsFolder%\db2\db2jcc4.jar
set CLASSPATH=%CLASSPATH%;%~dp0..\jars\sqlrunner.jar
dir ..\jars\sqlrunner.jar
%JAVA_HOME%\bin\java.exe oz.sqlrunner.SQLRunnerMain %~dp0..\args\database.properties
pause