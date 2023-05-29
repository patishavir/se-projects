@echo on
Rem set targetFolder=C:\oj\TEMP\AutoDeploy\MatafEAR_T_OZ
set targetFolder=\\matafcc\public$\autoDeploy2\watch\snifit\MatafEAR_T_OZ

del %targetFolder%\%~n0.*
echo quit > %targetFolder%\%~n0.exit
dir %targetFolder%
pause