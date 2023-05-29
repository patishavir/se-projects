/*          File Name      : HighEpiI1.java
*
*           Product        : CICS Transaction Gateway
*
*           Description    : This is a sample class to demonstrate the basic
*                            features of the CICS Transaction Gateway
*                            Higher Level EPI classes utilising Session
*                            objects and ATI features.
*
*           Pre-requisites : The transaction selected by the user must be
*                            installed on your CICS Server. Note that this
*                            sample is specifically tailored to
*                            demonstrate ATIs using transaction EP03.
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
import com.ibm.ctg.samples.security.*;


/* This class is an intermediate example aimed at clearly illustrating the
 * basic connectivity features provided by the High Level EPI classes whilst
 * utilising the callback mechanism through session objects. It is not a
 * template and should not be used as a foundation for your next mission
 * critical application. The program can accept two command line parameters,
 * the URL of the Gateway daemon that will be connected to and the port
 * number. Both parameters are optional. If they are not specified then the
 * local gateway and a default port will be selected.
 */

public class HighEpiI1 {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
    /* Constants:
     * WAIT_COUNT and SLEEP_PERIOD help to determine the behaviour of the
     * wait for reply method.
     */
    private final int WAIT_COUNT = 10;
    private final int SLEEP_PERIOD = 1000;
    private final int DEFAULT_PORT = 0;
    private final String DEFAULT_URL = "local:";
    private final int NO_SERVER_SELECTED = -1;
    private final int INSTALL_TIMEOUT = 0;
    private static final AID AID_KEY = AID.PF3;
    private final String ATI_TRANSACTION = "ep03";


    // Variables

    private EPIGateway gateway;
    private String server = null;
    private String transactionId = null;
    private InputStreamReader in = null;
    private BufferedReader reader = null;
    private Terminal cicsTerminal = null;
    private Screen screen = null;
    private ReplyHandler replyHandler = null;
    private ClientCompression clientSecurity = null;
    private ServerCompression serverSecurity = null;
    private String devType = null;
    private String netName = null;
    private String userID = null;
    private String password = null;
    private int readTimeOut = 0;
    private String encoding = null;


    /* main:
     * Illustrates the basic flow of the program.
     * 1: Display a banner
     * 2: Parse the command line arguments
     * 3: Open the EPIGateway
     * 4: Select a server from the list of servers
     *    in the ini file
     * 5: Run the selected transaction
     * 6: Close the gateway
     */

