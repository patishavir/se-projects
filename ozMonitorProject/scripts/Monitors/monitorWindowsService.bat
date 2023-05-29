set ServiceName=psexesvc
c:\windows\system32\sc.exe query %ServiceName% | find "RUNNING"
exit %ERRORLEVEL%