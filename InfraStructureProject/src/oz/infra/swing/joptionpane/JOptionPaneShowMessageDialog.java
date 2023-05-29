package oz.infra.swing.joptionpane;

import java.awt.Component;

import javax.swing.JOptionPane;

 class JOptionPaneShowMessageDialog implements Runnable {
	private Component parentComponent;
	private Object message;
	private String title;
	private int messageType;

	public JOptionPaneShowMessageDialog(final Component parentComponentP, final Object messageP,
			final String titleP, final int messageTypeP) {
		this.parentComponent = parentComponentP;
		this.message = messageP;
		this.title = titleP;
		this.messageType = messageTypeP;
	}

	public void run() {
		JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
	}
}
