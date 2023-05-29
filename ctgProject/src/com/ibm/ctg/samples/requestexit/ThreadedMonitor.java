/*
 *      File Name     : ThreadedMonitor.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This sample extends the StdoutMonitor to use the 
 *                      CICS Transaction Gateway request monitoring 
 *                      exits with a background thread to reduce the
 *                      execution time on each monitored request. 
 *                      This will simply output the data available at 
 *                      each exit point to the given "out" file. If one
 *                      is not provided output will go to the CTG Data Path
 *                      if you are on Windows, or stdout on UNIX.    
 *                      "out" is defined by the system property:
 *                      com.ibm.ctg.samples.requestexit.out
 *                      It will also log errors to the given "err" file.
 *                      If one is not provided errors will go to the CTG Data Path
 *                      if you are on Windows, or stderr on UNIX.
 *                      "err" is defined by the system property:
 *                      com.ibm.ctg.samples.requestexit.err
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

package com.ibm.ctg.samples.requestexit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.ibm.ctg.monitoring.RequestData;
import com.ibm.ctg.monitoring.RequestEvent;
import com.ibm.ctg.monitoring.TransientPayLoad;
import com.ibm.ctg.samples.requestexit.threadedmonitor.QueueMonitor;
import com.ibm.ctg.samples.requestexit.threadedmonitor.WorkElement;

/**
 * The ThreadedMonitor is designed to output all the data available
 * on the eventFired method.  It uses the methods provided by the 
 * BasicMonitor, but is also written so subclasses can re-use
 * the output methods.  Output will be written to System.out, unless
 * overridden by the system property defined by
 * {@link com.ibm.ctg.samples.requestexit.BasicMonitor#STDOUT_PROPERTY STDOUT_PROPERTY}
 * and error logs will be written to System.err, unless
 * overridden by the system property defined by
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#STDERR_PROPERTY STDERR_PROPERTY}
 * 
 * This exit will also allow overriding of:
 * <ul>
 * <li>maximum queue size using system property defined by 
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#QUEUESIZE_PROPERTY QUEUESIZE_PROPERTY}
 * </li>
 * <li>long running transaction timeout using system property defined by 
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#LRT_TIMEOUT_PROPERTY LRT_TIMEOUT_PROPERTY}
 * </li>
 * <li>reaper processing timeout using system property defined by 
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#REAPER_TIMEOUT_PROPERTY REAPER_TIMEOUT_PROPERTY}
 * </li>
 * <li>orphaned requests timeout using system property defined by 
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#ORPHAN_TIMEOUT_PROPERTY ORPHAN_TIMEOUT_PROPERTY}
 * </li>
 * <li>stats logging interval using system property defined by 
 * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#INTERVAL_PROPERTY INTERVAL_PROPERTY}
 * </li>
 * </ul>
 */

public class ThreadedMonitor extends BasicMonitor {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /**
    * The Java property name used to set the error output file name.
    */
   public static final String STDERR_PROPERTY = "com.ibm.ctg.samples.requestexit.err";

   /**
    * The Java property name used to set maximum queue size.
    */
   public static final String QUEUESIZE_PROPERTY = "com.ibm.ctg.samples.requestexit.queue";

   /**
    * The Java property name used to set the long running transaction 
    * timeout in milliseconds.
    */
   public static final String LRT_TIMEOUT_PROPERTY = "com.ibm.ctg.samples.requestexit.lrt";

   /**
    * The Java property name used to set the reaper processing  
    * timeout in milliseconds.
    */
   public static final String REAPER_TIMEOUT_PROPERTY = "com.ibm.ctg.samples.requestexit.reaper";

   /**
    * The Java property name used to set the orphaned requests  
    * timeout in milliseconds.
    */
   public static final String ORPHAN_TIMEOUT_PROPERTY = "com.ibm.ctg.samples.requestexit.orphan";

   /**
    * The Java property name used to set the stats logging interval in 
    * milliseconds.
    */
   public static final String INTERVAL_PROPERTY = "com.ibm.ctg.samples.requestexit.interval";

   /*
    * Maximum number of work elements permitted on queue (default value = 10,000).
    */
   private int queueSize = 10000;
   
   /*
    * Long Running Transaction Timeout (default value = 15 secs)
    */
   private long lrtTimeout = 15000;

   /*
    * Interval between running reaper processing (default value = 5 min)
    */
   private long reaperTimeout = 300000;

   /*
    * Interval after which orphaned requests are reaped (default value = 10 mins)
    */
   private long orphanTimeout = 600000;

