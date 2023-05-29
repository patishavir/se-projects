package oz.infra.spnego.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.spnego.SpnegoUtils;
import oz.infra.validation.string.StringValidationCriteria;
import oz.infra.validation.string.StringValidationUtils;

public class SpnegoTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String pageContents = SpnegoUtils.getProtectedPageContents(args[0], args[1]);
		if (pageContents != null) {
			logger.info(pageContents);
			logger.info(String.valueOf(StringValidationUtils.validateString(pageContents, "Negotiate YII",
					StringValidationCriteria.STRING_CONTAINS)));
		} else {
			logger.warning(" page contents is null !");
		}
	}

}
