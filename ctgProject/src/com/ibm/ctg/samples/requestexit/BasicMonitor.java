/*
 *      File Name     : BasicMonitor.java
 *
 *      Product       : CICS Transaction Gateway
 *
 *      Description   : This sample shows the basic use of the CICS Transaction
 *                      Gateway request monitoring exits. This will simply
 *                      output the data available at each exit point to the
 *                      given "out" file. If one is not provided output will go
 *                      to the CICS TG Data Path on Windows, or stdout on UNIX
 *                      and z/OS. "out" is defined by the system property:
 *                      com.ibm.ctg.samples.requestexit.out
 *                      
 *      Pre-Requisites: Use a version of the JDK that the CICS Transaction
 *                      Gateway supports if you recompile this sample. See
 *                      the product documentation for supported Java levels.
 *
 * Licensed Materials - Property of IBM  
 *  
 * 5724-I81,5725-B65,5655-Y20 
 *  
 * (C) Copyright IBM Corp. 2007, 2014 All Rights Reserved.  
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
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

import javax.transaction.xa.Xid;

import com.ibm.ctg.monitoring.ChannelInfo;
import com.ibm.ctg.monitoring.ContainerInfo;
import com.ibm.ctg.monitoring.ContainerInfo.DataType;
import com.ibm.ctg.monitoring.ContainerInfoContentException;
import com.ibm.ctg.monitoring.FlowTopology;
import com.ibm.ctg.monitoring.FlowType;
import com.ibm.ctg.monitoring.InvalidContainerTypeException;
import com.ibm.ctg.monitoring.RequestData;
import com.ibm.ctg.monitoring.RequestEvent;
import com.ibm.ctg.monitoring.RequestExit;
import com.ibm.ctg.monitoring.TransientPayLoad;
import com.ibm.ctg.util.OriginData;

/**
 * The BasicMonitor is designed to output all the data available
 * on the eventFired method.  It is written so subclasses can re-use
 * the output methods.  Output will be written to System.out, unless
 * overridden by the system property defined by
 * {@link com.ibm.ctg.samples.requestexit.BasicMonitor#STDOUT_PROPERTY STDOUT_PROPERTY}
 */
public class BasicMonitor implements RequestExit {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2007, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /**
    * The Java property name used to set the output file name.
    */
   public static final String STDOUT_PROPERTY = "com.ibm.ctg.samples.requestexit.out";
   
   public static final String EBCDIC = "Cp1047";  // Encoding name for US EBCDIC

   private PrintStream stdout;
   
   public boolean onZos;
   public boolean onWindows;

   /**
    * Default constructor initializes the output methods.
    */
   public BasicMonitor() {
      // Set variables for operating systems where there needs to be
      // special cases for those operating system
      onZos = System.getProperty("os.name").equals("z/OS");
      onWindows = System.getProperty("os.name").contains("Windows");
      
      // set up output destination
      String destination = System.getProperty(STDOUT_PROPERTY);
      
      // Since on Windows the gateway is started as a service
      // writing to standard out will not produce any output
      if (destination == null && !onWindows) {
         stdout = getDefaultStdout();
      } else {
         if(destination == null && onWindows) {
            // If the destination is not set and on windows
            // Set the file destination to be the product data directory
            String productDataPath = System.getenv("CTG_DATA_PATH");
            destination = productDataPath + "\\" + "basicMonitorOutput.txt";
         }
         
         File destFile = new File(destination);
         try {
            if (onZos) {
               stdout = new PrintStream(destFile, EBCDIC); // Ensure file is readable on z/OS 
            } else {
               stdout = new PrintStream(destFile);
            }
         } catch (FileNotFoundException e) {
            
            // On Windows the sample cannot run if the output cannot
            // be initialised.
            if(onWindows) {
               throw new RuntimeException(e);
            }
            stdout = getDefaultStdout();
            System.err.println("Print to '" + destination + "' failed with " + e);
            System.err.println("Using default PrintStream");
         } catch (UnsupportedEncodingException e) {
            // This should not be possible as the Gateway is using this encoding
            
            // On Windows the sample cannot run if the output cannot
            // be initialised.
            if(onWindows) {
               throw new RuntimeException(e);
            }
            stdout = getDefaultStdout();
            System.err.println("Print to '" + destination + "' failed with " + e);
            System.err.println("Using default PrintStream");
         }
      }
      
      println("Starting " + getClass().getSimpleName() + 
              " log stream at " + new Date());
      
      // now output some environment status, just for the record...
      println(getSystemEnvironment());
   }

