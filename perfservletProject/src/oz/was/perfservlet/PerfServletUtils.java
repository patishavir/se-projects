package oz.was.perfservlet;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.win.HttpWinClientUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class PerfServletUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static String getXmlString(final String xmlFilePath) {
		String xmlString = null;
		if (xmlFilePath.toLowerCase().startsWith(OzConstants.HTTP.toLowerCase())) {
			xmlString = HttpWinClientUtils.getProtectedPageContents(xmlFilePath);
			FileUtils.writeFile(PerfServletParameters.getXmlOutPutFilePath(), xmlString);
		} else {
			xmlString = FileUtils.readTextFile(xmlFilePath);
		}
		xmlString = StringUtils.removeLinesContainingSubString(xmlString, ".dtd");
		logger.finest("xmlString:\n" + xmlString);
		logger.info(xmlFilePath.concat(" has been successfully retreived"));
		return xmlString;
	}

	public static ArrayList<String> buildXslsArray() {
		// logger.info(PerformXslTransformation.class.getPackage().toString());
		ArrayList<String> xslsList = new ArrayList<String>();
		String[] xslFilesArray = PerfServletParameters.getXslFilesArray();
		for (int i = 0; i < xslFilesArray.length; i++) {
			String xslFilePath = xslFilesArray[i];
			logger.info(" Processing " + xslFilePath);
			File xslFile = new File(xslFilePath);
			if (xslFile.isFile()) {
				String xslString = FileUtils.readTextFile(xslFile);
				xslsList.add(xslString);
			} else if (xslFile.isDirectory()) {
				File[] xslFiles = FolderUtils.getFilesInFolder(xslFilePath);
				for (File xslFile1 : xslFiles) {
					String xslString = FileUtils.readTextFile(xslFile1);
					xslsList.add(xslString);
				}
			}
		}
		PerfServletParameters.setXslsList(xslsList);
		return xslsList;
	}
}