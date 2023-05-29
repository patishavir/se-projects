/*
*         File Name      : BasicPlugin.java
*
*         Product        : CICS Transaction Gateway
*
*         Description    : This sample shows how to implement a Gateway  
*                          intercept plug-in for use with CICS Transaction 
*                          Gateway. The sample plug-in is designed to be used 
*                          with the EciB1 Java sample included with CICS 
*                          Transaction Gateway.   
*  
*         Pre-requisites : Use a version of the JDK that the CICS Transaction 
*                          Gateway supports if you compile this sample. See   
*                          the product documentation for supported Java levels.                               
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5725-B65,5655-Y20 
*  
* (C) Copyright IBM Corp. 2012, 2014 All Rights Reserved.  
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


package com.ibm.ctg.samples.intercept;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.GatewayIntercept;
import com.ibm.ctg.client.GatewayRequest;
import com.ibm.ctg.client.JavaGateway;

/**
 * Sample {@link GatewayIntercept} plug-in that can be used with the {@code EciB1}
 * Java sample included with CICS Transaction Gateway.
 */
public class BasicPlugin implements GatewayIntercept {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2012, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
   
   /**
    * Intercepts the {@link JavaGateway#open()} method. This plug-in implementation
    * prevents the {@code open} call from continuing.
    * 
    * @param gateway the JavaGateway being opened
    * @return {@link InterceptAction#Return}
    * @throws IOException if an error occurs
    */
   public InterceptAction interceptOpen(JavaGateway gateway) throws IOException {
      return InterceptAction.Return;
   }

   /**
    * Intercepts the {@link JavaGateway#flow(GatewayRequest)} method. This plug-in
    * implementation prevents the {@code flow} call from continuing, and modifies
    * the request to simulate it being sent to a Gateway daemon.
    * 
    * @param gateway the JavaGateway on which {@code flow} was called
    * @param request the request being flowed
    * @return {@link InterceptAction#Return}
    * @throws IOException if an error occurs
    */
   public InterceptAction interceptFlow(JavaGateway gateway, GatewayRequest request) throws IOException {
      
      // This plug-in is only designed to handle ECI requests
      if (!(request instanceof ECIRequest)) {
         throw new IOException("Unexpected request type: " + request.getClass().getName());
      }
      
      ECIRequest eciReq = (ECIRequest)request;
      
      switch (eciReq.getCallType()) {
      case ECIRequest.CICS_EciListSystems:
         // EciB1 performs a listSystems call first, return a single server
         eciReq.SystemList = new Vector<String>(2);
         eciReq.SystemList.add("TESTSRV");
         eciReq.SystemList.add("Test server returned by the BasicPlugin");
         eciReq.numServersReturned = 1;
         eciReq.numServersKnown = 1;
         break;
         
      case ECIRequest.ECI_SYNC:
         
         if ((eciReq.getExtendMode() == ECIRequest.ECI_NO_EXTEND) ||
               (eciReq.getExtendMode() == ECIRequest.ECI_EXTENDED)) {
            // EciB1 calls the EC01 CICS program with a COMMAREA of at least 18
            // bytes to get the date and time
            
            // Check the Commarea and Commarea_Length fields are consistent
            int commareaLen = eciReq.Commarea_Length;
            if ((commareaLen == 0) && (eciReq.Commarea != null)) {
               commareaLen = eciReq.Commarea.length;
            } else if ((eciReq.Commarea == null) || (eciReq.Commarea.length < commareaLen)) {
               eciReq.Commarea = new byte[commareaLen];
            }
            eciReq.Commarea_Length = commareaLen;
            
            // Validate the request
            if ((eciReq.Program == null) || !eciReq.Program.equals("EC01")) {
               throw new IOException("Unexpected CICS program: " + eciReq.Program);
            }
            
            // Return the current date and time in the COMMAREA
            SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String dateStr = dateFmt.format(new Date());
            byte[] dateBytes = dateStr.getBytes();
            if (commareaLen < dateBytes.length) {
               throw new IOException("Commarea too small: " + commareaLen);
            }
            System.arraycopy(dateBytes, 0, eciReq.Commarea, 0, dateBytes.length);
         }
         break;
         
      default:
         // Unexpected call type
         throw new IOException("Unexpected request type: " + eciReq.getCallTypeString());
      }
      
      return InterceptAction.Return;
   }

   /**
    * Intercepts the {@link JavaGateway#close()} method. This plug-in
    * implementation prevents the {@code close} call from continuing.
    * 
    * @param gateway the JavaGateway being closed
    * @return {@link InterceptAction#Return}
    * @throws IOException if an error occurs
    */
   public InterceptAction interceptClose(JavaGateway gateway) throws IOException {
      return InterceptAction.Return;
   }

}
