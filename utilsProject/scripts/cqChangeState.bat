call \\hrfs\install\scripts\GUIGenerator.bat %~dp0..\XML\%~n0.xml
db2cmd /c /w "DB2 UPDATE CLI CFG AT USER LEVEL FOR SECTION %CONNECTION%  USING DBALIAS %CONNECTION% CURRENTSQLID %CURRENTSQLID%"
db2cmd /c /w  "DB2 UPDATE CLI CFG                         FOR SECTION %CONNECTION%  USING DBALIAS %CONNECTION% CURRENTSQLID %CURRENTSQLID%"
call %~dp0..\msgbox.bat 0 "Set currentSqlId" "CURRENTSQLID %CURRENTSQLID% set for Connection %CONNECTION%"