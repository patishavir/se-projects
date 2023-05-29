call \\hrfs\install\java\setJREPath5.bat
set CLASSPATH=%myjre%\lib\rt.jar;D:\oj\Projects\JarContainer\jdom.jar;%~d0%~p0%~n0.jar
%myjre%\bin\java.exe  oz.perfmon.PerfMonMain %~d0%~p0perfmon.xml 1>> c:\temp\%~n0.csv 2>>c:\temp\%~n0.err
pause