   /* (non-Javadoc)
    * @see com.ibm.ctg.monitoring.RequestExit#eventFired(com.ibm.ctg.monitoring.RequestEvent, java.util.Map)
    */
   public void eventFired(RequestEvent event, Map<RequestData, Object> data) {
      // log detailed event data for a request/response flow
      if (event.equals(RequestEvent.RequestDetails)
            || event.equals(RequestEvent.RequestEntry)
            || event.equals(RequestEvent.ResponseExit)) {
         // use dumpEventData() to format a detailed summary and log it 
         println(dumpEventData(myName() + ":eventFired", event, data));
      } else {
         // log non-request/response events with current time stamp
         Date now = new Date();
         // build common log entry prefix
         StringBuffer message = new StringBuffer(now.toString());
         message.append(" ");
         message.append(myName());
         message.append(":eventFired called with event = ");
         message.append(event.toString());

         // append event detail
         if (event.equals(RequestEvent.Command)) {
            // log the command event and data
            message.append(", data = ");
            message.append((String) data.get(RequestData.CommandData));
         } else if (event.equals(RequestEvent.ShutDown)) {
            // log the ShutDown event only (no resources need to be freed)
            message.append(" OK");
         } else {
            // log any unknown event types
            message.append(", unknown event type.");
         }
         println(message.toString());
      }
   }

   /**
    * This method can be overridden by subclasses to give a name to identify the
    * output.
    * 
    * @return defaults to the name of the current class
    */
   public String myName() {
      return this.getClass().getCanonicalName();
   }

   /**
    * This method can be overridden by subclasses to use
    * a different default PrintStream.
    * 
    * @return PrintStream to use as default output device
    */
   public PrintStream getDefaultStdout() {
      return System.out;
   }
   
   /**
    * Method to format all the information available at 
    * an event.
    * 
    * @param originator text to identify originator
    * @param event the event that fired
    * @param data the data found on that event
    * @return User readable dump of exit info
    */
   public String dumpEventData(String originator, RequestEvent event, 
         Map<RequestData, Object> data) {
      /*
       * Build message as one string buffer so separate threads
       * will not interleave the lines of data.
       */
      int ctgCorrelator = (Integer) data.get(RequestData.CtgCorrelator);
      String prefix = String.format("\n[%011d]: ", ctgCorrelator);
      
      // collect all values not used
      EnumSet<RequestData> remainingData = EnumSet.allOf(RequestData.class);

       // start the header with event details
      StringBuffer message = new StringBuffer();
      message.append(prefix);
      message.append(originator);
      message.append(" called with event = ");
      message.append(event);
      
      // next add flow description
      FlowType flowType = (FlowType) data.get(RequestData.FlowType);
      appendRequestData(message, RequestData.FlowType, flowType, prefix);
      remainingData.remove(RequestData.FlowType);
      FlowTopology flowTopology = (FlowTopology) data.get(RequestData.FlowTopology);
      appendRequestData(message, RequestData.FlowTopology, flowTopology, prefix);
      remainingData.remove(RequestData.FlowTopology);
      
      // add Request ID
      Object qualifier = data.get(RequestData.CtgApplidQualifier);
      Object applid = data.get(RequestData.CtgApplid);
      appendFullyQualifiedApplid(message, "Fully qualified APPLID", (String)qualifier, (String)applid, prefix);
      remainingData.remove(RequestData.CtgApplidQualifier);
      remainingData.remove(RequestData.CtgApplid);
      appendRequestData(message, RequestData.CtgCorrelator, ctgCorrelator, prefix);
      remainingData.remove(RequestData.CtgCorrelator);

      // add Client Request ID if present
      Object dataItem = data.get(RequestData.ClientCtgCorrelator); 
      if (dataItem != null){
         qualifier = data.get(RequestData.ClientCtgApplidQualifier);
         applid = data.get(RequestData.ClientCtgApplid);
         appendFullyQualifiedApplid(message, "Client fully qualified APPLID", (String)qualifier, (String)applid, prefix);
         appendRequestData(message, RequestData.ClientCtgCorrelator, dataItem, prefix);
      }
      remainingData.remove(RequestData.ClientCtgApplidQualifier);
      remainingData.remove(RequestData.ClientCtgApplid);
      remainingData.remove(RequestData.ClientCtgCorrelator);

      // add XID if present
      dataItem = data.get(RequestData.Xid); 
      if (dataItem != null){
         appendXid(message, (Xid)dataItem, prefix);
      }
      remainingData.remove(RequestData.Xid);

      // add URID if present
      dataItem = data.get(RequestData.Urid); 
      if (dataItem != null){
         appendRequestData(message, RequestData.Urid, dataItem, prefix);
      }
      remainingData.remove(RequestData.Urid);

      // add LUW if present
      dataItem = data.get(RequestData.LuwToken); 
      if (dataItem != null){
         appendRequestData(message, RequestData.LuwToken, dataItem, prefix);
      }
      remainingData.remove(RequestData.LuwToken);

      // add Origin Data if present
      dataItem = data.get(RequestData.OriginData); 
      if (dataItem != null){
         appendOriginData(message, (OriginData) dataItem, prefix);
      }
      remainingData.remove(RequestData.OriginData);

      // add all timestamps that are present
      // get base for the timestamp offsets 
      long base = (Long) data.get(RequestData.RequestReceived);
      appendTimeStamp(message, base, RequestData.RequestReceived, base, 1, prefix);
      remainingData.remove(RequestData.RequestReceived);
      dataItem = data.get(RequestData.RequestSent); 
      if (dataItem != null){
         appendTimeStamp(message, base, RequestData.RequestSent, (Long) dataItem, 2, prefix);
      }
      remainingData.remove(RequestData.RequestSent);
      dataItem = data.get(RequestData.ResponseReceived); 
      if (dataItem != null){
         appendTimeStamp(message, base, RequestData.ResponseReceived, (Long) dataItem, 3, prefix);
      }
      remainingData.remove(RequestData.ResponseReceived);
      dataItem = data.get(RequestData.ResponseSent); 
      if (dataItem != null){
         appendTimeStamp(message, base, RequestData.ResponseSent, (Long) dataItem, 4, prefix);
      }
      remainingData.remove(RequestData.ResponseSent);

      // output any remaining data fields in a consistent order
      for (RequestData requestData: remainingData){
         Object object = data.get(requestData);
         // only output records for data types that are present
         if (object != null){
            if (requestData == RequestData.PayLoad) {
               appendPayLoad(message, (TransientPayLoad) object, prefix);
            } else if (requestData == RequestData.Channel) {
               appendChannel(message, (ChannelInfo) object, prefix);
            } else {
               appendRequestData(message, requestData, object, prefix);
            }
         }
      }
      return message.toString();
   }

