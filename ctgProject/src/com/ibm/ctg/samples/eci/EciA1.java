/*
*         File Name      : EciA1.java
*
*         Product        : CICS Transaction Gateway
*
*         Description    : This sample shows the use of the CICS Transaction
*                                Gateway ECI API within a servlet. It queries
*                                the Client for a list of available servers
*                                and then launches a program on the selected
*                                server.
*
*         Pre-requisites : This requires the selected CICS Program (EC01/EC02)
*                                to be installed on the selected CICS Server
*                                for operation. The CICS Server must be set up
*                                to return the contents of the commarea as
*                                ASCII text. If the code page of the
*                                application differs to that of the server,
*                                data conversion must be performed at the
*                                server by making use of CICS supplied
*                                resource conversion capabilities, such as
*                                DFHCNV macro definitions.
*
*                                Use a version of the JDK that the CICS Transaction
*                                Gateway supports if you recompile this sample. See
*                                the product documentation for supported Java levels.
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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.ibm.ctg.client.*;


/* EciA1:
*
* This sample illustrates how the ECIRequest object may be utilised from
* within a servlet context. The servlet may be started with three optional
* initialisation parameters - The Gateway URL, SSL Classname and SSL
* Password. If no URL is provided a defeault URL of 'local:' is used.
*
* The servlet then displays an initial page that allows a user to select a
* server that is listed in the gateways' ini file and to input a user ID,
* password, and the size of the COMMAREA to be used.
*
* On submission the servlet runs the transaction and displays the results to
* the user. Should an exception occur at any point then the exception is
* displayed to the user and logged.
*
* The servlet uses a synchronous, non-extended ECIrequest object. For details
* or using an asynchronous callbackable ECIRequest object see the intermediate
* ECI sample - EciI1.java
*
* Note the this sample is not intended as the foundation on which to develop
* any further applications.
*/

public class EciA1 extends HttpServlet
{

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

 	// Define the serialVersionUID. It is good practice to define
 	// this for Serializable classes. Not doing so will generate a
 	// compiler warning.
 	private static final long serialVersionUID = 1L;
 	
    // Variables
    private static JavaGateway gateway = null;
    private String gatewayURL = "";
    private String servletURL = "";

    // Constants
    private static final int DEFAULT_SIZE = 100;
    private static final int MINIMUM_SIZE = 50;
    private static final int MAX_COMMAREA_SIZE = 32500;
    private static final String DEFAULT_URL = "local:";
    private static final String ENCODING = "ASCII";
    private static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC"
                                          + " \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
    private static final String EC02 = "EC02";

    /*
    * Servlet initialisation
    *
    * Reads the initialisation parameters and creates the gateway object
    */
    public void init(ServletConfig sc) throws ServletException
    {
        super.init(sc);

        gatewayURL = sc.getInitParameter("GatewayURL");

        if (gatewayURL == null)
        {
            gatewayURL = DEFAULT_URL;
        }

        gateway = new JavaGateway();

        String sslKeyring  = sc.getInitParameter("SSLClassname");
        String sslPassword = sc.getInitParameter("SSLPassword");

        if (sslKeyring != null && sslPassword != null)
        {
            Properties sslProps = new Properties();
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_CLASS, sslKeyring.trim());
            sslProps.setProperty(JavaGateway.SSL_PROP_KEYRING_PW, sslPassword.trim());

            try
            {
                gateway.setProtocolProperties(sslProps);
            }
            catch (IOException e)
            {
                StringWriter exceptionOut = new StringWriter();
                e.printStackTrace(new PrintWriter(exceptionOut ));
                String trace = exceptionOut.toString();

                log(trace);
            }
        }

        // To enable tracing uncomment the code below

