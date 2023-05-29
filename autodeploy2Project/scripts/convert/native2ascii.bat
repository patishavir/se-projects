set env=T_UE
# set env=T_OZ

set inputfile=O:\Scripts\java\autoDeploy\7.0\autoDeploy\args\earsProperties\source\%env%.properties
set outputFile=O:\Scripts\java\autoDeploy\7.0\autoDeploy\args\earsProperties\%env%.properties
set jdkBinFolder="C:\Program Files\Java\jdk1.7.0_67\bin"
%jdkBinFolder%\native2ascii.exe -encoding cp1255 %inputFile%    %outputFile%
type %outputFile%
pause