/*          File Name     : EpiB1.java
*
*           Product       : CICS Transaction Gateway
*
*           Description   : This sample shows how to connect to a CICS server
*                           using the EPI API. It starts a transaction on a
*                           chosen server and outputs the reply to the console.
*
*           Pre-Requisites: This sample requires the CICS Transaction EP01 to
*                           be installed on your CICS server for operation.
*                           This sample cannot be run against a CICS Transaction
*                           Gateway running on z/OS, which does not support
*                           EPI calls.
*
*                           Use a version of the JDK that the CICS Transaction
*                           Gateway supports if you recompile this sample. See
*                           the product documentation for supported Java levels.
*
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

package com.ibm.ctg.samples.epi;

import java.io.*;
import java.util.Properties;
import com.ibm.ctg.client.*;

public class EpiB1 {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    // Declare static objects for use in multiple methods

    private static JavaGateway javaGatewayObject;
    private static InputStreamReader isr = new InputStreamReader(System.in);
    private static BufferedReader input = new BufferedReader(isr);
    private static String strTransid = "EP01"; //For this Transaction, the id is passed in the datastream
    private static int iBufferSize = 100;      //buffer which is large enough for EP01 transaction
    private static String strEncoding = "ASCII";

    // Main method

    public static void main(String[] args) {
        try {

            // Declare and initialize method variables and EPIRequest object

            EPIRequest epiRequestObject = null;
            String strChosenServer = null;
            String strInput = null;
            String strUrl = "local:";
            String strServerNumber = null;
            String strSSLKeyring  = null;
            String strSSLPassword = null;
            int iPort = 2006;

            /*
             * The maximum number of servers that will be listed.
             * Increase this value if you have a larger number of servers
             * defined in your configuration file
             */
            int iServerCount = 20;

            int iChoice = 0;
            int iCursorRow = 0;
            int iCursorColumn = 0;
            boolean bDebug = false; //If true, data returned is displayed in hex
            byte[] abytData = null;


            // Display banner

            System.out.println("\nCICS Transaction Gateway Basic EPI Sample 1\n");
            System.out.println("Usage: java com.ibm.ctg.samples.epi.EpiB1 "
                               + "[Gateway URL]");
            System.out.println("                                          "
                               + "[Gateway port number]");
            System.out.println("                                          "
                               + "[debug]");
            System.out.println("                                          "
                               + "[SSL keyring]");
            System.out.println("                                          "
                               + "[SSL password]\n");


            // Process the command line arguments and display Gateway settings
            switch (args.length) {
            case 5:
                strSSLKeyring  = args[3];
                strSSLPassword = args[4];
            case 4:
                if (args.length != 5) {
                    strSSLKeyring  = args[2];
                    strSSLPassword = args[3];
                }
            case 3:
                if ((args.length != 4) && ((args[2]).equalsIgnoreCase("debug"))) {
                    bDebug = true;
                }
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
            default:
                for (int i = 5; i < args.length; i++) {
                   System.out.println("Error - unrecognised argument: " + args[i]);
                }
                System.exit(0);
            }
            System.out.println("The address of the Gateway has been set to "
                               + strUrl + " port: " + iPort
                               + "\nDebugging: " + bDebug + " (determines whether"
                               + " the 3270 datastream is displayed)\n");


            /*
             * Set the keyring and keyring password and then initialize
             * the JavaGateway object to flow data to the Gateway
             */
            if (strSSLKeyring != null && strSSLPassword != null)
            {
               Properties sslProps = new Properties();
               sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_CLASS, strSSLKeyring.trim());
               sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_PW, strSSLPassword.trim());

               javaGatewayObject = new JavaGateway(strUrl, iPort, sslProps);
            } else {
              javaGatewayObject = new JavaGateway(strUrl, iPort);
            }


            /*
             * Obtain a list of CICS servers defined
             *
             * If an EPI_ERR_MORE_SYSTEMS error occurs this is because you have
             * more servers defined in your CICS TG configuration than the
             * value stored in the iServerCount variable. To solve this problem,
             * increase the value assigned to iServerCount, recompile and
             * rerun this sample.
             */

            epiRequestObject = EPIRequest.listSystems(iServerCount);
            flowRequest(epiRequestObject);


            // Ask for user to choose from the list of defined servers
            do {
                if (epiRequestObject.SystemList.isEmpty() == true) {
                    System.out.println("No CICS servers have been defined.");
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    System.exit(0);
                }
                System.out.println("CICS servers defined:\n");
                for (int i = 0; i < epiRequestObject.numServersReturned; i++) {
                    if (i < 9) {
                        strServerNumber = "\t " + (i + 1) + ". ";
                    } else {
                        strServerNumber = "\t" + (i + 1) + ". ";
                    }


                    /*
                     * Prints the name and description of the server as
                     * contained in the SystemList vector
                     */
                    System.out.println
                    (strServerNumber
                     + epiRequestObject.SystemList.elementAt(2*i) + " -"
                     + epiRequestObject.SystemList.elementAt((2*i)+1));
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
                                       + "server to connect to or q to quit");
                    continue;
                }


                /*
                 * Validate that the choice is in range using the
                 * EPIRequest numServersReturned variable
                 */
                if (iChoice > 0 && iChoice <= epiRequestObject.numServersReturned) {
                    strChosenServer =
                    (String)epiRequestObject.SystemList.elementAt(((iChoice-1)*2));
                } else {
                    System.out.println("\nOut of range\n\n");
                }
            } while (strChosenServer == null);


            // Create 3270 dataStream to flow to the Gateway
            abytData = new byte[iBufferSize];

            abytData[0] = 0x27; //AID key. This byte represents 'Enter'
            abytData[1] = (byte)iCursorRow;
            abytData[2] = (byte)iCursorColumn;
            System.arraycopy(strTransid.getBytes(strEncoding), 0, abytData, 3, 4);


            /*
             * Use the addTerminal constructor to re-initialize the EPIRequest
             * object to connect to the chosen server
             */
            System.out.println("\nCreating a terminal on server "
                               + strChosenServer);
            epiRequestObject =
            EPIRequest.addTerminal(strChosenServer, //CICS server
                                   null,            //CICS terminal resource name
                                   null);           //CICS terminal model name

            /*
             * Call the flowRequest method and display returned data in hex and
             * ASCII format. If the method returns true a security error has
             * occurred and the user is prompted for a CICS user ID and password.
             */
            int attempts = 0;
            while (flowRequest(epiRequestObject) == true) {

                //increment the number of attempts
                attempts++;
                if (attempts <= 3) {
                    System.out.println("Validation failed\nPlease enter your CICS details.");
                    System.out.println("\nEnter your CICS user ID:");
                    String userid = input.readLine().trim();
                    System.out.println("\nEnter your CICS password:");
                    String password = input.readLine().trim();
                    //add a new terminal to the request object which has the uid/pwd set
                    epiRequestObject = EPIRequest.addTerminal(strChosenServer,
                                                              null,
                                                              null,
                                                              userid,
                                                              password,
                                                              0,0,0,0);
                } else {

                    //There have been too many unsuccessful attempts
                    System.out.println("Unable to log you onto the terminal");
                    System.exit(1);
                }
            }


            /*
             * Display information about the terminal before calling the
             * startTran method to start the EP01 Transaction by sending the
             * pre-constructed byte array
             */
            System.out.println(" Connected on terminal: "
                               + epiRequestObject.termID);
            System.out.println(" Terminal dimensions = "
                               + epiRequestObject.numLines
                               + "x" + epiRequestObject.numColumns);
            System.out.println("\n Starting " + strTransid + " on "
                               + strChosenServer);
            epiRequestObject.startTran(null,     //Transid
                                       abytData, //Byte array of 3270 datastream (Cannot be null)
                                       7);       //Size of datastream to be passed to Transaction, in bytes
            flowRequest(epiRequestObject);


            // Call the obtainEvent method to request an event from the terminal
            obtainEvent(epiRequestObject);


            /*
             * Check the Event set in the EPIRequest object for an END_TERM
             * Event which indicates that the terminal has been deleted
             */
            while (epiRequestObject.getEvent() != EPIRequest.EPI_EVENT_END_TERM) {


                /*
                 * Flow a delTerminal request to the Gateway if an END_TRAN
                 * Event shows that the Transaction has ended
                 */
                if (epiRequestObject.getEvent() == EPIRequest.EPI_EVENT_END_TRAN) {
                    System.out.println(" End of transaction; end reason "
                                       + epiRequestObject.getEndReasonString()
                                       + "\n");
                    epiRequestObject.delTerminal();
                    flowRequest(epiRequestObject);
                } else {


                    /*
                     * Call method to display the data returned to the
                     * EPIRequest object when a SEND Event has been received
                     */
                    dumpDatastream(epiRequestObject.data, epiRequestObject.size, bDebug);

                }
                obtainEvent(epiRequestObject);
            }


            /*
             * Display the relevant information held in the EPIRequest object
             * after the terminal has been deleted.
             */
            System.out.println(" Terminal deleted; end reason "
                               + epiRequestObject.getEndReasonString() + "\n");
            if (epiRequestObject.getEndReason() != EPIRequest.EPI_TRAN_NO_ERROR) {
                System.out.println(" EPI returned: "
                                   + epiRequestObject.getEndReturnCodeString());
            } else {
                System.out.println(" Event type "
                                   + epiRequestObject.getEventString()
                                   + " received from CICS.");
                dumpDatastream(epiRequestObject.data, epiRequestObject.size, bDebug);
            }
            System.out.println("\nTerminating EPI");


            //Close the JavaGateway object before exiting
            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("The " + strEncoding + " format is not "
                               + "supported.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
     * The flowRequest method flows data contained in the EPIRequest object to
     * the Gateway and determines whether it has been successful by checking
     * the return codes. If an error has occurred, the return code string is
     * printed to describe the error before the program exits.
     *
     * If a security error occurs then the method returns true else false
     */
    private static boolean flowRequest(EPIRequest requestObject) {
        try {
            int iRc = javaGatewayObject.flow(requestObject);


            // Checks for Gateway errors and returns if none are found
            switch (requestObject.getCicsRc()) {
            case EPIRequest.EPI_NORMAL:
                if (iRc != 0) {
                    System.out.println("\nError from Gateway ("
                                       + requestObject.getRcString()
                                       + "), correct and rerun this sample");
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    System.exit(0);
                } else {
                    return false;
                }

            // Checks for security errors and returns true
            case EPIRequest.EPI_ERR_SECURITY:
                return true;

            case EPIRequest.EPI_ERR_MORE_EVENTS:
                if (requestObject.getEvent() == EPIRequest.EPI_EVENT_END_TERM) {
                    return false;
                }
                if (requestObject.getEndReason() != EPIRequest.EPI_TRAN_NO_ERROR) {
                    System.out.println(" EPI returned: "
                                       + requestObject.getEndReasonString());
                }
                return false;
            }
            System.out.println("\nEPI returned: "
                               + requestObject.getCicsRcString());
            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }



    /*
     * The obtainEvent method flows a getEvent request to the Gateway and waits
     * for the Event to be returned. A check is made for Gateway errors, which
     * would cause the program to be terminated. Although all of the return
     * codes from the getEvent call are displayed, it is beyond the scope of
     * the basic sample to handle them.
     * The Event type is then printed to screen before control reverts to the
     * calling method.
     */
    private static void obtainEvent(EPIRequest requestObject) {
        try {
            requestObject.getEvent
            (EPIRequest.EPI_WAIT, //Wait for event
             iBufferSize);        //MAXIMUM size of data to be returned, in bytes
            int iRc = javaGatewayObject.flow(requestObject);
            switch (requestObject.getCicsRc()) {
            case EPIRequest.EPI_NORMAL:
                if (iRc != 0) {
                    System.out.println("\nError from Gateway ("
                                       + requestObject.getRcString()
                                       + "), correct and rerun this sample");
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    System.exit(0);
                }
            case EPIRequest.EPI_ERR_BAD_INDEX:
            case EPIRequest.EPI_ERR_FAILED:
            case EPIRequest.EPI_ERR_MORE_EVENTS:
            case EPIRequest.EPI_ERR_MORE_DATA:
            case EPIRequest.EPI_ERR_NO_EVENT:
            case EPIRequest.EPI_ERR_NOT_INIT:
            case EPIRequest.EPI_ERR_WAIT:
            case EPIRequest.EPI_ERR_NULL_PARM:
            case EPIRequest.EPI_ERR_IN_CALLBACK:
                System.out.println(" Event type "
                                   + requestObject.getEventString()
                                   + " received from CICS.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
     * The dumpDatastream method displays the data in the byte array passed to
     * it as a String formed from the byte array in the encoding specified by
     * strEncoding. If debugging has been switched on, the String is displayed
     * alongside the hexadecimal character codes for the data in the byte array.
     */
    private static void dumpDatastream(byte[] data, int dataSize, boolean debug) {
        try {
            int totalToDump = dataSize;
            System.out.println("\n Datastream length = " + dataSize);
            System.out.println(" Buffer for datastream length = " + data.length);
            if (data.length < dataSize) {
                System.out.println(" Datastream has been truncated");
                totalToDump = data.length;
            }

            if (debug == false) {
                System.out.println("\n Data returned: " +
                                   new String(data, 2, (totalToDump - 2), strEncoding));
                return;
            }
            int row = 0;
            int column = 0;
            int character = 0;
            int width = 16;

            System.out.println("\n Datastream returned:");
            for (row = 0; character < totalToDump; row++) {
                System.out.print("   ");
                while ((column < width) && (character < totalToDump)) {
                    if (data[character] != 0) {
                        System.out.print(Integer.toHexString(data[character]));
                    } else {
                        System.out.print("  ");
                    }
                    character++;
                    column++;
                }
                System.out.print("   " +
                                 new String(data, (row*width), column, strEncoding)
                                 + "\n");
                column = 0;
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("The " + strEncoding + " format is not "
                               + "supported.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
