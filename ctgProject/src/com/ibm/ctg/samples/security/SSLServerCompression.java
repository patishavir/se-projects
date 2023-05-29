/*
*         File Name      : SSLServerCompression.java
*
*         Product        : CICS Transaction Gateway
*
*         Description    : This is a general sample to demonstrate the basic
*                          functionality of the Java gateway security exits.
*                          This sample demonstrates the compression of the
*                          client/server dataflows using the java.util.zip
*                          package.
*                          This sample has been extended to expose an SSL
*                          client certificate.
*
*         Pre-requisites : Use a version of the JDK that the CICS Transaction
*                          Gateway supports if you recompile this sample. See
*                          the product documentation for supported Java levels.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5725-B65,5655-Y20 
*  
* (C) Copyright IBM Corp. 2000, 2014 All Rights Reserved.  
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
package com.ibm.ctg.samples.security;

import com.ibm.ctg.client.GatewayRequest;
import com.ibm.ctg.security.*;
import com.ibm.ctg.util.RACFUserid;
import javax.security.cert.*;
import java.security.Principal;
import java.util.zip.*;
import java.io.*;
import java.net.InetAddress;

/**  Class : SSLServerCompression
*
* <p>This class gives sample implementations of the methods defined in the
* com.ibm.ctg.security.JSSEServerSecurity interface.</p>
*
* <p>Users wishing to implement compression of the dataflows between the
* client and the Gateway daemon should study the code below.</p>
*
* <p>All compression functionality has been based upon the java.util.zip
* package, the InflaterInputStream and DeflaterOutputStream classes in
* particular. Although this class implements the ServerSecurity interface,
* it only defines the decodeRequest() and the encodeReply() methods; the
* handshake methods are not used.</p>
*
* <p>This sample is based on ServerCompression.java but extended to
* demonstrate the exposure of JSSE client certificates.</p>
* 
* <p>Note that use of security exits is not required in order to perform
* mapping between client certificates and RACF userids on z/OS. CICS
* Transaction Gateway for z/OS can be configured to automatically
* map X.509 client certificates to RACF userids, by specifying the
* clientauth=esmuserid property in the SSL or HTTPS protocol handler
* configuration.</p>
*/
public class SSLServerCompression implements JSSEServerSecurity {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2000, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /**
   * Method : receiveHandshake
   * Do nothing
   */
   public byte[] receiveHandshake(byte[] clientHandshake,
                                  InetAddress ipClient) throws IOException
   {
      return null;
   }

   /**
   * Method : encodeReply
   *
   * This method is called to encode a reply to the
   * client. The method takes a bytearray
   * containing the server-reply which it compresses
   * using the java.util.zip.DeflaterOutputStream
   * class. A byte-array representing this Deflater
   * stream is returned from the method.
   *
   * @param unencoded unencoded server-reply
   * data.
   * @return byteArray containing compressed server
   * reply datastream.
   */
   public byte[] encodeReply(byte[] unencodedReply, GatewayRequest gr) throws IOException
   {
      ByteArrayInputStream inStream = new ByteArrayInputStream(unencodedReply);
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      DeflaterOutputStream deflateStream = new DeflaterOutputStream(outStream);
      int inputByte;

      try {

         // loop until the last byte of the stream is reached
         while ((inputByte = inStream.read()) != -1) {

            // write the current byte to the compress stream
            deflateStream.write(inputByte);
         }

         // close the streams
         deflateStream.close();
         inStream.close();
         outStream.close();
      }
      catch (Exception e) {
         IOException compressException = new IOException(e.getMessage());
         throw compressException;
      }

      // return the compressed stream as a byteArray
      return(outStream.toByteArray());
   }

