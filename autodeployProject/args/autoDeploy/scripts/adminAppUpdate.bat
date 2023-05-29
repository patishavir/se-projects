set wsadminPath=O:\Scripts\Websphere\wsadmin\thinAdminClient.bat

set hostname=snif-mgr
set portNumber=8886

%wsadminPath% -lang jython -conntype SOAP -host %hostname% -port %portNumber% -f %~dpn0.py