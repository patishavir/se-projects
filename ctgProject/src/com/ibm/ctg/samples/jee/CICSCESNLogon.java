/*
*
*    File Name      : CICSCESNLogon.java
*
*    Product        : CICS Transaction Gateway
*
*    Description    : This is a sample class to demonstrate how to implement a
*                     LogonLogoff Class for the JEE EPI Resource Adapter.
*
*    Pre-requisites: This is part of the JEE EPI Sample which demonstrates the
*                    EPI JEE Resource Adapter, and requires a suitable JEE
*                    server which supports the JEE connector architecture
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
*    The following code is sample code created by IBM Corporation.  This
*    sample code is not part of any standard IBM product and is provided
*    to you solely for the purpose of assisting you in the development of
*    your applications.  The code is provided 'AS IS', without warranty
*    or condition of any kind.  IBM shall not be liable for any damages
*    arising out of your use of the sample code, even if IBM has been
*    advised of the possibility of such damages.
*/
package com.ibm.ctg.samples.jee;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;

import com.ibm.connector2.cics.AIDKey;
import com.ibm.connector2.cics.EPIFieldRecord;
import com.ibm.connector2.cics.EPIInteractionSpec;
import com.ibm.connector2.cics.EPIScreenRecord;
import com.ibm.connector2.cics.EPIScreenRecordImpl;

public class CICSCESNLogon implements LogonLogoff {

	static final String copyright_notice = "Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";

	/*
	 * The following entries are for CICS Transaction Server on OS/390 1.3 you
	 * may need to change the okmsg as appropriate.
	 */
	static final String okmsg = "DFHCE3549 Sign-on is complete";
	static final int userFld = 10; // location of userid entry field
	static final int passFld = 16; // location of password entry field
	static final int okmsgFld = 1; // location of CESN response msg
	static final int errMsgFld = 1; // location of CESN error msg

	static final boolean DEBUG = false;

	/**
	 * This will logon to a CICS Server on behalf of the EPI Resource Adapter.
	 *
	 * Each managed connection holds an instance of the LogonLogoff class if one
	 * is registered. It exists for the lifetime of the managed connection.
	 * LogonLogoff classes should not keep permanent references to the
	 * connection passed to it. It must either hold it in a method variable or
	 * set the reference to null on exit if held in a class variable.
	 *
	 * IMPORTANT: Never close the connection. This connection was not created by
	 * the LogonLogoff class.
	 *
	 * @param conn
	 *            a valid EPI connection
	 * @param security
	 *            the security credentials
	 */
	public void logon(javax.resource.cci.Connection conn, javax.security.auth.Subject security)
			throws javax.resource.ResourceException {
		if (conn == null || security == null) {
			throw new javax.resource.spi.SecurityException(
					"Unable to logon, no Connection and/or Security Information Provided");
		}

		// Create the necessary interaction objects.
		Interaction epiInt = (Interaction) (conn.createInteraction());
		EPIInteractionSpec spec = new EPIInteractionSpec();

		// Configure the spec to perform a CESN, and execute the call.
		spec.setAID(AIDKey.enter);
		spec.setFunctionName("CESN");
		spec.setInteractionVerb(EPIInteractionSpec.SYNC_SEND_RECEIVE);
		EPIScreenRecord screen = new EPIScreenRecordImpl();
		epiInt.execute(spec, null, screen);

		if (DEBUG) {
			System.out.println("Screen returned is:");
			printScreen(screen);
		}

		/*
		 * Extracting the security credentials from the security object.
		 *
		 * DoPrivileged calls require any variables used from outside the
		 * doPrivileged block to be declared final. We need a doPrivileged block
		 * as getPrivateCredentials is subject to a security check. You will
		 * need javax.security.auth.PrivateCredentialPermission granted to this
		 * code when run under a security manager.
		 */
		final javax.security.auth.Subject fSecurity = security;

		final Iterator<PasswordCredential> it = AccessController
				.doPrivileged(new PrivilegedAction<Iterator<PasswordCredential>>() {
					public Iterator<PasswordCredential> run() {
						return fSecurity.getPrivateCredentials(PasswordCredential.class).iterator();
					}
				});

		PasswordCredential pc = null;
		if (it.hasNext()) {
			pc = AccessController.doPrivileged(new PrivilegedAction<PasswordCredential>() {
				public PasswordCredential run() {
					return it.next();
				}
			});
		}
		if (pc == null) {
			throw new javax.resource.spi.SecurityException("Unable to logon, No Security Information Provided");
		}
		String user = pc.getUserName();
		String pass = new String(pc.getPassword());

		/*
		 * Put userid and password into the fields, ensuring the function name
		 * is set to null to continue with screen interaction and execute the
		 * call.
		 */
		EPIFieldRecord field = null;
		try {
			field = screen.getField(userFld);
			field.setText(user);
			field = screen.getField(passFld);
			field.setText(pass);
		} catch (ScreenException se) {
			epiInt.close();
			throw new javax.resource.spi.SecurityException("Unable to apply security credentials");
		}
		spec.setFunctionName(null);
		epiInt.execute(spec, screen, screen);

		if (DEBUG) {
			System.out.println("Screen returned is:");
			printScreen(screen);
		}

		// Check to see if the field returned is what we expect for successful
		// signon.
		try {
			field = screen.getField(okmsgFld);
		} catch (ScreenException se) {
			epiInt.close();
			throw new javax.resource.spi.SecurityException("Unable to determine if logon successful");
		}
		if (field.getText().indexOf(okmsg) == -1) {

			// Get the error message.
			String response = "";
			try {
				field = screen.getField(errMsgFld);
				response = field.getText();
			} catch (ScreenException se) {
				// Ignore this exception, response will be blank.
			}
			if (DEBUG) {
				System.out.println("Logon Unsuccessful");
				System.out.println("Response was:" + response);
			}
			epiInt.close();
			throw new javax.resource.spi.SecurityException("Logon Failed:" + response);
		}
		epiInt.close();

		/*
		 * Note that on exit, the reference to the connection will be lost which
		 * is ok. You must NEVER close the connection in the LogonLogoff class.
		 */
	}

	/**
	 * Logoff doesn't need to be implemented, the EPI Resource Adapter never
	 * calls the Logoff method
	 */
	public void logoff(javax.resource.cci.Connection conn) {
		return;
	}

	/**
	 * Simple method to display the screen when in debug mode.
	 */
	private void printScreen(EPIScreenRecord inscr) {
		int col = 1;
		int row = 1;
		System.out.println("\n Beginning of Screen \n\n");
		for (int i = 0; i < inscr.getFieldCount(); i++) {

			try {
				EPIFieldRecord f = inscr.getField(i + 1);
				while (f.getTextRow() > row) {
					System.out.print("\n");
					row++;
					col = 1;
				}
				while (f.getTextCol() > col) {
					System.out.print(" ");
					col++;
				}
				if (f.isDisplay()) {
					System.out.print(f.getText());
					col += f.getText().length();
				}
			} catch (ScreenException se) {
			}

		}
		System.out.print("\n End of Screen \n\n");
	}
}
