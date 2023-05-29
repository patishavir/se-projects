package oz.was.perfservlet;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class PerfServletMain {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		PerfServletParameters.processParameters(args[0]);
		String xmlString = PerfServletUtils.getXmlString(PerfServletParameters.getXmlFilePath());
		PerfServletUtils.buildXslsArray();
		XslTransformation.doXslTransformation(xmlString);
	}
}
