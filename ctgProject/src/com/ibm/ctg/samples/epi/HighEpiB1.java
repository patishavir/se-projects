/*          File Name      : HighEpiB1.java
*
*           Product        : CICS Transaction Gateway
*
*           Description    : This is a sample class to demonstrate the basic
*                            features of the CICS Transaction Gateway Higher
*                            Level EPI classes.
*
*           Pre-requisites : The transaction selected by the user must be
*                            installed on your CICS Server.
*
*                            This sample will not run on z/OS.
*
*                            Use a version of the JDK that the CICS Transaction
*                            Gateway supports if you recompile this sample. See
*                            the product documentation for supported Java levels.
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
import com.ibm.ctg.epi.*;


/* This class is a basic example aimed at clearly
 * illustrating the basic connectivity features
 * provided by the High Level EPI classes. It is not
 * a template and should not be used as a foundation
 * for your next mission critical application. The
 * program can accept two command line parameters,
 * the URL of the Gateway daemon that will be
 * connected to and the port number. Both
 * parameters are optional. If they are not specified
 * then the local gateway and a default port will be
 * selected.
 */

public class HighEpiB1 {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    private static EPIGateway gateway;

    public static void main(String[] args) {

        /* Aims at providing a clear example of basic
         * connectivity using the High Level EPI classes.
         * The method connects to the gateway,
         * requests the user to select a server, adds a
         * terminal to the server, requests and sends a
         * transaction, displays the result to the user
         * and finally checks the state of the server
         * before disconnecting.
         */
        Terminal cicsTerminal = null;
        Screen screen;
        String server = "";
        String transactionId = "";
        int state = 0;
        final int NO_SERVER_SELECTED = -1;
        final int DEFAULT_PORT = 0;
        final String DEFAULT_URL = "local:";

        try {

            // Display Banner

            System.out.println("\n\n\n");
            System.out.println("CICS Transaction Gateway "
                               + "Basic High Level EPI Sample");
            System.out.println("\n");

            /* First process the command line arguments.
             * Note that the switch statement will fall through.
             */

            gateway = new EPIGateway();

            switch (args.length) {
            case 2:
                try {
                    gateway.setPort(Integer.parseInt(args[1].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number");
                    System.out.println("Usage: java"
                                       + " com.ibm.ctg.samples.epi.HighEpiB1"
                                       + " [GatewayUrl]"
                                       + " [port]");
                    return;
                }
            case 1:
                gateway.setURL(args[0].trim());
                break;
            case 0:
                gateway.setURL(DEFAULT_URL);
                gateway.setPort(DEFAULT_PORT);
                break;
            default:
                for (int i = 2; i < args.length; i++) {
                   System.out.println("Error - unrecognised argument: " + args[i]);
                }
                System.out.println("\nUsage: java"
                                   + " com.ibm.ctg.samples.epi.HighEpiB1"
                                   + " [GatewayUrl]"
                                   + " [port]");
                System.out.println("\nTo enable client tracing, run the sample " +
                                   "with the following java option: -Dgateway.T.trace=on");
                return;
            }

            System.out.println("Usage: java"
                               + " com.ibm.ctg.samples.epi.HighEpiB1"
                               + " [GatewayUrl]"
                               + " [port]");



            // Then open the gateway

            System.out.println("\nOpening gateway to " + gateway.getURL());
            gateway.open();


            /* Get a list of the servers in the Gateway daemon configuration
             * file and ask the user to select one to connect to.
             */

            int totalServers = gateway.serverCount();

            if (totalServers == 0) {
                System.out.println("No servers are listed in the ctg.ini file" +
                                   " of this transaction gateway");
                gateway.close();
                return;
            }
            System.out.println("Select a server");

            for (int i = 0; i < totalServers; i++) {
                int counter = i+1;
                System.out.println(counter + ": " + gateway.serverName(counter));
            }

            InputStreamReader in = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(in);
            int selectedServer = 0;
            try {
                selectedServer = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                selectedServer = NO_SERVER_SELECTED;
            }

            if (selectedServer < 1 || selectedServer > totalServers
                || selectedServer == NO_SERVER_SELECTED) {
                System.out.println("Invalid integer entry");
                gateway.close();
                return;
            }
            server = gateway.serverName(selectedServer);


            /* Connect a terminal to the server selected by
             * the user using the Terminal object.
             */

            System.out.println("Connecting terminal");

            //Try connecting with a basic terminal
            try {
                cicsTerminal = new Terminal(gateway, server, null, null);
            } catch (EPIRequestException basicConnectionFailed) {

                //check if we couldn't connect because of a security error and prompt for a 
                //userid and password if necessary
                if (basicConnectionFailed.getErrorCode() == EPIRequestException.EPI_ERR_SECURITY) {

                    /*
                    * The connection failed with a security error so now we need to try and connect
                    * with an extended terminal using the provided userid and password
                    * Create a new extended terminal which will cope with the security parameters
                    */
                    cicsTerminal = new Terminal(gateway,
                                                server,
                                                null,
                                                null,
                                                Terminal.EPI_SIGNON_INCAPABLE,
                                                null,
                                                null,
                                                0,
                                                null);
                    boolean connected = false;
                    int attempts = 0;
                    while (!connected) {
                        try {

                            //increment the number of attempts
                            attempts++;

                            if (attempts <= 3) {
                                System.out.println("Validation failed\nPlease enter your CICS details.");
                                //prompt for the userid and password
                                System.out.println("Enter your CICS user ID:");
                                String userid = reader.readLine().trim();
                                System.out.println("Enter your CICS password:");
                                String password = reader.readLine().trim();
                                //set the userid and password
                                cicsTerminal.setUserid(userid);
                                cicsTerminal.setPassword(password);

                                //connect the terminal
                                cicsTerminal.connect();
                                connected = true;
                            } else {

                                //there have been too many failed attempts
                                System.out.println("Unable to log you onto the terminal");
                                System.exit(1);
                            }
                        } catch (EPIRequestException extendedConnectionFailed) {

                            //if the connection failed for any other reason than security throw the exception
                            if (extendedConnectionFailed.getErrorCode() != EPIRequestException.EPI_ERR_SECURITY) {
                                throw extendedConnectionFailed;
                            }
                        }
                    }
                } else {
                    //throw the exception up to the next catch block
                    throw basicConnectionFailed;
                }
            }


            /* Get the transaction id from the user.
             * The transaction id must be located on the CICS
             * server and be able to return the Server to an
             * idle state with PF3.
             */
            System.out.println("Enter a transaction id");
            transactionId = (reader.readLine()).trim();


            // Then send the transaction and display the reply

            System.out.println("Sending transaction");
            cicsTerminal.send(null, transactionId, null);

            screen = cicsTerminal.getScreen();
            System.out.println();
            for (int i = 1; i <= screen.fieldCount(); i++) {
                if ((screen.field(i)).textLength() > 0) {
                    System.out.println(i + screen.field(i).getText());
                }
            }
            System.out.println();


            // Lastly disconnect the terminal when it is idle

            state = cicsTerminal.getState();
            System.out.println("Terminal state before sending PF3 to end = " + HighEpiB1.getStateString(state));

            if (state != Terminal.idle) {
                screen.setAID(AID.PF3);
                cicsTerminal.send();
                state = cicsTerminal.getState();
                System.out.println("Terminal state after sending PF3 to end = " + HighEpiB1.getStateString(state));
            }

            cicsTerminal.disconnect();
            state = cicsTerminal.getState();
            System.out.println("Terminal state after disconnecting = " + HighEpiB1.getStateString(state));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Finally close the gateway

            try {
                if (gateway.isOpen()) {
                    gateway.close();
                }
            } catch (Exception e) {
                System.out.println("Exception closing gateway");
                e.printStackTrace();
            }
        }
    }


    // Static method to return the terminal state as a string

    public static String getStateString(int state) {
        switch (state) {
        case Terminal.start:
            return "start";
        case Terminal.idle:
            return "idle";
        case Terminal.client:
            return "client";
        case Terminal.server:
            return "server";
        case Terminal.discon:
            return "disconnected";
        case Terminal.error:
            return "error";
        case Terminal.failed:
            return "failed";
        default:
            return "state not found";
        }
    }
}
