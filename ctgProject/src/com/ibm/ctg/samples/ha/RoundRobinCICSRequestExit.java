/*
 *      File Name     : RoundRobinCICSRequestExit.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This sample shows an example of how to use the
 *                      CICSRequestExit interface to send work to a number
 *                      of CICS servers in a round robin fashion.
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

import com.ibm.ctg.ha.CICSRequestExit;
import com.ibm.ctg.ha.ExitEvent;
import com.ibm.ctg.ha.ExitEventData;
import com.ibm.ctg.ha.RequestDetails;

/**
 * The RoundRobinCICSRequestExit holds a list of CICS servers and returns
 * the next in the list (looping once the end is reached) every time the
 * exit is called. A lock is acquired on each call to ensure that only
 * one thread moves the counter along the list at any one time. 
 */
public class RoundRobinCICSRequestExit implements CICSRequestExit {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2008, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
   
   /**
    * The list of CICS servers that requests should be sent to.
    */
   private String[] serverList = {"SERVER1",
                                  "SERVER2",
                                  "SERVER3",
                                  "SERVER4"};
   
   /**
    * Stateful server index to keep track of where in the list the next 
    * server to use is. 
    */
   private int serverIndex = 0;
   /**
    * Lock object to serialize access to the server list.
    */
   private Object lock = new Object();

   /**
    * Main entry point for the exit which alters the CICS server name. 
    * @see com.ibm.ctg.ha.CICSRequestExit#getCICSServer(java.util.HashMap)
    */
   public String getCICSServer(Map<RequestDetails, Object> data) {
      //Acquire the lock to enable accessing the list
      synchronized(lock){
         //Get the CICS server name this request should be sent to 
         String server = serverList[serverIndex];
         //increment the position in the list
         serverIndex = (serverIndex + 1) % serverList.length;
         return server;
      }
   }
   
   /**
    * Indicate how many times the request should be called to retry a
    * request.
    * @see com.ibm.ctg.ha.CICSRequestExit#getRetryCount()
    */
   public int getRetryCount(){
      //Retry enough times that each server could be used.
      return serverList.length - 1;
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
