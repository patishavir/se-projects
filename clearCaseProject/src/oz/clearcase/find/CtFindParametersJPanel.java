package oz.clearcase.find;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import oz.clearcase.infra.ClearCaseClass;
import oz.infra.parameters.XMLFileParameters;

/**
 * @author Oded
 */
public class CtFindParametersJPanel extends JPanel implements FocusListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private static CtFindParametersJPanel ctFindParametersJPanel;
	private static String branchNameParameter;
	private static JComboBox branchComboBox;
	//
	private static JTextField vobDirectoryJTextField;
	private static JTextField creatorUseridJTextField;
	private static JTextField fromDateJTextField;
	private static JTextField toDateJTextField;
	private static JTextField namePatternJTextField;
	//
	private static JRadioButton branchJRadioButton;
	private static JRadioButton elementJRadioButton;
	private static JRadioButton versionJRadioButton;
	//
	private static JCheckBox currentViewOnlyJCheckBox;
	private static JCheckBox objectActivityJCheckBox;
	private static JCheckBox objectDateJCheckBox;
	private static JCheckBox objectUserJCheckBox;
	private static JCheckBox objectCommentJCheckBox;
	//
	private static Font smallFont = new Font("Arial", Font.BOLD, 12);
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Dimension textFieldDimension = null;
	private static Dimension jbuttonDimension = null;
	private static Dimension ctFindGUILabelDimension = null;
	//
	private static final Color BGCOLOR = new Color(250, 230, 210);
	//
	private static Border raisedetchedBorder = BorderFactory
			.createEtchedBorder(EtchedBorder.RAISED);
	private static Border emptyBorder = new EmptyBorder(0, 3, 0, 0);
	private static Border compoundBorder = new CompoundBorder(raisedetchedBorder, emptyBorder);
	private ClearCaseClass ccc = new ClearCaseClass();
	private String defaultVobDirectory = "";
	private String defaultCreatorOwner = "";
	private String defaultNamePattern = "";
	private String defaultFromDate = "";
	private String defaultToDate = "";
	private String defaultBranch = "      Select branch      ";
	private static final String NEW_LINE = "\n";

	//
	public CtFindParametersJPanel() {
		logger.finest("CCViewJPanel constructor");
		ctFindParametersJPanel = this;
		setBackground(BGCOLOR);
		if (!ClearCaseClass.doesClearToolExeExists()) {
			JOptionPane.showMessageDialog(this,
					"Cleartool.exe not found! CCViewManager is terminated.");
			System.exit(-1);
		}
		if (!ccc.getClearCaseHostInfo()) {
			JOptionPane.showMessageDialog(this,
					"ClearCase is not running on this machine! CCViewManager is terminated.");
			System.exit(-1);
		}
		//
		JLabel selectVOBLabel = new JLabel("Select  Folder / VOB ");
		setDimension(selectVOBLabel, ctFindGUILabelDimension);
		//
		JLabel selectBranchJLabel = new JLabel("Select branch ");
		setDimension(selectBranchJLabel, ctFindGUILabelDimension);
		//
		JLabel elementVersionJLabel = new JLabel("Element/Version ? ");
		setDimension(elementVersionJLabel, ctFindGUILabelDimension);
		//
		JLabel selectCreatorUserid = new JLabel("Creator/Owner userid ");
		setDimension(selectCreatorUserid, ctFindGUILabelDimension);
		//
		JLabel fromDateJLabel = new JLabel("From date: (dd-Mmm-yy)");
		setDimension(fromDateJLabel, ctFindGUILabelDimension);
		//
		JLabel toDateJLabel = new JLabel("To date: (dd-Mmm-yy)");
		setDimension(toDateJLabel, ctFindGUILabelDimension);
		//
		JLabel outputConfigurationJLabel = new JLabel("Output configuration");
		setDimension(outputConfigurationJLabel, ctFindGUILabelDimension);
		//
		JLabel namePatternJLabel = new JLabel("Name pattern");
		setDimension(namePatternJLabel, ctFindGUILabelDimension);
		//
		JLabel currentViewOnlyJLabel = new JLabel("Restrict to current view");
		setDimension(currentViewOnlyJLabel, ctFindGUILabelDimension);
		processInputParameters();
		vobDirectoryJTextField = new JTextField(30);
		setDimension(vobDirectoryJTextField, textFieldDimension);
		vobDirectoryJTextField.addFocusListener(this);
		vobDirectoryJTextField.setText(defaultVobDirectory);
		//
		creatorUseridJTextField = new JTextField(8);
		creatorUseridJTextField.setText(defaultCreatorOwner);
		setDimension(creatorUseridJTextField, textFieldDimension);
		//
		fromDateJTextField = new JTextField(8);
		fromDateJTextField.setText(defaultFromDate);
		setDimension(fromDateJTextField, textFieldDimension);
		//
		toDateJTextField = new JTextField(8);
		toDateJTextField.setText(defaultToDate);
		setDimension(toDateJTextField, textFieldDimension);
		//
		namePatternJTextField = new JTextField(8);
		namePatternJTextField.setText(defaultNamePattern);
		setDimension(namePatternJTextField, textFieldDimension);
		//
		branchJRadioButton = new JRadioButton("Branch");
		branchJRadioButton.setSelected(false);
		branchJRadioButton.setBackground(Color.WHITE);
		elementJRadioButton = new JRadioButton("Element");
		elementJRadioButton.setSelected(true);
		elementJRadioButton.setBackground(Color.WHITE);
		versionJRadioButton = new JRadioButton("Version");
		versionJRadioButton.setBackground(Color.WHITE);
		versionJRadioButton.setSelected(false);
		ButtonGroup group = new ButtonGroup();
		group.add(branchJRadioButton);
		group.add(elementJRadioButton);
		group.add(versionJRadioButton);
		JPanel elementVersionJPanel = new JPanel(new GridLayout(1, 3));
		elementVersionJPanel.setBackground(Color.WHITE);
		elementVersionJPanel.add(branchJRadioButton);
		elementVersionJPanel.add(elementJRadioButton);
		elementVersionJPanel.add(versionJRadioButton);
		setDimension(elementVersionJPanel, textFieldDimension);
		//
		currentViewOnlyJCheckBox = new JCheckBox("true");
		currentViewOnlyJCheckBox.setSelected(true);
		currentViewOnlyJCheckBox.setBackground(Color.WHITE);
		setDimension(currentViewOnlyJCheckBox, textFieldDimension);
		//
		objectDateJCheckBox = new JCheckBox("Date");
		objectDateJCheckBox.setSelected(true);
		objectDateJCheckBox.setBackground(Color.WHITE);
		objectUserJCheckBox = new JCheckBox("User");
		objectUserJCheckBox.setSelected(true);
		objectUserJCheckBox.setBackground(Color.WHITE);
		objectCommentJCheckBox = new JCheckBox("Comment");
		objectCommentJCheckBox.setSelected(true);
		objectCommentJCheckBox.setBackground(Color.WHITE);
		objectActivityJCheckBox = new JCheckBox("Activity");
		objectActivityJCheckBox.setSelected(true);
		objectActivityJCheckBox.setBackground(Color.WHITE);
		JPanel outputConfigurationJPanel = new JPanel(new GridLayout(1, 4));
		outputConfigurationJPanel.setBackground(Color.WHITE);
		//
		outputConfigurationJPanel.add(objectDateJCheckBox);
		outputConfigurationJPanel.add(objectUserJCheckBox);
		outputConfigurationJPanel.add(objectCommentJCheckBox);
		outputConfigurationJPanel.add(objectActivityJCheckBox);
		setDimension(outputConfigurationJPanel, textFieldDimension);
		//
		JButton vobBrowseJButton = new JButton("browse");
		vobBrowseJButton.setActionCommand("browse");
		setDimension(vobBrowseJButton, jbuttonDimension);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		vobBrowseJButton.setBorder(raisedbevel);
		CtFindBrowseButtonListener ctFindBrowseButtonListener = new CtFindBrowseButtonListener(this);
		vobBrowseJButton.addActionListener(ctFindBrowseButtonListener);
		//
		JButton processFindCommandJButton = new JButton("Process");
		processFindCommandJButton.setActionCommand("ProccessFindCommand");
		setDimension(processFindCommandJButton, jbuttonDimension);
		processFindCommandJButton.setBorder(raisedbevel);
		CtFindProcessJButtonListener ctFindProcessJButtonListener = new CtFindProcessJButtonListener();
		processFindCommandJButton.addActionListener(ctFindProcessJButtonListener);
		branchComboBox = new JComboBox();
		CtFindBranchComboBoxListener ctFindBranchComboBoxListener = new CtFindBranchComboBoxListener(
				this);
		branchComboBox.addActionListener(ctFindBranchComboBoxListener);
		setDimensionWithoutBorder(branchComboBox, textFieldDimension);
		branchComboBox.addItem(defaultBranch);
		branchComboBox.setBackground(Color.WHITE);
		//
		// setPreferredSize(new Dimension(screenSize.width - 4,
		// screenSize.height / 2));
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		int rowNum = 0;
		//
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.weighty = 0.0;
		c.ipadx = 10;
		c.ipady = 10;
		c.insets = new Insets(10, 20, 0, 0);
		add(selectVOBLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.weighty = 0.0;
		c.insets = new Insets(10, 10, 0, 0);
		add(vobDirectoryJTextField, c);
		//
		c.gridx = 2;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.weighty = 0.0;
		c.insets = new Insets(10, 10, 0, 20);
		add(vobBrowseJButton, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(namePatternJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(namePatternJTextField, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(currentViewOnlyJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(currentViewOnlyJCheckBox, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(selectCreatorUserid, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(creatorUseridJTextField, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(elementVersionJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(elementVersionJPanel, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(selectBranchJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(branchComboBox, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(fromDateJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(fromDateJTextField, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(toDateJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(toDateJTextField, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 0, 0);
		add(outputConfigurationJLabel, c);
		//
		c.gridx = 1;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 10, 0, 0);
		add(outputConfigurationJPanel, c);
		//
		rowNum++;
		c.gridx = 0;
		c.gridy = rowNum;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10, 20, 10, 0);
		add(processFindCommandJButton, c);
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		logger.fine("Focus gained" + e.toString());
	}

	public void focusLost(FocusEvent e) {
		logger.fine("Focus lost" + e.toString());
		if (vobDirectoryJTextField.getText().length() > 1)
			populateBranchComboBox();
	}

	void populateBranchComboBox() {
		String[] clearToolLstypeparams = { "", "lstype", "-kind", "brtype", "-short", "-invob",
				vobDirectoryJTextField.getText() };
		File vobDirectoryFile = new File(vobDirectoryJTextField.getText());
		if (!vobDirectoryFile.isDirectory()) {
			JOptionPane.showMessageDialog(this, "The specified directory is invalid.");
			return;
		}
		if (!ccc.clearToolCommand(clearToolLstypeparams)) {
			JOptionPane.showMessageDialog(this, "Failed to get branch list.");
			return;
		}
		branchComboBox.removeAllItems();
		String[] branchArray = ccc.getClearCaseOut().split(NEW_LINE);
		for (int i = 0; i < branchArray.length; i++) {
			branchComboBox.addItem(branchArray[i]);
		}
		branchComboBox.setMaximumRowCount(10);
	}

	/*
	 * 
	 */
	void setDimensions() {
		int heightFactor = 25;
		ctFindGUILabelDimension = new Dimension(screenSize.width / 8, screenSize.height
				/ heightFactor);
		textFieldDimension = new Dimension(screenSize.width / 8, screenSize.height / heightFactor);
		jbuttonDimension = new Dimension(screenSize.width / 10, screenSize.height / heightFactor);
	}

	private void setDimension(JComponent jcomp, Dimension dim) {
		setDimensionWithoutBorder(jcomp, dim);
		jcomp.setBorder(compoundBorder);
	}

	private void setDimensionWithoutBorder(JComponent jcomp, Dimension dim) {
		jcomp.setPreferredSize(dim);
		jcomp.setMinimumSize(dim);
		jcomp.setMaximumSize(dim);
		jcomp.setFont(smallFont);
	}

	void processInputParameters() {
		if (!CtFindMain.isCheck4InputParameters()) {
			return;
		}
		defaultVobDirectory = XMLFileParameters.getInputParameter("vobDirectory");
		defaultNamePattern = XMLFileParameters.getInputParameter("namePattern");
		defaultBranch = XMLFileParameters.getInputParameter("branch");
		defaultToDate = XMLFileParameters.getInputParameter("toDate");
		defaultFromDate = XMLFileParameters.getInputParameter("fromDate");
		defaultCreatorOwner = XMLFileParameters.getInputParameter("creatorOwner");
	}

	public JTextField getvobDirectoryJTextField() {
		return vobDirectoryJTextField;
	}

	public static String getBranchNameParameter() {
		return branchNameParameter;
	}

	public static void setBranchNameParameter(String branchNameParameter) {
		CtFindParametersJPanel.branchNameParameter = branchNameParameter;
	}

	public static JTextField getCreatorUseridJTextField() {
		return creatorUseridJTextField;
	}

	public static JTextField getFromDateJTextField() {
		return fromDateJTextField;
	}

	public static JTextField getToDateJTextField() {
		return toDateJTextField;
	}

	public static JTextField getVobDirectoryJTextField() {
		return vobDirectoryJTextField;
	}

	public static JRadioButton getElementJRadioButton() {
		return elementJRadioButton;
	}

	public static JRadioButton getVersionJRadioButton() {
		return versionJRadioButton;
	}

	public static JCheckBox getObjectCommentJCheckBox() {
		return objectCommentJCheckBox;
	}

	public static JCheckBox getObjectDateJCheckBox() {
		return objectDateJCheckBox;
	}

	public static JCheckBox getobjectActivityJCheckBox() {
		return objectActivityJCheckBox;
	}

	public static JCheckBox getObjectUserJCheckBox() {
		return objectUserJCheckBox;
	}

	public static JTextField getNamePatternJTextField() {
		return namePatternJTextField;
	}

	public static JRadioButton getBranchJRadioButton() {
		return branchJRadioButton;
	}

	public static JCheckBox getCurrentViewOnlyJCheckBox() {
		return currentViewOnlyJCheckBox;
	}
}