/*
 *      File Name     : WorkElement.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This class is part of the ThreadedMonitor sample. It
 *                      encapsulates pending request data items.
 *
 *      Pre-Requisites: Use a version of the JDK that the CICS Transaction
 *                      Gateway supports if you recompile this sample. See
 *                      the product documentation for supported Java levels.
 *
 * Licensed Materials - Property of IBM  
 *  
 * 5724-I81,5725-B65,5655-Y20 
 *  
 * (C) Copyright IBM Corp. 2002, 2014 All Rights Reserved.  
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

package com.ibm.ctg.samples.requestexit.threadedmonitor;

import java.util.Map;

import com.ibm.ctg.monitoring.RequestData;
import com.ibm.ctg.monitoring.RequestEvent;

public class WorkElement {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   private RequestEvent requestEvent;

   private Map<RequestData, Object> requestDataMap;

   private byte[] requestCorrelator;

   // This constructor initialises an entire work element
   public WorkElement(byte[] userCorrelator,
         RequestEvent event, Map<RequestData, Object> data) {

      requestCorrelator = userCorrelator;
      requestEvent = event;
      requestDataMap = data;
   }

   public byte[] getRequestCorrelator() {
      return requestCorrelator;
   }

   public Map<RequestData, Object> getRequestDataMap() {
      return requestDataMap;
   }

   public RequestEvent getRequestEvent() {
      return requestEvent;
   }

   /**
    * Requests are considered equal if they have the
    * same CtgCorrelator and RequestReceived values.
    *  
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object o) {
      boolean isEqual = false;
      if (o != null){
         if (o instanceof WorkElement) {
            WorkElement workElement = (WorkElement) o;
            long requestSent = getRequestReceivedTime();
            if (requestSent == workElement.getRequestReceivedTime()){
               int correlator = getCorrelator();
               isEqual = (correlator == workElement.getCorrelator());
            }
         }
      }
      return isEqual;
   }

   /**
    * @return correlator for this request
    */
   private int getCorrelator() {
      return (Integer) getRequestDataMap()
                       .get(RequestData.CtgCorrelator);
   }

   /**
    * @return timestamp for when the request was sent
    */
   private long getRequestReceivedTime() {
      return (Long) getRequestDataMap()
                    .get(RequestData.RequestReceived);
   }

   /**
    * Requests are considered equal if they have the
    * same CtgCorrelator and RequestReceived values.
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      return (int) (getCorrelator() + getRequestReceivedTime());
   }

}
