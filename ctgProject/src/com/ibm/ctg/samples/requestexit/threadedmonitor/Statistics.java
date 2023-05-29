/*
 *      File Name     : Statistics.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This class is part of the ThreadedMonitor sample. It
 *                      is used by the QueueMonitor class to track the number
 *                      of inflight and completed requests. The statistics are
 *                      output to the "out" file or stdout periodically.
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ibm.ctg.samples.requestexit.ThreadedMonitor;

public class Statistics {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /*
    * Interval between stats logging
    */
   private long intervalTimeout;

   // Internal Counters to track requests going in and out.
   private long inboundCount = 0;

   private long outboundCount = 0;
   
   private long detailsCount = 0;

   private long inboundMarker = 0;

   private long outboundMarker = 0;
   
   private long detailsMarker = 0;

   private Date intervalTimeCheck = new Date();

   private int curDay = 0;

   /**
    * The QueueMonitor that created us and provides our output
    * methods.
    */
   private QueueMonitor queueMonitor;

   /**
    * Initialise the Statistics object to keep a reference to the
    * parent QueueMonitor that provides output methods.
    * 
    * @param monitor
    */
   public Statistics(QueueMonitor monitor, long intervalTimeout) {
      queueMonitor = monitor;
      this.intervalTimeout = intervalTimeout;
   }

   public boolean isStatsTime() {
      long intervalTime = new Date().getTime() - intervalTimeCheck.getTime();
      return (intervalTime >= intervalTimeout);
   }

   /*
    * This part of the thread is responsible for the output of the regular
    * statistics. This is done periodically, and not every time through the
    * loop
    */
   public String outputStats() {
      StringBuffer output = new StringBuffer();
      long curInTPM = 0;
      long curOutTPM = 0;
      long curDetailsTPM = 0;
      long curInflight = 0;

      /*
       * Calculate the various stats. A marker is used to keep track of the
       * global counts as last processed so they never need to be reset and
       * can be used for cumulative data
       */
      curInTPM = inboundCount - inboundMarker;
      inboundMarker = inboundCount;
      curDetailsTPM = detailsCount - detailsMarker;
      detailsMarker = detailsCount;
      curOutTPM = outboundCount - outboundMarker;
      outboundMarker = outboundCount;
      curInflight = inboundCount - outboundCount;

      /*
       * It may be possible for this to go negative if close requests are
       * sent from Gateway, resulting in more complete requests than sent. In
       * this case we can't know the number of inflight requests so we'll set
       * it to 0
       */
      if (curInflight < 0) {
         curInflight = 0;
      }

      /*
       * Create a current timestamp, then log the stats data to stdout
       */
      output.append(ThreadedMonitor.getCurrentTimeStamp());
      output.append(" [In: ");
      output.append(curInTPM);
      output.append("] [Details: ");
      output.append(curDetailsTPM);
      output.append("] [Out: ");
      output.append(curOutTPM);
      output.append("] [Inflight: ");
      output.append(curInflight);
      output.append("] [Total: ");
      output.append(outboundCount);
      output.append("]");
      intervalTimeCheck = new Date();
      Calendar timeCheck = new GregorianCalendar();
      timeCheck.setTime(intervalTimeCheck);

      /*
       * According to Java documentation, the last minute of the day is
       * 23:59, so if we are in that minute, reset the counters for a 
       * new day
       */
      if (curDay == 0) {
         curDay = timeCheck.get(Calendar.DAY_OF_YEAR);
      }
      if (timeCheck.get(Calendar.DAY_OF_YEAR) != curDay) {

         output.append("\n");
         output.append(ThreadedMonitor.getCurrentTimeStamp());
         output.append(" End of Day - resetting counts");

         // Reset all the counters
         inboundCount = 0;
         detailsCount = 0;
         outboundCount = 0;
         inboundMarker = 0;
         detailsMarker = 0;
         outboundMarker = 0;
         curInTPM = 0;
         curDetailsTPM = 0;
         curOutTPM = 0;
         curInflight = 0;

         // Empty the table of stored requests
         queueMonitor.resetRequests();

         // reset the current day to the new one
         curDay = timeCheck.get(Calendar.DAY_OF_YEAR);
      }
      return output.toString();
   }

   public void incrementInboundCount() {
      inboundCount++;
   }

   public void incrementDetailsCount() {
      detailsCount++;
   }

   public void incrementOutboundCount() {
      outboundCount++;
   }
}
