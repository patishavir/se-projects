package oz.guigenerator.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observer;
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
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import oz.guigenerator.GUIElement;
import oz.guigenerator.ValidationRuleEnum;
import oz.guigenerator.listeners.GuiGBrowseButtonActionListener;
import oz.guigenerator.listeners.GuiGButtonListener;
import oz.guigenerator.listeners.GuiGDateButtonActionListener;
import oz.guigenerator.listeners.GuiGLinkMouseListener;
import oz.guigenerator.listeners.GuiGRunButtonActionListener;
import oz.guigenerator.listeners.GuiGSubmitButtonListener;
import oz.infra.font.FontUtils;
import oz.infra.swing.CopyPastePopupMenuAdapter;
import oz.infra.swing.ExcelAdapter;

public class GuiGeneratorJPanel extends JPanel {
	//
	private static final Font NORMAL_FONT = FontUtils.ARIAL_BOLD_14;
	private static final Font LARGE_FONT = FontUtils.ARIAL_BOLD_16;
	private static final Color BGCOLOR = new Color(250, 230, 210);
	private static final Insets NORMAL_INSETS = new Insets(5, 10, 0, 10);
	private static final Insets BROWSE_INSETS = new Insets(10, 0, 0, 30);
	private static boolean registerKeyboardAction = true;
	private ArrayList<GUIElement> paramJComponents;
	private GuiGSubmitButtonListener guiGSubmitButtonListener;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private GridBagLayout gridbag = new GridBagLayout();
	private GridBagConstraints gridBagConstraints = new GridBagConstraints();
	private int rowNumber = 0;
	private int columnNumber = 0;
	private int maxColumns = 1;

	//
	public GuiGeneratorJPanel(final GuiGSubmitButtonListener guiGSubmitButtonListener) {
		setBackground(BGCOLOR);
		setLayout(gridbag);
		setBorder(null);
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 5;
		gridBagConstraints.insets = NORMAL_INSETS;
		this.guiGSubmitButtonListener = guiGSubmitButtonListener;
		paramJComponents = guiGSubmitButtonListener.getGuiGComponents();
	}

	/*
	 * addButton
	 */
	public final void addButton(final String buttonText, final String jOptionPaneText) {
		performInitialComponentProcessing(null, null, null, false);
		gridBagConstraints.gridwidth = maxColumns + 1;
		JButton jButton = new JButton(buttonText);
		jButton.setFont(LARGE_FONT);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		jButton.setBorder(raisedbevel);
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		add(jButton, gridBagConstraints);
		GuiGButtonListener buttonListener = new GuiGButtonListener(jOptionPaneText);
		jButton.addActionListener(buttonListener);
		rowNumber++;
		logger.finest("Button");
	}

	/*
	 * addCheckBox
	 */
	public final void addCheckBox(final String name, final String displayName, final String defaultValue,
			final String toolTipText, final String helpText) {
		performInitialComponentProcessing(name, displayName, toolTipText, true);
		JCheckBox checkBox = new JCheckBox();
		checkBox.setName(name);
		checkBox.setSelected(defaultValue.equalsIgnoreCase("yes"));
		paramJComponents.add(new GUIElement(checkBox, null));
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(checkBox, gridBagConstraints);
		addHelpText(helpText);
		rowNumber++;
	}

	/*
	 * addCombobox
	 */
	public final void addCombobox(final String name, final String displayName, final String[] comboboxValues,
			final String defaultValue, final boolean editAble, final String toolTipText, final String helpText) {
		performInitialComponentProcessing(name, displayName, toolTipText, true);
		JComboBox paramsCombobox = new JComboBox(comboboxValues);
		paramsCombobox.setFont(NORMAL_FONT);
		paramsCombobox.setBackground(Color.WHITE);
		paramsCombobox.setName(name);
		paramsCombobox.setEditable(editAble);
		if (defaultValue != null && defaultValue.length() > 0) {
			paramsCombobox.setSelectedItem(defaultValue);
		}
		paramJComponents.add(new GUIElement(paramsCombobox, null));
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(paramsCombobox, gridBagConstraints);
		addHelpText(helpText);
		rowNumber++;
		if (columnNumber > maxColumns) {
			maxColumns = columnNumber;
		}
		logger.finest("Param: " + name);
	}

