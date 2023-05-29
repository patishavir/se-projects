package oz.idgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import oz.infra.swing.CopyPastePopupMenuAdapter;
import oz.infra.swing.ExcelAdapter;

public class IdJPanel extends JPanel implements ActionListener {
	private JTextField startingIdJTextField = new JTextField();
	private JTextField ids2GenerateJTextField = new JTextField(String.valueOf(ValidIdsGeneration
			.getIDS2GENERATE()));
	private JTable idJTable;
	private JScrollPane scrollPane = new JScrollPane();
	private Dimension headerDimension;
	private Dimension labelDimension;
	private Dimension textFieldDimension;
	private Dimension scrollPaneDimension;
	private Color backgroundColor = new Color(250, 225, 200);

	IdJPanel() {
		this.setBackground(backgroundColor);
		Font normalFont = new Font("Arial", Font.BOLD, 14);
		Font largeFont = new Font("Arial", Font.BOLD, 16);
		Insets insets = new Insets(2, 2, 2, 2);
		Insets leftInsets = new Insets(2, 4, 2, 2);
		setDimensions();
		// Insets(int top,
		// int left,
		// int bottom,
		// int right)
		Random random = new Random();
		startingIdJTextField.setText(String.valueOf(Math.abs(random.nextLong() % 100000000)));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.ipady = 1;
		c.ipadx = 1;
		JLabel headerLabel = new JLabel("אאאאאאאאאאאאאאאאאאאאאאא");
		headerLabel.setFont(largeFont);
		setSize(headerLabel, headerDimension);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = insets;
		add(headerLabel, c);
		//
		startingIdJTextField.setFont(normalFont);
		gridbag.setConstraints(startingIdJTextField, c);
		setSize(startingIdJTextField, textFieldDimension);
		startingIdJTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = leftInsets;
		add(startingIdJTextField, c);
		//
		JLabel startingIdLabel = new JLabel("בבבבבבבבבבבבבבבבבבבבבבב:");
		startingIdLabel.setFont(normalFont);
		setSize(startingIdLabel, labelDimension);
		startingIdLabel.setBorder(new MatteBorder(1, 1, 1, 1, Color.orange));
		startingIdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.gridx = 1;
		c.gridy = 1;
		add(startingIdLabel, c);
		//
		setSize(ids2GenerateJTextField, textFieldDimension);
		ids2GenerateJTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		ids2GenerateJTextField.setFont(normalFont);
		c.gridx = 0;
		c.gridy = 2;
		c.insets = leftInsets;
		add(ids2GenerateJTextField, c);
		//
		JLabel ids2GenerateLabel = new JLabel("גגגגגגגגגגגגגגגגגגגגגגגגגגגגג:");
		setSize(ids2GenerateLabel, labelDimension);
		ids2GenerateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ids2GenerateLabel.setFont(normalFont);
		ids2GenerateLabel.setBorder(new MatteBorder(1, 1, 1, 1, Color.orange));
		c.gridx = 1;
		c.gridy = 2;
		c.insets = insets;
		add(ids2GenerateLabel, c);
		//
		JButton startGenerationJButton = new JButton("דדדדדדדדדדדדדדדדדדדדדדדדדדדדדדדד");
		startGenerationJButton.addActionListener(this);
		setSize(startGenerationJButton, labelDimension);
		startGenerationJButton.setHorizontalAlignment(SwingConstants.RIGHT);
		startGenerationJButton.setFont(normalFont);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		startGenerationJButton.setBorder(raisedbevel);
		startGenerationJButton.setHorizontalAlignment(SwingConstants.CENTER);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		add(startGenerationJButton, c);
		setSize(scrollPane, scrollPaneDimension);
		scrollPane.setFont(normalFont);
		scrollPane.setBorder(new MatteBorder(1, 1, 1, 1, Color.orange));
		scrollPane.setBackground(backgroundColor);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		generateIds();
		// add(idJTable, c);
		add(scrollPane, c);
	}

	public final void actionPerformed(final ActionEvent e) {
		generateIds();
	}

	final void generateIds() {
		IdTable myIdTable = new IdTable();
		String[][] idTable = myIdTable.getIdTable(ids2GenerateJTextField.getText(),
				startingIdJTextField.getText());
		String[] idTableHeader = myIdTable.getIdTableHeader();
		idJTable = new IdJTable(idTable, idTableHeader);
		ExcelAdapter excelAdapter = new ExcelAdapter(idJTable);
		new CopyPastePopupMenuAdapter(idJTable, excelAdapter, CopyPastePopupMenuAdapter.COPYONLY);
		scrollPane.setViewportView(idJTable);
	}

	void setDimensions() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		headerDimension = new Dimension(screenSize.width / 5, screenSize.height / 30);
		labelDimension = new Dimension(screenSize.width / 6, screenSize.height / 40);
		textFieldDimension = new Dimension(screenSize.width / 12, screenSize.height / 40);
		scrollPaneDimension = new Dimension(screenSize.width / 4, screenSize.height * 10 / 45);
	}

	private void setSize(JComponent jcomp, Dimension dim) {
		jcomp.setPreferredSize(dim);
		jcomp.setMinimumSize(dim);
		jcomp.setMaximumSize(dim);
	}
}