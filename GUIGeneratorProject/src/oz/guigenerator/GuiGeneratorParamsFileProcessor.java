package oz.guigenerator;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Window;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import oz.guigenerator.gui.GuiGeneratorJPanel;
import oz.guigenerator.listeners.GuiGRunButtonActionListener;
import oz.guigenerator.listeners.GuiGSubmitButtonListener;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.color.ColorUtils;
import oz.infra.swing.container.ContainerUtils;
import oz.infra.swing.filechooser.FileChooserUtils;
import oz.infra.swing.jdialog.JDialogUtils;
import oz.infra.system.SystemUtils;
import oz.infra.xml.XMLUtils;

public class GuiGeneratorParamsFileProcessor {
	private enum MethodEnum {
		button, checkbox, combobox, toplevelcontainer, jpanel, link, log, pane, password, radiobutton, radiobuttongroup, runcommanddelimiter, submitbutton, textfield, label, variable, textarea

	}

	private Logger logger = Logger.getLogger(this.getClass().toString());
	private Observer generalGUIObserver;
	private HashMap<String, String> variables = new HashMap<String, String>();
	private List<?> attributes;
	private Element parameterElement;
	private Iterator<?> attributesIterator;
	private GuiGeneratorJPanel guiGeneratorJPanel = null;
	private Window guiGeneratorWindow = null;
	private GuiGSubmitButtonListener guiGSubmitButtonListener = new GuiGSubmitButtonListener();
	private GuiGeneratorDefaultsProviderInterface guiGeneratorDefaultsSupplier = null;
	private ArrayList<JRadioButton> radioButtonsArrayList = new ArrayList<JRadioButton>();
	private String currentRadioButtonGroup = null;
	private String topLevelComtainerTitle = "GUI Generator";
	private String paramsXmlFilePath = "";
	private ArrayList<JComponent> jComponentArrayList = new ArrayList<JComponent>();
	private ArrayList<JPanel> paneArrayList = new ArrayList<JPanel>();
	private ArrayList<String> paneTitleArrayList = new ArrayList<String>();
	private ComponentOrientation componentOrientation = ComponentOrientation.LEFT_TO_RIGHT;
	// common parameters
	private String name = null;
	private String displayName = null;
	private String defaultValue = null;
	private String toolTipText = null;
	private String helpText = null;
	private String value = null;
	private String message = null;
	private ValidationRuleEnum validationRule = null;
	private int size = 0;

	/*
	 * add2JComponentArrayList
	 */
	private void add2JComponentArrayList() {
		JComponent jComponent = guiGeneratorJPanel;
		if (paneArrayList.size() > 1) {
			JTabbedPane jTabbedPane = new JTabbedPane();
			for (int i = 0; i < paneArrayList.size(); i++) {
				jTabbedPane.addTab(paneTitleArrayList.get(i), paneArrayList.get(i));
			}
			jComponent = jTabbedPane;
		}
		if (jComponent != null) {
			jComponent.setBorder(null);
			jComponentArrayList.add(jComponent);
		}
		paneArrayList = new ArrayList<JPanel>();
		paneTitleArrayList = new ArrayList<String>();
	}

