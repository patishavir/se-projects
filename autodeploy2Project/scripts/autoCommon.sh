hostname=$(hostname)
export sendMail=yes
export autoDeploySystem=myTestPortal
if [[ $hostname = 'suswastest1' ]]; then
        export autoDeploySystem=myTestPortal
elif [[ $hostname = 'suswastest2' ]]; then
        export autoDeploySystem=suswastest2
elif [[ $hostname = 'supportl-idmgr' ]]; then
        export autoDeploySystem=internalProdPortal
        export sendMail=no
elif [[ $hostname = 'supportl-stg' ]]; then
        export autoDeploySystem=externalProdPortal
        export sendMail=yes
elif [[ $hostname = 'WPSQ-DMGR' ]]; then
        export autoDeploySystem=PortalQ
        export sendMail=yes
fi

logsFolder=$scriptDir/logs
if [[ ! -d $logsFolder ]] ; then
        mkdir $logsFolder
fi
export logFilePath=$logsFolder/${operation}_${hostname}.$timeStamp.err
mainClass=oz.utils.autodeploy.AutoDeployMain


