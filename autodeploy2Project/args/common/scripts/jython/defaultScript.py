from sys import argv
import  os, os.path
from time import localtime, strftime
import time
import java.lang.System as javaSystem

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
	
def logMessage (message):
        print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + message
        return
		
def viewBuildVersion (applicationName) :
		print AdminApp.view(applicationName, '-buildVersion')
				
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
                        logMessage (startOperation + ' operation succeeded for ' + appManager1 + startReturnCode)
                        startCount += 1
                except:
                #       print startOperation + ' operation failed for ' + appManager1 + startReturnCode
                        pass
        print  applicationName + ' has been successfully started on ' + str(startCount) + ' servers'
        return startCount;

def rippleStartCluster ( cluster2Reastart ) :
		searchString = 'type=Cluster,name=' + cluster2Reastart + ',*'
		clusterObjName = AdminControl.completeObjectName(searchString)
		logMessage ('clusterObjName: ' + clusterObjName)
		if (clusterObjName):
			AdminControl.invoke(clusterObjName, 'rippleStart')
			logMessage ('rippleStart invokation has completed for ' + cluster2Reastart + ' ...')
		else:
			logMessage ('Error: Cluster ' + cluster2Reastart + ' not found. rippleStart will not be performed !!')

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
	logMessage (applicationName + " is deployed on " + str( deployedNodeList))

def exportEar (applicationName) :
    timeStamp = strftime("%Y.%m.%d_%H.%M.%S", localtime())
    exportFolder = os.path.join ( '/tmp', 'export')
    if not os.path.exists ( exportFolder ):
           os.makedirs( exportFolder )
    exportFile =  os.path.join(exportFolder , applicationName + "_" + timeStamp + ".ear")
    logMessage("Export current ear to: " + exportFile)
    AdminApplication.exportAnAppToFile(applicationName, exportFile)
    logMessage("Export of " + applicationName + " to " + exportFile + " has completed ...")

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
    
def updateApplication (earFilePath, applicationName):
	logMessage  ("Starting updateApplication script. earFilePath: " + earFilePath + " applicationName: " + applicationName + " ...")

	exportEar ( applicationName)
	
	updateApplicationWithUpdateIgnoreNewOption(applicationName, earFilePath)
	applicationNamePrefix = applicationName[:9]
	applicationNameSuffix = applicationName[9:]
	applicationEnv = applicationNameSuffix[:1]
	nonProdEnvs = ['T', 'Q', 'V', 'K']
	if applicationNamePrefix == 'MatafEAR_' and applicationEnv in  nonProdEnvs:
		editSnifitApp (applicationName, applicationNameSuffix, applicationEnv)
 
	syncActiveNodes()
# startCount = stopStartApplication (applicationName)
	appReady = isAppReady(applicationName)
	logMessage (' appReady: ' + appReady)

def editSnifitApp (applicationName, applicationNameSuffix, applicationEnv):
	logMessage  ("Starting editSnifitApp ...")
		
	editParams = ['[ -CtxRootForWebMod [[ MatafServer MatafServer.war,WEB-INF/web.xml MatafServer_' + applicationNameSuffix +' ]]]']
	#	editParams.append ( '[ -CtxRootForWebMod [[ BTTRuntimeMonitor BTTRuntimeMonitor.war,WEB-INF/web.xml BTTRuntimeMonitor_' + applicationNameSuffix + ' ]]]' )

	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml log4j java.net.URL url/log4j_' + applicationNameSuffix + ' "" "" "" ]]]' )

	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml adroot java.net.URL url/adroot_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml kyc java.net.URL url/kyc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml mamadim java.net.URL url/mamadim_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml shlifatmismach java.net.URL url/shlifatmismach_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml klitatmismach java.net.URL url/klitatmismach_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml srikatmismach java.net.URL url/srikatmismach_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml agilepointWorkflow java.net.URL url/agilepointWorkflow_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml getcheque java.net.URL url/getcheque_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml updatecheque java.net.URL url/updatecheque_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml insertcheque java.net.URL url/insertcheque_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml copycheque java.net.URL url/copycheque_' + applicationEnv + ' "" "" "" ]]]' )
		
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml collregsavedoc java.net.URL url/collregsavedoc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml collreggetdoc java.net.URL url/collreggetdoc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml galashinsertdoc java.net.URL url/galashinsertdoc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml galashdeletedoc java.net.URL url/galashdeletedoc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml galashgetdoclist java.net.URL url/galashgetdoclist_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml galashgetdoc java.net.URL url/galashgetdoc_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml view360 java.net.URL url/view360_' + applicationEnv + ' "" "" "" ]]]' )
	
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml gm javax.jms.Queue jms/gm_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml log javax.jms.Queue jms/log_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml or javax.jms.Queue jms/or_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml ph javax.jms.Queue jms/ph_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml p8req javax.jms.Queue jms/p8req_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brreq javax.jms.Queue jms/brreq_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brreq2 javax.jms.Queue jms/brreq2_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brres javax.jms.Queue jms/brres_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brres2 javax.jms.Queue jms/brres2_' + applicationEnv + ' "" "" "" ]]]' )
	
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cnreq javax.jms.Queue jms/cnreq_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cnreq2 javax.jms.Queue jms/cnreq2_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cnres javax.jms.Queue jms/cnres_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cnres2 javax.jms.Queue jms/cnres2_' + applicationEnv + ' "" "" "" ]]]' )

	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml pool javax.resource.cci.ConnectionFactory eis/pool_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml pool2 javax.resource.cci.ConnectionFactory eis/pool2_' + applicationEnv + ' "" "" "" ]]]' )

	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml gmcf javax.jms.QueueConnectionFactory jms/gmcf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml p8cf javax.jms.QueueConnectionFactory jms/p8cf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml smcf javax.jms.QueueConnectionFactory jms/smcf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml phcf javax.jms.QueueConnectionFactory jms/phcf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brcf javax.jms.QueueConnectionFactory jms/brcf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml brcf2 javax.jms.QueueConnectionFactory jms/brcf2_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cncf javax.jms.QueueConnectionFactory jms/cncf_' + applicationEnv + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml cncf2 javax.jms.QueueConnectionFactory jms/cncf2_' + applicationEnv + ' "" "" "" ]]]' )

	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml matafdb javax.sql.DataSource jdbc/matafdb_' + applicationNameSuffix + ' "" "" "" ]]]' )
	editParams.append ( '[  -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml commondb javax.sql.DataSource jdbc/commondb_' + applicationEnv + ' "" "" "" ]]]' )
	
	editParams.append ( '[ -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml protocache com.ibm.websphere.cache.DistributedMap services/cache/protocache_'  + applicationEnv + ' "" "" "" ]]]'  )
	editParams.append ( '[ -MapResRefToEJB [[ MatafServer "" MatafServer.war,WEB-INF/web.xml adcache com.ibm.websphere.cache.DistributedMap services/cache/adcache_'  + applicationEnv + ' "" "" "" ]]]'  )
	
	for editParam1 in editParams:
		print editParam1
		AdminApp.edit(applicationName, editParam1 )
		AdminConfig.save()	
	return		

action = sys.argv[0]
if action == 'UPDATEAPPLICATION':
	earFilePath = sys.argv[1]
	applicationName = sys.argv[2]
	updateApplication(earFilePath, applicationName)
	viewBuildVersion (applicationName)
    
elif action == 'RIPPLESTARTCLUSTER':
	cluster = sys.argv[1]
	rippleStartCluster (cluster)
	
elif action == 'EDITSNIFITAPP':
	applicationName = sys.argv[1]
	editSnifitApp (applicationName)	

else:
	logMessage ('all done ')