   /*
    * Interval between stats logging (default value = 1 hour)
    */
   private long intervalTimeout = 3600000;

   /**
    * Used for formatting the current time stamp.
    */
   private static SimpleDateFormat logTime = new SimpleDateFormat(
   "yyyy-MM-dd HH:mm:ss");

   /*
    * The Queue monitor - this actually provides the monitoring data. 
    * There is one per Gateway, and so the statistics are delivered on 
    * a per Gateway basis.
    */
   private QueueMonitor queueMonitor;

   /*
    * Main monitor thread - this actually provides the monitoring data 
    * using the QueueMonitor object.
    */
   private Thread queueMonitorThread = null;

   // Internal lock
   private static Object monitorLock = new Object();

   private PrintStream stderr;

   /**
    * The constructor will initialise the output routines 
    * (BasicMonitor) and error output routines and then start the 
    * monitor thread if not already started. 
    */
   public ThreadedMonitor() {
      /*
       * Initialise default output routines.
       */
      super();
      /*
       * Initialise default error output routines.
       */
      String destination = System.getProperty(STDERR_PROPERTY);
      // Since on Windows the gateway is started as a service
      // writing to standard error will not produce any output
      if (destination == null && !onWindows){
         stderr = getDefaultStderr();
      } else {
         if(destination == null && onWindows) {
            // If the destination is not set and on Windows
            // Set the file destination to be the product data directory
            String productDataPath = System.getenv("CTG_DATA_PATH");
            destination = productDataPath + "\\" + "threadedMonitorError.txt";
         }
         
         File destFile = new File(destination);
         try {
            if (onZos) {
               stderr = new PrintStream(destFile, EBCDIC); // Ensure file is readable on z/OS 
            } else {
               stderr = new PrintStream(destFile);
            }
         } catch (FileNotFoundException e) {
            // On Windows the sample cannot run if the output cannot
            // be initialised.
            if(onWindows) {
               throw new RuntimeException(e);
            }
            stderr = getDefaultStdout();
            System.err.println("Print to '" + destination + "' failed with " + e);
            System.err.println("Using default PrintStream");
         } catch (UnsupportedEncodingException e) {
            // This should not be possible as the Gateway is using this encoding
            
            // On Windows the sample cannot run if the output cannot
            // be initialised.
            if(onWindows) {
               throw new RuntimeException(e);
            }
            stderr = getDefaultStdout();
            System.err.println("Print to '" + destination + "' failed with " + e);
            System.err.println("Using default PrintStream");
         }
      }
      println("Starting " + getClass().getSimpleName() + 
              " error log stream  at " + new Date());
      /*
       * Initialise the QueueMonitor object and main monitoring thread.
       */
      int value = getPropertyInt(QUEUESIZE_PROPERTY);      
      if (value >= 0){
         queueSize = value;
      }
      value = getPropertyInt(LRT_TIMEOUT_PROPERTY);      
      if (value >= 0){
         lrtTimeout = value;
      }
      value = getPropertyInt(REAPER_TIMEOUT_PROPERTY);      
      if (value >= 0){
         reaperTimeout = value;
      }
      value = getPropertyInt(ORPHAN_TIMEOUT_PROPERTY);      
      if (value >= 0){
         orphanTimeout = value;
      }
      value = getPropertyInt(INTERVAL_PROPERTY);      
      if (value >= 0){
         intervalTimeout = value;
      }
      synchronized (monitorLock) {
         if (queueMonitor == null) {
            queueMonitor = new QueueMonitor(this, queueSize, lrtTimeout,
                  reaperTimeout, orphanTimeout, intervalTimeout);
            try {
               println(getCurrentTimeStamp()
                     + ": Starting Queue Monitor");
               queueMonitorThread = new Thread(queueMonitor , "QueueMonitorThread");
               queueMonitorThread.start();
            } catch (Exception e) {
               printErrorln(getCurrentTimeStamp()
                     + ": Queue Monitor Initialisation Error: Monitoring disabled");
               throw new RuntimeException(
                     "Queue Monitor Initialisation Error: Monitoring disabled:" + e);
            }
         }
      }
   }

   /**
    * Check for system property, and if set read it as an int.
    * 
    * @param key system property name
    * 
    * @return integer value, or -1 if invalid
    */
   private int getPropertyInt(String key) {
      String property = System.getProperty(key);
      int value = -1; // property is null
      if (property != null){
         try {
            value = Integer.parseInt(property);
         } catch (Exception e) {
            value = -2; // property is invalid
         }
         if (value <= 0){
            printErrorln("Property '" + key + "' has invalid value '" + 
                  property + "'");
         }
      }
      return value;
   }
  
