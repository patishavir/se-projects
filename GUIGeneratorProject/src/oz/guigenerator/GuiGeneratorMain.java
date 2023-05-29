package oz.guigenerator;

import java.awt.Component;
import java.io.File;
import java.io.InputStream;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JFrame;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.messages.OzMessages;
import oz.infra.system.SystemUtils;

public final class GuiGeneratorMain {
	private GuiGeneratorMain() {
	}

	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		if (args.length < 2) {
			SystemUtils.printMessageAndExit("Two parameters should be specified." + OzMessages.PROCCESSING_TERMINATED,
					OzConstants.EXIT_STATUS_ABNORMAL);
		}
		logger.info(SystemUtils.getRunInfo());
		String paramsXmlFileName = args[0];
		String filePath = args[1];
		GuiGeneratorObserver guiGeneratorObserver = new GuiGeneratorObserver(filePath);
		File paramsXmlFile = new File(paramsXmlFileName);
		if (!paramsXmlFile.exists()) {
			logger.warning("Parameter file " + paramsXmlFile.getAbsolutePath() + " not found! processing aborted!");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		JFrame jFrame = new JFrame();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor = new GuiGeneratorParamsFileProcessor();
		guiGeneratorParamsFileProcessor.setGuiGeneratorWindow(jFrame);
		guiGeneratorParamsFileProcessor.getParamsXmlDocument(paramsXmlFileName, guiGeneratorObserver);
	}

	public static GuiGeneratorParamsFileProcessor showGui(
			final GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor, final String xmlFilePath,
			final Observer observer, final Component rootComponent) {
		return showGui(guiGeneratorParamsFileProcessor, xmlFilePath, observer, null, rootComponent);
	}

	public static GuiGeneratorParamsFileProcessor showGui(
			final GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor, final String xmlFilePath,
			final Observer observer, final GuiGeneratorDefaultsProviderInterface guiGeneratorDefaultsSupplier,
			final Component rootComponent) {
		GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor2Return = guiGeneratorParamsFileProcessor;
		if (guiGeneratorParamsFileProcessor != null) {
			guiGeneratorParamsFileProcessor.getGuiGeneratorWindow().setVisible(true);
		} else {
			InputStream xmlInputStream = new GuiGeneratorMain().getClass().getResourceAsStream(xmlFilePath);
			if (xmlInputStream == null) {
				logger.warning("load of resource " + xmlFilePath + " failed.");
			}
			guiGeneratorParamsFileProcessor2Return = new GuiGeneratorParamsFileProcessor();
			guiGeneratorParamsFileProcessor2Return.getParamsXmlDocument(xmlInputStream, observer,
					guiGeneratorDefaultsSupplier, rootComponent);
		}
		return guiGeneratorParamsFileProcessor2Return;
	}

}