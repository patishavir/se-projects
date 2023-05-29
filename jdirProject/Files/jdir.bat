set myjre="C:\Program Files\Java\jre1.5.0_06"
set CLASSPATH=%myjre%\lib\rt.jar;D:\oj\Projects\JarContainer\jdom.jar;%~dp0jdir.jar
%myjre%\bin\java.exe  oz.jdir.JDirMain 
:%myjre%\bin\java.exe  oz.jdir.JDirMain %~dp0XMLFILES\jdir0.xml