    public static void main(String[] args) {
        HighEpiI1 sample = new HighEpiI1();

        try {
            sample.displayBanner();
            if (sample.parseCommandLine(args)) {
                sample.openGateway();
                if (sample.getServers()) {
                    sample.runTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sample.closeGateway();
        }
    }


    // Display Banner:

    public void displayBanner() {
        System.out.println("\n\n\n");
        System.out.println("CICS Transaction Gateway "
                           + "Intermediate High Level EPI Sample");
        System.out.println("\n");
    }


    // Parse command line. Get the Gateway URL and Port if supplied

    private boolean parseCommandLine(String[] args) throws IOException
    {
        gateway = new EPIGateway();

        switch (args.length) {
        case 2:
            try {
                gateway.setPort(Integer.parseInt(args[1].trim()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number");
                System.out.println("Usage: java"
                                   + " com.ibm.ctg.samples.epi.HighEpiI1"
                                   + " [GatewayURL]"
                                   + " [port]");
                return false;
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
                               + " com.ibm.ctg.samples.epi.HighEpiI1"
                               + " [GatewayURL]"
                               + " [port]");
            System.out.println("\nTo enable client tracing, run the sample"
                               + " with the following java option:"
                               + " -Dgateway.T.trace=on");
            return false;
        }

        System.out.println("Usage: java"
                           + " com.ibm.ctg.samples.epi.HighEpiI1"
                           + " [GatewayURL]"
                           + " [port]");

        System.out.println("Gateway: " + gateway.getURL());
        return true;
    }


    /* Open gateway
     * Note that this sample includes an example of using the security samples.
     * However not all applications would require this.
     */

    private void openGateway() throws IOException
    {
        System.out.println("\nOpening the JavaGateway object to " +
                           gateway.getAddress() + "\n");
        clientSecurity = new ClientCompression();
        serverSecurity = new ServerCompression();
        gateway.setSecurity(clientSecurity.getClass().getName(),
                            serverSecurity.getClass().getName());
        gateway.open();
    }


    /* Get servers
     * Retrieves a list of those servers listed in the configuration file
     * of the Gateway daemon.
     */

    private boolean getServers() throws IOException, EPIException
    {
        boolean aValidChoice = false;
        int totalServers = gateway.serverCount();

        if (totalServers == 0) {
            System.out.println("No servers are listed in the configuration file" +
                               " for this Gateway daemon");
            gateway.close();
            return false;
        }

        do {
            System.out.println("Select a server by number or Q to quit\n");

            for (int i = 0; i < totalServers; i++) {
                int counter = i+1;
                System.out.println(counter + ": "
                                   + gateway.serverName(counter));
            }
            System.out.println();

            in = new InputStreamReader(System.in);
            reader = new BufferedReader(in);
            int selectedServer = 0;
            try {
                String choice = reader.readLine().trim();
                if (choice.equalsIgnoreCase("Q")) {
                    System.out.println("Quitting.");
                    return false;
                }
                selectedServer = Integer.parseInt(choice);
            } catch (Exception e) {
                selectedServer = NO_SERVER_SELECTED;
            }

            if (selectedServer < 1 || selectedServer > totalServers
                || selectedServer == NO_SERVER_SELECTED) {
                System.out.println("Invalid entry");
            } else {
                aValidChoice = true;
                server = gateway.serverName(selectedServer);
            }
        }while (!aValidChoice);
        return true;
    }


    /* Run transaction
     * Connect a terminal to the server selected by the user using the extended
     * Terminal constructor.
     *
     * Points to note are:
     *
     * 1: A synchronous or asynchronous call may be made to connect the
     * terminal. This sample uses the synchronous method.
     *
     * 2: The setATI(true) method must be called if the transaction is EP03.
     *
     * 3: After the terminal is connected an object is created to handle the
     * subsequent replies which are received Asynchronously. However
     * this sample in effect simulates a synchronous call as it waits for a
     * reply to be received. Instead of waiting for the reply an application
     * could take this opportunity to perform other tasks and in this sample
     * that is simply to display a '.' character every second to inform the
     * user how long the application has been waiting for a reply.
     *
     * 4: This sample assumes that the transaction selected by the user must
     * be able to return the server to an idle state with PF3.
     */

    private void runTransaction() throws IOException, Exception
    , InterruptedException
    {

        /* Initialise the terminal object.
         * The terminal is extended and signon incapable.
         */

        cicsTerminal = new Terminal(
                                   gateway,
                                   server,
                                   devType,
                                   netName,
                                   Terminal.EPI_SIGNON_INCAPABLE,
                                   userID,
                                   password,
                                   readTimeOut,
                                   encoding);

        System.out.println("\n\tGateway \t= \t"
                           + gateway.getURL()
                           + " \n\tserver \t\t= \t"
                           + cicsTerminal.getServerName()
                           + " \n\textended \t= \t"
                           + cicsTerminal.isExtendedTerminal());
        System.out.println("\nState before connecting = "
                           + HighEpiI1.getStateString(cicsTerminal.getState()));


        // Connect - synchronously

        System.out.println("Connecting.");
        boolean connected = false;
        int attempts = 0;
        while (!connected) {
            try {
                cicsTerminal.connect(INSTALL_TIMEOUT);
                connected = true;
            } catch (EPIRequestException requestException) {

                //check if the connection has failed because of a security error
                if (requestException.getErrorCode() == EPIRequestException.EPI_ERR_SECURITY) {

                    //increment the number of attempts to connect
                    attempts++;
                    if (attempts <= 3) {
                        System.out.println("Validation failed\nPlease reenter your CICS details.");

                        //prompt for the userid/password
                        System.out.println("Enter your CICS user ID:");
                        String userid = reader.readLine().trim();
                        System.out.println("Enter your CICS password:");
                        String password = reader.readLine().trim();

                        //set them in the terminal and try to reconnect
                        cicsTerminal.setUserid(userid);
                        cicsTerminal.setPassword(password);
                    } else {

                        //There have been too many unsuccessful attempts
                        System.out.println("Unable to log you onto the terminal");
                        System.exit(1);
                    }
                } else {

                    //rethrow the exception
                    throw requestException;
                }
            }
        }

        System.out.println("\n\tTerminal ID \t= \t"
                           + cicsTerminal.getTermid());


        /* Get the transaction id from the user
         * The transaction id must be located on the CICS server and be able to
         * return the Server to an idle state with PF3.
         * Note that it is transaction EP03 that demonstrates an ATI.
         */

        System.out.print("\n\nEnter a transaction ID\n\n\t");
        transactionId = (reader.readLine()).trim();
        reader.close();


        /* Before sending the transaction the inner class that handles the
         * asynchronous replies to the client application is initialised and
         * provided as a parameter to the send method. Also if the
         * transaction is EP03 the terminal is ATI enabled.
         * The transaction is then sent and the client application waits for
         * the reply.
         */

        System.out.println("\nState before sending transaction = "
                           + HighEpiI1.getStateString(cicsTerminal.getState()));
        System.out.println("Sending transaction " + transactionId);
        replyHandler = new ReplyHandler();


        /* Note that before the terminal is ATI enabled the session object
         * must be set.
         */

        if (transactionId.equalsIgnoreCase(ATI_TRANSACTION)) {
            cicsTerminal.setSession(replyHandler);
            cicsTerminal.setATI(true);
            cicsTerminal.send(transactionId, null);
        } else {
            cicsTerminal.send(replyHandler, transactionId, null);
        }
        replyHandler.waitForReply();


        // Loop to process the results of the transaction

        boolean processTransaction = true;
        do {
            switch (cicsTerminal.getState()) {
            case Terminal.client:

                /* The server is expecting a reply.
                 * In this sample the PF3 key is sent. As stated above the
                 * transaction must be capable of changing the terminal to idle
                 * on receipt of PF3.
                 */

                System.out.println("Server is expecting input - " +
                                   "display current screen and send " + AID_KEY
                                   + " key:");

                try {
                    screen = cicsTerminal.getScreen();
                    this.displayScreen();
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Encoding is not supported");
                    e.printStackTrace();
                }

                screen.setAID(AID_KEY);
                cicsTerminal.send();
                replyHandler.waitForReply();
                break;

            case Terminal.server:
                // State has returned to server - continue waiting.
                break;
            case Terminal.error:

                // Break out of the loop on error

                System.out.println("Error");
                throw new Exception("Terminal error reported");

            case Terminal.idle:


                try {
                    /* Test to see if there is an automated transaction in
                     * process. If there is then wait for a reply, otherwise
                     * attempt to disconnect.
                     */

                    if (transactionId.equalsIgnoreCase(ATI_TRANSACTION)) {
                        System.out.println("ATI enabled = "
                                           + cicsTerminal.queryATI());
                        try {
                            replyHandler.waitForReply();
                        } catch (InterruptedException e) {
                            System.out.println("\n" + e);
                        }
                    }

                    /* The server is finished, so the results are displayed and the
                     * terminal disconnected.
                     */
                    System.out.println("Display final output and attempt to disconnect.");
                    try {
                        this.displayScreen();
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("Encoding is not supported");
                        e.printStackTrace();
                    }

                    cicsTerminal.disconnect();
                    processTransaction = false;
                    break;
                } catch (EPIException e) {
                    throw e;
                }

            case Terminal.discon:

                // Break out of the loop if the terminal is disconnected

                processTransaction = false;
                break;

            case Terminal.failed:

                // Throw an exception if the terminal fails

                System.out.println("Failed");
                throw new Exception("Terminal failed reported");

            default:

                /* Other case are not required by this sample. Should any occur
                 * then throw them as an exception.
                 */

                System.out.println("State = "
                                   + HighEpiI1.getStateString(cicsTerminal.getState()));
                throw new Exception("Unexpected terminal state reported.");
            }
        }while (processTransaction);
    }


    // Close the EPIGateway

    public void closeGateway() {
        try {
            if (gateway!=null) {
                if (gateway.isOpen()) {
                    gateway.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception when closing JavaGateway object.");
            e.printStackTrace();
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


    // Display the contents of the current screen object

    private void displayScreen() throws UnsupportedEncodingException
    {
        screen = cicsTerminal.getScreen();
        int lastRow = 0;
        int row = 0;
        int difference = 0;
        StringBuffer text = null;
        String space = " ";

        System.out.println();
        for (int i = 1; i <= screen.fieldCount(); i++) {
            if ((screen.field(i)).textLength() > 0) {
                text = new StringBuffer(screen.field(i).getText().trim());
                difference = screen.field(i).length() - screen.field(i).textLength();

                if (difference > 0) {
                    for (int p = 0; p<difference; p++) {
                        text = text.append(space);
                    }
                }
                row = screen.field(i).getRow();

                if (row == lastRow) {
                    System.out.print(text);
                } else {
                    System.out.print("\n " + text);
                }
                lastRow = row;
            }
        }
        System.out.println("\n");
    }


    /* Inner class to handle replies
     *
     * This implements Session by implementing two methods, getSyncType and
     * handleReply. In this sample getSyncType sets the Session to
     * Asynchronous. This means that when the sample application sends the
     * transaction it is free to continue its own thread, although all that is
     * implemented here is a wait for reply which in effect simulates a
     * synchronous session. On reply the method handleReply is run in its own
     * thread which notifies the sample application thread a reply has been
     * received. Upon notification the sample thread breaks out of the loop in
     * waitForEvent (see below).
     *
     * In addition to the two methods this class also implements
     * handleException which is called in the event of an exception.
     */

    private class ReplyHandler implements Session {
        private boolean reply = false;


        // Get sync type

        public int getSyncType() {
            return Session.async;
        }


        // Handle the reply

        public void handleReply(TerminalInterface terminal) {
            System.out.println("\nHandle reply called.");
            cicsTerminal = (Terminal) terminal;
            System.out.println("Current state of terminal = "
                               + HighEpiI1.getStateString(cicsTerminal.getState()));


            /* Continue waiting if the state is Terminal.server as more data
             * is to follow.
             */

            if (cicsTerminal.getState() != Terminal.server) {
                if (this.getSyncType()==Session.async) {
                    synchronized(this) {
                        reply = true;
                        this.notify();
                    }
                }
            }
        }

        /* Method to wait for the reply from the server
         * This causes the sample application thread to wait for the length of
         * time determined by the SLEEP_PERIOD constant or until a notify
         * has been received. The thread then checks to see if a reply has
         * been received and if not it loops round until the WAIT_COUNT
         * is exceeded.
         */

        public synchronized void waitForReply() throws InterruptedException
        {
            System.out.println("\nWaiting for a reply.");
            int loopCounter = 0;

            try {
                if (!reply) {
                    boolean loop = true;
                    do {
                        if (!reply) {
                            this.wait(SLEEP_PERIOD);
                        }
                        if (!reply) {
                            loopCounter++;
                            if (loopCounter == WAIT_COUNT) {
                                throw new InterruptedException("Reply not"
                                                               + " received within wait period");
                            }
                            System.out.print(".");
                        } else {
                            loop = false;
                            reply=false;
                        }
                    }while (loop);
                }
                reply=false;
            } catch (InterruptedException e) {
                reply = false;
                throw e;
            }
        }
    }
}
