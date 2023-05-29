set jrePath="C:\Program Files\Java\jre1.6.0_02"
%jrePath%\bin\java.exe -Xrs -cp  %myjre%\lib\rt.jar -jar %~dp0jars\%~n0.jar xxx
pause
