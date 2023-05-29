/*
*         File Name      : EciI1.java
*
*         Product        : CICS Transaction Gateway
*
*         Description    : This sample shows the use of the CICS Transaction
*                          Gateway ECI API using the callback mechanism. It
*                          queries the Client for a list of available servers
*                          and then launches a transaction on the selected
*                          server.
*
*         Pre-requisites : This requires the CICS Program EC02 to be
*                          installed on your CICS Server for operation. The
*                          CICS Server must be set up to return the contents
*                          of the commarea as ASCII text.
*                          If the code page of the application differs to
*                          that of the server, data conversion must be
*                          performed at the server by making use of CICS
*                          supplied resource conversion capabilities, such as
*                          DFHCNV macro definitions.
*
*                          Use a version of the JDK that the CICS Transaction
*                          Gateway supports  if you recompile this sample. See
*                          the product documentation for supported Java levels.
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

import java.util.*;
import java.io.*;
import com.ibm.ctg.client.*;
import com.ibm.ctg.samples.security.ClientCompression;
import com.ibm.ctg.samples.security.ServerCompression;

public class EciI1
{
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    // Constants
    private final String DEFAULT_URL = "local:";
    private final int DEFAULT_PORT = 2006;
    private final int SLEEP_PERIOD = 1000;
    private final int WAIT_COUNT = 10;

    // Variables
    private JavaGateway jgate = null;
    private ECIRequest eci = null;
    private ClientCompression clientSecurity = null;
    private ServerCompression serverSecurity = null;
    private String chosenServer = "";
    private String program = "EC02";
    private String sslKeyring  = "";
    private String sslPassword = "";
    private String password = "";
    private String userId = "";
    private String encoding = "ASCII";
    private int commAreaSize = 80;
    private int selection = -1;
    private int returnCode;
    private InputStreamReader in = null;
    private BufferedReader reader = null;
    private Vector<String> servers;
    private byte[] commArea = new byte[commAreaSize];

    /*
    * main(String[] args)
    *
    * This method provides an example of using
    * the ECIRequest class with a callbackable
    * object.
    *
    * 1: Handle the command line arguments -
    * A gateway URL may be provided as the first
    * parameter and/or the port number for the
    * second. The third and fourth parameters
    * concern SSL keyring and password.
    * If no parameters are supplied the local
    * gateway will be used.
    *
    * 2: Open the gateway and request a list of
    * available servers listed in the configuration 
    * file referenced by the gateway. This list is
    * displayed to the user and the user either
    * selects one or quits.
    *
    * 3: Set up an Asynchronous ECIRequest to
    * execute program 'EC02' on the selected
    * server and set the Callbackable object. It
    * then flows the request and waits for the
    * result to be returned via the Callbackable
    * object.
    * Finally the returned result is analysed.
    *
    * 4: Ask the user if they wish to execute the
    * program again within the same Logical Unit
    * of Work.
    *
    * 5: Ask the user if they wish to Commit or
    * Rollback
    * - This uses a Synchronous call which requires
    * the callback object to be set to null.
    *
    * 10: 'Finally' closes the gateway
    */
    public static void main(String[] args)
    {
        EciI1 IntermediateEci = new EciI1();

        try
        {
            IntermediateEci.displayBanner();

            if (IntermediateEci.parseCommandLine(args) == false)
            {
                return;
            }

            if (IntermediateEci.getServers()==false)
            {
                return;
            }

	    if (IntermediateEci.eciInitialiseParms() == false)
	    {
		return;
	    }

            do
            {
                if (IntermediateEci.startTransaction()==false)
                {
                    return;
                }
            }
            while (IntermediateEci.continueTransaction());

            IntermediateEci.commitOrRollback();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            IntermediateEci.closeGateway();
        }
    }

    /*
    * List of methods
    *
    * 1: Display Banner
    * 2: ParseCommandLine
    * 3: GetServers
    * 4: StartTransaction
    * 5: ContinueTransaction
    * 6: CommitOrRollback
    * 7: CheckReturnCode
    * 8: Run (Inner class)
    * 9: SetResults (InnerClass)
    * 10: WaitForEvent (InnerClass)
    */

    // Display Banner:
    public void displayBanner()
    {
        System.out.println("\n\n\n");
        System.out.println("CICS Transaction Gateway Intermediate ECI Sample\n");
    }

    /* Handle the command line arguments which are
    * optional:
    * NOTE: The switch statement falls through
    * The first argument is a gateway URL
    * The second is a port
    * The third is an SSL Keyring
    * The fourth is an SSL password
    * The default is the localhost
    */
    public boolean parseCommandLine(String[] args) throws IOException
    {
        boolean returnValue = false;
        jgate = new JavaGateway();

        switch (args.length)
        {
        case 4:
            sslKeyring  = args[2].trim();
            sslPassword = args[3].trim();

            Properties sslProps = new Properties();
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_CLASS, sslKeyring);
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_PW, sslPassword);

            jgate.setProtocolProperties(sslProps);
        case 2:
            try
            {
                jgate.setPort(Integer.parseInt(args[1].trim()));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid port number");
                System.out.println("Usage: java "
                                   + "com.ibm.ctg.samples.eci.EciI1"
                                   + " [GatewayURL]"
                                   + " [port]"
                                   + " [SSL keyring]"
                                   + " [SSL password]");
                return returnValue;
            }
        case 1:
            jgate.setURL(args[0].trim());
            break;
        case 0:
            jgate.setURL(DEFAULT_URL);
            jgate.setPort(DEFAULT_PORT);
            break;
        case 3:
           System.out.println("An SSL password must be specified if " +
                              "using an SSL keyring.");
        default:
           for (int i = 4; i < args.length; i++) {
              System.out.println("Error - unrecognised argument: " + args[i]);
           }
           
            System.out.println("\nUsage: java "
                               + "com.ibm.ctg.samples.eci.EciI1"
                               + " [GatewayURL]"
                               + " [port]"
                               + " [SSL keyring]"
                               + " [SSL password]");
            System.out.println("\nTo enable client tracing, run the sample " +
                               "with the following java option: -Dgateway.T.trace=on");
            return returnValue;
        }
        System.out.println("Usage: java "
                               + "com.ibm.ctg.samples.eci.EciI1"
                               + " [GatewayURL]"
                               + " [port]"
                               + " [SSL keyring]"
                               + " [SSL password]");

        System.out.println("Gateway: " + jgate.getURL() + " SSL keyring: "
                           + sslKeyring + " SSL password: " + sslPassword + "\n");

        returnValue = true;
        return returnValue;
    }

    /*
    * getServers()
    * Opens the gateway and then flows a
    * request to the gateway to obtain a
    * list of the CICS Servers in its ctg.ini file.
    */
    public boolean getServers() throws IOException, Exception
    {
        boolean returnValue = false;
        clientSecurity = new ClientCompression();
        serverSecurity = new ServerCompression();

        jgate.setSecurity(clientSecurity.getClass().getName(), serverSecurity.getClass().getName());

        jgate.open();
        System.out.println(jgate.getAddress() + " Gateway opened");

        /*
        * If an ECI_ERR_MORE_SYSTEMS error occurs this is because you have more servers
        * defined in your CICS TG configuration than the value given in the listSystems()
        * method. To prevent this you should either remove some servers from the configuration
        * or increase the listSystems() parameter below so that it is greater than the
        * number of servers defined.
        */

        eci = ECIRequest.listSystems(20);
        returnCode = jgate.flow(eci);

        try
        {
            this.checkReturnCode(returnCode);
        }
        catch (Exception e)
        {
            System.out.println("Request to list defined CICS servers failed");
            throw e;
        }

        servers = eci.SystemList;

        if (eci.numServersReturned > 0)
        {
            System.out.println("\nNumber of systems = "
                               + eci.numServersReturned);
            System.out.println("\n");
            System.out.println("Systems:");
            int count = 1;
            System.out.println("");
            int serverListLength = servers.size();
            for (int i = 0; i<serverListLength; i++)
            {
                System.out.println(count + ": "
                                   + (servers.elementAt(i))
                                   + " - " + servers.elementAt(++i));
                count++;
            }
            System.out.println("");
        }
        else
        {
            System.out.println("\nNo servers available");
            return returnValue;
        }

        /*
        * Select server:
        * Asks the user to select a CICS Server from
        * the list provided or to quit
        */
        System.out.println("\nSelect a server by number or 'Q' quit\n");
        boolean validServer = false;
        in = new InputStreamReader(System.in);
        reader = new BufferedReader(in);

        // Loop until a valid server has been chosen
        do
        {
            try
            {
                String userResponse = (reader.readLine()).trim();
                if (userResponse.equalsIgnoreCase("q"))
                {
                    return returnValue;
                }
                selection = Integer.parseInt(userResponse);
                selection--;

                if (selection < 0 || selection >= eci.numServersReturned)
                {
                    throw new NumberFormatException();
                }
                selection = selection *2;
                validServer = true;
            }
            catch (NumberFormatException e)
            {
                if (eci.numServersReturned == 1)
                {
                    System.out.println("Enter 1 to select the server or Q to quit");
                }
                else
                {
                    System.out.println("Enter a number between 1 and "
                                       + eci.numServersReturned + " or Q to quit");
                }
            }
        }
        while (!validServer);

        chosenServer = servers.elementAt(selection);
        chosenServer = chosenServer.trim();
        System.out.println("\nChosen server = " + chosenServer);

        returnValue = true;
        return returnValue;
    }

    /*
    * eciInitialiseParms()
    * Set up the ECI parameter block
    */
    public boolean eciInitialiseParms()
    {
	eci = new ECIRequest(ECIRequest.ECI_ASYNC,
			     chosenServer,
			     userId,
			     password,
			     program,
			     null,
			     commArea,
			     commAreaSize,
			     ECIRequest.ECI_EXTENDED,
			     ECIRequest.ECI_LUW_NEW);


	if (eci == null)
	{
	    return false;
	}

	return true;
    }

    /*
    * startTransaction()
    * It is to be Asynchronous and will provide
    * a callback object with setCallback using
    * the inner class to handle the reply
    */
    public boolean startTransaction() throws IOException, InterruptedException, Exception
    {
        ReplyHandler handler = new ReplyHandler();
        eci.setCallback(handler);

        int loopCounter = 0;
        boolean loop = true;

        // Loop until transaction has been processed
        do
        {
            // Flow the request:
            jgate.flow(eci);

            /*
            * Wait for the reply: The boolean 'reply' is set
            * to true in the callback routine.
            * See setResults(GatewayRequest request)
            * below.
            * Also check the return code for errors
            */
            handler.waitForEvent("flow request");
            returnCode = eci.getRc();

            try
            {
                this.checkReturnCode(returnCode);
                System.out.println("No errors reported");
                loop=false;
            }
            catch (Exception e)
            {
                /*
                * Check the abend code
                * Note the following codes:
                * ECOM = CommArea Size is too small
                * AEY7 = Invalid userid and/or password
                */
                if(eci.getCicsRc() == ECIRequest.ECI_ERR_TRANSACTION_ABEND)
                {
                    System.out.println("\nTransaction abended");
                    System.out.println("Abend code = " + eci.Abend_Code);
                }

                // Check that the exception is not a security error
                System.out.println("Error reported flowing request");
                if (eci.getCicsRc()==ECIRequest.ECI_ERR_SECURITY_ERROR)
                {
                    if (loopCounter==3)
                    {
                        System.out.println("Unable to validate security credentials");
                        return false;
                    }
                    else
                    {
                        System.out.println("Enter your CICS user ID:");
                        userId = reader.readLine().trim();
                        eci.Userid = userId;

                        System.out.println("Enter your CICS password or password phrase:");
                        password = reader.readLine().trim();
                        eci.Password = password;

                        loopCounter++;
                    }
                }
                else
                {
                    /*
                    * If the exception is not security then
                    * re-throw it to the main routine
                    */
                    throw e;
                }
            }
        }while (loop);

        // Process the reply and display the commarea in ASCII
        System.out.println("\n");
        System.out.println(encoding + ":");
        try
        {
            System.out.println(new String(eci.Commarea, encoding));
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.print(" Not supported");
        }
        System.out.println("");
        // Display the commarea in Hex
        System.out.println("Hex:");
        for (int i = 0; i<eci.Commarea.length; i++)
        {
           //The hex string requires special formatting logic
           System.out.print(getHexString(eci.Commarea[i]));
        }
        System.out.println("\n");
        return true;
    }

    /*
    * continueTransaction()
    * Asks the user if they wish to execute the
    * transaction again within the current
    * Logical Unit of Work
    */
    public boolean continueTransaction() throws IOException
    {
        boolean aValidChoice = false;
        boolean returnValue = false;

        System.out.println("Do you wish to run the program again"
                           + " in this logical unit of work? - (Y/N)");

        while (!aValidChoice)
        {
            String answer = (reader.readLine()).trim();
            if (answer.equalsIgnoreCase("y"))
            {
                aValidChoice = true;
                returnValue = true;
            }
            else
            {
                if (answer.equalsIgnoreCase("n"))
                {
                    aValidChoice = true;
                }
                else
                {
                    System.out.println("Invalid choice - select 'Y' or 'N' and press enter");
                }
            }
        }
        return returnValue;
    }

    /*
    * Commit/Backout (Rollback):
    * Asks the user whether they want to keep or
    * discard the results of the current logical unit
    * of work
    */
    public void commitOrRollback() throws IOException, Exception
    {
        System.out.println("\nC - commit or R - rollback?");
        String decision = "";
        boolean validChoice = false;

        while (!validChoice)
        {
            decision = (reader.readLine()).trim();

            if (decision.equalsIgnoreCase("c"))
            {
                // commit
                eci.Extend_Mode = ECIRequest.ECI_COMMIT;
                decision = "Committed";
                validChoice = true;
            }
            else
            {
                // rollback
                if (decision.equalsIgnoreCase("r"))
                {
                    eci.Extend_Mode = ECIRequest.ECI_BACKOUT;
                    decision = "Rolledback";
                    validChoice = true;
                }
                else
                {
                    System.out.println("Invalid choice - select either 'C' or 'R' and press enter");
                }
            }
        }

        /*
        * Set up other parameters:
        * Sets the other parameters required for a
        * successful Synchronous Commit or Rollback.
        *
        * Important - to reset an existing ECIRequest object
        * from Asynch to Synch you must set the callback to
        * null otherwise the Synch will be ignored
        */
        eci.Call_Type = ECIRequest.ECI_SYNC;
        eci.Program = null;
        eci.setCallback(null);

        returnCode = jgate.flow(eci);

        try
        {
            this.checkReturnCode(returnCode);
        }
        catch (Exception e)
        {
            System.out.println("Rollback/commit failed");
            throw e;
        }
        System.out.println(decision + " logical unit of work on " + chosenServer);
    }

    // Close gateway
    public void closeGateway()
    {
        try
        {
            if (reader!=null)
            {
                reader.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (jgate == null)
        {
            return;
        }

        if (jgate.isOpen())
        {
            try
            {
                jgate.close();
                System.out.println("Gateway closed");
            }
            catch (Exception e)
            {
                System.out.println("Failed to close gateway");
                e.printStackTrace();
            }
        }
    }

    /*
    * checkReturnCode(int returnCode)
    * When a request is flowed, the return code
    * is analysed by this method to determine
    * if an exception occurred and if so whether the
    * exception came from the Gateway or CICS
    */
    public void checkReturnCode(int returnCode) throws Exception
    {
        if (returnCode!=0)
        {
            if (eci.getCicsRc()==0)
            {
                throw new Exception("Gateway exception: Return code number:"
                                    + returnCode
                                    + " Return code string: "
                                    + eci.getRcString());
            }
            else
            {
                throw new Exception("CICS exception: Return code number:"
                                    + returnCode
                                    + " Return code string: "
                                    + eci.getCicsRcString());
            }
        }
    }

    /*
    * This method is passed an integer value that
    * represents a single byte. This number is
    * converted to a hex string using
    * Integer.toHexString().
    * However, this sometimes formats it as a
    * single character (e.g. F), or it
    * incorrectly interprets the unsigned byte
    * that is represented by value as a signed
    * number. The result of the latter is a
    * String of 8 hex characters, of which only
    * the last two are correct.
    */

    private String getHexString(int value) {

        String tmp = Integer.toHexString(value);

        /*
        * If the byte is represented by a single hex
        * character
        */
        if(tmp.length() == 1){
            /*
            * Pre-append a zero to ensure each byte is two
            * characters long
            */
            tmp = "0"+tmp;
        }
        /*
        * If we receive more than two chars, we only
        * want the last two
        */
        else if(tmp.length() > 2){
            tmp = tmp.substring(tmp.length() - 2);
        }

        return tmp.toUpperCase();
    }

    /*
    * ReplyHandler is an inner class which
    * implements the Callbackable interface.
    * When it is set by the setCallback method
    * it is used to notify the outer class that a
    * reply has been received from the CICS
    * server. This process is initiated with the
    * calling of the setResults method and then
    * running the ReplyHandler in its own thread
    */

    class ReplyHandler implements Callbackable
    {
        private boolean reply = false;
        /*
        * Required by the Callbackable interface:
        * This sets the flag which is checked by the
        * waitForEvent() method. Notify then allows
        * wait() to end.
        */
        public void run()
        {
            synchronized(this)
            {
                reply = true;
                this.notify();
            }
        }

        /*
        * Required by the Callbackable interface
        *
        * This is the routine that is executed when an
        * Asynchronous call is made
        */
        public void setResults(GatewayRequest request)
        {
            System.out.println("\n*");
            eci = (ECIRequest) request;
        }

        /*
        * waitForEvent(String eventMessage)
        * causes the program thread to wait
        * for a notification which comes when the
        * callback object is started. To prevent
        * hanging if no notification is received the
        * wait has a defined time of waiting before
        * an exception is thrown. If this were an
        * application with an interface this time
        * spent waiting for a reply would allow a
        * user to continue using the User Interface
        */
        public synchronized void waitForEvent(String eventMessage) throws InterruptedException
        {
            System.out.println("\nWaiting for a reply to a request to " + eventMessage);
            int loopCounter = 0;

            try
            {
                if (!reply)
                {
                    boolean loop = true;

                    /*
                    * Loop until a reply has been received or
                    * the wait period is exceeded
                    */
                    do
                    {
                        if (!reply)
                        {
                            this.wait(SLEEP_PERIOD);
                        }
                        if (!reply)
                        {
                            loopCounter++;
                            if (loopCounter == WAIT_COUNT)
                            {
                                throw new InterruptedException("Reply not received within wait period"
                                                               + " while waiting for a reply to "
                                                               + eventMessage);
                            }
                            System.out.print(".");
                        }
                        else
                        {
                            loop = false;
                            reply=false;
                        }
                    }while (loop);
                }
                System.out.println("Reply received");
                reply=false;
            }
            catch (InterruptedException e)
            {
                reply = false;
                throw e;
            }
        }
    }
}
