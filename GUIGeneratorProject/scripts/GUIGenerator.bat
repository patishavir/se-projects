set myjre="C:\Program Files\Java\jre1.5.0_02"
set CLASSPATH=%myjre%\lib\rt.jar;D:\oj\Projects\JarContainer\jdom.jar;%~d0%~p0generalGUI.jar
%myjre%\bin\java.exe  oz.generalgui.GeneralGUIMain %~d0%~p0generalGUI.xml
pause