/*
 *      File Name     : Queue.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This class is part of the ThreadedMonitor sample. It
 *                      provides a first-in-first-out (FIFO) queue which holds
 *                      pending WorkElement objects. The ThreadedMonitor class
 *                      adds WorkElement objects to a Queue when an event
 *                      occurs, and the QueueMonitor class reads objects from
 *                      the Queue and processes them.
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

import java.util.Vector;

public class Queue {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   // A Vector to act as an internal FIFO work queue
   private Vector<WorkElement> requestQueue = new Vector<WorkElement>();

   // set a limit on how big the queue will get.
   private int maxQueueSize = 1000;

   /**
    * The QueueMonitor that created us and provides our output
    * methods.
    */
   private QueueMonitor queueMonitor;

   /**
    * Initialise the Queue to keep a reference to the
    * parent QueueMonitor that provides output methods and
    * set the maximum size the queue can reach so we can
    * protect against heap exhaustion.
    * 
    * @param monitor
    */
   public Queue(QueueMonitor monitor, int maxQueueSize) {
      queueMonitor = monitor;
      this.maxQueueSize = maxQueueSize;
   }

   /**
    * Add a WorkElement to the Queue
    * 
    * @param outboundRequest WorkElement to add
    */
   public synchronized void addToQueue(WorkElement outboundRequest) {
       {
         if (requestQueue.size() < maxQueueSize) {
            requestQueue.add(outboundRequest);
         } else {
            printErrorln("Queue is full.  Work element lost");
         }
      }
   }

   /**
    * Get the first element from the queue and remove
    * it from there.
    * 
    * @return first WorkElement or null
    */
   public synchronized WorkElement removeFromQueue() {
      WorkElement value = requestQueue.firstElement();
      requestQueue.removeElementAt(0);
      return value;
   }

   /**
    * Empty the Queue
    */
   public synchronized void clearQueue() {
      requestQueue.clear();
   }

   /**
    * @return true if the Queue is empty
    */
   public boolean queueIsEmpty() {
      return requestQueue.isEmpty();
   }

   /**
    * Use our related QueueMonitor to output an error message.
    * 
    * @param message to output
    */
   private void printErrorln(String message) {
      queueMonitor.printErrorln(message);
   }

}
