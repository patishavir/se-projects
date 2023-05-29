/*
*    File Name     : EC03ChannelClient.java
*
*    Product       : CICS Transaction Gateway
*
*    Description   : This is a basic client which calls the EC03 Channel EJB.
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

import java.util.HashMap;
import java.util.Set;

import java.rmi.RemoteException;
import javax.resource.ResourceException;
import javax.ejb.CreateException;
import javax.naming.*;
import javax.rmi.*;

public class EC03ChannelClient {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2008, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   public static void main(String[] args) {

      try {
         
         EC03Channel chanEjb;
         
         try {
            //Create the initial context and look up the EC03Channel bean.
            Context context = new InitialContext();
            Object obj = context.lookup(
               "ejb/com/ibm/ctg/samples/jee/EC03ChannelHome"
            );
            
            //Check that an object was retrieved
            if (obj == null) {
               System.out.println(
                     "Lookup for EC03ChannelHome resulted in a null reference");
               return;
            }
            
            //Retrieved a reference to the EJB home interface. Narrow it and
            //then create the EJB bean, obtaining the remote interface.
            EC03ChannelHome home = (EC03ChannelHome)
               PortableRemoteObject.narrow(obj, EC03ChannelHome.class);
            chanEjb = home.create();
            
         } catch (NamingException ne) {
            System.out.println("Failed to get a reference to EC03ChannelHome");
            throw ne;
         } catch (CreateException ce) {
            System.out.println("Failed to create the EJB");
            throw ce;
         }
         
         
         //Set the text to pass to the CICS program
         String inputData = "Sample data from EC03ChannelClient";
                
         //Call the business method, which returns a collection of strings
         //representing the names and contents of all containers returned
         //by the CICS program
         HashMap<String,String> response;
         try {
            response = chanEjb.execute(inputData);
         } catch (ResourceException re) {
            System.out.println("The EJB business method failed");
            throw re;
         }
                
         
         System.out.println("Response received.");
                
         //Display each name and value pair returned by the EJB
         System.out.println("Containers returned by the program:");
         Set<String> keys = response.keySet();
         for (String key : keys) {
             
            //Pad name to 16 chars for formatting.
            //Container names are limited to 16 chars.
            String paddedKey = (key + "                ").substring(0, 16);
            System.out.print("Name: " + paddedKey);
            System.out.println("\tValue: " + response.get(key));
         }
         
         //Remove the EJB
         chanEjb.remove();
         
      } catch (RemoteException rme) {
         System.out.println("An exception was thrown during communication" +
                            " with a remote object");
         //Print a stack trace
         rme.printStackTrace();
        
      } catch (Exception e) {
         //Print a stack trace
         e.printStackTrace();
      }
   }
}
