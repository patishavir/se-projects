package oz.infra.xml.keep;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public final class XMLUtils {
	private static Logger logger = JulUtils.getLogger();

	// /*
	// * setXMLElementValue
	// */
	// private final boolean setXMLElementValue(final String inXMLFilePath,
	// final String elementPath, final String myElementDelimiter,
	// final String newElementText, final String outXMLFilePath) {
	// Document myDoc = getXMLdocument(inXMLFilePath);
	// Element myElement = getXMLElement(myDoc, elementPath,
	// myElementDelimiter);
	// myElement.setText(newElementText);
	// boolean returnCode = writeXMLDocument(myDoc, outXMLFilePath);
	// if (returnCode) {
	// logger.info("element " + elementPath
	// + " text has been successfully set to " + newElementText);
	// } else {
	// logger.warning("Failed to set attribute " + elementPath
	// + " text to " + newElementText);
	// }
	// return returnCode;
	// }
	// /*
	// * setXMLAttributeValue
	// */
	// private final boolean setXMLAttributeValue(final String inXMLFilePath,
	// final String elementPath, final String myElementDelimiter,
	// final String attributeName, final String newAttriuteText,
	// final String outXMLFilePath) {
	// Document myDoc = getXMLdocument(inXMLFilePath);
	// Element myElement = getXMLElement(myDoc, elementPath,
	// myElementDelimiter);
	// myElement.setAttribute(attributeName, newAttriuteText);
	// boolean returnCode = writeXMLDocument(myDoc, outXMLFilePath);
	// if (returnCode) {
	// logger.info("attribute " + attributeName
	// + " value has been successfully set to " + newAttriuteText);
	// } else {
	// logger.warning("Failed to set attribute " + attributeName
	// + " value to " + newAttriuteText);
	// }
	// return returnCode;
	// }
	/*
	 * setXMLAttributeValueByElementandAttribName
	 */
	public static final boolean setXMLAttribValueByElementandAttribValue(final String inXMLFilePath,
			final String elementPath, final String myElementDelimiter, final String attributeName,
			final String newAttributeValue, final String outXMLFilePath) {
		logger.finest("newAttributeValue on entry: \"" + newAttributeValue + "\"");
		int elementsFound = 0;
		Document myDoc = getXMLdocument(inXMLFilePath);
		Iterator docIterator = myDoc.getRootElement().getDescendants();
		String[] elementPathArray = elementPath.split(myElementDelimiter);
		Element targetElement = null;
		logger.finest("Looking for element: " + elementPathArray[0]);
		logger.finest("Looking for attribute: " + elementPathArray[1] + " with value: " + elementPathArray[2]);
		while (docIterator.hasNext()) {
			Object obj = docIterator.next();
			if (obj instanceof Element) {
				Element myElement = (Element) obj;
				logger.finest("Element name: " + myElement.getName());
				if (!myElement.getName().equalsIgnoreCase(elementPathArray[0])) {
					continue;
				}
				Attribute myAttribute = myElement.getAttribute(elementPathArray[1]);
				if (myAttribute == null) {
					continue;
				}
				if (myAttribute.getValue().equalsIgnoreCase(elementPathArray[2])) {
					targetElement = myElement;
					elementsFound++;
				}
			}
		}
		if (elementsFound > 1) {
			throw new RuntimeException("Multiple elements found for path " + elementPath);
		}
		if (elementsFound < 1) {
			throw new RuntimeException("No elements found for path " + elementPath);
		}
		targetElement.setAttribute(attributeName, newAttributeValue);
		boolean returnCode = writeXMLDocument(myDoc, outXMLFilePath);
		if (returnCode) {
			logger.info(
					"attribute " + attributeName + " value has been successfully set to \"" + newAttributeValue + "\"");
		} else {
			logger.warning("Failed to set attribute " + attributeName + " value to " + newAttributeValue);
		}
		return returnCode;
	}

	public static final String getXMLElementValueFromXmlString(final String xmlString, final String elementPath,
			final String myElementDelimiter) {
		String value = null;
		try {
			Document myDoc = new SAXBuilder().build(new StringReader(xmlString));
			value = getXMLElementValue(myDoc, elementPath, myElementDelimiter);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return value;
	}

	public static final String getXMLElementValue(final Document myDoc, final String elementPath,
			final String myElementDelimiter) {
		Element myElement = getXMLElement(myDoc, elementPath, myElementDelimiter);
		return myElement.getText();
	}

	/*
	 * getRootElementChildren
	 */
	public static final List getRootElementChildren(final String inXMLFilePath, final String myRootElementName) {
		logger.finer("Starting getRootElementChildren for " + inXMLFilePath);
		Document myDoc = getXMLdocument(inXMLFilePath);
		if (myDoc == null) {
			return null;
		}
		Element root = myDoc.getRootElement();
		if (!root.getName().equalsIgnoreCase(myRootElementName)) {
			return null;
		}
		return root.getChildren();
	}

	/*
	 * getXMLdocument
	 */
	public static final Document getXMLdocument(final String inXMLFilePath) {
		SAXBuilder builder = new SAXBuilder();
		try {
			FileInputStream inXMLFileInputStream = new FileInputStream(inXMLFilePath);
			Document doc = builder.build(inXMLFileInputStream);
			return doc;
		} catch (JDOMException e) {
			logger.warning(inXMLFilePath + " is not well-formed.");
			logger.warning(e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
			return null;
		}
	}

	/*
	 * getXMLdocument
	 */
	public static final Document getXMLdocument(final InputStream inXMLInputStream) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(inXMLInputStream);
			return doc;
		} catch (JDOMException e) {
			logger.warning("XML file is not well-formed.");
			logger.warning(e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning(e.getMessage());
			return null;
		}
	}

	/*
	 * getXMLElement
	 */
	private static Element getXMLElement(final Document doc, final String elementPath,
			final String myElementDelimiter) {
		Element myElement = doc.getRootElement();
		String[] elementArray = elementPath.split(myElementDelimiter);
		int initialIndex = 0;
		if (myElement.getName().equalsIgnoreCase(elementArray[initialIndex])) {
			initialIndex++;
			logger.info("Found root element. name:" + myElement.getName() + " value:" + myElement.getValue());
		}
		for (int i = initialIndex; i < elementArray.length; i++) {
			myElement = myElement.getChild(elementArray[i]);
			if (myElement == null) {
				throw new RuntimeException(
						"Element " + elementArray[i] + " not found in document. Processing aborted.");
			}
			logger.info("founnd element. name:" + myElement.getName() + " value:" + myElement.getValue());
		}
		return myElement;
	}

	private static boolean writeXMLDocument(final Document doc, final String outXMLFilePath) {
		XMLOutputter xMLOutputter = new XMLOutputter();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outXMLFilePath);
			xMLOutputter.output(doc, fileOutputStream);
			return true;
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	public static String formatXml(final String xmlString) {
		String xmlString1 = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			org.w3c.dom.Document document = getDomDocument(xmlString);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			xmlString1 = result.getWriter().toString();
			logger.finest(xmlString1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return xmlString1;
	}

	public static org.w3c.dom.Document getDomDocument(final String xml) throws Exception {
		DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
		DocumentBuilder bldr = fctr.newDocumentBuilder();
		InputSource insrc = new InputSource(new StringReader(xml));
		return bldr.parse(insrc);
	}

	public static List<Element> getElements(final String xmlString, final String... elementNameFilers) {
		Document document = getDocument(xmlString);
		return getElements(document, elementNameFilers);
	}

	public static List<Element> getElements(final Document doc, final String... elementNameFilers) {
		ElementFilter elementFilter = new ElementFilter();
		if (elementNameFilers.length == 1) {
			elementFilter = new ElementFilter(elementNameFilers[0]);
		}
		Iterator<Element> processDescendants = doc.getDescendants(elementFilter);
		List<Element> elementList = new ArrayList<Element>();
		while (processDescendants.hasNext()) {
			Element element = processDescendants.next();
			elementList.add(element);
			String currentName = element.getName();
			logger.info(currentName);
		}
		return elementList;
	}

	public static Document getDocument(final String xmlString) {
		Document document = null;
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
			document = saxBuilder.build(new StringReader(xmlString));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return document;
	}

	public static String getElementValue(final Element element) {
		return element.getValue();
	}

	public static String formatXmlusingJdom(final String xmlString) {
		Document document = getDocument(xmlString);
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat(Format.getPrettyFormat());
		String outputString = xmlOutputter.outputString(document);
		return outputString;
	}
}