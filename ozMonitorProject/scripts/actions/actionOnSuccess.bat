@echo off
echo %~n0  %1% %2% %3%>c:\temp\%~n0.txt
@echo sleep 77 >>c:\temp\%~n0.txt
@echo sleep 88 >>c:\temp\%~n0.txt
exit 99