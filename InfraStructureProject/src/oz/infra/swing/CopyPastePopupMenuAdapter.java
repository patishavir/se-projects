package oz.infra.swing;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class CopyPastePopupMenuAdapter implements MouseListener {
	public static final int COPYONLY = 1;
	public static final int BOTH = 2;
	public static final int PASTEONLY = 3;
	private JPopupMenu popup;

	public CopyPastePopupMenuAdapter(JComponent myJComponent, ActionListener myActionListener,
			int whichMenu) {
		// System.out.println("CopyPastePopupMenuAdapter contructor");
		String copyString = "Copy";
		String pasteString = "Paste";
		popup = new JPopupMenu();
		if (whichMenu == COPYONLY || whichMenu == BOTH) {
			JMenuItem jmiCopy = new JMenuItem(copyString);
			jmiCopy.setActionCommand(copyString);
			jmiCopy.addActionListener(myActionListener);
			popup.add(jmiCopy);
		}
		if (whichMenu == PASTEONLY || whichMenu == BOTH) {
			JMenuItem jmiPaste = new JMenuItem(pasteString);
			jmiPaste.setActionCommand(pasteString);
			jmiPaste.addActionListener(myActionListener);
			popup.add(jmiPaste);
		}
		myJComponent.addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) {
		// System.out.println("CopyPastePopupMenuAdapter mouseclicked");
		int m = e.getModifiers();
		if ((m & InputEvent.BUTTON3_MASK) != 0) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public JPopupMenu getPopup() {
		return popup;
	}
}