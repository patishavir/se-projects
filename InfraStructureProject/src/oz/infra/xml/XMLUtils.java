package oz.infra.xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public final class XMLUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String formatXmlString(final String xmlString) {
		String outputString = null;
		if (xmlString != null && xmlString.length() > 0) {
			Document document = getDocument(xmlString);
			if (document != null) {
				XMLOutputter xmlOutputter = new XMLOutputter();
				xmlOutputter.setFormat(Format.getPrettyFormat());
				outputString = xmlOutputter.outputString(document);
			}
		}
		return outputString;
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
			logger.finest(currentName);
		}
		return elementList;
	}

	public static List<Element> getElements(final String xmlString, final String... elementNameFilers) {
		Document document = getDocument(xmlString);
		return getElements(document, elementNameFilers);
	}

	public static String getElementValue(final Element element) {
		return element.getValue();
	}

	public static String getElementValue(final String xmlString, final String elementName) {
		String value = null;
		List<Element> elements = getElements(xmlString, elementName);
		if (elements.size() > 0) {
			value = elements.get(0).getValue();
		}
		return value;
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

	public static boolean writeXMLDocument(final Document doc, final String outXMLFilePath) {
		XMLOutputter xMLOutputter = new XMLOutputter();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outXMLFilePath);
			xMLOutputter.output(doc, fileOutputStream);
			logger.info(outXMLFilePath + " has been successfully written");
			return true;
		} catch (Exception ex) {
			logger.severe(ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}
}