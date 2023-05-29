/*
*         File Name      : CtgStat1.java
*
*         Product        : CICS Transaction Gateway
*
*         Description    : This sample shows the basic use of the CICS
*                          statistics API. It demonstrates calls to 
*                          connect to a Gateway daemon, obtain id result sets,
*                          generate query strings from id result sets and
*                          obtain stat result sets.
*
*         Pre-Requisites : Use a version of the JDK that the CICS Transaction
*                          Gateway supports if you recompile this sample. See
*                          the product documentation for supported Java levels.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5725-B65,5655-Y20 
*  
* (C) Copyright IBM Corp. 2008, 2014 All Rights Reserved.  
*  
* US Government Users Restricted Rights - Use, duplication or  
* disclosure restricted by GSA ADP Schedule Contract with  
* IBM Corp.  
* 
* Status: Version 9 Release 1 
*
*         The following code is sample code created by IBM Corporation.  This
*         sample code is not part of any standard IBM product and is provided
*         to you solely for the purpose of assisting you in the development of
*         your applications.  The code is provided 'AS IS', without warranty
*         or condition of any kind.  IBM shall not be liable for any damages
*         arising out of your use of the sample code, even if IBM has been
*         advised of the possibility of such damages.
*/

package com.ibm.ctg.samples.stats;

import com.ibm.ctg.client.stats.GatewayConnection;
import com.ibm.ctg.client.stats.StatData;
import com.ibm.ctg.client.stats.StatResultSet;

public class Ctgstat1 {
 
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2008, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
	
    public static void main(String[] args) {	
    	
    	GatewayConnection gateway = null;
    	
    	int port = 2980;
    	String host = "localhost";
    	
    	StatResultSet resultSet = null;
    	
    	int uptime = 0;
    	
    	String ids = "";
    	
    	// Display banner
        System.out.println("CICS Transaction Gateway Statistics Sample\n");
        System.out.println("Usage: java com.ibm.ctg.samples.stats.Ctgstat1 [host] [port]\n");

        // Process the command line arguments and display Gateway daemon settings
        switch (args.length) {
        case 2:
        	try{
        		host = args[0];
        	} catch (Exception e){
        		System.out.println("Error: Invalid host specified.\n");        		
        	}
        	try{
        		port = Integer.parseInt(args[1]);
        	} catch (Exception e){
        		System.out.println("Error: Invalid port specified, defaulting to 2980.\n");        		
        	}
        	break;
        case 1:
        	try{
        		port = Integer.parseInt(args[0]);
        	} catch (Exception e){
        		System.out.println("Error: Invalid port specified, defaulting to 2980.\n");        		
        	}
        	break;
        case 0:
           break;
        default:
           System.exit(0);
        }
        System.out.print("The port of the Gateway daemon has been set to "+ port + ".\n");
        
        // Connect to the Gateway daemon
        try {
        	gateway = new GatewayConnection(host, port);
        } catch (Exception e) {
        	System.out.println("Error: Failed to connect to Gateway daemon on port "+port+".\n");
        	System.exit(0);
        }
        
        System.out.println("Successfully connected to Gateway daemon on port "+port+".\n");
        
        String statsRc = gateway.getStatsAPIVersion();
        System.out.println("API Version: "+statsRc);      
        
        /*Get the values of all the statistics in the CM resource group*/
        ids = "CM";
        
        // Get IDs for CM and then generate a query string from that list
        try {
        	resultSet = gateway.getStats(ids);
        } catch (Exception e) {
        	System.out.println("Error: Failed to collect result set" + ids + ".\n");
        	System.exit(0);
        }
    	
    	/* Iterate over the result set and display the statistics */
        System.out.println("Statistics values: \n");
        for (StatData stat : resultSet){
        	if (stat.getRc() == StatData.SUCCESSFUL_QUERY) {
        		System.out.println(stat.getStatId() + "=" + stat.getValue() + " (" + stat.getShortDesc()+")");
        	} else {
        		System.out.println("Query for "+stat.getQueryElement()+" failed with "+stat.getReturnString(stat.getRc()));
        	}
        }
        System.out.println("End of results.\n");
        
        /* Get the Gateway running time statistic */
        try {
        	resultSet = gateway.getStats("GD_LRUNTIME");
        } catch (Exception e) {
        	System.out.println("Error: Failed to collect resultSet for GD_RUNTIME.\n");
        	System.exit(0);
        }
        
        for (StatData stat : resultSet){
        	if (stat.getRc() == StatData.SUCCESSFUL_QUERY) {
        	    uptime = Integer.parseInt(stat.getValue());
        		int seconds = uptime % 60;
        		int minutes = uptime / 60;
        		int hours = minutes / 60;
        		minutes = minutes % 60;
        		System.out.println("Gateway uptime = "+hours+" hrs "+minutes+" mins "+seconds+" secs.\n"); 

        	} else {
        		System.out.println("Query for "+stat.getQueryElement()+" failed with "+stat.getReturnString(stat.getRc()));
        	}
        }
        
        /* Get the all requests statistic */
        try {
        	resultSet = gateway.getStats("GD_LALLREQ");
        } catch (Exception e) {
        	System.out.println("Error: Failed to collect resultSet for GD_LALLREQ.\n");
        	System.exit(0);
        }
        
        for (StatData stat : resultSet){
        	if (stat.getRc() == StatData.SUCCESSFUL_QUERY) {
        		int numberOfRequests = Integer.parseInt(stat.getValue());
        		System.out.println("The Gateway daemon has processed "+(long)numberOfRequests/uptime+" requests/sec.\n"); 

        	} else {
        		System.out.println("Query for "+stat.getQueryElement()+" failed with "+stat.getReturnString(stat.getRc())+".");
        	}
        }
        
        /* Close the connection to the Gateway */
        gateway.close();
        
        System.out.println("Ctgstat1 completed successfully.\n"); 	
    }
}