   /**
    * The PayLoad is provided so that embedded correlators used by
    * 3rd party applications can be used in the monitoring exits.
    * As an example the first and last 32 bytes are displayed.
    *  
    * @param message the message that is being built up for this event
    * @param tpl the TransientPayLoad object passed to the exit
    * @param prefix a prefix appended to each line of output
    */
   private void appendPayLoad(StringBuffer message, TransientPayLoad tpl, String prefix) {
      message.append(prefix);
      message.append(RequestData.PayLoad);
      message.append(" = COMMAREA is ");
      message.append(tpl.getLength());
      message.append(" long");
      byte[] first = tpl.getBytes(0, 32);
      message.append(prefix);
      message.append("   First 32 bytes: ");
      message.append(dumpToHex(first));
      byte[] last = tpl.getBytes(0, -32);
      message.append(prefix);
      message.append("   Last 32 bytes:  ");
      message.append(dumpToHex(last));
   }
   
   /**
    * The Channel field contains details of the channel and any containers in the
    * channel.
    * 
    * @param message the message that is being built up for this event
    * @param channel the ChannelInfo object passed to the exit
    * @param prefix a prefix appended to each line of output
    */
   private void appendChannel(StringBuffer message, ChannelInfo channel, String prefix) {
      // Append channel details
      message.append(prefix);
      message.append(RequestData.Channel);
      message.append(" = ");
      message.append(channel.getName());
      // Append container details
      for (ContainerInfo container : channel.getContainers()) {
         message.append(prefix);
         message.append("   ");
         message.append(container.getName());
         message.append("(").append(container.getType()).append(") = ");
         int length = container.getLength();
         message.append(length);
         if (length > 32) {
            length = 32;
         }
         if (container.getType() == DataType.BIT) {
            message.append(" bytes");
            try {
               byte[] first;
               first = container.getBitData(0, length);
               message.append(prefix);
               message.append("   ");
               message.append("   First " + length + " bytes: ");
               message.append(dumpToHex(first));
            } catch (IndexOutOfBoundsException e) {
               // this should not happen
               e.printStackTrace();
            } catch (InvalidContainerTypeException e) {
               // this should not happen
               e.printStackTrace();
            } catch (ContainerInfoContentException e) {
               // this should not happen
               e.printStackTrace();
            }
         } else {
            message.append(" characters");
            try {
               String first;
               first = container.getCharData(0, length);
               message.append(prefix);
               message.append("   ");
               message.append("   First " + length + " characters: '");
               message.append(first);
               message.append("'");
            } catch (IndexOutOfBoundsException e) {
               // this should not happen
               e.printStackTrace();
            } catch (InvalidContainerTypeException e) {
               // this should not happen
               e.printStackTrace();
            } catch (ContainerInfoContentException e) {
               // this should not happen
               e.printStackTrace();
            }
         }
      }
   }

