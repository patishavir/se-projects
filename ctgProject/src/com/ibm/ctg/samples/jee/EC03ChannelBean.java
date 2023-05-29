/*
*    File Name     : EC03ChannelBean.java
*         
*    Product       : CICS Transaction Gateway
*
*    Description   : This is the implementation of the EC03 Channel EJB
*
*                    This is part of the JEE EC03 Channel Bean sample which
*                    demonstrates the support for channels and containers in 
*                    the CICS ECI resource adapter. Channels and containers are
*                    only supported with the IPIC protocol.
*                  
*    Pre-requisites: Requires a JEE server with the CICS ECI resource adapter
*                    or CICS ECI XA resource adapter deployed and an ECI
*                    connection factory defined. The connection factory must be
*                    configured to use a CICS server over an IPIC connection and
*                    the CICS server must have the EC03 program installed.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5655-Y20 
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

package com.ibm.ctg.samples.jee;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;

import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;


public class EC03ChannelBean implements SessionBean {
   private static final long serialVersionUID = 1549981453803444656L;

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2008, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 


   //The name of the CICS program to execute
   private static final String PROGRAM_NAME = "EC03";
   
   //The name of the channel which will be passed to the program
   private static final String CHANNEL_NAME = "ECICHAN";
   
   //The name of the input container which will be added to the channel
   private static final String INPUT_CONTAINER_NAME = "INPUTDATA";
    

   //The ECI connection used to perform the request
   private Connection eciConn;
   
   //The ECI interaction used to perform the request
   private Interaction eciInt;
   
   //The properties of the ECI request
   private ECIInteractionSpec eSpec;
   
   
   /**
    * This method is called when a request to create the EJB is made.
    */
   public void ejbCreate() throws javax.ejb.CreateException {
      //Create an Interaction spec to work with, This is specific to ECI.
      eSpec = new ECIInteractionSpec();
   }
   
   
   /**
    * Invokes the EC03 CICS program with the specified input string, and returns
    * the names and contents of the containers returned from the program.
    * 
    * @param inputData The string to pass to the EC03 program.
    * @return A HashMap containing the names and contents of the containers
    *         returned by the EC03 program.
    * @throws ResourceException If the EJB is unable to create a connection or
    *         interaction.
    */
   public HashMap<String,String> execute(String inputData)
      throws ResourceException {
      
      //Create an ECI connection and an interaction
      getConnection();

      //Create a channel
      ECIChannelRecord inputRec = new ECIChannelRecord(CHANNEL_NAME);
      ECIChannelRecord outputRec = new ECIChannelRecord(CHANNEL_NAME);
      
      //Add a single CHAR container to the channel, placing inputData into the
      //container. Codepage conversion for CHAR containers is automatically
      //provided by the Channels and Containers architecture
      inputRec.put(INPUT_CONTAINER_NAME, inputData);

      //Set up the ECIInteractionSpec
      eSpec.setFunctionName(PROGRAM_NAME);
      eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);

      //Make the ECI call
      try {
         eciInt.execute(eSpec, inputRec, outputRec);
         
      } catch (ResourceException e) {
         //Output the stacktrace and re-throw it back to the client.
         e.printStackTrace();
         //Close the ECI connection
         dropConnection();
         throw e;
      }
      
      //Close the ECI connection
      dropConnection();

      //Place returned containers into results variable
      HashMap<String,String> results = new HashMap<String,String>();
      
      //Iterate over the containers in the channel
      for (Object container : outputRec.keySet()) {
         Object containerData = outputRec.get(container);
         
         if (containerData instanceof String) {
            //CHAR containers contain text data, so their contents are returned
            //as strings
            results.put(container.toString(), containerData.toString());
            
         } else {
            //BIT containers contain binary data, so their contents are returned
            //as byte arrays
            
            //Format the binary data as a hexadecimal string.
            String hexString = formatBinaryData((byte[]) containerData);

            //Add to results
            results.put(container.toString(), hexString);               
         }
      }
      
      //Return results to the caller
      return results;
   }
   
   
   /**
    * Gets a connection to the EIS using the ECI resource adapter.
    * 
    * @throws ResourceException If the EJB is unable to create a connection or
    *         interaction.
    */
   private void getConnection() throws ResourceException {

      //Lookup an ECI connection factory, get a connection from the connection
      //factory and then get an interaction. You will need to ensure that you
      //setup your environment so that this JNDI name refers to the required
      //ECI connection factory.
      ConnectionFactory connFactory = null;
      Object cfLookup = null;

      //Obtain a reference to the ECI connection factory
      try {
         javax.naming.Context context = new javax.naming.InitialContext();
         cfLookup = context.lookup("java:comp/env/ECI");
      } catch (Exception e) {
         e.printStackTrace();
         throw new ResourceException(
               "Lookup for java:comp/env/ECI failed: " + e.getMessage());
      }
      
      //Check that an object was retrieved
      if (cfLookup == null) {
         throw new ResourceException(
               "Lookup for java:comp/env/ECI resulted in a null reference");
      }

      //Cast returned object to a ConnectionFactory
      connFactory = (ConnectionFactory) cfLookup;

      //Obtain a connection
      try {
         eciConn = (Connection) connFactory.getConnection();
      } catch (ResourceException e) {
         e.printStackTrace();
         throw new ResourceException(
               "Failed to get a connection: " + e.getMessage());
      }
      
      //Obtain an interaction
      try {
         eciInt = (Interaction) eciConn.createInteraction();
      } catch (ResourceException e) {
         e.printStackTrace();
         throw new ResourceException(
               "Failed to get an interaction: " + e.getMessage());
      }
   }

   
   /**
    * Drops the connection to the EIS.
    */
   private void dropConnection() {
      //Tidy up the interaction and connection and set references to null
      try {
         eciInt.close();
         eciConn.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      eciInt = null;
      eciConn = null;
   }
   

   /**
    * Formats byte data into a hexademical string.
    * 
    * @param data The bytes to format
    * @return A String containing the hexadecimal values of the bytes
    */
   private String formatBinaryData(byte[] data) {
      //Turn into hexadecimal string
      StringBuilder hexString = new StringBuilder(data.length * 4);
      hexString.append("[HEX] ");
      
      //Convert each byte into hex, separated by spaces
      for (int i = 0; i < data.length; i++) {
        String hex = "0" + Integer.toHexString(data[i]);
        hexString.append(hex.substring(hex.length() - 2));
        hexString.append(' ');
        
        //Insert newlines every 16 chars
        if ((i % 16 == 0) && (i != 0)) { 
           hexString.append("\n      ");
        }
      }
      return hexString.toString();
   }

   
   /**
    * This method is called when the EJB is to be removed.
    */
   public void ejbRemove() throws java.rmi.RemoteException {
      eSpec = null;
   }
   
   /**
    * Part of session bean interface, should not be called for stateless bean.
    */
   public void ejbActivate() throws EJBException, RemoteException {
   }

   /**
    * Part of session bean interface, should not be called for stateless bean.
    */
   public void ejbPassivate() throws EJBException, RemoteException {
   }

   /**
    * Part of session bean interface.
    */
   public void setSessionContext(SessionContext arg0)
      throws EJBException, RemoteException {
   }

}
