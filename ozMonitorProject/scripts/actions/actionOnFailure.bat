@echo off
@echo %~n0   >c:\temp\%~n0.txt
@echo %~n0  %1% >>c:\temp\%~n0.txt
@echo %~n0  %2% >>c:\temp\%~n0.txt
@echo %~n0  %3% >>c:\temp\%~n0.txt
@echo sleep 22 >>c:\temp\%~n0.txt
@echo sleep 33 >>c:\temp\%~n0.txt
exit 77