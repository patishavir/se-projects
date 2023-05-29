call s:\scripts\common\common.bat
set CLASSPATH=%JarContainer%\jdom\jdom.jar;%JREPath%\lib\rt.jar;%~dp0..\jars\FilesCleanUp.jar                             
%JREPath%\bin\java.exe  oz.utils.files.FilesCleanUp  %~dp0..\args\filesCleanUp.xml
pause                                                                                                                                     