@echo on
call \\s6540001\SCRIPT\scripts\Java\setJREPath.bat

set CLASSPATH=%JREPath%\lib\rt.jar
set jarsFolder=c:\oj\projects\JarContainer
set jarsFolder=\\s6540001\SCRIPT\scripts\Java\JarContainer
set CLASSPATH=%CLASSPATH%;%jarsFolder%\db2\db2jcc4.jar;%jarsFolder%\db2\db2jcc_license_cu.jar
set CLASSPATH=%CLASSPATH%;%jarsFolder%\commons\commons-codec-1.3.jar;%jarsFolder%\commons\commons-httpclient-3.1.jar
set CLASSPATH=%CLASSPATH%;%jarsFolder%\commons\commons-email-1.0.jar;%jarsFolder%\commons\commons-logging-1.0.4.jar
set CLASSPATH=%CLASSPATH%;%jarsFolder%\MQ\com.ibm.mq.jar;%jarsFolder%\MQ\connector.jar
set CLASSPATH=%CLASSPATH%;%jarsFolder%\EE\mail.jar
set CLASSPATH=%CLASSPATH%;%~dp0..\jars\ozmonitor.jar

%JREPath%\bin\java.exe oz.monitor.OzMonitorMain C:\oj\projects\ozMonitorProject\files\Fibi
pause