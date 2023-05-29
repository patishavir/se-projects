set inputFile=C:\oj\projects\utilsProject\args\autoDeploy\dev\earsProperties\T_OZ.properties
set jdkBinFolder="C:\Program Files\Java\jdk1.6.0_37\bin"
%jdkBinFolder%\native2ascii.exe -encoding cp1255 %inputFile%    %inputFile%.HEB
type %inputFile%.HEB
pause