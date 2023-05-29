/*
*    File Name     : EC03ChannelHome.java
*         
*    Product       : CICS Transaction Gateway
*
*    Description   : This is the home interface for the EC03 Channel EJB.
*
*                    This is part of the JEE EC03 Channel Bean sample which
*                    demonstrates the support for channels and containers in 
*                    the CICS ECI resource adapter. Channels and containers are
*                    only supported with the IPIC protocol.
*                  
*    Pre-requisites: Requires a JEE server with the CICS ECI resource adapter
*                    or CICS ECI XA resource adapter deployed and an ECI
*                    connection factory defined. The connection factory must be
*                    configured to use a CICS server over an IPIC connection and
*                    the CICS server must have the EC03 program installed.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5655-Y20 
*  
* (C) Copyright IBM Corp. 2008, 2014 All Rights Reserved.  
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

import javax.ejb.EJBHome;

public interface EC03ChannelHome extends EJBHome {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2008, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /**
    * Creates a default instance of Session Bean: EC03Channel
    */
   public com.ibm.ctg.samples.jee.EC03Channel create()
      throws javax.ejb.CreateException, java.rmi.RemoteException;
}
