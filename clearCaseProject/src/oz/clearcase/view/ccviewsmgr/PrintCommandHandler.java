/*
 * Created on 13/01/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package oz.clearcase.view.ccviewsmgr;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public class PrintCommandHandler {
	private static Logger logger = JulUtils.getLogger();

	public static final void printCommand(final String[] clearToolparams) {
		StringBuffer stringBuffer = new StringBuffer("cleartool.exe ");
		for (int i = 1; i < clearToolparams.length; i++) {
			stringBuffer.append(" ");
			stringBuffer.append(clearToolparams[i]);
		}
		logger.info(stringBuffer.toString());
	}
}
