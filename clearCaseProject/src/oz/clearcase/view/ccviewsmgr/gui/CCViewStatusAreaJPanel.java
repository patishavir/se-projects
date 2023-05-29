package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CCViewStatusAreaJPanel extends JPanel {
	static final long serialVersionUID = 100L;
	private static CCViewStatusAreaJPanel statusAreaJPanel = new CCViewStatusAreaJPanel();
	private JLabel status1 = new JLabel(" ");
	private JLabel status2 = new JLabel(" ");

	CCViewStatusAreaJPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridLayout(1, 2, 0, 10));
		add(status1);
		add(status2);
		status1.setBorder(BorderFactory.createEtchedBorder());
		status2.setBorder(BorderFactory.createEtchedBorder());
		add(status2);
		status1.setHorizontalAlignment(JLabel.LEFT);
		status1.setText("Click column header to sort ");
	}

	public static JPanel getStatusArea() {
		return statusAreaJPanel;
	}

	public static void setStatus1(final String s) {
		statusAreaJPanel.status1.setText(s);
	}

	public static void setStatus2(final String s) {
		statusAreaJPanel.status2.setText(s);
	}
}
