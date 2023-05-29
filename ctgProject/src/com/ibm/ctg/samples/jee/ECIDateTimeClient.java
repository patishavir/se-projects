/*
*         File Name     : ECIDateTimeClient.java
*
*         Product       : CICS Transaction Gateway
*
*         Description   : This is a simple client to test the ECI EJB.
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

import javax.naming.*;
import javax.rmi.*;

public class ECIDateTimeClient {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    public static void main(String[] args) {
        try {

            // Create the initial context and look up the ECIDateTime bean.
            Context ic = new InitialContext();
            Object or = ic.lookup("ECIDateTimeBean1");
            if (or != null) {

                /*
                 * Successfully retrieved a reference to the EJB Home interface. Narrow it
                 * and then create the EJB bean, obtaining the remote interface.
                 */
                ECIDateTimeHome home = (ECIDateTimeHome)PortableRemoteObject.narrow(or, ECIDateTimeHome.class);
                ECIDateTime ecd = home.create();

                // call the business method, then remove the EJB.
                System.out.println("CICS Date/Time=" + ecd.execute());
                ecd.remove();
            }
            else {
                System.out.println("null lookup for ECIDateTimeBean1");
            }
            System.exit(0);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
