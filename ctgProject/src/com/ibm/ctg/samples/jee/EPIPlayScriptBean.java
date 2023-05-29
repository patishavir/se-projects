/*
*
*     File Name     : EPIPlayScriptBean.java
*
*     Product       : CICS Transaction Gateway
*
*     Description   : This is the actual implementation of the EPI EJB
*
*     Pre-requisites: This is part of the JEE EPI Sample which demonstrates the
*                     EPI JEE Resource Adapter, and requires a suitable JEE
*                     server which supports the JEE connector architecture
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
*     The following code is sample code created by IBM Corporation.  This
*     sample code is not part of any standard IBM product and is provided
*     to you solely for the purpose of assisting you in the development of
*     your applications.  The code is provided 'AS IS', without warranty
*     or condition of any kind.  IBM shall not be liable for any damages
*     arising out of your use of the sample code, even if IBM has been
*     advised of the possibility of such damages.
*/

package com.ibm.ctg.samples.jee;

import com.ibm.connector2.cics.*;
import javax.resource.cci.*;
import javax.ejb.*;
import java.util.*;


/**
 * This implementation is for a Stateful Session EJB. It is designed for the EJB 1.1
 * Environment. It demonstrates the Managed Scenario of the CICS EPI Resource
 * Adapter.
 */
public class EPIPlayScriptBean implements SessionBean {

   private static final long serialVersionUID = 1L;
   
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    private javax.ejb.SessionContext mySessionCtx = null;

    // Internal Properties
    private Connection epiConn;              // EPI Connection
    private Interaction epiInt;              // EPI Interaction
    private EPIInteractionSpec eSpec;        // EPI Interaction Spec
    private EPIScreenRecord scr;             // Screen Record
    private AIDKey enter;                    // The enter key

    /**
     * This method gets called by the container when reactivating a bean.
     * This code re-establishes a connection, recreate any internal properties
     * and then replays all the scripts that have been used with this bean
     * to get to where we were before.
     */
    public void ejbActivate() throws java.rmi.RemoteException {

        // Recreate the internal objects.
        scr = new EPIScreenRecordImpl();
        eSpec = new EPIInteractionSpec();
        enter = new AIDKey("enter");

        try {

            // Get a connection to CICS using EPI.
            getConnection();
        }
        catch (CreateException ce) {
            ce.printStackTrace();
        }
    }

    /**
     * This method gets called by the container when creating a bean.
     * This establishes a connection and creates internal properties.
     */
    public void ejbCreate() throws javax.ejb.CreateException  {

        // Create the internal objects.
        scr = new EPIScreenRecordImpl();
        eSpec = new EPIInteractionSpec();
        enter = new AIDKey("enter");
        getConnection();
    }

