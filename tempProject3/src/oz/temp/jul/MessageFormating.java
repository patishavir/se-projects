package oz.temp.jul;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class MessageFormating {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		logger.log(Level.INFO, " ma {0} koerh {1} {2}", new Object[] { "mah", "gever ", "gever" });

	}

}
