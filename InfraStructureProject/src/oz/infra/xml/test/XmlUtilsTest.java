package oz.infra.xml.test;

import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.xml.XMLUtils;

public class XmlUtilsTest {
	public static String xmlString1 = "<?xml version='1.0' encoding='UTF-8'?><T05C0GAK><HEADERK><USERIDK></USERIDK><TACHANAK>11</TACHANAK><BANKPAKIDK>31</BANKPAKIDK><SNIFPAKIDK>001</SNIFPAKIDK><BANKK>031</BANKK><SNIFK>001</SNIFK><MCHK>110086</MCHK><SCHK>105</SCHK><MATBEAK>001</MATBEAK><GILAYONK>10</GILAYONK><MFTDATAK>01</MFTDATAK></HEADERK><T05C0RSH><RSHAGRRK>2</RSHAGRRK><RSHBANKK>031</RSHBANKK><RSHSNIFK>001</RSHSNIFK><RSHMCHK>110086</RSHMCHK></T05C0RSH><T05C0QOT><QOTMATBEAK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>001</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>020</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK></QOTMATBEAK><QOTDATESK><QOTWORKDAYK>20160511</QOTWORKDAYK><QOTRIVONENDK>20160511</QOTRIVONENDK><QOTYEARENDK>20160511</QOTYEARENDK></QOTDATESK><QOTKFK></QOTKFK></T05C0QOT><T05C0CIF><CIFBANKK>031</CIFBANKK><CIFSNIFK>001</CIFSNIFK><CIFMCHK>110086</CIFMCHK></T05C0CIF><T05C0OBL><OBLAGRRK>2</OBLAGRRK><OBLBANKK>031</OBLBANKK><OBLSNIFK>001</OBLSNIFK><OBLMCHK>110086</OBLMCHK></T05C0OBL></T05C0GAK>";
	public static String xmlString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><HEADER appId=\"/MatafServer_Q_Z9\" date=\"18/10/2017-20:06:07\" serverName=\"S603FC00.fibi.corp\"><TEST_TYPE id=\"2\" name=\"CicsSm\"><RET_CODE>success</RET_CODE><DESC> "
			+ "Cics=A01CICSQ-SNALU0. Status=success. Desc=4. Long desc=. "
			+ "Cics=A01CICSR-SNALU0. Status=success. Desc=4. Long desc=. "
			+ "Cics=CICSQ-BR3270. Status=success. Desc=64. Long desc=. "
			+ "Cics=CICSR-BR3270. Status=success. Desc=65. Long desc=.</DESC><LONG_DESC/></TEST_TYPE></HEADER> ";
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testFolrmatXml(xmlString1);
		testFolrmatXml(xmlString2);
		testGetElements(xmlString1);
		testGetElements(xmlString2);
		testGetElements(xmlString2, "RET_CODE");
		testGetElementValue(xmlString2, "RET_CODE");
		testFormatXmlString(xmlString2);
		testWriteXMLDocument(xmlString2, "c:/temp/xmlUtils.xml");
	}

	private static void testFolrmatXml(final String xmlString) {
		logger.info(xmlString);
		logger.info(XMLUtils.formatXmlString(xmlString));
	}

	private static void testGetElements(final String xmlString, final String... elementNameFilers) {
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_128));
		XMLUtils.getElements(xmlString, elementNameFilers);
	}

	private static void testGetElementValue(final String xmlString, final String... elementNameFilers) {
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_128));
		List<Element> elements = XMLUtils.getElements(xmlString, elementNameFilers);
		String value = XMLUtils.getElementValue(elements.get(0));
		logger.info(value);
	}

	private static void testFormatXmlString(final String xmlString) {
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_128));
		logger.info(xmlString);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_128));
		String formattedXmlString = XMLUtils.formatXmlString(xmlString);
		logger.info(formattedXmlString);
	}

	private static void testWriteXMLDocument(final String xmlString, final String xmlFilePath) {
		Document document = XMLUtils.getDocument(xmlString);
		XMLUtils.writeXMLDocument(document, xmlFilePath);
	}
}