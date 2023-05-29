package oz.clearcase.view.ccviewsmgr.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import oz.clearcase.view.ccviewsmgr.CCViewsInfo;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJTableJPanel;
import oz.infra.logging.jul.JulUtils;

public class CCViewsMenuBarKeyListener implements KeyListener {
	private static final Logger logger = JulUtils.getLogger();

	public final void keyPressed(final KeyEvent e) {
		logger.info("Start processing keyPressed");
		if ((e.getKeyCode() == KeyEvent.VK_F5)) {
			CCViewsInfo.setRefresh(true);
			CCViewsJTableJPanel.updateCCViewJTable();
		}
		return;
	}

	public final void keyReleased(final KeyEvent ex) {
		return;
	}

	public final void keyTyped(final KeyEvent ex) {
		return;
	}
}