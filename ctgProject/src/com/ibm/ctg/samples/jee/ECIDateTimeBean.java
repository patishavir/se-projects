/*
*
*         File Name     : ECIDateTimeBean.java
*
*         Product       : CICS Transaction Gateway
*
*         Description   : This is the actual implementation of the ECI EJB
*
*         Pre-requisites: This is part of the JEE ECI Sample which demonstrates the
*                         ECI JEE Resource Adapter, and requires a suitable JEE
*                         server which supports the JEE connector architecture
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5655-Y20 
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

package com.ibm.ctg.samples.jee;

import com.ibm.connector2.cics.*;
import javax.resource.cci.*;
import javax.resource.*;
import javax.ejb.*;


/**
 * This implementation is for a Stateless EJB. It is designed for the EJB 1.1
 * Environment. It demonstrates the Managed Scenario of the CICS ECI Resource
 * Adapter.
 */
public class ECIDateTimeBean implements SessionBean {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
	
 	// Define the serialVersionUID. It is good practice to define
 	// this for Serializable classes. Not doing so will generate a
 	// compiler warning.
 	private static final long serialVersionUID = 1L;

    private javax.ejb.SessionContext mySessionCtx;
    private Connection eciConn;
    private Interaction eciInt;
    private ECIInteractionSpec eSpec;

    /**
     * This method is called by the container, when a request to create the
     * EJB is made.
     */
    public void ejbCreate() throws javax.ejb.CreateException {

        // create an Interaction spec to work with, This is specific to ECI.
        eSpec = new ECIInteractionSpec();
    }

    /**
     * The container calls this method when the EJB is to be removed.
     */
    public void ejbRemove() throws java.rmi.RemoteException {

        eSpec = null;
    }

    /**
     * This is the main method that does the call to the CICS Server.
     */
    public String execute() throws ResourceException, Exception {

        getConnection();

        /*
        * Create a record for use, nothing to put into it as this program
        * doesn't take any input from the Commarea.
        */

        JavaStringRecord jsr = new JavaStringRecord();

        // Setup the interactionSpec.

        eSpec.setCommareaLength(20);
        eSpec.setReplyLength(20);
        eSpec.setFunctionName("EC01");
        eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);

        // Make the call
        try {
            eciInt.execute(eSpec, jsr, jsr);
        }
        catch (ResourceException e) {

            // Output the stacktrace and re-throw it back to the client.
            e.printStackTrace();
            dropConnection();
            throw e;
        }
        dropConnection();
        return jsr.getText();
    }

    /**
     * Part of session bean interface, not really used in this sample.
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * Part of session bean interface, not really used in this sample.
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) throws java.rmi.RemoteException {
        mySessionCtx = ctx;
    }

    /**
     * Part of session bean interface, should not be called for stateless bean.
     */
    public void ejbActivate() throws java.rmi.RemoteException {}

    /**
     * Part of session bean interface, should not be called for stateless bean.
     */
    public void ejbPassivate() throws java.rmi.RemoteException {}

    /**
     * Get a connection to the EIS using the resource adapter.
     */
    private void getConnection() throws Exception {

        /* Lookup an ECI Connection Factory (You will need to ensure that you
         *  setup your environment so that this JNDI name refers to the required
         *  ECI Resource Adapter deployed), get a connection from the connection
         *  factory and then get an interaction.
         */
        ConnectionFactory cf = null;
        Object cfLookup = null;

        try {
            javax.naming.Context ic = new javax.naming.InitialContext();
            cfLookup = ic.lookup("java:comp/env/ECI");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lookup for java:comp/env/ECI failed.Exception Message=" + e.getMessage());
        }
        if (cfLookup == null) {
            throw new Exception("Lookup for java:comp/env/ECI resulted in a null reference");
        }

        // Cast returned object as a ConnectionFactory
        cf = (ConnectionFactory)cfLookup;

        try {
            eciConn = (Connection)cf.getConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("failed to get connection from ECI Factory.Exception Message=" + e.getMessage());
        }
        try {
            eciInt = (Interaction)eciConn.createInteraction();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("failed to get interaction from ECI Connection.Exception Message=" + e.getMessage());
        }
    }

    /**
     *  drops the connection to the EIS
     */
    private void dropConnection() {

        // tidy up the interaction and connection and set references to null.
        try {
            eciInt.close();
            eciConn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        eciInt = null;
        eciConn = null;
    }
}
