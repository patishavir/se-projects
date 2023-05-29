package rami.nuna;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CalculatorJFrame extends JFrame implements ActionListener {

	public JTextField menTextField, womenTextField, vegiTextField, kidsTextField,
			required$TextField;
	public JButton submitButton;
	private Logger logger = Logger.getLogger(this.getName());

	public CalculatorJFrame() {
		super("form");

		JLabel menLabel = new JLabel("מספר גברים");
		JLabel womenLabel = new JLabel("מספר נשים");
		JLabel kidsLabel = new JLabel("מספר ילדים");
		JLabel vegiLabel = new JLabel("מספר צמחונים");
		JLabel required$Label = new JLabel("הסכום הדרוש לתשלום");

		submitButton = new JButton("בצע");

		JPanel pane = new JPanel();
		int columns = 5;
		menTextField = new JTextField("0", columns);
		womenTextField = new JTextField("0", columns);
		kidsTextField = new JTextField("0", columns);
		vegiTextField = new JTextField("0", columns);
		required$TextField = new JTextField("0", columns);

		pane.add(menLabel);
		pane.add(menTextField);

		pane.add(womenLabel);
		pane.add(womenTextField);

		pane.add(kidsLabel);
		pane.add(kidsTextField);

		pane.add(vegiLabel);
		pane.add(vegiTextField);

		pane.add(required$Label);
		pane.add(required$TextField);

		pane.add(submitButton);
		setContentPane(pane);

		submitButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == submitButton) {
			submitButton.removeActionListener(this);
			logger.finest("starting ...");
			Parameters parameters = new Parameters();
			parameters.setMen(Integer.parseInt(menTextField.getText()));
			parameters.setWomen(Integer.parseInt(womenTextField.getText()));
			parameters.setKids(Integer.parseInt(kidsTextField.getText()));
			parameters.setVegi(Integer.parseInt(vegiTextField.getText()));
			parameters.setRequired$(Integer.parseInt(required$TextField.getText()));

			setVisible(false);
			int isStudent = JOptionPane.showConfirmDialog(null, "?האם את/ה סטודנט/ית",
					"הנחת סטודנט", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			parameters.setStudent(isStudent == 0);

			String name = JOptionPane.showInputDialog(null, "?מה שמך", "שם",
					JOptionPane.QUESTION_MESSAGE);

			parameters.setName(name);

			Order.processOrder(parameters);
		}
	}
}
