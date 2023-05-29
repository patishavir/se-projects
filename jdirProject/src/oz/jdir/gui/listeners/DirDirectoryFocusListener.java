package oz.jdir.gui.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Logger;

import javax.swing.JTextField;

import oz.jdir.JdirInfo;

public class DirDirectoryFocusListener implements FocusListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private JdirInfo jdirInfo;

	public DirDirectoryFocusListener(final JdirInfo jdirInfoP) {
		this.jdirInfo = jdirInfoP;
	}

	public final void focusGained(final FocusEvent e) {
	}

	public final void focusLost(final FocusEvent e) {
		assert (e.getSource() instanceof JTextField);
		jdirInfo.setDirName(((JTextField) e.getSource()).getText());
		logger.finest(jdirInfo.getDirName());
	}
}