/*
 *      File Name     : BasicCICSRequestExit.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This sample shows an example of how to use the
 *                      CICSRequestExit interface to re-map the CICS
 *                      server name specified on a request to the name
 *                      of a CICS region accessible by the CICS Transaction
 *                      Gateway.
 *                      
 *      Pre-Requisites: Use a version of the JDK that the CICS Transaction
 *                      Gateway supports if you recompile this sample. See
 *                      the product documentation for supported Java levels.
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
 *      The following code is sample code created by IBM Corporation.  This
 *      sample code is not part of any standard IBM product and is provided
 *      to you solely for the purpose of assisting you in the development of
 *      your applications.  The code is provided 'AS IS', without warranty
 *      or condition of any kind.  IBM shall not be liable for any damages
 *      arising out of your use of the sample code, even if IBM has been
 *      advised of the possibility of such damages.
 */
package com.ibm.ctg.samples.ha;

import java.util.Map;
import java.util.Hashtable;

import com.ibm.ctg.ha.CICSRequestExit;
import com.ibm.ctg.ha.ExitEvent;
import com.ibm.ctg.ha.ExitEventData;
import com.ibm.ctg.ha.RequestDetails;

/**
 * The BasicCICSRequestExit looks at the CICS Server name that is passed
 * in as part of the data HashMap and either returns the CICS server that
 * the original name is mapped to, or the passed in server name if no
 * mapping is defined.
 */
public class BasicCICSRequestExit implements CICSRequestExit{
   
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
      
   /**
    * Mapping table between a logical CICS server name and an actual
    * CICS server.
    */
   private Hashtable<String, String> serverMappings;
   
   /**
    * Constant to specify how many times the Gateway should retry the
    * request. 
    */
   private final int retryCount = 0;
   
   /**
    * Default constructor which creates and populates the mapping table. 
    */
   public BasicCICSRequestExit(){
      serverMappings = new Hashtable<String, String>();
      serverMappings.put("LSERVER1", "CICS1");
      serverMappings.put("LSERVER2", "CICS2");
   }
   
   /**
    * Main entry point for the exit which alters the CICS server name. 
    * @see com.ibm.ctg.ha.CICSRequestExit#getCICSServer(java.util.HashMap)
    */
   public String getCICSServer(Map<RequestDetails, Object> data) {
      //Retrieve the CICS server name specified on the request
      String logicalServer = (String)data.get(RequestDetails.Server);
      String realServer = null;
      //If the request had a CICS server name specified
      if(logicalServer != null){
         //See if we have a remapping definition and set the return value 
         realServer = serverMappings.get(logicalServer);
         //If there is no defined mapping then return the original Server name
         if(realServer == null){
            realServer = logicalServer;
         }
      }
      return realServer;
   }
   
   /**
    * Indicate how many times the request should be called to retry a
    * request.
    * @see com.ibm.ctg.ha.CICSRequestExit#getRetryCount()
    */
   public int getRetryCount(){
      return retryCount;
   }
   
   /**
    * Clean-up any resources as the Gateway daemon shuts down.
    * @see com.ibm.ctg.ha.CICSRequestExit#shutdown()
    */
   public void shutdown(){
      return;
   }

   /**
    * Process any events sent to this exit.
    * @see com.ibm.ctg.ha.CICSRequestExit#eventFired(com.ibm.ctg.ha.ExitEvent, java.util.Map)
    */
   public void eventFired(ExitEvent event, Map<ExitEventData, Object> data) {
      switch(event){
      case ShutDown:
         //call the shutdown method
         shutdown();
         break;
      case Command:
         System.out.println("Exit received command : " + 
               data.get(ExitEventData.CommandData));
         break;
      default:
         System.out.println("Exit received unknown ExitEvent : " + event);
      }
   }

}
