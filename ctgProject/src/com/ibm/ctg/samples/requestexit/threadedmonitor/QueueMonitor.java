/*
 *      File Name     : QueueMonitor.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This class is part of the ThreadedMonitor sample. It
 *                      creates a Queue which the ThreadedMonitor uses to add
 *                      WorkElement objects to when events occur. A background
 *                      thread reads objects from the Queue and processes them,
 *                      outputting request data to the "out" file or stdout
 *                      - instructions on setting the "out" and "err" locations
 *                      can be found in the ThreadedMonitor documentation.
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.ibm.ctg.monitoring.FlowType;
import com.ibm.ctg.monitoring.RequestData;
import com.ibm.ctg.monitoring.RequestEvent;
import com.ibm.ctg.monitoring.RequestType;
import com.ibm.ctg.samples.requestexit.BasicMonitor;
import com.ibm.ctg.samples.requestexit.ThreadedMonitor;

public class QueueMonitor implements Runnable {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /*
    * Specify the various timeouts which will be used. 
    * All times are in milliseconds.
    */

   /*
    * Long Running Transaction Timeout
    */
   private long lrtTimeout;

   /*
    * Interval between running reaper processing
    */
   private long reaperTimeout;

   /*
    * Interval after which requests are reaped
    */
   private long orphanTimeout;

   /**
    * Keep a track of last time we were called.
    */
   private long reaperTimeCheck = System.currentTimeMillis();

   /*
    * We need some local storage for various checks and stats
    * Stores the RequestEntry with an ArrayList of the RequestDetails
    * received.
    */
   private HashMap<WorkElement, ArrayList<WorkElement>> storedRequests = new HashMap<WorkElement, ArrayList<WorkElement>>();

   /**
    * The ThreadedMonitor that created us and provides our output
    * methods.
    */
   private ThreadedMonitor threadedMonitor;

   private Statistics stats;

   private Queue queue;
   
   private boolean shuttingDown = false;

   /**
    * This QueueMonitor is related to a ThreadedMonitor.
    * 
    * @param monitor to remember
    * @param longRunningTransactionTimeout
    */
   public QueueMonitor(ThreadedMonitor monitor, 
         int queueSize, long longRunningTransactionTimeout, 
         long reaperTimeout, long orphanTimeout,
         long intervalTimeout) {
      threadedMonitor = monitor;
      this.reaperTimeout = reaperTimeout;
      this.orphanTimeout = orphanTimeout;
      stats = new Statistics(this, intervalTimeout);
      queue = new Queue(this, queueSize);
      lrtTimeout = longRunningTransactionTimeout;
      StringBuffer message = new StringBuffer(256);
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
      message.append("Created QueueMonitor with:"); 
      message.append("\n   "); 
      message.append(String.format("queue size of %,d,", queueSize));
      message.append("\n   "); 
      calendar.setTimeInMillis(lrtTimeout);
      message.append(String.format("LRT timeout of %1$tH:%1$tM:%1$tS.%1$tL,", 
            calendar));
      message.append("\n   "); 
      calendar.setTimeInMillis(this.reaperTimeout);
      message.append(String.format("reaper timeout of %1$tH:%1$tM:%1$tS.%1$tL,", 
            calendar));
      message.append("\n   "); 
      calendar.setTimeInMillis(this.orphanTimeout);
      message.append(String.format("orphan request of %1$tH:%1$tM:%1$tS.%1$tL and", 
            calendar));
      message.append("\n   ");
      calendar.setTimeInMillis(intervalTimeout);
      message.append(String.format("statistics interval of %1$tH:%1$tM:%1$tS.%1$tL", 
            calendar));
      println(message.toString());
   }

   /*
    * The run() method implements the main QueueMonitor thread. 
    * This does all the work so the threads passing through the 
    * ThreadedMonitor exit don't need to. The idea is to
    * preserve performance of requests as much as possible.
    * 
    * In flight requests will be stored in a HashMap - this will be 
    * 'reaped' periodically to prevent excessive growth caused by lost 
    * responses.
    */
   public void run() {
      try {
         boolean keepGoing = true;
         while (keepGoing) {
            if (!queue.queueIsEmpty()) {
               String time = ThreadedMonitor.getCurrentTimeStamp();
               WorkElement workRequest = queue.removeFromQueue();
               RequestEvent event = workRequest.getRequestEvent();
               Map<RequestData, Object> data = 
                  workRequest.getRequestDataMap();
               int ctgCorrelator = (Integer) data
                     .get(RequestData.CtgCorrelator);
               if (event.equals(RequestEvent.RequestDetails)){
                  rememberDetails(ctgCorrelator, workRequest);
               } else if (event.equals(RequestEvent.RequestEntry)) {
                  rememberRequest(ctgCorrelator, workRequest);
               } else if (event.equals(RequestEvent.ResponseExit)) {
                  recordRequestResponse(data, ctgCorrelator, 
                                        workRequest, time);
               } else {
                  recordOther(event, time);
               }
            }

            if (stats.isStatsTime()) {
               println(stats.outputStats());
            }

            cleanUp();

            if (queue.queueIsEmpty()) {
               /*
                * Once all the work is done, check if we should
                * shut down or not...
                */
               if (shuttingDown){
                  keepGoing = false;
               } else {
                  keepGoing = sleepABit();
               }
            }
         }
      } catch (Exception e) {
         printErrorln(ThreadedMonitor.getCurrentTimeStamp()
               + " Monitor Thread Error: Monitoring disabled");
         threadedMonitor.printStackTrace(e);
      }
      println(ThreadedMonitor.getCurrentTimeStamp()
            + ": Queue Monitor stopping");
      /*
       * Stop any further work by the ThreadedMonitor.
       */
      queue = null;
      /*
       * Force a tidy up.
       */
      stats.outputStats();
      tidyUp();
      return;
   }

   /*
    * If the work queue is empty, the thread may spin rapidly which may use
    * system resources. In this case the thread can sleep for a short time while
    * waiting for work.
    */
   private boolean sleepABit() throws Exception {
      try {
         Thread.sleep(1000);
      } catch (Exception e) {
         printErrorln(ThreadedMonitor.getCurrentTimeStamp()
               + " Monitor Thread Sleep Error: Monitoring disabled");
         throw e;
      }
      return true;
   }

   private void recordOther(RequestEvent event, String time) {
      /*
       * This model is extensible - other types of work could be added 
       * - so a simple catch to trap anything done by error
       */
      printErrorln(time + " Unknown event: " + event);
   }

   private void rememberRequest(Integer ctgCorrelator,
         WorkElement workRequest) {
      /*
       * If this is an inbound request, store it for future reference, along
       * with its timestamp.
       */
      stats.incrementInboundCount();
      storedRequests.put(workRequest, new ArrayList<WorkElement>());
   }

   private void rememberDetails(Integer ctgCorrelator,
         WorkElement workRequest) {
      /*
       * If this is a details request, store it with the inbound request
       * for future reference.
       */
      ArrayList<WorkElement> details = storedRequests.get(workRequest);
      if (details != null) {
         stats.incrementDetailsCount();
         details.add(workRequest);
      }
   }

   private void recordRequestResponse(Map<RequestData, Object> data,
         Integer ctgCorrelator, WorkElement workRequest,
         String time) {
      /*
       * If this is an outbound request, check to see how long it took
       */
      Entry<WorkElement, ArrayList<WorkElement>> requestEntry = null;
      for (Entry<WorkElement, ArrayList<WorkElement>> entry : storedRequests.entrySet()){
         if (entry.getKey().equals(workRequest)){
            /*
             * found it so remove it
             */
            requestEntry = entry;
            storedRequests.remove(entry.getKey());
            break;
         }
      }
      if (requestEntry != null) {
         /*
          * The request was found in the hashmap, 
          * now do the time check 
          */

         // Increment the global count
         stats.incrementOutboundCount();

         long responseSent = (Long) data.get(RequestData.ResponseSent);
         long requestReceived = (Long) requestEntry.getKey().getRequestDataMap().get(
               RequestData.RequestReceived);
         // Work out how long this request took
         long elapsedTime = responseSent - requestReceived;

         recordFlowDetails(data, ctgCorrelator, workRequest, time,
               requestEntry.getKey(), requestEntry.getValue(), responseSent, requestReceived, elapsedTime);
      } else {
         /*
          * A stored inbound request matching this outbound request was not
          * found
          */
         println(time + " Request not found: " + ctgCorrelator);
      }
   }

   public void recordFlowDetails(Map<RequestData, Object> data,
         Integer ctgCorrelator, WorkElement workRequest,
         String time, WorkElement originalRequest,
         ArrayList<WorkElement> requestDetails,
         long responseSent, long requestReceived, long elapsedTime) {
      
      String prefix = String.format("\n[%011d]: ", ctgCorrelator);
      /*
       * Build message as one string buffer so separate threads
       * will not interleave the lines of data.
       */
      StringBuffer message = new StringBuffer();
      
      message.append("Processing request ");
      message.append(ctgCorrelator);
      message.append(dumpEventData(this.getClass().getCanonicalName() + 
                                   ":run",
                                   originalRequest.getRequestEvent(),
                                   originalRequest.getRequestDataMap()));
      message.append(prefix);
      message.append("User correlator ");
      message.append(BasicMonitor.dumpToHex(
            originalRequest.getRequestCorrelator()));
      message.append("\n");
      for (WorkElement detail : requestDetails){
         message.append(dumpEventData(this.getClass().getCanonicalName() + 
               ":run",
               detail.getRequestEvent(),
               detail.getRequestDataMap()));
         message.append(prefix);
         message.append("User correlator ");
         message.append(BasicMonitor.dumpToHex(
               detail.getRequestCorrelator()));
         message.append("\n");
      }
      message.append(dumpEventData(this.getClass().getCanonicalName() + 
                                   ":run", 
                                   workRequest.getRequestEvent(), 
                                   workRequest.getRequestDataMap()));
      message.append(prefix);
      message.append("User correlator ");
      message.append(BasicMonitor.dumpToHex(
            workRequest.getRequestCorrelator()));
      message.append("\n");
      println(message.toString());
      if (elapsedTime > lrtTimeout) {
         /*
          * If the request took more than 15 seconds - log an alert. If 
          * the request is an ECI Request, we can extract some useful 
          * data to log, otherwise just warn that a long running 
          * transaction occurred.
          */
         FlowType flowType = (FlowType) data.get(RequestData.FlowType);
         if (flowType.getRequestType().equals(RequestType.Eci)) {
            printErrorln(time + " !LRT-ALERT! [Program: "
                  + data.get(RequestData.Program) + "] [TransId: "
                  + data.get(RequestData.TranName) + "] [Rc: "
                  + data.get(RequestData.CtgReturnCode) + "] [Start: "
                  + new Date(requestReceived) + "] [End: " 
                  + new Date(responseSent) + "] [Time: "
                  + elapsedTime + "ms]");
         } else {
            printErrorln(time 
                  + " !LRT-ALERT! : Long running Transaction took "
                  + elapsedTime + " ms");
         }
      }
   }

   /**
    * Method to format all the information available at 
    * an event.
    * 
    * @param originator Text to identify originator
    * @param event that fired
    * @param data found on that event
    * @return User readable dump of exit info
    */
   private String dumpEventData(String originator, 
         RequestEvent event, Map<RequestData, Object> data) {
      return threadedMonitor.dumpEventData(originator, event, data);
   }

   /**
    * Use our related ThreadedMonitor to output a message.
    * Used by our related objects for their output.
    * 
    * @param message to output
    */
   public void println(String message) {
      threadedMonitor.println(message);
   }

   /**
    * Use our related ThreadedMonitor to output an error message.
    * Used by our related objects for their error output.
    * 
    * @param message to output
    */
   public void printErrorln(String message) {
      threadedMonitor.printErrorln(message);
   }

   /**
    * @return our Statistics object.
    */
   public Statistics getStats() {
      return stats;
   }

   /**
    * Reset everything as part of end of day processing.
    */
   public void resetRequests() {
      storedRequests.clear();
      queue.clearQueue();
   }

   /**
    * Add the workElement to the queue.
    * If there is no queue, we will throw an exception and so
    * terminate the exit.
    *  
    * @param workElement
    */
   public void addToQueue(WorkElement workElement) {
      if (queue == null){
         throw new RuntimeException("Ending exit as queue has closed");
      }
      queue.addToQueue(workElement);
   }

   /*
    * Force the cleanUp process to clear all data.
    */
   public void tidyUp() {
      cleanUp(true);
   }

   /*
    * Reaper processing needs to be included to cater for any dropped or
    * broken connections. In those cases when work completes, the exit 
    * is not driven, which may leave orphaned objects in the hashmap, 
    * causing it to expand over time. This processing periodically 
    * cleans out old entries in the hashmap to prevent that. This is not
    * done on every loop iteration - it uses a much longer interval than
    * the stats loop.
    */
   public void cleanUp() {
      cleanUp(false);
   }

   /*
    * Remove any requests which are probably orphaned from the hashmap, or
    * clear the hashmap entirely. Log an error for each orphaned request.
    */
   public void cleanUp(boolean clearAll) {
      long reaperTime = System.currentTimeMillis() - reaperTimeCheck;

      if ((reaperTime >= reaperTimeout) || clearAll) {
         /*
          * Loop through the Hashmap, check the time of each object 
          * against current time.
          */
         Iterator<WorkElement> keys = storedRequests.keySet().iterator();
         while (keys.hasNext()){
            Map<RequestData, Object> data = 
               keys.next().getRequestDataMap();
            long requestReceived = (Long) data.get(RequestData.RequestReceived);
            long elapsedTime = System.currentTimeMillis() - requestReceived;
            if ((elapsedTime > orphanTimeout) || clearAll) {
               /*
                * Requests taking longer than the specified timeout are
                * probably orphaned, so reap them and log them
                */
               logOrphan(data, requestReceived, elapsedTime);
               // This will also remove the key value pair from storedRequests
               keys.remove();
               getStats().incrementOutboundCount();
            }
         }
         reaperTimeCheck = System.currentTimeMillis();
      }
   }

   private void logOrphan(Map<RequestData, Object> data,
         long requestSent, long elapsedTime) {
      /*
       * If the request is an ECI Request, we can extract some useful data to
       * log, otherwise just warn that a long running transaction occurred.
       */
      FlowType flowType = (FlowType) data.get(RequestData.FlowType);
      if (flowType.getRequestType().equals(RequestType.Eci)) {
         printErrorln(ThreadedMonitor.getCurrentTimeStamp() 
               + " !RPR-ALERT! [Program: "
               + data.get(RequestData.Program) + "] [TransId: "
               + data.get(RequestData.TranName) + "] [Rc: "
               + data.get(RequestData.CtgReturnCode) + "] " 
               + "[Start: " + new Date(requestSent) 
               + "] [Time: " + elapsedTime + "ms]");
      } else {
         printErrorln(ThreadedMonitor.getCurrentTimeStamp()
               + " !RPR-ALERT! [Long running Transaction " 
               + "removed from table after  "
               + elapsedTime + " ms]");
      }
   }

   /**
    * Mark the QueueMonitor as shutting down.
    */
   public void requestShutdown() {
      shuttingDown = true;      
   }

}
