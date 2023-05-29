package oz.guigenerator.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import oz.guigenerator.GUIElement;
import oz.guigenerator.GuiGeneratorParameters;
import oz.guigenerator.ValidationRuleEnum;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class GuiGSubmitButtonListener extends Observable implements KeyListener, ActionListener {
	private ArrayList<GUIElement> guiGComponents = new ArrayList<GUIElement>();
	private String jOptionPaneText;
	private JButton submitButton;

	private Logger logger = JulUtils.getLogger();

	public final void actionPerformed(final ActionEvent event) {
		performSubmitProcessing();
	}

	public final void keyPressed(final KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
			performSubmitProcessing();
		}
		return;
	}

	public final void keyReleased(final KeyEvent ex) {
		return;
	}

	public final void keyTyped(final KeyEvent ex) {
		return;
	}

	/*
	 * performSubmitProcessing
	 */
	private void performSubmitProcessing() {
		if (jOptionPaneText != null) {
			Component rootComponent = SwingUtilities.getRoot(submitButton);
			JOptionPane.showMessageDialog(rootComponent, jOptionPaneText);
			logger.finest(rootComponent.toString());
		}
		JPasswordField jpasswordField;
		JTextField jTextField;
		JTextArea jTextArea;
		JComboBox jComboBox;
		JCheckBox jCheckBox;
		String name;
		String value;
		boolean validationOK = true;
		Hashtable<String, String> parametersHashTable = new Hashtable<String, String>();
		for (int i = 0; i < guiGComponents.size(); i++) {
			name = null;
			value = null;
			Object currentJComponent = guiGComponents.get(i).getGuiComponent();
			ValidationRuleEnum validationRule = guiGComponents.get(i).getValidationRule();
			if (currentJComponent instanceof JPasswordField) {
				jpasswordField = (JPasswordField) currentJComponent;
				name = jpasswordField.getName();
				value = new String(jpasswordField.getPassword());
			} else if (currentJComponent instanceof JTextField) {
				jTextField = (JTextField) currentJComponent;
				name = jTextField.getName();
				value = jTextField.getText();
			} else if (currentJComponent instanceof JTextArea) {
				jTextArea = (JTextArea) currentJComponent;
				name = jTextArea.getName();
				value = jTextArea.getText();
				String fileFullPath = PathUtils.getFullPath(GuiGeneratorParameters.getTempFolder(),
						name + OzConstants.TXT_SUFFIX);
				FileUtils.writeFile(new File(fileFullPath), value);

			} else if (currentJComponent instanceof JComboBox) {
				jComboBox = (JComboBox) currentJComponent;
				name = jComboBox.getName();
				value = (String) jComboBox.getSelectedItem();
			} else if (currentJComponent instanceof JCheckBox) {
				jCheckBox = (JCheckBox) currentJComponent;
				name = jCheckBox.getName();
				value = "no";
				if (jCheckBox.isSelected()) {
					value = "yes";
				}
			} else if (currentJComponent instanceof ButtonGroup) {
				ButtonGroup group = (ButtonGroup) currentJComponent;
				name = null;
				value = null;
				for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
					JRadioButton radioButton = (JRadioButton) e.nextElement();
					if (radioButton.getModel() == group.getSelection()) {
						name = radioButton.getActionCommand();
						value = radioButton.getName();
					}
				}
			} else {
				SystemUtils.printMessageAndExit("Unrecognized object. Processing terminated!", -1);

			}
			if (validationRule != null && validationOK) {
				validationOK = processValidationRule(validationRule, value, name);
			}

			if (name != null) {
				parametersHashTable.put(name, value.trim());
			}
		}
		if (validationOK) {
			Component rootComponent = SwingUtilities.getRoot(submitButton);
			rootComponent.setVisible(false);
			setChanged();
			notifyObservers(parametersHashTable);
		}
	}

	private boolean processValidationRule(final ValidationRuleEnum validationRule, final String value,
			final String name) {
		boolean validationOK = true;
		File validationFile;
		switch (validationRule) {
		case NOT_NULL:
			validationOK = (value.trim().length() > 0);
			break;
		case NUMERIC:
			validationOK = (value.trim().length() > 0) && StringUtils.isJustDigits(value);
			break;
		case NUMERIC_IF_NOTNULL:
			validationOK = (value.trim().length() == 0) || StringUtils.isJustDigits(value);
			break;
		case FILE_EXISTS:
			validationFile = new File(value.trim());
			validationOK = (value.trim().length() > 0) && (validationFile.exists() && validationFile.isFile());
			break;
		case FILE_EXISTS_IF_NOTNULL:
			validationFile = new File(value.trim());
			validationOK = (value.trim().length() == 0) || (validationFile.exists() && validationFile.isFile());
			break;
		case FOLDER_EXISTS:
			validationFile = new File(value.trim());
			validationOK = (value.trim().length() > 0) && (validationFile.exists() && validationFile.isDirectory());
			break;
		case FOLDER_EXISTS_IF_NOTNULL:
			validationFile = new File(value.trim());
			validationOK = (value.trim().length() == 0) || (validationFile.exists() && validationFile.isDirectory());
			break;
		default:
			SystemUtils.printMessageAndExit("Invalid validtionrule.\nProcessing terminated!", -1);
			break;
		}

		if (!validationOK) {
			JOptionPane.showMessageDialog(null,
					"Field " + name + " failed validation rule " + validationRule.toString(), "Validation rule failed",
					JOptionPane.ERROR_MESSAGE);

		}
		return validationOK;
	}

	public final ArrayList<GUIElement> getGuiGComponents() {
		return guiGComponents;
	}

	public final void setJOptionPaneText(final String optionPaneText) {
		jOptionPaneText = optionPaneText;
	}

	public final void setGuiGeneratorSubmitJButton(final JButton submitJButton) {
		this.submitButton = submitJButton;
	}
}
