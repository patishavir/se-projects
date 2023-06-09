/*
*         File Name     : EciB1.java
*
*         Product       : CICS Transaction Gateway
*
*         Description   : This sample shows the basic use of the CICS
*                         Transaction Gateway API. It queries the Client for
*                         a list of available servers and then launches
*                         a transaction on the server chosen.
*
*         Pre-Requisites: This sample requires the CICS Program EC01 to
*                         be installed on your CICS server for operation.
*                         The CICS server must be set up to return the
*                         contents of the commarea as ASCII text.
*                         If the code page of the application is different
*                         from the code page of the server, data conversion
*                         must be performed at the server by making use of
*                         CICS-supplied resource conversion capabilities,
*                         such as the DFHCNV macro definitions.
*
*                         Use a version of the JDK that the CICS Transaction
*                         Gateway supports if you recompile this sample. See
*                         the product documentation for supported Java levels.
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
*         The following code is sample code created by IBM Corporation.  This
*         sample code is not part of any standard IBM product and is provided
*         to you solely for the purpose of assisting you in the development of
*         your applications.  The code is provided 'AS IS', without warranty
*         or condition of any kind.  IBM shall not be liable for any damages
*         arising out of your use of the sample code, even if IBM has been
*         advised of the possibility of such damages.
*/

package com.ibm.ctg.samples.eci;

import java.io.*;
import java.util.Properties;
import com.ibm.ctg.client.*;

public class EciB1 {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 


   // Declare objects for use in both methods
   private static JavaGateway javaGatewayObject;
   private static InputStreamReader isr = new InputStreamReader(System.in);
   private static BufferedReader input = new BufferedReader(isr);
   private static int iValidationFailed = 0;

   // Main method
   public static void main(String[] args) {
      try {

         // Declare and initialize method variables and ECIRequest object
         ECIRequest eciRequestObject = null;
         String strProgram = "EC01";
         String strChosenServer = null;
         String strInput = null;
         String strUrl = "local:";
         String strServerNumber = null;
         String strSSLKeyring  = null;
         String strSSLPassword = null;
         int iPort = 2006;

         /*
         * The maximum number of servers that will be listed
         * Increase this value if you have a larger number of servers defined
         * in your CTG.INI file
         */
         int iServerCount = 30;

         int iChoice = 0;
         int iCommareaSize = 18;
         byte[] abytCommarea = new byte[iCommareaSize];

         // Display banner
         System.out.println("\nCICS Transaction Gateway Basic ECI Sample 1");
         System.out.println("\nUsage: java com.ibm.ctg.samples.eci.EciB1 "
                            + "[Gateway URL]");
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
            System.exit(0);
         default:
            for (int i = 4; i < args.length; i++) {
               System.out.println("ERROR - unrecognised argument: " + args[i]);
            }
            System.exit(0);
         }
         System.out.print("The address of the Gateway has been set to ");
         System.out.println(strUrl + " port:" + iPort + ".\n");
         
         // Check if running in local mode under a 64-bit JVM, which is only
         // supported on z/OS
         if (strUrl.equalsIgnoreCase("local:")) {
            String osName = System.getProperty("os.name", "");
            String bitmode = System.getProperty("sun.arch.data.model", "");
            if (!osName.equals("z/OS") && bitmode.equals("64")) {
               System.out.println("ERROR - cannot run this sample in local mode using a 64-bit JVM");
               System.exit(0);
            }
         }

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
         flowRequest(eciRequestObject);

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

               /*
               * Prints the name and description of the server as contained
               * in the SystemList vector
               */
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
                                  + "server to connect to or q to quit.\n");
               continue;
            }

            /*
            * Validate that the choice is in range using the
            * ECIRequest numServersReturned variable
            */
            if (iChoice > 0 && iChoice <= eciRequestObject.numServersReturned) {
               strChosenServer = eciRequestObject.SystemList.elementAt(((iChoice-1)*2));
            } else {
               System.out.println("\n\nOut of range.\n");
            }
         } while (strChosenServer == null);

         /*
         * Use the extended constructor to set the parameters on the
         * ECIRequest object
         */
         eciRequestObject =
         new ECIRequest(ECIRequest.ECI_SYNC,      //ECI call type
                        strChosenServer,          //CICS server
                        null,                     //CICS userid
                        null,                     //CICS password
                        strProgram,               //CICS program to be run
                        null,                     //CICS transid to be run
                        abytCommarea,             //Byte array containing the
                                                  // COMMAREA
                        iCommareaSize,            //COMMAREA length
                        ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                        0);                       //ECI LUW token

         /*
         * Call the flowRequest method and display returned data in hex and
         * ASCII format. If the method returns true a security error has
         * occurred and the user is prompted for a CICS user ID and password.
         */
         while (flowRequest(eciRequestObject) == true) {
            System.out.println("\nEnter your CICS user ID:");
            eciRequestObject.Userid = input.readLine().trim();
            System.out.println("\nEnter your CICS password or password phrase:");
            eciRequestObject.Password = input.readLine().trim();
            iValidationFailed++;
         }
         System.out.println("\nProgram " + strProgram
                            + " returned with data:- \n");
         System.out.print("\tHex: ");
         for (int i = 0; i < abytCommarea.length; i++) {
            System.out.print(Integer.toHexString(abytCommarea[i]));
         }
         try {
            System.out.println("\n\tASCII text: "
                               + new String(abytCommarea, "ASCII"));
         } catch (UnsupportedEncodingException e) {
            System.out.println
            ("\tThe ASCII encoding scheme is not supported.");
         }

         // Close the JavaGateway object before exiting
         if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /*
   * The flowRequest method flows data contained in the ECIRequest object to
   * the Gateway and determines whether it has been successful by checking the
   * return code. If an error has occurred, the return code string and abend
   * codes are printed to describe the error before the program exits.
   * Note: Security may be required for client connection to the server and
   *       not just for the ECI request. Refer to the security chapter in the
   *       product documentation for further details.
   */
   private static boolean flowRequest(ECIRequest requestObject) {
      try {
         int iRc = javaGatewayObject.flow(requestObject);

         // Checks for gateway errors and returns false if there are no errors
         switch (requestObject.getCicsRc()) {
         case ECIRequest.ECI_NO_ERROR:
            if (iRc == 0) {
               return false;
            } else {
               System.out.println("\nError from Gateway ("
                                  + requestObject.getRcString()
                                  + "), correct and rerun this sample.");
               if (javaGatewayObject.isOpen() == true) {
                  javaGatewayObject.close();
               }
               System.exit(0);
            }

         /*
         * Checks for security errors and returns true if validation has
         * failed on four or less occasions
         */
         case ECIRequest.ECI_ERR_SECURITY_ERROR:
            if (iValidationFailed == 0) {
               return true;
            }
            System.out.print("\n\nValidation failed. ");
            if (iValidationFailed < 3) {
               System.out.println("Try entering your details again.");
               return true;
            }
            break;

         /*
         * Checks for transaction abend errors where the user is authorised
         * to access the server but not run the EC01 program.
         * The sample should be rerun and a user ID and password with the
         * required authorisation entered.
         */
         case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
            System.out.println("\nAn error was returned from the server."
                               + "\nRefer to the abend code for further details.");
         }
         System.out.println("\nECI returned: "
                            + requestObject.getCicsRcString());
         System.out.println("Abend code was "
                            + requestObject.Abend_Code + ".\n");
         if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
         }
         System.exit(0);

      } catch (Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
      return true;
   }
}
