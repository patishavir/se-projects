from sys import argv
from time import localtime, strftime
import time
import java.lang.System as javaSystem

def logMessage (message):
        print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + message
        return

def isAppReady  (applicationName):
    appReady ='false'
    loopCount = 100
    sleepInterval = 2
    i = 0
    logMessage("check application state loop")
    while i < loopCount :
        i += 1
        time.sleep(sleepInterval)
        appReady = AdminApp.isAppReady(applicationName)
        print str(i) + ' appReady: ' + appReady
        if appReady == 'true':
                logMessage ("application " + applicationName + " is ready ... ")
                break        
    return appReady
	
def stopStartApplication ( applicationName ) :
        lineSeparator = javaSystem.getProperty('line.separator')
        appManagers = AdminControl.queryNames('type=ApplicationManager,*')
        # print appManagers

        managersList = appManagers.split (lineSeparator)
        stopOperation = 'stopApplication'
        startOperation = 'startApplication'
        startCount = 0

        for appManager1 in managersList:
        #       print appManager1 + '\n======================================'
                stopReturnCode = ''
                try:
                        stopReturnCode = AdminControl.invoke(appManager1, stopOperation, applicationName)
                        print stopOperation + ' operation succeeded for ' + appManager1 + stopReturnCode
                except:
                #       print stopOperation + ' operation failed for ' + appManager1 + stopReturnCode
                        pass

                startReturnCode = ''
                try:
                        startReturnCode = AdminControl.invoke(appManager1, startOperation, applicationName)
                        print startOperation + ' operation succeeded for ' + appManager1 + startReturnCode
                        startCount += 1
                except:
                #       print startOperation + ' operation failed for ' + appManager1 + startReturnCode
                        pass
        print  applicationName + ' has been successfully started on ' + str(startCount) + ' servers'
        return startCount;

def rippleStartCluster ( cluster2Reastart ) :
        searchString = 'type=Cluster,name=' + cluster2Reastart + ',*'
        clusterObjName = AdminControl.completeObjectName(searchString)
        print 'cluster: ' + clusterObjName

        AdminControl.invoke(clusterObjName, 'rippleStart')
        print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + "rippleStart has completed ... " + clusterObjName

def syncActiveNodes  ():
# "syncActiveNodes" can only be run on the deployment manager's MBean, # it will fail in standalone environment
	dm = AdminControl.queryNames("type=DeploymentManager,*")
	if dm:
		logMessage ("Synchronizing configuration repository with active nodes.")
		# Force sync with all currently active nodes
		nodes = AdminControl.invoke(dm, "syncActiveNodes", "true")
		logMessage ("The following nodes have been synchronized:\n" + str(nodes)+ "\n")
	else:
		logMessage ("Standalone server, no nodes to sync")
		
def getAppDeployedNodes ():	
	deployedNodeList = AdminApplication.getAppDeployedNodes(applicationName)
	print  applicationName + " is deployed on " + str( deployedNodeList)

def exportEar (applicationName) :
	timeStamp = strftime("%Y.%m.%d_%H.%M.%S", localtime())
	exportFile = "/tmp/" + applicationName + "_" + timeStamp + ".ear"
	logMessage("Export current ear to: " + earFilePath)
	AdminApplication.exportAnAppToFile(applicationName, exportFile)
	logMessage("Export of " + applicationName + " has completed ...")

def updateApplicationWithUpdateIgnoreNewOption (applicationName, earFilePath) :
	AdminApplication.updateApplicationWithUpdateIgnoreNewOption(applicationName, earFilePath)
	logMessage  ("Update has completed ...")
	AdminConfig.save()
	logMessage ("Save has completed ...")	

def getCell ():
	return AdminControl.getCell()
		
def getServers ():
	return AdminTask.listServers('[-serverType APPLICATION_SERVER ]')
	
def getNodes ():
	return AdminTask.listNodes()
	
def getApplications ():
	return AdminApp.list()

print sys.argv[1:]
earFilePath = sys.argv[0]
print "earFilePath: " + earFilePath
applicationName = sys.argv[1]

logMessage  ("Starting wsadmin script ...")

exportEar ( applicationName)
updateApplicationWithUpdateIgnoreNewOption(applicationName, earFilePath)
syncActiveNodes()
# startCount = stopStartApplication (applicationName)
appReady = isAppReady(applicationName)
logMessage (' appReady: ' + appReady)
# rippleStartCluster ('ClusterOZ')
