@echo on
set targetFolder=\\matafcc\public$\snifit\autoDeploy\AutomaticApplicationUpdate7
del %targetFolder%\%~n0
echo quit > %targetFolder%\%~n0
dir %targetFolder%
pause