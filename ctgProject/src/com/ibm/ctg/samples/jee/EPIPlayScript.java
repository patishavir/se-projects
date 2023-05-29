/*
*
*       File Name     : EPIPlayScript.java
*
*       Product       : CICS Transaction Gateway
*
*       Description   : This is the remote interface for the EPI EJB.
*
*       Pre-requisites: This is part of the JEE EPI Sample which demonstrates the
*                       EPI JEE Resource Adapter, and requires a suitable JEE
*                       server which supports the JEE connector architecture
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
*       The following code is sample code created by IBM Corporation.  This
*       sample code is not part of any standard IBM product and is provided
*       to you solely for the purpose of assisting you in the development of
*       your applications.  The code is provided 'AS IS', without warranty
*       or condition of any kind.  IBM shall not be liable for any damages
*       arising out of your use of the sample code, even if IBM has been
*       advised of the possibility of such damages.
*
*/
package com.ibm.ctg.samples.jee;

public interface EPIPlayScript extends javax.ejb.EJBObject {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5655-Y20 (c) Copyright IBM Corp. 2001, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

    String[] playScript(String script) throws java.rmi.RemoteException;
}
