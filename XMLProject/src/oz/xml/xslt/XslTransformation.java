package oz.xml.xslt;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.HTTPUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.xml.xslt.XsltUtils;
import oz.stats.entry.StatsEntry;

public class XslTransformation {

	/**
	 * @param args
	 */
	private static final Logger logger = JulUtils.getLogger();

	public static void doXslTransformation(final String[] args) {
		// logger.info(PerformXslTransformation.class.getPackage().toString());
		logger.info(SystemUtils.getRunInfo());
		XslTransformationParameters.processParameters(args[0]);
		String xmlString = getXmlString(XslTransformationParameters.getXmlFilePath());
		FileUtils.writeFile(XslTransformationParameters.getXmlOutPutFilePath(), xmlString);
		String[] xslFilesArray = XslTransformationParameters.getXslFilesArray();
		for (int i = 0; i < xslFilesArray.length; i++) {
			String xslFilePath = xslFilesArray[i];
			logger.info(" Processing " + xslFilePath);
			File xslFile = new File(xslFilePath);
			if (xslFile.isFile()) {
				doTransformation(xslFile, xmlString);
			} else if (xslFile.isDirectory()) {
				File[] xslFiles = FolderUtils.getFilesInFolder(xslFilePath);
				for (File xslFile1 : xslFiles) {
					doTransformation(xslFile1, xmlString);
				}
			}
		}
		logger.info("All done !");
	}

	private static String getXmlString(final String xmlFilePath) {
		String xmlString = null;
		if (xmlFilePath.toLowerCase().startsWith(OzConstants.HTTP.toLowerCase())) {
			// xmlString =
			// SpnegoUtils.getProtectedPageContents(propertiesFilePath, url);
			xmlString = HTTPUtils.getPageContents(xmlFilePath, XslTransformationParameters.getUsername(),
					XslTransformationParameters.getPassword());
		} else {
			xmlString = FileUtils.readTextFile(xmlFilePath);
		}
		xmlString = StringUtils.removeLinesContainingSubString(xmlString, ".dtd");
		logger.finest("xmlString:\n" + xmlString);
		logger.info(xmlFilePath.concat(" has been successfully retreived"));
		return xmlString;
	}

	private static void doTransformation(final File xslFile, final String xmlString) {
		if (xslFile.exists()) {
			String xslString = FileUtils.readTextFile(xslFile);
			String outputLine = XsltUtils.transform(xmlString, xslString);
			StatsEntry statsEntry = new StatsEntry(outputLine);
			System.out.print(SystemUtils.LINE_SEPARATOR + outputLine);

			logger.finest("****** transformation using " + xslFile.getAbsolutePath() + " is done *******");
		} else {
			logger.warning(xslFile.getAbsolutePath() + " not found !");
		}
	}

}