   /**
    * This method can be overridden by subclasses to use
    * a different default error PrintStream.
    * 
    * @return PrintStream to use as default error output device
    */
   public PrintStream getDefaultStderr() {
      return System.err;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see com.ibm.ctg.samples.requestexit.BasicMonitor#eventFired(com.ibm.ctg.monitoring.RequestEvent,
    *      java.util.Map)
    */
   @Override
   public void eventFired(RequestEvent event, Map<RequestData, Object> data) {
      if (event.equals(RequestEvent.ShutDown)){
         /*
          * If we are shutting down, then we do not have a RequestData
          * Map, and we must close down the QueueMonitor thread.
          */
         queueMonitor.requestShutdown();
         
      } else if (event.equals(RequestEvent.RequestDetails)
            || event.equals(RequestEvent.RequestEntry)
            || event.equals(RequestEvent.ResponseExit)) {
         /*
          * Only track RequestDetails, RequestEntry and ResponseExit
          * exit points.
          * We only want the start, details and end of the request.
          */
         
         /*
          * Normal processing.
          * Extract any user correlator now, as payload is not available 
          * once this exit completes.
          */
         byte[] userCorrelator = null;
         TransientPayLoad payLoad = (TransientPayLoad) data
               .get(RequestData.PayLoad);
         if (payLoad != null) {
            /*
             * Take the first 32 bytes as a user correlator
             * as an example of what could be used.
             */
            userCorrelator = payLoad.getBytes(0, 32);
         }

         /*
          * Get the current time, and create a new WorkElement using 
          * that and the inbound or outbound GatewayRequest object. 
          * The WorkElement is then passed to the monitor thread for 
          * processing using a FIFO work queue.
          * 
          * This thread does as little work as possible to preserve
          * performance as best as possible.
          */
         WorkElement workElement = new WorkElement(
               userCorrelator, event, data);
         queueMonitor.addToQueue(workElement);
         
      } else {
         /*
          * Delegate the handling of the event to BasicMonitor.
          */
         super.eventFired(event, data);
      }
   }

   /**
    * @return a String version of the current date / time.
    */
   public static String getCurrentTimeStamp() {
      StringBuffer timestamp = new StringBuffer();
      logTime.format(new Date(), timestamp, new FieldPosition(0));
      return timestamp.toString();
   }

   public QueueMonitor getQueueMonitor() {
      return queueMonitor;
   }

   /**
    * @return the monitorLock
    */
   public static Object getMonitorLock() {
      return monitorLock;
   }

   /**
    * Simple output routine to send a string to our currently set
    * PrintWriter, appending a newline, and flushing the buffer.
    * 
    * Output will be written to System.err, unless
    * overridden by the system property defined by
    * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#STDERR_PROPERTY STDERR_PROPERTY}.
    * 
    * To ensure the whole message goes in one block it is synchronized. 
    * 
    * @param string to output
    */
   public void printErrorln(String string){
      synchronized (ThreadedMonitor.class) {
         printError(string + "\n");
         if (stderr == null){
            System.err.flush();
         } else {
            stderr.flush();
         }
      }
   }
   
   /**
    * Writes an exception stack trace to the currently set PrintWriter,
    * and flushes the buffer.
    * 
    * Output will be written to System.err, unless
    * overridden by the system property defined by
    * {@link com.ibm.ctg.samples.requestexit.ThreadedMonitor#STDERR_PROPERTY STDERR_PROPERTY}.
    * 
    * To ensure the whole message goes in one block it is synchronized. 
    * 
    * @param ex the exception to output
    */
   public void printStackTrace(Exception ex) {
      synchronized (ThreadedMonitor.class) {
         if (stderr == null) {
            ex.printStackTrace(System.err);
            System.err.flush();
         } else {
            ex.printStackTrace(stderr);
            stderr.flush();
         }
      }
   }

   /**
    * Simple output routine to send a string to our currently set
    * error PrintWriter.
    * 
    * @param string to output
    */
   private void printError(String args) {
      if (stderr == null){
         System.err.print(args);
      } else {
         try {
            stderr.print(args);
         } catch (Throwable t) {
            System.err.println("Error using stderr: " + t);
            if (stderr != getDefaultStderr()){
               stderr = getDefaultStderr();
               System.err.println("Using default error PrintStream");
            } else {
               /*
                * as we cannot output anything we cannot do
                * anything more, so Throw something to disable
                * this exit.
                */
               throw new RuntimeException(
                     "Default error PrintStream has failed!");
            }
            printError(args);
         }
      }
   }

}
