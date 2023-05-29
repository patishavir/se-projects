/*          File Name     : EsiB1.java
*
*           Product       : CICS Transaction Gateway
*
*           Description   : This sample shows the basic principles of the
*                           ESI API. It takes user supplied values for UserID
*                           and Password and verifies them on a chosen server.
*
*           Pre-Requisites: Before using this sample ensure that CICS program 
*                           EC01 is installed on your CICS server. The CICS 
*                           server must also support password expiry management.
*                           The communication protocol used for connections to 
*                           CICS must be one of the following:
*                           
*                           CICS TG for Multiplatforms: - SNA (local mode or 
*                                                         remote mode)
*                                                       - IPIC (remote mode)
*                                                
*                           CICS TG for z/OS:           - IPIC (remote mode)
*
*                           Use a version of the JDK that the CICS Transaction
*                           Gateway supports if you recompile this sample. See
*                           the product documentation for supported Java levels.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5725-B65,5655-Y20 
*  
* (C) Copyright IBM Corp. 2001, 2014 All Rights Reserved.  
*  
* US Government Users Restricted Rights - Use, duplication or  
* disclosure restricted by GSA ADP Schedule Contract with  
* IBM Corp.  
* 
* Status: Version 9 Release 1 
*
*           The following code is sample code created by IBM Corporation.  This
*           sample code is not part of any standard IBM product and is provided
*           to you solely for the purpose of assisting you in the development of
*           your applications.  The code is provided 'AS IS', without warranty
*           or condition of any kind.  IBM shall not be liable for any damages
*           arising out of your use of the sample code, even if IBM has been
*           advised of the possibility of such damages.
*/


package com.ibm.ctg.samples.esi;

import java.io.*;
import java.util.*;
import java.text.*;
import com.ibm.ctg.client.*;

public class EsiB1 {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   // Declare objects for use in both methods

   private static JavaGateway javaGatewayObject;
   private static InputStreamReader isr = new InputStreamReader(System.in);
   private static BufferedReader input = new BufferedReader(isr);


   // Main method

   public static void main(String[] args) {
      try {

         // Declare and initialize method variables and objects

         ECIRequest eciRequestObject = null;
         ESIRequest esiRequestObject = null;
         DateFormat dateFormatter = DateFormat.getDateInstance();
         DateFormat timeFormatter = DateFormat.getTimeInstance();
         String strServerNumber = null;
         String strChosenServer = null;
         String strInput = null;
         String strUrl = "local:";
         String strUserid = "";
         String strPassword = "";
         String strSSLKeyring = null;
         String strSSLPassword = null;
         int iPort = 2006;

         /*
         * The maximum number of servers that will be listed
         * Increase this value if you have a larger number of servers defined
         * in your CTG.INI file
         */
         int iServerCount = 20;

         int iChoice = 0;

         // Display banner

         System.out.println("\nCICS Transaction Gateway Basic ESI Sample 1\n");
         System.out.println("Usage: java com.ibm.ctg.samples.esi.EsiB1 "
                            + "[Gateway Url]");
         System.out.println("                                          "
                            + "[Gateway port number]");
         System.out.println("                                          "
                            + "[SSL keyring]");
         System.out.println("                                          "
                            + "[SSL password]");
         System.out.println("\nTo enable client tracing, run the sample with "
                            + "the following Java option:");
         System.out.println(" -Dgateway.T.trace=on\n");


         // Process the command line arguments and display Gateway settings

         switch (args.length) {
         case 4:
            strSSLKeyring  = args[2].trim();
            strSSLPassword = args[3].trim();
         case 2:
            try {
               iPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
               System.out.println("Invalid port number.");
               System.exit(0);
            }
         case 1:
            strUrl = args[0];
         case 0:
            break;
         case 3:
            System.out.println("An SSL password must be specified if " +
                               "using an SSL keyring.");
            return;
         default:
            for (int i = 4; i < args.length; i++) {
               System.out.println("Error - unrecognised argument: " + args[i]);
            }
            return;
         }
         System.out.print("The address of the Gateway has been set to ");
         System.out.println(strUrl + " port:" + iPort + "\n");

         /*
         * Set the keyring and keyring password and then initialize
         * the JavaGateway object to flow data to the Gateway
         */
         if (strSSLKeyring != null && strSSLPassword != null)
         {
            Properties sslProps = new Properties();
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_CLASS, strSSLKeyring);
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_PW, strSSLPassword);

            javaGatewayObject = new JavaGateway(strUrl, iPort, sslProps);
         } else {
            javaGatewayObject = new JavaGateway(strUrl, iPort);
         }

         /*
         * Obtain a list of CICS servers defined
         *
         * If an ECI_ERR_MORE_SYSTEMS error occurs this is because you have more servers
         * defined in your CICS TG configuration than the value stored in the iServerCount
         * variable. To solve this problem, increase the value assigned to iServerCount,
         * recompile and rerun this sample.
         */

