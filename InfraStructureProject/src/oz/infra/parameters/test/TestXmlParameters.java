package oz.infra.parameters.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.XMLFileParameters;

public class TestXmlParameters {
private static Logger logger = JulUtils.getLogger();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testXmlParameters ("src/oz/infra/parameters/test/data/InputParameters.xml");
	}
	private static void testXmlParameters (final String filePath) {
		int numberOfInputParameters = XMLFileParameters.buildInputParameters(filePath);
		logger.info("numberOfInputParameters: " + String.valueOf(numberOfInputParameters));
		logger.info("param11=" + XMLFileParameters.getInputParameter("param11", 0));
		logger.info("param21=" + XMLFileParameters.getInputParameter("param21", 1));
		logger.info("param31=" + XMLFileParameters.getInputParameter("param31", 2));
	}
}