	private void addComponentNameLabel(final String name, final String toolTipText) {
		JLabel myJLabel = new JLabel(name);
		myJLabel.setFont(NORMAL_FONT);
		// myJLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		if (toolTipText != null) {
			myJLabel.setToolTipText(toolTipText);
		}
		add(myJLabel, gridBagConstraints);
	}

	private void addHelpText(final String helpText) {
		if (helpText != null) {
			columnNumber++;
			gridBagConstraints.gridx = columnNumber;
			gridBagConstraints.insets = BROWSE_INSETS;
			JLabel myJLabel = new JLabel(helpText);
			myJLabel.setFont(NORMAL_FONT);
			myJLabel.setForeground(Color.GRAY);
			add(myJLabel, gridBagConstraints);
		}
	}

	/*
	 * addTitle
	 */
	public final void addLabel(final String title, final Color foreGroundColor) {
		performInitialComponentProcessing(null, null, null, false);
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		JLabel titleJlabel = new JLabel(title);
		titleJlabel.setFont(LARGE_FONT);
		titleJlabel.setForeground(foreGroundColor);
		add(titleJlabel, gridBagConstraints);
		rowNumber++;
	}

	/*
	 * addLink
	 */
	public final void addLink(final String name, final String displayName, final String url, final String text,
			final String toolTipText, final String helpText) {
		performInitialComponentProcessing(name, displayName, toolTipText, true);
		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontSize(style, 14);
		StyleConstants.setSpaceAbove(style, 0);
		StyleConstants.setSpaceBelow(style, 0);
		StyleConstants.setUnderline(style, true);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		// Insert content
		try {
			document.insertString(document.getLength(), text, style);
		} catch (BadLocationException badLocationException) {
			badLocationException.printStackTrace();
			logger.warning(badLocationException.getMessage());
		}
		GuiGeneratorJTextPane textPane = new GuiGeneratorJTextPane();
		textPane.setStyledDocument(document);
		textPane.setEditable(false);
		textPane.setUrl(url);
		textPane.setText(text);
		textPane.addMouseListener(new GuiGLinkMouseListener());
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(textPane, gridBagConstraints);
		textPane.setForeground(Color.BLUE);
		textPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		if (toolTipText != null) {
			textPane.setToolTipText(toolTipText);
		}
		gridBagConstraints.gridx = columnNumber;
		gridBagConstraints.insets = BROWSE_INSETS;
		addHelpText(helpText);
		rowNumber++;
		if (columnNumber > maxColumns) {
			maxColumns = columnNumber;
		}
		logger.finest("Param: " + name);
	}

	/*
	 * addPasswordField
	 */
	public final void addPasswordField(final String name, final String displayName, final String defaultValue,
			final String toolTipText, final int size, final ValidationRuleEnum validationRule) {
		final int passwordSize = size;
		performInitialComponentProcessing(name, displayName, toolTipText, true);
		JPasswordField passwordField;
		if (defaultValue != null && defaultValue.length() > 0) {
			passwordField = new JPasswordField(defaultValue, passwordSize);
		} else {
			passwordField = new JPasswordField(passwordSize);
		}
		passwordField.setName(name);
		paramJComponents.add(new GUIElement(passwordField, validationRule));
		ExcelAdapter excelAdapter = new ExcelAdapter(passwordField);
		new CopyPastePopupMenuAdapter(passwordField, excelAdapter, CopyPastePopupMenuAdapter.PASTEONLY);
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(passwordField, gridBagConstraints);
		rowNumber++;
	}

	/*
	 * addRadioButtonGroup
	 */
	public final void addRadioButtonGroup(final String name, final String displayName,
			final ArrayList<JRadioButton> radioButtonsArrayList, final int rowsP, final String toolTipText) {
		logger.finest("name: " + name + " size: " + String.valueOf(radioButtonsArrayList.size()));
		final int MAX_BUTTONS_IN_A_ROW = 4;
		int rows = rowsP;
		performInitialComponentProcessing(name, displayName, toolTipText, true);
		//
		ButtonGroup rdGroup = new ButtonGroup();
		if (rows == 0) {
			rows = radioButtonsArrayList.size() / MAX_BUTTONS_IN_A_ROW;
		}
		if (rows == 0) {
			rows = 1;
		}
		logger.finest("New radioButtonsJPanel ");
		logger.finest("rows " + String.valueOf(rows));
		JPanel radioButtonsJPanel = new JPanel(
				new GridLayout(rows, (radioButtonsArrayList.size() / rows) + (radioButtonsArrayList.size() % rows)));
		for (int i = 0; i < radioButtonsArrayList.size(); i++) {
			logger.finest((radioButtonsArrayList.get(i)).getText());
			JRadioButton jRadioButton = radioButtonsArrayList.get(i);
			jRadioButton.setFont(NORMAL_FONT);
			rdGroup.add(jRadioButton);
			radioButtonsJPanel.add((JRadioButton) radioButtonsArrayList.get(i));
		}
		gridBagConstraints.gridx = columnNumber;
		gridBagConstraints.gridheight = rows;
		paramJComponents.add(new GUIElement(rdGroup, null));
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(radioButtonsJPanel, gridBagConstraints);
		rowNumber = rowNumber + rows;
	}

	/*
	 * addSubmitButton
	 */
	public final void addSubmitButton(final String submitButtonText, final String submitButtonMessage,
			final boolean registerKeyboardAction, final Observer generalGUIObserver) {
		logger.finest("message: " + submitButtonMessage);
		performInitialComponentProcessing(null, null, null, false);
		gridBagConstraints.gridwidth = maxColumns + 1;
		JButton submitJButton = new JButton(submitButtonText);
		submitJButton.setFont(LARGE_FONT);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		submitJButton.setBorder(raisedbevel);
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		add(submitJButton, gridBagConstraints);
		guiGSubmitButtonListener.setGuiGeneratorSubmitJButton(submitJButton);
		guiGSubmitButtonListener.setJOptionPaneText(submitButtonMessage);
		submitJButton.addActionListener(guiGSubmitButtonListener);
		guiGSubmitButtonListener.addObserver(generalGUIObserver);
		submitJButton.addKeyListener(guiGSubmitButtonListener);
		//
		if (registerKeyboardAction && this.registerKeyboardAction) {
			KeyStroke submitKeyStoke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
			submitJButton.registerKeyboardAction(guiGSubmitButtonListener, "Submit", submitKeyStoke,
					JComponent.WHEN_IN_FOCUSED_WINDOW);
			//
		}
		logger.finest("addSubmitButton processin done !");
	}

	/*
	 * addTextArea
	 */
	public final void addTextArea(final String name, final String displayName, final String defaultValue,
			final String toolTipText, final int width, final int height, final int gridWidth,
			final ValidationRuleEnum validationRule) {
		performInitialComponentProcessing(name, displayName, toolTipText, false);
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 16));
		textArea.setLineWrap(false);
		textArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(width, height));
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(displayName);
		titledBorder.setTitleFont(NORMAL_FONT);
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(titledBorder, emptyBorder), areaScrollPane.getBorder()));
		if (defaultValue != null && defaultValue.length() > 0) {
			textArea.setText(defaultValue.replace(',', '\n'));
		}
		textArea.setFont(NORMAL_FONT);
		textArea.setName(name);
		textArea.setVisible(true);
		textArea.setLineWrap(true);
		paramJComponents.add(new GUIElement(textArea, validationRule));
		ExcelAdapter excelAdapter = new ExcelAdapter(textArea);
		new CopyPastePopupMenuAdapter(textArea, excelAdapter, CopyPastePopupMenuAdapter.BOTH);
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.gridwidth = gridWidth;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		add(areaScrollPane, gridBagConstraints);
		rowNumber++;
		if (columnNumber > maxColumns) {
			maxColumns = columnNumber;
		}
		registerKeyboardAction = false;
		logger.finest("Param: " + name);
	}

	/*
	 * addTextField
	 */
	public final void addTextField(final String name, final String displayName, final String defaultValue,
			final boolean browseButton, final String fileSelectionModeCodeString, final boolean runButton,
			final boolean dateButton, final String dateFormat, final String toolTipText, final String helpText,
			final boolean visible, final int size, final ValidationRuleEnum validationRule) {
		if (visible) {
			performInitialComponentProcessing(name, displayName, toolTipText, true);
		}
		JTextField paramValueJTextField;
		if (defaultValue != null && defaultValue.length() > 0) {
			paramValueJTextField = new JTextField(defaultValue, size);
		} else {
			paramValueJTextField = new JTextField(size);
		}
		paramValueJTextField.setFont(NORMAL_FONT);
		paramValueJTextField.setName(name);
		paramValueJTextField.setVisible(visible);
		paramJComponents.add(new GUIElement(paramValueJTextField, validationRule));
		ExcelAdapter excelAdapter = new ExcelAdapter(paramValueJTextField);
		new CopyPastePopupMenuAdapter(paramValueJTextField, excelAdapter, CopyPastePopupMenuAdapter.BOTH);
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		add(paramValueJTextField, gridBagConstraints);
		if (browseButton || runButton || dateButton) {
			columnNumber++;
			JButton myJButton = new JButton();
			myJButton.setBorder(BorderFactory.createRaisedBevelBorder());
			if (browseButton) {
				GuiGBrowseButtonActionListener browseButtonActionListener = new GuiGBrowseButtonActionListener(this,
						paramValueJTextField);
				myJButton.setText("browse");
				myJButton.addActionListener(browseButtonActionListener);
				myJButton.setActionCommand(fileSelectionModeCodeString);
			} else if (runButton) {
				GuiGRunButtonActionListener runButtonActionListener = new GuiGRunButtonActionListener(
						paramValueJTextField);
				myJButton.setText("run");
				if (!visible) {
					myJButton.setToolTipText(defaultValue);
				}
				myJButton.addActionListener(runButtonActionListener);
			} else if (dateButton) {
				GuiGDateButtonActionListener dateButtonActionListener = new GuiGDateButtonActionListener(
						paramValueJTextField, dateFormat);
				myJButton.setText("select date");
				if (!visible) {
					myJButton.setToolTipText(defaultValue);
				}
				myJButton.addActionListener(dateButtonActionListener);
			}
			gridBagConstraints.gridx = columnNumber;
			gridBagConstraints.insets = BROWSE_INSETS;
			add(myJButton, gridBagConstraints);
		}
		addHelpText(helpText);
		rowNumber++;
		if (columnNumber > maxColumns) {
			maxColumns = columnNumber;
		}
		logger.finest("Param: " + name);
	}

	/*
	 * checkforNameDuplicates
	 */
	public final void checkforNameDuplicates(final String name) {
		for (int i = 0; i < paramJComponents.size(); i++) {
			if (paramJComponents.get(i).getGuiComponent() instanceof JComponent) {
				JComponent jc = (JComponent) paramJComponents.get(i).getGuiComponent();
				if (jc.getName().equalsIgnoreCase(name)) {
					String errorMessage = "Duplicate parameter " + name + ".\nProcessing aborted!";
					JOptionPane.showMessageDialog(this, errorMessage, "Bad parameter file", JOptionPane.ERROR_MESSAGE);
					logger.severe(errorMessage);
					System.exit(-1);
				}
			}
		}
	}

	private void performInitialComponentProcessing(final String name, final String displayName,
			final String toolTipText, final boolean isAddComponentNameLabel) {
		gridBagConstraints.insets = NORMAL_INSETS;
		columnNumber = 0;
		rowNumber++;
		gridBagConstraints.gridx = columnNumber;
		gridBagConstraints.gridy = rowNumber;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.LINE_END;
		if (name != null) {
			checkforNameDuplicates(name);
			validateName(name);
		}
		if (isAddComponentNameLabel) {
			addComponentNameLabel(displayName, toolTipText);
			columnNumber++;
			gridBagConstraints.gridx = columnNumber;
		}
	}

	/*
	 * validateName
	 */
	private void validateName(final String name) {
		if (name.indexOf(" ") != -1) {
			JOptionPane.showMessageDialog(this, "name: " + name + "\nName cannot have blanks. Processing aborted!",
					"Bad parameter file", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
}
