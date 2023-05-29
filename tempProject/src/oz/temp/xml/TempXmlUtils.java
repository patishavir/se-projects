package oz.temp.xml;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oz.infra.logging.jul.JulUtils;

public class TempXmlUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		xxx();
	}

	private static void xxx() {
		String widgetXMLFilePath = "./args/xml/piechart.xml";
		File widgetXMLFile = new File(widgetXMLFilePath);
		if (widgetXMLFile.exists()) {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			try {
				DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(widgetXMLFile);
				NodeList nodelist = doc.getChildNodes();
				Node node = nodelist.item(0);
				logger.info(nodelist.toString());
				printNodeInfo(node);
				NodeList nodeList1 = node.getChildNodes();
				Node node1 = nodeList1.item(4);
				printNodeInfo(node1);
				// Node n = doc.getElementById("/chart");
				//
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			logger.warning(widgetXMLFilePath + " does not exists !");
		}
	}

	private static void printNodeInfo(final Node node) {
		logger.info("node name: " + node.getNodeName());
		logger.info("node value: " + node.getNodeValue());
	}
}
