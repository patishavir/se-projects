package layout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JavaGridBagLayoutExample {
	private JTextField jtfName;
	private JTextField jtfAge;
	private JTextArea jtaAddress;
	private JTextField jtfState;
	private JTextField jtfCity;
	private JRadioButton jrbMale;
	private JRadioButton jrbFemale;
	private JButton jbtSubmit;

	public void addComponentsToPane(JFrame frame) {
		Container pane = frame.getContentPane();
		ButtonGroup group = new ButtonGroup();
		group.add(jrbMale);
		group.add(jrbFemale);

		GridBagConstraints gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		gBC.insets = new Insets(5, 5, 0, 5);

		JLabel jlbName = new JLabel("Name : ");
		gBC.gridx = 0;
		gBC.gridy = 0;
		pane.add(jlbName, gBC);

		jtfName = new JTextField(20);
		gBC.gridx = 1;
		gBC.gridy = 0;
		gBC.gridwidth = 2;
		pane.add(jtfName, gBC);

		JLabel jlbAge = new JLabel("Age : ");
		gBC.gridx = 0;
		gBC.gridy = 1;
		gBC.gridwidth = 1;
		pane.add(jlbAge, gBC);

		jtfAge = new JTextField(5);
		gBC.gridx = 1;
		gBC.gridy = 1;
		gBC.fill = GridBagConstraints.NONE;
		gBC.anchor = GridBagConstraints.WEST;
		gBC.gridwidth = 2;
		pane.add(jtfAge, gBC);

		gBC.fill = GridBagConstraints.HORIZONTAL;

		JLabel jlbSex = new JLabel("Sex : ");
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		pane.add(jlbSex, gBC);

		jrbMale = new JRadioButton("Male");
		gBC.gridx = 1;
		gBC.gridy = 2;
		pane.add(jrbMale, gBC);

		jrbFemale = new JRadioButton("Female");
		gBC.gridx = 2;
		gBC.gridy = 2;
		pane.add(jrbFemale, gBC);

		JLabel jlbAddress = new JLabel("Address : ");
		gBC.gridx = 0;
		gBC.gridy = 3;
		pane.add(jlbAddress, gBC);

		jtaAddress = new JTextArea(5, 10);
		gBC.gridx = 1;
		gBC.gridy = 3;
		gBC.gridwidth = 2;
		pane.add(jtaAddress, gBC);

		JLabel jlbCity = new JLabel("City : ");
		gBC.gridx = 0;
		gBC.gridy = 4;
		gBC.gridwidth = 1;
		pane.add(jlbCity, gBC);

		jtfCity = new JTextField(10);
		gBC.gridx = 1;
		gBC.gridy = 4;
		gBC.gridwidth = 2;
		pane.add(jtfCity, gBC);

		gBC.gridwidth = 1;

		JLabel jlbState = new JLabel("State : ");
		gBC.gridx = 3;
		gBC.gridy = 4;
		gBC.insets.right = 0;
		pane.add(jlbState, gBC);

		gBC.insets.right = 5;

		jtfState = new JTextField(10);
		gBC.gridx = 4;
		gBC.gridy = 4;
		pane.add(jtfState, gBC);

		jbtSubmit = new JButton("Submit");
		gBC.gridx = 4;
		gBC.gridy = 5;
		pane.add(jbtSubmit, gBC);
	}

	public static void main(String[] args) {
		JavaGridBagLayoutExample test = new JavaGridBagLayoutExample();
		JFrame frame = new JFrame("GridBagLayout Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridBagLayout());
		test.addComponentsToPane(frame);
		frame.pack();
		frame.setVisible(true);
	}
}