	/*
	 * Button processing
	 */
	private void button() {
		logger.finest("Start button processing !");
		String[] params = { "value", "message" };
		int[] manadatotyParams = { 1, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		setGGuiJPanel();
		guiGeneratorJPanel.addButton(substituteVariable(value), substituteVariable(message));
	}

	/*
	 * checkbox processing
	 */
	private void checkbox() {
		String[] params = { "name", "displayName", "defaultValue", "toolTipText", "helpText" };
		int[] manadatotyParams = { 1, 0, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		setGGuiJPanel();
		guiGeneratorJPanel.addCheckBox(name, displayName, defaultValue, toolTipText, helpText);
	}

	/*
	 * combobox processing
	 */
	private void combobox() {
		String[] params = { "name", "displayName", "values", "delimiter", "defaultValue", "editAble",
				"populationParameters", "sort", "toolTipText", "helpText" };
		int[] manadatoryParams = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatoryParams);
		String delimiter = ",";
		if (parametersHashMap.get("delimiter") != null) {
			delimiter = parametersHashMap.get("delimiter");
		}
		boolean editAble = ((parametersHashMap.get("editable") != null)
				&& parametersHashMap.get("editable").equalsIgnoreCase("yes"));
		String populationParameters = parametersHashMap.get("populationparameters");
		String[] comboboxValues = { " " };
		if (populationParameters != null) {
			String[] populationParametersArray = parametersHashMap.get("populationparameters").split("#");
			int numberOfParams = populationParametersArray.length - 2;
			String classString = populationParametersArray[0];
			String methodString = populationParametersArray[1];
			logger.finest("methodString=" + methodString);
			Object resultObject = null;
			if (populationParameters != null) {
				try {
					Class<?> myClassObject = Class.forName(classString);
					logger.finest("myClassObject.toString() = " + myClassObject.toString());
					logger.finest("myClassObject.getClass().getName() = " + myClassObject.getClass().getName());
					Class<?>[] methodParamTypes = new Class[numberOfParams];
					Object[] methodParams = new Object[numberOfParams];
					for (int i = 0; i < numberOfParams; i++) {
						methodParamTypes[i] = String.class;
						methodParams[i] = populationParametersArray[i + 2];
					}
					Method myMethod = myClassObject.getDeclaredMethod(methodString, methodParamTypes);
					resultObject = myMethod.invoke(myClassObject.newInstance(), methodParams);
				} catch (Exception ex) {
					logger.warning("Exception message: " + ex.getMessage());
					logger.warning("classString = " + classString);
					logger.warning("methodString = " + methodString);
					ex.printStackTrace();
					System.exit(-1);
				}
			}
			if (resultObject != null) {
				comboboxValues = (String[]) resultObject;
			}
		} else {
			String valuesString = parametersHashMap.get("values");
			if (valuesString != null) {
				comboboxValues = substituteVariable(valuesString).split(delimiter);
			}
			if (guiGeneratorDefaultsSupplier != null) {
				String[] valuesArray = guiGeneratorDefaultsSupplier.getValues(name);
				if (valuesArray != null) {
					comboboxValues = valuesArray;
				}
			}
		}
		setGGuiJPanel();
		comboboxValues = substituteVariables(comboboxValues);
		String sortOrder = parametersHashMap.get("sort");
		if (sortOrder != null) {
			if (sortOrder.equalsIgnoreCase("ascending")) {
				Arrays.sort(comboboxValues, String.CASE_INSENSITIVE_ORDER);
			} else if (sortOrder.equalsIgnoreCase("descending")) {
				Arrays.sort(comboboxValues, String.CASE_INSENSITIVE_ORDER);
				Collections.reverse(Arrays.asList(comboboxValues));
			}
		}
		guiGeneratorJPanel.addCombobox(name, displayName, substituteVariables(comboboxValues), defaultValue, editAble,
				toolTipText, helpText);
	}

	/*
	 * convertFileSectionMode
	 */
	private String convertFileSectionMode(final String fileSelectionModeString, final Boolean browseButton) {
		if (fileSelectionModeString == null) {
			return String.valueOf(JFileChooser.FILES_AND_DIRECTORIES);
		}
		if (!browseButton) {
			errorExit("FileSelectionMode parameter requires browseButton=yes. processing terminated!");
			return null;
		}
		try {
			return String.valueOf(FileChooserUtils.getFileSelectionMode(fileSelectionModeString));
		} catch (IllegalArgumentException illegalArgumentException) {
			illegalArgumentException.printStackTrace();
			logger.warning(illegalArgumentException.getMessage());
			return null;
		}
	}

	private void errorExit(final String errorMessage) {
		logger.severe(errorMessage);
		System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
	}

	public final Window getGuiGeneratorWindow() {
		return guiGeneratorWindow;
	}

	public final void getParamsXmlDocument(final InputStream paramsXmlInputStream, final Observer generalGUIObserverP,
			final GuiGeneratorDefaultsProviderInterface guiGeneratorDefaultsSupplier, final Component rootComponent) {
		generalGUIObserver = generalGUIObserverP;
		this.guiGeneratorDefaultsSupplier = guiGeneratorDefaultsSupplier;
		guiGeneratorWindow = JDialogUtils.getJDialog((Window) rootComponent);
		Document xmlDoc = XMLUtils.getXMLdocument(paramsXmlInputStream);
		processParametersXMLDocument(xmlDoc, generalGUIObserverP);
		logger.finest(guiGeneratorWindow.toString());
		return;
	}

	/*
	 * readParamsXmlFile
	 */
	public final void getParamsXmlDocument(final String paramsXmlFilePathP, final Observer generalGUIObserverP) {
		paramsXmlFilePath = paramsXmlFilePathP;
		logger.info("Processing " + paramsXmlFilePath);
		generalGUIObserver = generalGUIObserverP;
		Document xmlDoc = XMLUtils.getXMLdocument(paramsXmlFilePath);
		processParametersXMLDocument(xmlDoc, generalGUIObserverP);
		return;
	}

	/*
	 * jpanel processing
	 */
	private void jpanel() {
		add2JComponentArrayList();
		guiGeneratorJPanel = new GuiGeneratorJPanel(guiGSubmitButtonListener);
		paneArrayList.add(guiGeneratorJPanel);
	}

	/*
	 * label processing
	 */
	private void label() {
		String[] params = { "value", "foreGroundColor" };
		int[] manadatotyParams = { 1, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		Color foreGroundColor = Color.BLACK;
		String foreGroundColorString = parametersHashMap.get("foregroundcolor");
		if (foreGroundColorString != null) {
			foreGroundColor = ColorUtils.getColor(foreGroundColorString);
		}
		setGGuiJPanel();
		guiGeneratorJPanel.addLabel(value, foreGroundColor);
	}

	/*
	 * link processing
	 */
	private void link() {
		String[] params = { "name", "displayName", "url", "text", "tooltipText", "helpText" };
		int[] manadatotyParams = { 1, 0, 1, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		String url = parametersHashMap.get("url");
		String text = parametersHashMap.get("text");
		setGGuiJPanel();
		guiGeneratorJPanel.addLink(name, displayName, url, text, toolTipText, helpText);
	}

	/*
	 * log processing
	 */
	private void log() {
		String[] params = { "level" };
		int[] manadatotyParams = { 1 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		String level = parametersHashMap.get("level");
		JulUtils.setLoggingLevel(level.toUpperCase());
	}

	/*
	 * pane processing
	 */
	private void pane() {
		String[] params = { "title" };
		int[] manadatotyParams = { 1 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		guiGeneratorJPanel = new GuiGeneratorJPanel(guiGSubmitButtonListener);
		paneArrayList.add(guiGeneratorJPanel);
		paneTitleArrayList.add(parametersHashMap.get("title"));
	}

	/*
	 * password processing
	 */
	private void password() {
		String[] params = { "name", "displayName", "defaultValue", "tooltiptext", "size", "validationRule" };
		int[] manadatotyParams = { 1, 0, 0, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		setGGuiJPanel();
		guiGeneratorJPanel.addPasswordField(name, displayName, defaultValue, toolTipText, size, validationRule);
	}

	private void performFinalProcessing(final ArrayList<JComponent> jComponentArrayList,
			final String topLevelComtainerTitle) {
		Container cp = null;
		if (guiGeneratorWindow instanceof JFrame) {
			((JFrame) guiGeneratorWindow).setTitle(topLevelComtainerTitle);
			cp = ((JFrame) guiGeneratorWindow).getContentPane();
		} else if (guiGeneratorWindow instanceof JDialog) {
			((JDialog) guiGeneratorWindow).setTitle(topLevelComtainerTitle);
			cp = ((JDialog) guiGeneratorWindow).getContentPane();
		}
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		for (int i = 0; i < jComponentArrayList.size(); i++) {
			JScrollPane jScrollPane = new JScrollPane(jComponentArrayList.get(i),
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			if (guiGeneratorWindow instanceof JFrame) {
				((JFrame) guiGeneratorWindow).getContentPane().add(jScrollPane);
			} else if (guiGeneratorWindow instanceof JDialog) {
				((JDialog) guiGeneratorWindow).getContentPane().add(jScrollPane);
			}
		}
		guiGeneratorWindow.applyComponentOrientation(componentOrientation);
		guiGeneratorWindow.pack();
		ContainerUtils.positionComponentCentrallyInParent((Component) guiGeneratorWindow);
		guiGeneratorWindow.setVisible(true);
	}

	private void processCommonParameters(final HashMap<String, String> parametersHashMap) {
		name = substituteVariable(parametersHashMap.get("name"));
		displayName = substituteVariable(parametersHashMap.get("displayname"));
		if (parametersHashMap.get("displayname") == null) {
			displayName = name;
		}
		toolTipText = substituteVariable(parametersHashMap.get("tooltiptext"));
		helpText = substituteVariable(parametersHashMap.get("helptext"));
		value = substituteVariable(parametersHashMap.get("value"));
		message = substituteVariable(parametersHashMap.get("message"));
		String validationRuleString = parametersHashMap.get("validationrule");
		validationRule = null;
		if (validationRuleString != null) {
			validationRule = ValidationRuleEnum.valueOf(validationRuleString);
		}
		defaultValue = substituteVariable(parametersHashMap.get("defaultvalue"));
		//
		size = 10;
		String sizeString = parametersHashMap.get("size");
		if (sizeString != null) {
			size = Integer.parseInt(sizeString);
		} else {
			if (defaultValue != null) {
				size = 0;
			}
		}
		//
		if (guiGeneratorDefaultsSupplier != null && name != null) {
			String value = guiGeneratorDefaultsSupplier.getDefaultValue(name);
			if (value != null && value.length() > 0) {
				defaultValue = value;
			}
		}
	}

	/*
	 * processElement processing
	 */
	private HashMap<String, String> processElement(final String[] params, final int[] manadatotyParams) {
		HashMap<String, String> parametersHashMap = new HashMap<String, String>();
		if (params.length != manadatotyParams.length) {
			errorExit("Bad array length: " + parameterElement.getName() + ". Processing aborted");
		}
		int[] attributeCounter = new int[params.length];
		if (attributes.size() > params.length) {
			errorExit("Bad xml format. Wrong number of attributes for element: " + parameterElement.getName()
					+ ". Processing aborted");
		} else {
			while (attributesIterator.hasNext()) {
				Attribute attribute = (Attribute) attributesIterator.next();
				String attributeName = attribute.getName();
				String attributeValue = parameterElement.getAttribute(attributeName).getValue();
				logger.finest(attributeName + " = " + attributeValue);
				boolean attributeFound = false;
				for (int i = 0; i < params.length; i++) {
					if (params[i].equalsIgnoreCase(attributeName)) {
						parametersHashMap.put(attributeName.toLowerCase(), attributeValue);
						attributeFound = true;
						attributeCounter[i]++;
						if (attributeCounter[i] > 1) {
							errorExit("Bad xml format. wrong attribute " + attributeName + " value: " + attributeValue
									+ ".  Processing aborted");
						}
						manadatotyParams[i]--;
						break;
					}
				}
				if (!attributeFound) {
					errorExit("Bad xml format. wrong attribute " + attributeName + " value: " + attributeValue
							+ ". Processing aborted");
				}
			}
			for (int i = 0; i < params.length; i++) {
				if (manadatotyParams[i] > 0) {
					errorExit("Bad xml format. Manadatory attribute is missing: " + params[i] + ". Processing aborted");
				}
			}
		}
		processCommonParameters(parametersHashMap);
		return parametersHashMap;
	}

	public final void processParametersXMLDocument(final Document xmlDoc, final Observer generalGUIObserverP) {
		final String notPerformedMessage = "Input parameters processing has not been performed.";
		if (xmlDoc == null) {
			errorExit(paramsXmlFilePath + " is not well-formed. \n" + notPerformedMessage);
		}
		List<Element> children = xmlDoc.getRootElement().getChildren();
		if (children == null) {
			errorExit(paramsXmlFilePath + " XML element not found. \n" + notPerformedMessage);
		}
		Iterator<Element> childrenIterator = children.iterator();
		/*
		 * loop to process all elements
		 */
		while (childrenIterator.hasNext()) {
			parameterElement = childrenIterator.next();
			attributes = parameterElement.getAttributes();
			attributesIterator = attributes.iterator();
			String myMethodStr = parameterElement.getName().toLowerCase();
			MethodEnum methodToRun = MethodEnum.valueOf(myMethodStr);
			logger.finest("myMethodStr=" + myMethodStr + "  " + methodToRun.toString());
			switch (methodToRun) {
			case button:
				button();
				break;
			case checkbox:
				checkbox();
				break;
			case combobox:
				combobox();
				break;
			case toplevelcontainer:
				toplevelcontainer();
				break;
			case jpanel:
				jpanel();
				break;
			case link:
				link();
				break;
			case log:
				log();
				break;
			case pane:
				pane();
				break;
			case textfield:
				textField();
				break;
			case password:
				password();
				break;
			case radiobutton:
				radiobutton();
				break;
			case radiobuttongroup:
				radiobuttongroup();
				break;
			case runcommanddelimiter:
				runcommanddelimiter();
				break;
			case submitbutton:
				submitbutton();
				break;
			case textarea:
				textarea();
				break;
			case label:
				label();
				break;
			case variable:
				variable();
				break;
			default:
				SystemUtils.printMessageAndExit("Method " + myMethodStr + " not found!\nProcessed aborted!", -1);
				break;
			}
		}
		// String myMethodStr = null;
		// try {
		// myMethodStr = parameterElement.getName().toLowerCase();
		// logger.finest("myMethodStr=" + myMethodStr);
		// Method myMethod = this.getClass().getDeclaredMethod(
		// myMethodStr, new Class[0]);
		// myMethod.invoke(this, null);
		// } catch (Exception ex) {
		// logger.warning("Exception message: " + ex.getMessage());
		// logger.warning("myMethodStr=" + myMethodStr);
		// while (attributesIterator.hasNext()) {
		// Attribute catchAttribute = (Attribute) attributesIterator
		// .next();
		// logger.warning(catchAttribute.getName() + "="
		// + catchAttribute.getValue());
		// }
		// ex.printStackTrace();
		// System.exit(-1);
		// }
		// }
		add2JComponentArrayList();
		performFinalProcessing(jComponentArrayList, topLevelComtainerTitle);
	}

	/*
	 * radioButton processing
	 */
	private void radiobutton() {
		logger.finest("Starting radiobutton method");
		String[] params = { "name", "displayName", "group", "selected", "toolTipText" };
		int[] manadatotyParams = { 1, 0, 1, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		String selected = parametersHashMap.get("selected");
		String group = parametersHashMap.get("group");
		if (!group.equalsIgnoreCase(currentRadioButtonGroup)) {
			currentRadioButtonGroup = group;
			radioButtonsArrayList = new ArrayList<JRadioButton>();
		}
		JRadioButton myRadioButton = new JRadioButton(displayName);
		// myRadioButton.setText("test");
		myRadioButton.setName(name);
		myRadioButton.setToolTipText(toolTipText);
		myRadioButton.setActionCommand(currentRadioButtonGroup);
		myRadioButton.setSelected(selected != null && selected.equalsIgnoreCase("yes"));
		radioButtonsArrayList.add(myRadioButton);
	}

	/*
	 * radiobuttongroup processing
	 */
	private void radiobuttongroup() {
		logger.finest("Starting radiobuttongroup method");
		String[] params = { "name", "displayName", "rows", "toolTipText" };
		int[] manadatotyParams = { 1, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		int rows = 0;
		if (parametersHashMap.get("rows") != null) {
			rows = Integer.parseInt(parametersHashMap.get("rows"));
		}
		if (!name.equalsIgnoreCase(currentRadioButtonGroup)) {
			errorExit("Bad parameter. Processing aborted");
		}
		radioButtonsArrayList.trimToSize();
		setGGuiJPanel();
		guiGeneratorJPanel.addRadioButtonGroup(name, displayName, radioButtonsArrayList, rows, toolTipText);
	}

	/*
	 * runCommandDelimiter processing
	 */
	private void runcommanddelimiter() {
		String[] params = { "value" };
		int[] manadatotyParams = { 1 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		GuiGRunButtonActionListener.setRunCommandDelimiter(parametersHashMap.get("value"));
	}

	private void setGGuiJPanel() {
		if (guiGeneratorJPanel == null) {
			guiGeneratorJPanel = new GuiGeneratorJPanel(guiGSubmitButtonListener);
			paneArrayList.add(guiGeneratorJPanel);
			paneTitleArrayList.add(null);
		}
	}

	public final void setGuiGeneratorWindow(final Window guiGeneratorWindow) {
		this.guiGeneratorWindow = guiGeneratorWindow;
	}

	/*
	 * Submit Button processing
	 */
	private void submitbutton() {
		logger.finest("Start submitButton processing !");
		String[] params = { "value", "message", "registerKeyboardAction" };
		int[] manadatotyParams = { 1, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		String submitButtonText = "Submit";
		if (value != null) {
			submitButtonText = value;
		}
		boolean registerKeyboardAction = true;
		if (parametersHashMap.get("registerkeyboardaction") != null) {
			String registerKeyboardActionString = parametersHashMap.get("registerkeyboardaction");
			registerKeyboardAction = registerKeyboardActionString.equalsIgnoreCase("yes");
		}
		logger.finest("submitButtonText: " + submitButtonText + " submitButtonMessage: " + message);
		setGGuiJPanel();
		guiGeneratorJPanel.addSubmitButton(substituteVariable(submitButtonText), message, registerKeyboardAction,
				generalGUIObserver);
	}

	/*
	 * substituteVariables
	 */
	private String substituteVariable(final String inputString) {
		String outputString = StringUtils.substituteVariables(inputString, variables);
		logger.finest(outputString);
		return StringUtils.substituteEnvironmentVariables(outputString);
	}

	/*
	 * substituteVariables for String array (used for comboBox)
	 */
	private String[] substituteVariables(final String[] inputStrings) {
		String[] outputStrings = new String[inputStrings.length];
		for (int i = 0; i < inputStrings.length; i++) {
			outputStrings[i] = substituteVariable(inputStrings[i]);
		}
		return outputStrings;
	}

	/*
	 * textarea processing
	 */
	// TODO add validation rules: numeric, notNull
	private void textarea() {
		String[] params = { "name", "displayName", "defaultValue", "delimiter", "wdith", "height", "tooltipText",
				"validationRule", "gridWidth" };
		int[] manadatotyParams = { 1, 0, 0, 0, 0, 0, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		int width = 120;
		String widthString = parametersHashMap.get("width");
		if (widthString != null) {
			width = Integer.parseInt(widthString);
		}
		int height = 150;
		String heightString = parametersHashMap.get("height");
		if (heightString != null) {
			height = Integer.parseInt(heightString);
		}
		int gridWidth = 1;
		String gridWidthString = parametersHashMap.get("gridwidth");
		if (gridWidthString != null) {
			gridWidth = Integer.parseInt(gridWidthString);
		}
		logger.finest("tooltiptext: " + toolTipText);
		setGGuiJPanel();
		guiGeneratorJPanel.addTextArea(name, displayName, defaultValue, toolTipText, width, height, gridWidth,
				validationRule);
	}

	/*
	 * param processing
	 */
	// TODO add validation rules: numeric, notNull
	private void textField() {
		String[] params = { "name", "displayName", "defaultValue", "browseButton", "fileSelectionMode", "runButton",
				"dateButton", "dateFormat", "tooltipText", "helpText", "visible", "size", "validationRule" };
		int[] manadatotyParams = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		boolean browseButton = (parametersHashMap.get("browsebutton") != null)
				&& parametersHashMap.get("browsebutton").equalsIgnoreCase("yes");
		String fileSelectionModeCodeString = convertFileSectionMode(parametersHashMap.get("fileselectionmode"),
				browseButton);
		boolean runButton = (parametersHashMap.get("runbutton") != null)
				&& parametersHashMap.get("runbutton").equalsIgnoreCase("yes");
		boolean dateButton = (parametersHashMap.get("datebutton") != null)
				&& parametersHashMap.get("datebutton").equalsIgnoreCase("yes");
		String dateFormat = "";
		if (parametersHashMap.get("dateformat") != null) {
			dateFormat = parametersHashMap.get("dateformat");
		}
		boolean visible = (parametersHashMap.get("visible") == null)
				|| parametersHashMap.get("visible").equalsIgnoreCase("yes");
		setGGuiJPanel();
		guiGeneratorJPanel.addTextField(name, displayName, defaultValue, browseButton, fileSelectionModeCodeString,
				runButton, dateButton, dateFormat, toolTipText, helpText, visible, size, validationRule);
	}

	/*
	 * JFrametitle processing
	 */
	private void toplevelcontainer() {
		String[] params = { "title", "orientation" };
		int[] manadatotyParams = { 0, 0 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		String title = parametersHashMap.get("title");
		if (title != null) {
			topLevelComtainerTitle = substituteVariable(title);
		}
		String orientation = parametersHashMap.get("orientation");
		if (orientation != null) {
			if (orientation.equalsIgnoreCase("RIGHT_TO_LEFT")) {
				componentOrientation = ComponentOrientation.RIGHT_TO_LEFT;
			} else if (orientation.equalsIgnoreCase("LEFT_TO_RIGHT")) {
				componentOrientation = ComponentOrientation.LEFT_TO_RIGHT;
			} else {
				errorExit("Bad xml format. wrong value for orientation: " + orientation + ".  Processing aborted");
			}
		}
	}

	/*
	 * variable processing
	 */
	private void variable() {
		logger.finest("Start variable processing !");
		String[] params = { "key", "value" };
		int[] manadatotyParams = { 1, 1 };
		HashMap<String, String> parametersHashMap = processElement(params, manadatotyParams);
		variables.put(parametersHashMap.get("key"), parametersHashMap.get("value"));
	}
}
