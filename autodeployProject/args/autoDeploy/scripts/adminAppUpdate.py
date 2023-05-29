import time
from time import gmtime, localtime, strftime
print strftime("%d/%m/%Y %H:%M:%S ", localtime())

print "Starting wsadmin script ..."
targetCell=AdminControl.getCell()
updateParams= '[  -operation update -contents /snifit/tmp/autoDeploy7/MatafEAR_T_OZ.2014.11.25_18.22.27_T_OZ.ear  -nopreCompileJSPs -installed.ear.destination $(APP_INSTALL_ROOT)/' + targetCell + ' -distributeApp -nouseMetaDataFromBinary -nodeployejb -createMBeansForResources -noreloadEnabled -nodeployws -validateinstall warn -processEmbeddedConfig  -filepermission .*\.dll=755#.*\.so=755#.*\.a=755#.*\.sl=755 -noallowDispatchRemoteInclude  -noallowServiceRemoteInclude -MapWebModToVH [[ MatafServer MatafServer.war,WEB-INF/web.xml default_host ][ BTTRuntimeMonitor BTTRuntimeMonitor.war,WEB-INF/web.xml default_host ]] -MapRolesToUsers [[ snifituser AppDeploymentOption.No AppDeploymentOption.Yes "" "" AppDeploymentOption.No "" "" ]]]'
# print 'updateParams: ' + updateParams
AdminApp.update('MatafEAR_T_OZ', 'app', updateParams)
print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + "Update has completed ..."
AdminConfig.save()
print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + "Save has completed ..."

for node in [ 'S603FB00Node03' , 'S5380369Node03'] :	
	SyncName = AdminControl.completeObjectName('type=NodeSync,node=' + node +',*')
	print SyncName
	SyncResult = AdminControl.invoke(SyncName, 'sync')
	print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + "Sync has completed ... " + SyncName + " sync result: " + SyncResult
	time.sleep(5)

cluster = AdminControl.completeObjectName('type=Cluster,name=ClusterOZ,*')
print 'cluster: ' + cluster

AdminControl.invoke(cluster, 'rippleStart')
print strftime("%d/%m/%Y %H:%M:%S ", localtime()) + "rippleStart has completed ... " + cluster

loopCount = 20
sleepInterval = 12
i = 0
while i < loopCount :
        i+=1
        time.sleep(sleepInterval)
        clusterState=AdminControl.getAttribute(cluster, 'state')
        print str(i) +  ' clusterState: ' + clusterState
        if clusterState == 'websphere.cluster.running':
                break
                
loopCount = 20
sleepInterval = 12
i = 0
while i < loopCount :
        i+=1
        time.sleep(sleepInterval)
        appReady = AdminApp.isAppReady('MatafEAR_T_OZ')
        print str(i) +  ' appReady: ' + appReady
        if appReady == 'true':
                break                