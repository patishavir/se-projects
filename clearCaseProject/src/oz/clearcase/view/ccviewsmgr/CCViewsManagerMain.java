package oz.clearcase.view.ccviewsmgr;

import oz.clearcase.view.ccviewsmgr.gui.CCViewsJFrame;
import oz.infra.logging.jul.JulUtils;

public final class CCViewsManagerMain {
	public static void main(final String[] args) {
		JulUtils.addFileHandler(args[0]);
		CCViewsParameters.setUserName(System.getenv("USERNAME"));
		new CCViewsJFrame();
	}

	private CCViewsManagerMain() {
	}
}