   /**
    * As most of the Origin Data is already included in other
    * RequestData fields, just output the Transaction Group ID and the
    * User Correlator if it has been set.
    * 
    * @param message the message that is being built up for this event
    * @param originData the OriginData object passed to the exit
    * @param prefix a prefix appended to each line of output
    */
   private void appendOriginData(StringBuffer message, OriginData originData, String prefix) {
      // most of the origin data is included in other fields
      message.append(prefix);
      message.append(RequestData.OriginData);
      message.append(" - Transaction Group ID = ");
      message.append(toHex(originData.getTransGroupId()));
      if(originData.getUserCorrelator() != null && originData.getUserCorrelator().length() > 0){
         message.append(prefix);
         message.append(RequestData.OriginData);
         message.append(" - User Correlator = ");
         message.append(originData.getUserCorrelator());
      }
   }

   /**
    * Format the XID into the constituent parts: Format ID, 
    * Branch Qualifier and GTRID.
    * 
    * @param message the message that is being built up for this event
    * @param xid the Xid object passed to the exit
    * @param prefix a prefix appended to each line of output
    */
   private void appendXid(StringBuffer message, Xid xid, String prefix) {
      message.append(prefix);
      message.append(RequestData.Xid);
      message.append(" = Format ID: ");
      message.append(Integer.toHexString(xid.getFormatId()));
      message.append(prefix);
      message.append("   BQUAL: ");
      message.append(toHex(xid.getBranchQualifier()));
      message.append(prefix);
      message.append("   GTRID: ");
      message.append(toHex(xid.getGlobalTransactionId()));
   }

   /**
    * Append the timestamp in a user readable form.
    * We add the T1 - T4 tags used in the javadoc to describe
    * the location of the timestamps in the flow.
    * Timestamps are formatted as date / time and as an offset
    * in milliseconds from the time the request is first received.
    *
    * @param message the message that is being built up for this event
    * @param base the T1 timestamp, for calculating time offsets
    * @param requestData the RequestData field for the timestamp
    * @param value the timestamp value
    * @param tFlag the sequence number of the timestamp
    * @param prefix a prefix appended to each line of output
    */
   private void appendTimeStamp(StringBuffer message, long base, 
         RequestData requestData, Long value, int tFlag, String prefix) {
      Date timestamp = new Date(value);
      message.append(prefix);
      message.append("T");
      message.append(tFlag);
      message.append(" - ");
      message.append(requestData);
      message.append(" (");
      message.append(value);
      message.append(") \t= ");
      if (value != 0){
         message.append(timestamp);
         if (tFlag > 1){
             message.append(" offset = ");
             message.append((value.longValue() - base));
         }
      } 
      else {
       message.append("No timestamp set");
      }
   }

   /**
    * Appends the fully qualified APPLID to the message that is output
    * for this event.
    * 
    * The fully qualified APPLID is made up of the 2 CICS TG fields used 
    * to identify the Gateway daemon or Gateway classes.
    * 
    * @param message the message that is being built up for this event
    * @param name the name of the field we are outputting
    * @param qualifier the APPLID qualifier value
    * @param applid the APPLID value
    * @param prefix the prefix for the message insert
    */
   public void appendFullyQualifiedApplid(StringBuffer message, String name, 
         String qualifier, String applid, String prefix) {
      message.append(prefix);
      message.append(name);
      message.append(" = ");
      if (qualifier != null){
         message.append(qualifier);
      }
      if (applid != null){
         if (qualifier != null) {
            message.append(".");
         }
         message.append(applid);
      } else {
         if (qualifier == null) {
            message.append("No APPLID");
         }
      }
   }

   /**
    * Append the request data using simple formatting.
    * Using the getDataType method to select formatting method. 
    * 
    * @param message the message that is being built up for this event
    * @param requestData data field to be formatted
    * @param object data to be formatted
    */
   public void appendRequestData(StringBuffer message, RequestData requestData, 
         Object object, String prefix) {
      message.append(prefix);
      message.append(requestData); // toString() generates a readable string
      message.append(" = ");
      if (requestData.getDataType().equals(RequestData.DATA_TYPE_BYTE_ARRAY)) {
         /*
          * As basic toString() formatting of byte arrays is not
          * very readable, we use an internal method for request data
          * of that type. 
          */
         byte[] bytes = (byte[]) object;
         message.append(toHex(bytes));
      } else {
         message.append(object);
      }
   }

