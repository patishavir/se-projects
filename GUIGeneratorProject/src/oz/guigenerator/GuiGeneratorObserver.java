package oz.guigenerator;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class GuiGeneratorObserver implements Observer {
	private String filePath = null;
	private static Logger logger = JulUtils.getLogger();

	GuiGeneratorObserver(final String filePath) {
		this.filePath = filePath;
	}

	@Override
	public final void update(final Observable observable, final Object parametersHashTableObj) {
		StringBuffer guiGeneratorStringBuffer = new StringBuffer();
		Hashtable<String, String> parametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		for (Enumeration<String> enumeration = parametersHashTable.keys(); enumeration
				.hasMoreElements();) {
			String key = enumeration.nextElement();
			String value = parametersHashTable.get(key);
			guiGeneratorStringBuffer.append(GuiGeneratorParameters.getSetVariableCommand() + " "
					+ key + "=" + value + "\n");
		}
		String generalGUIString = guiGeneratorStringBuffer.toString();
		logger.fine(generalGUIString);
		try {
			FileUtils.writeFile(new File(filePath), generalGUIString);
			logger.finer("Parameters have been successfully written to " + filePath);
			logger.finer(generalGUIString);
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());
			System.exit(-1);
		}
	}

}
