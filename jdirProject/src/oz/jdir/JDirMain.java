// Oded team is responsible to this code
package oz.jdir;

import oz.infra.logging.jul.handlers.JFRameLoggingHandler;
import oz.jdir.gui.DirFrame;

public class JDirMain {
	public static void main(final String[] args) {
		JdirParameters.processParameters(args);
		if (JdirParameters.isGui()) {
			new DirFrame();
			if (JdirParameters.isEnableJFRameLoggingHandler()) {
				new JFRameLoggingHandler();
			}
		} else {
			new JdirBatch().process();
		}
	}
}