package oz.bundle;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class TestResouceBundle {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testResouceBundle();
	}

	private static void testResouceBundle() {
		Locale locale = new Locale("iw_IL");
		Locale.setDefault(locale);
		logger.info(Locale.getDefault().toString());
		ResourceBundle bundle = ResourceBundle.getBundle("messages");
		logger.info(bundle.toString());
		logger.info(bundle.getString("m1"));
		logger.info(bundle.getString("m2"));
		//
		bundle = ResourceBundle.getBundle("smtp_fibi");
		logger.info(bundle.getString("startSubject"));

	}
}
