@echo on
set targetFolder=\\matafcc\public$\snifit\autoDeploy\AutomaticApplicationUpdate7\T_OZ\
dir %targetFolder%
del %targetFolder%*.ear
copy %~dp0MatafEAR.ear %targetFolder%
dir %targetFolder%
pause