         eciRequestObject = ECIRequest.listSystems(iServerCount);
         int iRc = javaGatewayObject.flow(eciRequestObject);
         switch (eciRequestObject.getCicsRc()) {
         case ECIRequest.ECI_NO_ERROR:
            if (iRc == 0) {
               break;
            } else {
               System.out.println("Error from Gateway ("
                                  + eciRequestObject.getRcString()
                                  + "), correct and rerun this sample");
               if (javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }
               System.exit(0);
            }
         default:
            System.out.println("\nECI returned: "
                               + eciRequestObject.getCicsRcString());
            System.out.println("Abend code was "
                               + eciRequestObject.Abend_Code + "\n");
            if (javaGatewayObject.isOpen() == true) {
               javaGatewayObject.close();
            }
            System.exit(0);
         }


         // Ask for user to choose from the list of defined servers

         do {
            if (eciRequestObject.SystemList.isEmpty() == true) {
               System.out.println("No CICS servers have been defined.");
               if (javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }
               System.exit(0);
            }
            System.out.println("CICS servers defined:\n");
            for (int i = 0; i < eciRequestObject.numServersReturned; i++) {
               if (i < 9) {
                  strServerNumber = "\t " + (i + 1) + ". ";
               } else {
                  strServerNumber = "\t" + (i + 1) + ". ";
               }


               /* Prints the name and description of the server as contained
                  in the SystemList vector */

               System.out.println
                (strServerNumber
                 + eciRequestObject.SystemList.elementAt(2*i) + " -"
                 + eciRequestObject.SystemList.elementAt((2*i)+1));
            }
            System.out.println("\n\nChoose server to connect to,"
                               + " or q to quit:");
            strInput = input.readLine().trim();
            if ((strInput).equalsIgnoreCase("q")) {
               if (javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }
               System.exit(0);
            }
            try {
               iChoice = java.lang.Integer.parseInt(strInput);
            } catch (java.lang.NumberFormatException e) {
               System.out.println("\n\nType the number of the "
                                  + "server to connect to or q to quit\n");
               continue;
            }


            /*
            * Validate that the choice is in range using the
            * ECIRequest numServersReturned variable
            */

            if (iChoice > 0 && iChoice <= eciRequestObject.numServersReturned) {
               strChosenServer = eciRequestObject.SystemList.elementAt(((iChoice-1)*2));
            } else {
               System.out.println("\n\nOut of range\n");
            }
         } while (strChosenServer == null);


         // Get user ID and password to verify on server

         System.out.println("\n\nEnter your user ID for verification:");
         strUserid = input.readLine();
         System.out.println("\nEnter password or password phrase for user " + strUserid + ":");
         strPassword = input.readLine();


         // Use ESI to check if password is valid

         System.out.println("\nVerifying password\n");
         esiRequestObject = ESIRequest.verifyPassword(strUserid,
                                                      strPassword,
                                                      strChosenServer);
         flowRequest(esiRequestObject);
         Date dateLastVerified = esiRequestObject.getLastVerified().getTime();
         Date dateExpires = esiRequestObject.getExpiry().getTime();
         System.out.println("Password verified.");
         System.out.println("  User ID and password or password phrase last verified on: "
                            + dateFormatter.format(dateLastVerified)
                            + " at "
                            + timeFormatter.format(dateLastVerified));
         if (esiRequestObject.passwordExpirySet()){
            System.out.println("  Password or password phrase expires on: "
                               + dateFormatter.format(dateExpires)
                               + " at "
                               + timeFormatter.format(dateExpires));
         } else {
            System.out.println("  Password or password phrase is non-expiring");
         }


         // Close the JavaGateway object before exiting

         if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   /* The flowRequest method flows data contained in the ESIRequest object to
    * the Gateway and determines whether it has been successful by checking the
    * return code. If an error has occurred, the return code string describing
    * the error is returned before the program exits.
    * Note: Security may be required for client connection to the server and
    * not just for the ESI request. Refer to the security chapter in the
    * product documentation for further details.
    */

   private static void flowRequest(ESIRequest requestObject) {
      try {
         int iRc = javaGatewayObject.flow(requestObject);

         // Checks for gateway errors and returns if there are no errors

         switch (requestObject.getCicsRc()) {
         case ESIRequest.ESI_NO_ERROR:
            if (iRc == 0) {
               return;
            } else {
               System.out.println("Error from Gateway ("
                                  + requestObject.getRcString()
                                  + "), correct and rerun this sample");
               if (javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }
               System.exit(0);
            }

         // Checks whether the password has expired
         case ESIRequest.ESI_ERR_PASSWORD_EXPIRED:
            System.out.println("Password expired.");
            break;
         default:
            System.out.println("Verification failed."
                               + "\nESI returned: "
                               + requestObject.getCicsRcString());
         }
         if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
         }
         System.exit(0);

      } catch (Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
   }
}
