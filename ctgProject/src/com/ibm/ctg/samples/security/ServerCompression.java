/*
*    File Name      : ServerCompression.java
*
*    Product        : CICS Transaction Gateway
*
*    Description    : This is a general sample to demonstrate the basic
*                     functionality of the java gateway security exits.
*                     This sample demonstrates the compression of the
*                     client/server dataflows using the java.util.zip
*                     package.
*
*    Pre-requisites : Use a version of the JDK that the CICS Transaction
*                     Gateway supports if you recompile this sample. See the
*                     product documentation for supported Java levels.
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
*    The following code is sample code created by IBM Corporation.  This
*    sample code is not part of any standard IBM product and is provided
*    to you solely for the purpose of assisting you in the development of
*    your applications.  The code is provided 'AS IS', without warranty
*    or condition of any kind.  IBM shall not be liable for any damages
*    arising out of your use of the sample code, even if IBM has been
*    advised of the possibility of such damages.
*/

package com.ibm.ctg.samples.security;

import com.ibm.ctg.client.*;
import com.ibm.ctg.security.*;

import java.io.*;
import java.util.zip.*;
import java.net.InetAddress;

/**
 * Class : ServerCompression
 *
 * This class gives sample implementations of the methods defined in the
 * com.ibm.ctg.security.ServerSecurity interface.
 *
 * Users wishing to implement compression of the dataflows between the
 * client and the server should study the code below.
 *
 * All compression functionality has been based upon the java.util.zip
 * package, the InflaterInputStream and DeflaterOutputStream classes in
 * particular. Although this class implements the ServerSecurity interface,
 * it only defines the encodeReply() and the decodeRequest() methods - the
 * handshake methods are not used.
 *
 * This sample will write any exception messages to stderr, using the
 * printStackTrace method.
 */
public class ServerCompression implements ServerSecurity {

static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2000, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 

   /**
   * Largest value we need the buffer to be.
   * A smaller value could be used to save memory, but it must be large
   * enough to take the largest commarea in use (plus headers etc).
   * 32k Commarea + 178 + few bytes spare
   */
   private static final int BUFFER_SIZE = 33000;

   /** Buffer, one per thread, which will be reused. */
   private byte buffer[] = new byte[BUFFER_SIZE];


   /**
    * Method : receiveHandshake
    */
   public byte[] receiveHandshake(byte[] clientHandshake,
                                  InetAddress ipClient) throws IOException {
      return null;
   }


   /**
    * Method : encodeReply
    *
    * This method is called to encode a reply to the client.
    * The method takes a byte array containing the reply data which it
    * compresses using the java.util.zip.DeflaterOutputStream class.
    * A byte array representing this Deflater stream is returned from
    * the method.
    *
    * @param replyFlow non-encoded server reply data.
    * @param gatewayRequest GatewayRequest object that the reply data represents.
    * @return byte array containing encoded server reply data-flow.
    */
   public byte[] encodeReply(byte[] replyFlow, GatewayRequest gatewayRequest) throws IOException {

      ByteArrayOutputStream outStream = null;
      DeflaterOutputStream deflateStream = null;

      // Don't compress zero length flows.
      if (replyFlow.length == 0)
         return replyFlow;

      try {

         /*
         * Deflater has several strategies to choose from,
         * you may wish to change this depending on your requirements.
         */
         Deflater deflater = new Deflater();
         deflater.setStrategy(Deflater.BEST_SPEED);

         // Set up the streams.
         outStream = new ByteArrayOutputStream(replyFlow.length);
         deflateStream = new DeflaterOutputStream(outStream, deflater, replyFlow.length);

         // Compress the data.
         deflateStream.write(replyFlow, 0, replyFlow.length);

      } catch (Exception e) {
         e.printStackTrace();
         IOException compressException = new IOException(e.getMessage());
         throw compressException;
      } finally {
         // Close the streams.
         try {if (deflateStream != null) deflateStream.close();}
          catch (Exception e) {e.printStackTrace();};
         try {if (outStream != null) outStream.close();}
          catch (Exception e) {e.printStackTrace();};
      }

      // Return the compressed data as a byte array.
      return(outStream.toByteArray());
   }


   /**
    * Method : decodeRequest
    *
    * This method is called to decode a client request received by the server. The
    * method takes a compressed request, feeds it through an InflaterInputStream,
    * and reconstructs the original server reply.  A buffer is reused between
    * successive requests to improve performance.
    *
    * @param encryptedRequestFlow encoded client request data.
    * @return byte array containing decoded client request.
    */
   public byte[] decodeRequest(byte[] encryptedRequestFlow) throws IOException {

      int numread = 0;

      Inflater inflater = null;
      ByteArrayInputStream inStream = null;
      InflaterInputStream inflateStream = null;

      // Don't compress zero length flows.
      if (encryptedRequestFlow.length == 0)
         return encryptedRequestFlow;

      try {

         // Set up the streams.
         inflater = new Inflater();
         inStream = new ByteArrayInputStream(encryptedRequestFlow);
         inflateStream = new InflaterInputStream(inStream, inflater, encryptedRequestFlow.length);

         // Read uncompressed data into the byte array.
         numread = inflateStream.read(buffer, 0, BUFFER_SIZE);

      } catch (Exception e) {
         e.printStackTrace();
         IOException decompressException = new IOException(e.getMessage());
         throw decompressException;
      } finally {
         // Close the streams.
         try {if (inflateStream != null) inflateStream.close();}
          catch (Exception e) {e.printStackTrace();};
         try {if (inStream != null) inStream.close();}
          catch (Exception e) {e.printStackTrace();};
      }

      // Return the decompressed data as a byte array.
      byte tempbuf[] = new byte[numread];
      System.arraycopy(buffer, 0, tempbuf, 0, numread);
      return tempbuf;
   }


   /**
    * Method : afterDecode
    */
   public void afterDecode(GatewayRequest gatewayRequest) {
   }

}
