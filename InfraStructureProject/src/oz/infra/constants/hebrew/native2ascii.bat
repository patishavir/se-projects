set baseFolder=%~dp0.

set inputfile=%baseFolder%\hebrewCharacters.heb
set outputFile=%baseFolder%\hebrewCharacters.ascii

set jdkBinFolder="C:\Program Files\Java\jdk1.7.0_55\bin"
%jdkBinFolder%\native2ascii.exe -encoding cp1255 %inputFile%    %outputFile%
type %outputFile%
pause