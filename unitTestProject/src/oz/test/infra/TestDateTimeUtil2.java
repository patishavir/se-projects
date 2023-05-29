package oz.test.infra;

import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;

public class TestDateTimeUtil2 {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		logger.info(DateTimeUtils.formatDateTime());
		logger.info(DateTimeUtils.formatCurrentTime());
		logger.info(DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss));
	}
}
