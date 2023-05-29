/*
*         File Name     : Ctgstat2.java
*
*         Product       : CICS Transaction Gateway
*
*         Description   : This sample shows basic processing of an XML
*                         statistics log file. It takes in the location of 
*                         the file, parses and validates it against the XML 
*                         schema file, and outputs this information in a 
*                         simple text format.
*                         
*                         Note that the schema file must be located in the
*                         same directory as the statistics log file.
*
*         Pre-Requisites: Use a version of the JDK that the CICS Transaction
*                         Gateway supports if you recompile this sample. See
*                         the product documentation for supported Java levels.
*
* Licensed Materials - Property of IBM  
*  
* 5724-I81,5725-B65,5655-Y20 
*  
* (C) Copyright IBM Corp. 2012, 2014 All Rights Reserved.  
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

package com.ibm.ctg.samples.stats;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Sample program to demonstrate using Java to read an XML 
 * statistics log file.
 */
public class Ctgstat2 {
static final String copyright_notice ="Licensed Materials - Property of IBM 5724-I81,5725-B65,5655-Y20 (c) Copyright IBM Corp. 2012, 2014 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp."; 
   
   private static final String STATRECORD_TAG_NAME = "statRecord";
   private static final String RESOURCEGROUP_TAG_NAME = "resourceGroup";
   private static final String STATISTIC_TAG_NAME = "statistic";
   private static final String NAME_TAG_NAME = "name";
   private static final String VALUE_TAG_NAME = "value";
   private static final String TIME_ATTRIBUTE_NAME = "time";
   private static final String TYPE_ATTRIBUTE_NAME = "type";
   private static final String LENGTH_ATTRIBUTE_NAME = "length";
   private static final String NAME_ATTRIBUTE_NAME = "name";
   private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
   private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
   /**
     * Program entry point.
     * 
     * @param args  command line arguments passed to the program
     */
   public static void main(String[] args) {
      // Display banner
      System.out.println("CICS Transaction Gateway Statistics Sample 2");
      System.out.println();
      System.out.println("Usage: java com.ibm.ctg.samples.stats.Ctgstat2 <filename>");
      
      // Check if no parameter has been supplied
      if (args.length != 1) {
         System.out.println();
         if (args.length > 1) {
            System.out.println("ERROR - Too many arguments.");
         }
         System.out.println("Example: java com.ibm.ctg.samples.stats.Ctgstat2 "
                            + "CTGStatsLog_20121016-100558.xml");
         return;
      }
      String filename = args[0];
      
      try {
         // Begin parsing the file
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         
         // Set up the DocumentBuilder so that it will look for the XML 
         // schema when parsing the file
         Schema schema = schemaFactory.newSchema();
         factory.setNamespaceAware(true);
         factory.setSchema(schema);
         DocumentBuilder builder = factory.newDocumentBuilder();
         
         // Set an error handler to throw exceptions so that they are 
         // handled by the surrounding try/catch
         builder.setErrorHandler(new ErrorHandler() {
            public void error(SAXParseException e) throws SAXException {
               throw e;
            }
            public void fatalError(SAXParseException e) throws SAXException {
               throw e;
            }
            public void warning(SAXParseException e) throws SAXException {
               throw e;
            }
         });
         
         System.out.println();
         System.out.println("Processing statistics log file " + filename + ".");
         
         // Parse exceptions will be thrown by this statement
         Document document = builder.parse(filename);
         
         System.out.println("Validated against schema file.");
         
         // Get the statRecord tag Elements from the document
         Element documentElement = document.getDocumentElement();
         NodeList statRecords = documentElement.getElementsByTagName(STATRECORD_TAG_NAME);
         for (int i1 = 0; i1 < statRecords.getLength(); i1++) {
            Element statRecord = (Element) statRecords.item(i1);
            // Print the details of the statistic record, formatting as needed
            Date recordTime = INPUT_DATE_FORMAT.parse(statRecord.getAttribute(TIME_ATTRIBUTE_NAME));
            System.out.println();
            System.out.println("Statistic record at " + OUTPUT_DATE_FORMAT.format(recordTime));
            System.out.println("Record type = " + statRecord.getAttribute(TYPE_ATTRIBUTE_NAME));
            System.out.println("Interval length = " + statRecord.getAttribute(LENGTH_ATTRIBUTE_NAME));
            
            // Get the resourceGroup tag Elements for this statRecord tag
            NodeList resourceGroups = statRecord.getElementsByTagName(RESOURCEGROUP_TAG_NAME);
            for (int i2 = 0; i2 < resourceGroups.getLength(); i2++) {
               Element resourceGroup = (Element) resourceGroups.item(i2);
               System.out.println("Resource group: " + resourceGroup.getAttribute(NAME_ATTRIBUTE_NAME));
               
               // Get the statistic tag Elements for this resourceGroup tag
               NodeList statistics = resourceGroup.getElementsByTagName(STATISTIC_TAG_NAME);
               for (int i3 = 0; i3 < statistics.getLength(); i3++) {
                  // For each statistic, the data in the name and value tags is
                  // in the first child Node
                  Element statistic = (Element) statistics.item(i3);
                  Node statisticName = statistic.getElementsByTagName(NAME_TAG_NAME).item(0).getFirstChild();
                  Node statisticValue = statistic.getElementsByTagName(VALUE_TAG_NAME).item(0).getFirstChild();
                  
                  // If a tag is empty, the Node will be null, so this is 
                  // checked before using getNodeValue()
                  String name;
                  String value;
                  if (statisticName == null) {
                     name = "";
                  } else {
                     name = statisticName.getNodeValue();
                  }
                  if (statisticValue == null) {
                     value = "";
                  } else {
                     value = statisticValue.getNodeValue();
                  }
                  System.out.println(name + " = " + value);
               }
            }
         }
      } catch (IOException e) {
         // If there is any issue reading the file
         System.out.println("Statistics log file " + filename + " could not be read.");
      } catch (SAXException e) {
         // SAXExceptions are thrown for several reasons when parsing
         System.out.println("Failed to validate the statistics log file against the schema:");
         System.out.println(e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
