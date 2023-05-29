package oz.swing;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

class labelTest extends JDialog implements ActionListener {

	JTextField txtField = new JTextField();
	JButton btnOk = new JButton("Ok");
	int lineNumber = 0;
	// Fetch icon
	ImageIcon icon = new ImageIcon("C:\\oj\\projects\\swingSamplesProject\\src\\images\\image1.jpg");

	// Create a label with text and an icon; the icon appears to the left of the
	// text
	JLabel label = new JLabel("Text Label", icon, JLabel.CENTER);

	public labelTest(Dialog dlg, boolean modal) {

		JLabel label = new myLabel("Text Label", icon.getImage()); // ,
																	// JLabel.TOP)

		btnOk.setName("Ok");
		// this.getContentPane().add(txtField, BorderLayout.CENTER);
		this.getContentPane().add(label, BorderLayout.CENTER);
		this.getContentPane().add(btnOk, BorderLayout.SOUTH);
		btnOk.addActionListener(this);
		this.setTitle("Select Line no.");
		this.setSize(200, 100);
		this.setModal(modal);
		this.show();
	}

	public void actionPerformed(ActionEvent event) {
		Object obj = event.getSource();

		if (obj instanceof JButton) {
			if (((JButton) obj).getName().equalsIgnoreCase("Ok")) {
				String str = txtField.getText();

				if (str != null && !str.equals("")) {
					try {
						lineNumber = Integer.parseInt(str);
					} catch (Exception ex) {
						lineNumber = 0;
					}
				}
				this.setVisible(false);
				System.exit(0);
			}
		}
	}

	int getLineNumber() {
		return lineNumber;
	}

	public static void main(String[] s) {
		new labelTest(null, true);
	}
}

class myLabel extends JLabel {
	Image image;
	String text;

	public myLabel(String text, Image image) {
		this.image = image;
		this.text = text;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Paint background

		// Draw image at its natural size first.
		g.drawImage(image, 25, 10, this);
		g.drawString("mystring", 15, 28);
	}

}