    /**
     * This method gets called by the container when passivating a bean.
     * A connection cannot be maintained so this code tidies up and reset any
     * references which are not worth saving or can't be set to null.
     */
    public void ejbPassivate() throws java.rmi.RemoteException {
        try {
            epiInt.close();
            epiConn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        epiInt = null;
        eSpec = null;
        epiConn = null;
        scr = null;
        enter = null;
    }

    /**
     * This method gets called by the container when removing a bean.
     * This tidies up the connection and sets any references to null.
     */
    public void ejbRemove() throws java.rmi.RemoteException {
        try {
            if (epiInt != null) {
                epiInt.close();
            }
            if (epiConn != null) {
                epiConn.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        epiInt = null;
        eSpec = null;
        epiConn = null;
        scr = null;
        enter = null;
    }

    /**
     * This is the business method of this bean. It is designed to take
     * a script and navigate through 3270 transactions based on the
     * contents of the script. Once the script has been played, it returns
     * the current contents of the screen as a SimpleScreenRecord.
     *
     * The Script language is as follows
     *    S(xxxx)               Start Transaction "xxxx"
     *    F(nn)="text"          Set Field number nn to "text"
     *    P(aid)                Press key "aid", see JavaDoc for AIDKey class for information of aid name strings
     *    C(row,col)            Set cursor to row, col
     *
     * So an example script might be: S(CESN) F(6)=\"myuser\" F(9)=\"mypass\" P(enter)
     * Note the escaping of the quotes.
     *
     * @param script The script to play
     * @return the Current Screen Contents
     */
    public String[] playScript(String script) {

        Vector<String> fieldTexts = new Vector<String>();

        // Parse the script
        int step = 0;
        for (int i = 0; i< script.length(); i++) {
            char x = script.charAt(i);

            // look for a start transaction request
            if (x == 'S') {
                step = startTxn(script,i);
                i = i + step;
            }

            // look for a set field request
            if (x == 'F') {
                step = updateField(script,i);
                i = i + step;
            }

            // look for a press key request
            if (x == 'P') {
                step = pressKey(script, i);
                i = i + step;
            }

            // look for a set cursor request
            if (x == 'C') {
                step = setCursor(script, i);
                i = i + step;
            }

            // look for a return field text request
            if (x == 'R') {
                step = returnField(script, i, fieldTexts);
                i = i + step;
            }
        }

        String[] textToRet = new String[fieldTexts.size()];
        for (int i = 0; i < fieldTexts.size(); i++) {
            textToRet[i] = (String)fieldTexts.elementAt(i);
        }
        return textToRet;
    }

    /**
     * get a connection from the EPI Resource Adapter.
     */
    private void getConnection() throws CreateException {

        /*
         * Lookup an EPI Connection Factory (You will need to ensure that you
         * setup your environment so that the JNDI name refers to the required
         * EPI Resource Adapter deployed).  Finally, get a connection from the
         * connection factory and get an interaction.
         */
        ConnectionFactory cf = null;
        Object cfLookup = null;

        try {
            javax.naming.Context ic = new javax.naming.InitialContext();
            cfLookup = ic.lookup("java:comp/env/EPI");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CreateException("Lookup for java:comp/env/EPI failed.Exception Message=" + e.getMessage());
        }
        if (cfLookup == null) {
            throw new CreateException("Lookup for java:comp/env/EPI resulted in a null reference");
        }

        cf = (ConnectionFactory)cfLookup;

        try {
            epiConn = (Connection)cf.getConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CreateException("failed to get connection from EPI Factory.Exception Message=" + e.getMessage());
        }
        try {
            epiInt = (Interaction)epiConn.createInteraction();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CreateException("failed to get interaction from EPI Connection.Exception Message=" + e.getMessage());
        }
    }

    /**
     * Parse the next part of the script as a Start Transaction was
     * Requested.
     */
    private int startTxn(String script, int start) {

        int step = 1;

        // The next character after S must be "(".
        char x = script.charAt(start + step);
        if (x != '(') {
            return step;
        }
        step++;

        // Get the contents within the brackets, i.e. the transaction name.
        StringBuffer txn = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ')') {
            txn.append(x);
            step++;
        }

        /*
         * Now setup the InteractionSpec. First, the transaction name and the
         * interaction verb are set. The outputAttributeType is set to NULL so
         * that the Screen Record can locate the fields, finally the call is
         * executed.  Note that the input record is null as there is no screen
         * being sent to CICS.  This is possible because the function name is set.
         */
        eSpec.setFunctionName(txn.toString());
        eSpec.setInteractionVerb(EPIInteractionSpec.SYNC_SEND_RECEIVE);
        eSpec.setAID(enter);
        try {
            epiInt.execute(eSpec, null, scr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return step;
    }

    /**
     * Parse the next part of the script, as a set-field text was requested.
     */
    private int updateField(String script, int start) {
        int step = 1;

        // The next character after F must be "(".
        char x = script.charAt(start + step);
        if (x != '(') {
            return step;
        }
        step++;

        // Get the contents within the () which is the field number.
        StringBuffer fldindex = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ')') {
            fldindex.append(x);
            step++;
        }
        int fldno = -1;
        try {
            fldno = Integer.parseInt(fldindex.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (fldno <= -1) {
            return step;
        }
        step++;

        // The next character after ")" must be "=".
        x = script.charAt(start + step);
        if (x != '=') {
            return step;
        }
        step++;

        // The next character after "=" must be " (a double quote).
        x = script.charAt(start + step);
        if (x != '"') {
            return step;
        }
        step++;

        // Get the contents within the quotes, which is the field text.
        StringBuffer fldtext = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != '"') {
            fldtext.append(x);
            step++;
        }

        // Set the field text for the selected field.
        try {
            EPIFieldRecord f = scr.getField(fldno);
            f.setText(fldtext.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return step;
    }

    /**
     * Parse the next part of the script, as a press-key was requested.
     */
    private int pressKey(String script, int start) {
        int step = 1;

        // The next character after "P" must be "(".
        char x = script.charAt(start + step);
        if (x != '(') {
            return step;
        }
        step++;

        // Get the text within the brackets.
        StringBuffer aidstr = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ')') {
            aidstr.append(x);
            step++;
        }

        // Construct the AID key object
        AIDKey aid = new AIDKey(aidstr.toString());


        /*
         * Now build the Interaction Spec.
         * Ensure that the function name is null so that the contents of the
         * screen is used, and another transaction is not started. Set the mode
         * to send_receive and set the aid key.  Finally, execute the call.
         */
        eSpec.setFunctionName(null);
        eSpec.setInteractionVerb(EPIInteractionSpec.SYNC_SEND_RECEIVE);
        eSpec.setAID(aid);
        try {
            epiInt.execute(eSpec, scr, scr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return step;
    }

    /**
     * Parse the next part of the script, as a set-cursor was requested.
     */
    private int setCursor(String script, int start) {
        int step = 1;

        // The next character after C must be "(".
        char x = script.charAt(start + step);
        if (x != '(') {
            return step;
        }
        step++;

        // Get the text up to the comma, this will be the row.
        StringBuffer rowstr = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ',') {
            rowstr.append(x);
            step++;
        }
        step++;

        // Get the text from the comma up to the ")", this will be the column.
        StringBuffer colstr = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ')') {
            colstr.append(x);
            step++;
        }

        int col = 1;
        int row = 1;
        try {
            col = Integer.parseInt(colstr.toString());
            row = Integer.parseInt(rowstr.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return step;
        }

        // Set the cursor row and column in the interaction spec.
        eSpec.setCursorColumn(col);
        eSpec.setCursorRow(row);
        return step;
    }

    /**
     * Parse the next part of the script, as a return-field was requested.
     */
    private int returnField(String script, int start, Vector<String> fldTexts) {
        int step = 1;

        // Next character after "R" must be "("
        char x = script.charAt(start + step);
        if (x != '(') {
            return step;
        }
        step++;

        // Get the contents within the brackets, the field number.
        StringBuffer fldindex = new StringBuffer();
        while (start + step < script.length() && (x = script.charAt(start + step)) != ')') {
            fldindex.append(x);
            step++;
        }
        int fldno = -1;
        try {
            fldno = Integer.parseInt(fldindex.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (fldno <= -1) {
            return step;
        }

        // Get the field text and add it to the list of returned texts.
        try {
            EPIFieldRecord field = scr.getField(fldno);
            fldTexts.add(field.getText());
        }
        catch (Exception e) {
            // Just ignore a request for a bad field.
        }
        return step;
    }

    /**
     * Return the saved Session Context.
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * Store the provided Session Context.
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) throws java.rmi.RemoteException {
        mySessionCtx = ctx;
    }
}
