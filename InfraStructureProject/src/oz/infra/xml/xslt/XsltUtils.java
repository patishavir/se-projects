package oz.infra.xml.xslt;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class XsltUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String transform(final String xmlString, final InputStream xslInputStream) {
		Source xsltSource = new StreamSource(xslInputStream);
		return transform(xmlString, xsltSource);
	}

	public static String transform(final String xmlString, final Source xsltSource) {
		logger.finest("xmlString:\n" + xmlString);
		final StringReader xmlReader = new StringReader(xmlString);
		final Source xmlSource = new StreamSource(xmlReader);
		final TransformerFactory transFact = TransformerFactory.newInstance();
		final StringWriter stringWriter = new StringWriter();
		try {
			Transformer trans = transFact.newTransformer(xsltSource);
			trans.transform(xmlSource, new StreamResult(stringWriter));
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			logger.warning("xslt Source:\n" + xsltSource.toString());
		}
		String output = stringWriter.getBuffer().toString();
		logger.finest(SystemUtils.LINE_SEPARATOR + output);
		return output;
	}

	public static String transform(final String xmlString, final String xslString) {
		logger.finest("xslString:\n" + xslString);
		final StringReader xsltReader = new StringReader(xslString);
		final Source xsltSource = new StreamSource(xsltReader);
		return transform(xmlString, xsltSource);
	}
}
