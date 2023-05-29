/*
*
*     File Name     : EPIPlayScriptClient.java
*
*     Product       : CICS Transaction Gateway
*
*     Description   : This is a simple client to test the EPI EJB.
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

import javax.naming.*;
import javax.rmi.*;

public class EPIPlayScriptClient {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    public static void main(String[] args) {

        EPIPlayScript eps = null;

        Context ic = null;
        Object or = null;

        try {

            // Create the initial context for lookups.
            ic = new InitialContext();
        }
        catch (Exception e) {

            // Ignore any exception at this point.
        }

        try {
            // Lookup the EPIPlayScript Bean.
            or = ic.lookup("EPIPlayScriptBean1");
            if (or != null) {
                /*
                 * A reference to the EJB Home interface has been retrieved.
                 * Narrow it and then create the EJB bean, obtaining its remote
                 * interface.
                 */
                EPIPlayScriptHome home = (EPIPlayScriptHome)PortableRemoteObject.narrow(or, EPIPlayScriptHome.class);
                eps = home.create();

                /*
                 * Call the business method. The script provided here is just a
                 * sample, you can provide your own scripts if you wish.
                 */
                String[] texts = eps.playScript("S(EP02)P(enter)P(enter)P(enter)P(enter)R(2)R(6)");

                // Display the results.
                if (texts.length >= 2) {
                    System.out.println("Field 2 = " + texts[0]);
                    System.out.println("Field 6 = " + texts[1]);
                }
                else {
                    System.out.println("The expected fields were not returned.");
                }
            }
            else {
                System.out.println("null lookup for EPIPlayScriptBean1");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // Ensure the EJB is removed.
            if (eps != null) {
                try {
                    System.out.println("invoking remove on the bean");
                    eps.remove();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }
}