   /**
   *  Method : decodeRequest
   *
   * This method is called to decode a request sent
   * by the client. The method takes a compressed
   * request, feeds it through an InflaterInputStream,
   * and reconstructs the original client request.
   *
   * @param encodedRequest encoded client
   * requestflow.
   * @return byteArray containing decompressed client
   * request.
   */
   public byte[] decodeRequest(byte[] encodedRequest) throws IOException
   {

      // set up the streams
      ByteArrayInputStream inStream = new ByteArrayInputStream(encodedRequest);
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      InflaterInputStream inflateStream = new InflaterInputStream(inStream);
      int inputByte;

      try {

         // loop until the end of the stream is reached
         while ((inputByte = inflateStream.read()) != -1) {

            // inflate each byte read from the inStream
            outStream.write(inputByte);
         }

         // close the streams
         inflateStream.close();
         inStream.close();
         outStream.close();
      }
      catch (Exception e) {

         // generate a new IOException
         IOException decompressException = new IOException(e.getMessage());
         throw decompressException;
      }

      // return the decompressed stream as a byteArray
      return(outStream.toByteArray());
   }

   // For this sample we use the overridden afterDecode() method below
   public void afterDecode(GatewayRequest gatewayRequest) {
      return;
   }

   /**
   * Method : afterDecode
   *
   * This method is called after an in-bound request
   * has been decoded. It is passed the request
   * that was just receive and can alter its contents
   * if required. If Client Authentication is supported
   * on the connection, the Client Certificate chain
   * will also be exposed in this exit.
   *
   * For example, userid and password information
   * could be added to a request at this point,
   * based off information exchanged in the
   * handshake.
   *
   * @param gatewayRequest - the request
   * GatewayRequest object that was just decoded
   * @param clientCertificate - client certificate chain
   * passed on the initial SSL handshake
   * @exception IOException *can* be thrown if the
   * Client Certificate is invalid or unacceptable
   */
   public void afterDecode (GatewayRequest gatewayRequest,
                            javax.security.cert.X509Certificate[] clientCert) throws IOException
   {

      /*
      * if no certificate chain was provided then client authentication
      * probably has not been enabled
      */
      if (clientCert == null) {
         System.out.println("Null certificate; is Client Authentication enabled ?");
         return;
      }

      // output the complete certificate information
      System.out.println("\nPeer Certificate:");
      System.out.println(clientCert[0].toString());


      // get the certificate distinguished names
      Principal issuer = clientCert[0].getIssuerDN();
      Principal subject = clientCert[0].getSubjectDN();
      if (issuer != null) {
         System.out.println("issuer distinguished name: " + issuer.getName());
      }
      if (subject != null) {
         System.out.println("subject distinguished name: " + subject.getName());
      }

      // get the date range of the certificate
      java.util.Date from = clientCert[0].getNotBefore();
      java.util.Date to = clientCert[0].getNotAfter();
      if (from != null) {
         System.out.println("Valid From: " + from);
      }
      if (to != null) {
         System.out.println("        To: " + to);
      }

      // make a decision based on the peer certificate
      if (issuer.getName().indexOf("O=ACME CO") > 0)  {
         System.out.println("Refusing connection!");
         IOException myException = new IOException("!Suspect signer Exception!");
         throw myException;
      }

      /*
      * If the Gateway is running on OS/390, we can
      * attempt to map the client certificate to a
      * registered RACF userid.
      */
      String osname = System.getProperty("os.name");
      if (osname.equalsIgnoreCase("z/OS") ||
          osname.equalsIgnoreCase("OS/390"))
      {
         String racfUserid = null;
         try {
            RACFUserid racf = new RACFUserid(clientCert[0].getEncoded());
            racfUserid = racf.getRACFUserid();
            System.out.println("The USERID for this certificate is = " +
                               racfUserid);
         }
         catch (IOException eIO) {
            System.out.println(eIO);
            throw eIO;
         }
         catch (CertificateEncodingException cee) {
            System.out.println(cee);
            throw new IOException(cee.getMessage());
         }
         if (racfUserid.equals("BLOGGSF")) {
            System.out.println("Refusing connection!");
            IOException myException = new IOException("!UNAUTHORISED USER Exception!");
            throw myException;
         }
      }
   }


}
