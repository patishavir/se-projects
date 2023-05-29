print "\ncell:\n" + AdminControl.getCell()
print "\nservers:\n" + AdminTask.listServers('[-serverType APPLICATION_SERVER ]')
print "\nnodes:\n" + AdminTask.listNodes()
print "\napplications:\n" + AdminApp.list()