        //T.setDebugOn(true);
    }

    /*
    * Servlet destruction
    *
    * Closes the gateway.
    */
    public void destroy()
    {
        try
        {
            if (gateway!=null)
            {
                if (gateway.isOpen())
                {
                    gateway.close();
                }
            }
        }
        catch (IOException e)
        {
            StringWriter exceptionOut = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionOut ));
            String trace = exceptionOut.toString();

            log(trace);
        }
    }

    /*
    * doGet:
    *
    * Connects to the gateway and then sets the initial page of the servlet
    * where the user can input the user ID, password, COMMAREA size and select
    * a server
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        if (servletURL == null || servletURL.trim().equals(""))
        {
        	servletURL = request.getRequestURI();
        }
        try
        {
            synchronized(this)
            {
                if (!gateway.isOpen())
                {
                    gateway.setURL(gatewayURL);
                    gateway.open();
                }
            }
            this.displayInitialPage(session, response);
        }
        catch (IOException e)
        {
            this.displayException(e, response, session);
        }
    }

    /*
    * displayInitialPage
    *
    * Displays the name of the gateway opened, a combo box containing the
    * results of the getServers call, a text area for a user ID, a text area
    * for a password, a list box containing available programs and a text area
    * for the comm area size
    */
    private void displayInitialPage(HttpSession session, HttpServletResponse response) throws IOException
    {
        try
        {
            PrintWriter out = response.getWriter();
            out.println(DOCTYPE);
            out.println("<HTML>");
            out.println("<HEAD><TITLE>IBM CICS Transaction Gateway</TITLE></HEAD>");
            out.println("<BODY BGCOLOR=\"#FDF5E6\">");
            out.println("<H1 ALIGN=CENTER>"
                        + "IBM CICS Transaction Gateway Advanced "
                        + "Java ECI Sample</H1>\n");
            out.println("<FORM ACTION=\""+servletURL+"\" METHOD=\"POST\">");
            out.println("<CENTER>");
            out.println("<PRE>");
            out.println("Gateway = " + gatewayURL);
            out.println("</PRE>");
            this.getServers(out);
            out.println("<LABEL FOR=\"ProgToRun\">Select program to run</LABEL>");
            out.println("<BR><SELECT ID=\"ProgToRun\" NAME = \"Program\" SIZE=\"2\">");
            out.println("<OPTION VALUE = \"EC01\" SELECTED>EC01");
            out.println("<OPTION VALUE = \"EC02\">EC02");
            out.println("</SELECT>");
            out.println("<BR>");
            out.println("<LABEL FOR=\"CommAreaSize1\">COMMAREA size</LABEL>");
            out.println("<BR><INPUT TYPE=\"TEXT\" "+
                        "NAME=\"CommAreaSize\" VALUE=\"50\" MAXLENGTH=\"5\" "
                        + "SIZE=\"6\" ID=\"CommAreaSize1\"><BR>");
            out.println("<LABEL FOR=\"UserId1\">User ID</LABEL>");
            out.println("<BR><INPUT TYPE=\"TEXT\""+
                        "NAME=\"UserId\"  VALUE=\"\" MAXLENGTH=\"8\" ID=\"UserId1\"><BR>");
            out.println("<LABEL FOR=\"Password1\">Password or password phrase</LABEL>");
            out.println("<BR><INPUT TYPE=\"PASSWORD\""+
                        "NAME=\"Password\" VALUE=\"\" MAXLENGTH=\"8\" ID=\"Password1\"><BR>");
            out.println("<INPUT TYPE=\"SUBMIT\""+
                        "NAME=\"connectButton\"  VALUE=\"Submit\">");
            out.println("</CENTER>");
            out.println("</FORM>");
            out.println("</BODY>");
            out.println("</HTML>");
        }
        catch (Exception e)
        {
            this.displayException(e, response, session);
        }
    }

    /*
    * getServers:
    *
    * Queries the gateway for a list of available servers from which the user
    * selects a server to run a selected program against.
    */
    private void getServers(PrintWriter out) throws IOException, Exception
    {
        ECIRequest eci = ECIRequest.listSystems(99);
        int returnCode = gateway.flow(eci);

        this.checkReturnCode(returnCode, eci);

        Vector<String> servers = eci.SystemList;

        if (eci.numServersReturned > 0 && servers != null)
        {
            int serverListLength = servers.size();
            boolean display = true;

            out.println("<LABEL FOR=\"ServerSelect\">Choose a server to send the request to</LABEL>");
            out.println("<BR><SELECT ID=\"ServerSelect\" NAME = \"Server\">");
            for (int i = 0; i<serverListLength; i++)
            {
                if (display)
                {
                    out.println("<OPTION VALUE = \"" + servers.elementAt(i)
                                + "\" >" + servers.elementAt(i));
                    display = false;
                }
                else
                {
                    display = true;
                }
            }
            out.println("</SELECT>");
            out.println("<BR>");
        }
    }

    /*
    * doPost:
    *
    * Handles the submission of the parameters listed in the initial page
    * (see displayInitialPage).
    */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = null;

        try
        {
            session = request.getSession(true);
            response.setContentType("text/html");

            this.runTransaction(session, request);
            this.displayOutput(response, session);
        }
        catch (Exception e)
        {
            this.displayException(e, response, session);
        }
    }

    /*
    * runTransaction:
    *
    * This reads the values submitted by the user and then creates a
    * synchronous, non extended ECIRequest. For an example of how an
    * Asynchronous, callbackable ECIRequest may be used refer to the
    * Intermediate ECI Sample - EciI1.java.
    * The request is then flowed to the gateway and the result analysed.
    *
    * Note that as this is not extended the logical unit of work (LUW) is
    * automatically committed if the program ends normally, otherwise it is
    * backed out (Rolledback).
    */
    private void runTransaction(HttpSession session, HttpServletRequest request)
    throws IOException, Exception
    {
        String userId = request.getParameter("UserId");
        String password = request.getParameter("Password");
        String server = request.getParameter("Server");
        String program = request.getParameter("Program");

        int commAreaSize;
        if (program.equals(EC02))
        {
            commAreaSize = 50;
        }
        else
        {
            try
            {
                commAreaSize = Integer.parseInt(request.getParameter("CommAreaSize").trim());
            }
            catch (NumberFormatException e)
            {
                commAreaSize = DEFAULT_SIZE;
            }

            if (commAreaSize>MAX_COMMAREA_SIZE)
            {
                commAreaSize = MAX_COMMAREA_SIZE;
            }

            if (commAreaSize<MINIMUM_SIZE)
            {
                commAreaSize = MINIMUM_SIZE;
            }
        }

        byte[] commArea = new byte[commAreaSize];

        ECIRequest eci = new ECIRequest(ECIRequest.ECI_SYNC,
                                        server.trim(),
                                        userId.trim(),
                                        password.trim(),
                                        program.trim(),
                                        null,
                                        commArea,
                                        commAreaSize,
                                        ECIRequest.ECI_NO_EXTEND,
                                        ECIRequest.ECI_LUW_NEW);

        int returnCode = gateway.flow(eci);
        this.checkReturnCode(returnCode, eci);
        session.setAttribute("SESSION_ECI_OBJECT", eci);
    }

    /*
    * displayOutput:
    *
    * Displays the values returned from running the selected program on the
    * selected server. The values displayed are the gateway URL, server,
    * program, comm area size, userid, password, Cics_Rc, Abend Code and the
    * CommArea in both the coded encoding and Hex.
    */
    private void displayOutput(HttpServletResponse response,
                               HttpSession session) throws IOException
    {
        PrintWriter out = response.getWriter();
        ECIRequest eci = (ECIRequest)session.getAttribute("SESSION_ECI_OBJECT");
        out.println(DOCTYPE);
        out.println("<HTML>");
        out.println("<HEAD><TITLE>IBM CICS Transaction Gateway</TITLE></HEAD>");
        out.println("<BODY BGCOLOR=\"#FDF5E6\">");
        out.println("<H1 ALIGN=CENTER>Program results</H1>\n");
        out.println("<PRE>");
        out.println("\tGateway \t=\t" + gatewayURL);

        String server = eci.Server.trim();
        if (server.length()==0)
        {
            out.println("\tServer \t\t=\tDefault server");
        }
        else
        {
            out.println("\tServer \t\t=\t" + eci.Server);
        }
        out.println("\tProgram \t=\t" + eci.Program);
        out.println("\tCOMMAREA size \t=\t" + eci.Commarea_Length);
        out.println("\tUser ID \t\t=\t" + eci.Userid);
        out.println("\tPassword \t=\t" + eci.Password);

        if (eci.Commarea != null)
        {
            out.println("\tReturn code \t=\t" + eci.Cics_Rc);
            out.println("\tAbend code \t=\t" + eci.Abend_Code);
            try
            {
                out.println("\n\n\tCOMMAREA:\n\t"
                            + new String(eci.Commarea, ENCODING).trim());
            }
            catch (UnsupportedEncodingException e)
            {
                out.println("\n\n\tCOMMAREA: Encoding '"
                            + ENCODING + "' not supported");
            }

            out.println("\tCOMMAREA hex value:");
            String hexValue = "";
            for (int i = 0; i<eci.Commarea.length; i++)
            {
                hexValue = hexValue + Integer.toHexString(eci.Commarea[i]);
            }
            out.println("\t" + hexValue);
        }
        out.println("</PRE>");
        out.println("</BODY>");
        out.println("</HTML>");
    }


    /*
    * checkReturnCode:
    *
    * Verify no exception has occurred by analysing the result of the flow.
    * If there is a problem then identify which type and rethrow the
    * exception.
    */
    private void checkReturnCode(int returnCode, ECIRequest eci)
    throws Exception
    {
        if (returnCode!=0)
        {
            if (eci.getCicsRc()==0)
            {
                throw new Exception("Gateway exception: Return code number:"
                                    + returnCode
                                    + " Return code String: "
                                    + eci.getRcString());
            }
            else
            {

                if (eci.getCicsRc()==ECIRequest.ECI_ERR_SECURITY_ERROR
                    || (eci.Abend_Code != null
                        && eci.Abend_Code.equalsIgnoreCase("AEY7")))
                {
                    throw new Exception("Server is unable to validate " +
                                        "user ID or password");
                }
                else
                {
                    throw new Exception("CICS exception: Return code number:"
                                        + returnCode
                                        + " Return code String: "
                                        + eci.getCicsRcString());
                }
            }
        }
    }

    /*
    * displayException:
    *
    * Display any thrown exceptions
    */
    private void displayException(Exception e,
                                  HttpServletResponse response,
                                  HttpSession session)
    {
        log("Exception displayed:\n" + e.getMessage());

        ECIRequest eci = (ECIRequest)session.getAttribute("SESSION_ECI_OBJECT");
        PrintWriter out = null;
        try
        {
            out = response.getWriter();

            StringWriter exceptionOut = new StringWriter();
            e.printStackTrace(new PrintWriter(exceptionOut ));
            String trace = exceptionOut.toString();

            out.println(DOCTYPE);
            out.println("<HTML>");
            out.println("<HEAD><TITLE>IBM CICS Transaction Gateway</TITLE></HEAD>");
            out.println("<BODY BGCOLOR=\"#FDF5E6\">");
            out.println("<H1 ALIGN=CENTER>Exception</H1>\n");
            out.println("<PRE>");
            out.println(trace);
            if (eci != null)
            {
                out.println("\tCICS Return code \t=\t" + eci.getCicsRcString());
                out.println("\tReturn Code \t\t=\t" + eci.getRcString());
                out.println("\tAbend code \t\t=\t" + eci.Abend_Code);
            }
            out.println("</PRE>");
            out.println("</BODY>");
            out.println("</HTML>");
        }
        catch (IOException f)
        {
            log("Exception reported while trying to report original exception");
            log("Additional exception:\n" + f.getMessage());
        }
    }
}
