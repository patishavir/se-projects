package oz.jdir.gui.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import oz.jdir.DirMatchTable;

class DirFrameKeyListener implements KeyListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	/** Handle the key typed event from the text field. */
	public final void keyTyped(final KeyEvent e) {
		return;
	}

	/** Handle the key pressed event from the text field. */
	public final void keyPressed(final KeyEvent e) {
		logger.fine("KeyPressed");
		if ("F5".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
			DirMatchTable.getDirMatchTable().buidMatchTable(true);
			logger.info("F5 keyPressed");
			return;
		}
	}

	/** Handle the key released event from the text field. */
	public final void keyReleased(final KeyEvent e) {
		return;
	}
}