@echo on
set JREPath="C:\Program Files\java\jre6"

set CLASSPATH=%JREPath%\lib\rt.jar
set jarsFolder=%~dp0..\JarContainer

dir %jarsFolder%

@set CLASSPATH=%CLASSPATH%;%jarsFolder%\db2\db2jcc4.jar;%jarsFolder%\db2\db2jcc_license_cu.jar
@set CLASSPATH=%CLASSPATH%;%jarsFolder%\commons\commons-codec-1.3.jar;%jarsFolder%\commons\commons-httpclient-3.1.jar
@set CLASSPATH=%CLASSPATH%;%jarsFolder%\commons\commons-email-1.0.jar;%jarsFolder%\commons\commons-logging-1.0.4.jar
@set CLASSPATH=%CLASSPATH%;%jarsFolder%\MQ\com.ibm.mq.jar;%jarsFolder%\MQ\connector.jar
@set CLASSPATH=%CLASSPATH%;%jarsFolder%\EE\mail.jar
@set CLASSPATH=%CLASSPATH%;%jarsFolder%\log4j\log4j-1.2.15.jar
set CLASSPATH=%CLASSPATH%;%~dp0jars\ozmonitor.jar

%JREPath%\bin\java.exe oz.monitor.OzMonitorMain %~dp0DCA %~dp0DCA\log4j.properties
pause