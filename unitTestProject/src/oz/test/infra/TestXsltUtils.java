package oz.test.infra;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.http.HTTPUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.xml.xslt.XsltUtils;

public class TestXsltUtils {
	/**
	 * @param args
	 */
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// File xmlFile = new File(args[0]);
		// String xmlString = FileUtils.readTextFile(xmlFile);
		File xslFile = new File(args[1]);
		String xmlString = HTTPUtils.getPageContents("http://snif-http-tyv", "/wasPerfTool/servlet/perfservlet",
				"s177571", "j7j7k8k8");
		System.out.println("**********************************************\n" + xmlString);
		String xslString = FileUtils.readTextFile(xslFile);
		logger.info("xmlString:\n" + xmlString);
		logger.info("xslString:\n" + xslString);
		logger.info(XsltUtils.transform(xmlString, xslString));
		logger.info("All done");
	}
}
