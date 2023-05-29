package oz.infra.swing.lookandfeel;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.operaion.Outcome;
import oz.infra.varargs.VarArgsUtils;

public class LookAndFeelUtils {
	/*
	 * Windows Classic=com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
	 * Nimbus=javax.swing.plaf.nimbus.NimbusLookAndFeel
	 * Metal=javax.swing.plaf.metal.MetalLookAndFeel
	 * Windows=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
	 * CDE/Motif=com.sun.java.swing.plaf.motif.MotifLookAndFeel
	 */
	public static final String Metal_LAF = "Nimbus";
	public static final String NIMBUS_LAF = "Nimbus";
	public static final String WINDOWS_LAF = "Windows";
	private static Logger logger = JulUtils.getLogger();

	public static LookAndFeelInfo[] getLafInfoList(final Boolean... verbose) {
		Boolean isVerbose = VarArgsUtils.getMyArg(Boolean.FALSE, verbose);
		LookAndFeelInfo[] lafsInfo = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lafInfo : lafsInfo) {
			if (isVerbose) {
				logger.info("name: " + lafInfo.getName() + "   classname: " + lafInfo.getClassName());
			}
		}
		return lafsInfo;
	}

	public static Map<String, String> getLafInfoMap(final Level... levels) {
		LookAndFeelInfo[] lafsInfo = UIManager.getInstalledLookAndFeels();
		Map<String, String> lafInfoMap = new HashMap<String, String>();
		for (LookAndFeelInfo lafInfo : lafsInfo) {
			lafInfoMap.put(lafInfo.getName(), lafInfo.getClassName());
		}
		MapUtils.printMap(lafInfoMap, levels);
		return lafInfoMap;
	}

	public static Outcome setLoolAndFeel(final String lafName, final String... lafClassNames) {
		Outcome outcome = Outcome.SUCCESS;
		String lafClassName = VarArgsUtils.getMyArg(null, lafClassNames);
		if (lafClassName == null) {
			Map<String, String> lafInfoMap = getLafInfoMap();
			lafClassName = lafInfoMap.get(lafName);
		}
		if (lafClassName == null) {
			logger.warning("Class name not found for LAF: " + lafName);
			outcome = Outcome.FAILURE;
		} else {
			try {
				UIManager.setLookAndFeel(lafClassName);
				logger.info("look and feel set to " + lafName + " class name: " + lafClassName);
			} catch (Exception ex) {
				logger.warning("failed to set look and feel to " + lafName + " class name: " + lafClassName);
				outcome = Outcome.FAILURE;
			}
		}
		return outcome;
	}

	public static Outcome setLoolAndFeelByClassName(final String lafClassName) {
		Outcome outcome = Outcome.SUCCESS;
		try {
			UIManager.setLookAndFeel(lafClassName);
			logger.info("look and feel class set to " + lafClassName);
		} catch (Exception ex) {
			logger.warning("failed to set look and feel class to " + lafClassName);
			ExceptionUtils.printMessageAndStackTrace(ex);
			outcome = Outcome.FAILURE;
		}
		return outcome;
	}
}
