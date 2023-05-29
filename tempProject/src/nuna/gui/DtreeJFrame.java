package nuna.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class DtreeJFrame extends JFrame {

	private GridBagConstraints c = new GridBagConstraints();
	private DtreeListener dtreeListener = new DtreeListener(this);
	private Map<String, JTextField> textFieldMap = new HashMap<String, JTextField>();

	public DtreeJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dtree ");
		setLocation(400, 400);
		setLayout(new GridBagLayout());
		c.insets = new Insets(25, 5, 5, 5);
		c.gridx = 0;
		c.gridy = -1;
		addButtonAndJTextField("load");
		addButtonAndJTextField("addWord");
		addButtonAndJTextField("search");
		addButtonAndJTextField("delete");
		this.pack();
		this.setVisible(true);
	}

	private void addButtonAndJTextField(final String text) {
		c.gridx = 0;
		c.gridy = c.gridy + 1;
		JButton jButton1 = new JButton(text);
		getContentPane().add(jButton1, c);
		jButton1.setActionCommand(text.toUpperCase());
		jButton1.addActionListener(dtreeListener);
		c.gridx = 1;
		JTextField jTextField = new JTextField(25);
		getContentPane().add(jTextField, c);
		textFieldMap.put(text.toUpperCase(), jTextField);
	}

	public Map<String, JTextField> getTextFieldMap() {
		return textFieldMap;
	}
}
