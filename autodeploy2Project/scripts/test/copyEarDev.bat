set targetFolder=C:\oj\TEMP\AutomaticApplicationUpdate\T_OZ\
set earFilePath=O:\Scripts\java\autoDeploy\scripts\test\MatafEAR.ear
dir %targetFolder%
del  %targetFolder%*.ear
copy %earFilePath% %targetFolder%
dir %targetFolder%
pause