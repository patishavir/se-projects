package oz.was.perfservlet;

import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.infra.xml.xslt.XsltUtils;
import oz.stats.entry.StatsEntry;

public class XslTransformation {

	private static StatsEntry previosSstatsEntry = null;

	private static final Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void doXslTransformation(final String xmlString) {
		// logger.info(PerformXslTransformation.class.getPackage().toString());
		ArrayList<String> xslsList = PerfServletParameters.getXslsList();
		for (String xslString : xslsList) {
			doTransformation(xslString, xmlString);
		}
		logger.info("All done !");
	}

	private static void doTransformation(final String xslString, final String xmlString) {
		String statsLines = XsltUtils.transform(xmlString, xslString);
		System.out.print(SystemUtils.LINE_SEPARATOR + statsLines);
		String[] statsLinesArray = statsLines.split(OzConstants.CARRIAGE_RETURN_LINE_FEED);
		for (String statsLine1 : statsLinesArray) {
			StatsEntry statsEntry = new StatsEntry(statsLine1);
			logger.info(statsEntry.getAsString());
			if (previosSstatsEntry == null) {
				previosSstatsEntry = statsEntry;
			} else {
				logger.info("bla bla");
			}
		}
		logger.finest("****** xsl transformation using\n" + xslString + "\nis done *******");
	}
}