   /**
    * Simple output routine to send a string to our currently set
    * PrintWriter, appending a newline, and flushing the buffer.
    * 
    * Output will be written to System.out, unless
    * overridden by the system property defined by
    * {@link com.ibm.ctg.samples.requestexit.BasicMonitor#STDOUT_PROPERTY STDOUT_PROPERTY}.
    * 
    * To ensure the whole message goes in one block it is synchronized. 
    * 
    * @param string string to output
    */
   public void println(String string){
      synchronized (BasicMonitor.class) {
         print(string + "\n");
         if (stdout == null){
            System.out.flush();
         } else {
            stdout.flush();
         }
      }
   }

   /**
    * Simple output routine to send a string to our currently set
    * output PrintWriter.
    * 
    * @param args string to output
    */
   public void print(String args) {
      if (stdout == null){
         System.out.print(args);
      } else {
         try {
            stdout.print(args);
         } catch (Throwable t) {
            System.err.println("Error using stdout: " + t);
            if (stdout != getDefaultStdout()){
               stdout = getDefaultStdout();
               System.err.println("Using default PrintStream");
            } else {
               /*
                * as we cannot output anything we cannot do
                * anything more, so throw something to disable
                * this exit.
                */
               throw new RuntimeException(
                     "Default PrintStream has failed!");
            }
            print(args);
         }
      }
   }

   /**
    * Format an array of bytes into a string containing the
    * bytes as hexadecimal characters and a printable representation of
    * the bytes as a string
    * 
    * @param bytes array to format
    * @return printable string
    */
   public static String dumpToHex(byte[] bytes) {
      String hexString = toHex(bytes);
      if (bytes != null){
         hexString += " '";
         for (int x = 0; x < bytes.length; x++) {
            byte ch = bytes[x];
            if ( (ch < 32) || (ch > 127) ) {
               ch = '?';
            }
            hexString += new String(Character.toChars(ch));
         }
         hexString += "'";
      }
      return hexString;
   }

   /**
    * Method to format an array of bytes into a readable string of
    * hexadecimal characters.
    * 
    * @param bytes array to format
    * @return String representing bytes in hex
    */
   public static String toHex(byte [] bytes){
      String hexString = "";
      if (bytes == null){
         hexString = "{empty}";
      } else {
         for (int x = 0; x < bytes.length; x++) {
            if ( (x > 0) && (x % 4 == 0) ){
               hexString += " ";
            }
            hexString += String.format("%02X", bytes[x] & 0xFF);
         }
      }
      return hexString;
   }
   
   /**
    * Create a multiline string of the significant system properties.
    * 
    * @return the system properties
    */
   public String getSystemEnvironment() {
      StringBuffer message = new StringBuffer("System properties :\n");
      appendProperty(message, "java.version");
      appendProperty(message, "java.vendor");
      appendProperty(message, "java.class.path");
      appendProperty(message, "java.class.version");
      appendOsDetails(message); 
      appendProperty(message, "java.library.path");
      appendProperty(message, "user.name");
      appendProperty(message, "user.dir");
      appendProperty(message, "user.language");
      appendProperty(message, "user.timezone");
      appendProperty(message, "file.encoding");
      return message.toString();
   }

   /**
    * Append the property details.
    * Some properties will throw a SecurityException if used on an 
    * untrusted applet.  Do a getProperty before the first append, 
    * so that we get a clean output if it generates a security exception.
    * 
    * @param message the message that is being built with each property
    * @param property the property to output
    */
   private void appendProperty(StringBuffer message, String property) {
      try {
         String tmp = System.getProperty(property);
         message.append(property);
         message.append(" = ");
         message.append(tmp);
         message.append('\n');
         tmp=null;
      } catch (SecurityException eSec) {
         message.append("Security Exception getting " + property + "\n");
      }      
   }

   /**
    * Append the details about the operating system.
    * 
    * @param message the message that is being built with each property
    */
   private void appendOsDetails(StringBuffer message) {
      message.append("OS details :- ");
      message.append(System.getProperty("os.name"));
      message.append(' ');
      message.append(System.getProperty("os.version"));
      message.append(' ');
      message.append(System.getProperty("os.arch"));
      message.append('\n');
   }

}
