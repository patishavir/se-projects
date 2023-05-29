SET inProgressFolder=c:\temp\inProgress
SET batFileName=1.bat
SET watchedFolder=c:\temp\watch

IF NOT EXIST %inProgressFolder% md %inProgressFolder%
IF EXIST %watchedFolder%\%batFileName% del %watchedFolder%\%batFileName%

copy %~dp0%batFileName% %watchedFolder%
pause