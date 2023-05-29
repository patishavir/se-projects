package oz.infra.windowsregistry.test;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.windowsregistry.WindowsRegistryUtils;

public class TestWindowsRegistryUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		try {
			String str1 = WindowsRegistryUtils.readString(WindowsRegistryUtils.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\Atria\\ClearCase\\CurrentVersion\\", "ClearCaseMajorVersion");
			logger.info("str1: " + str1);
			String str2 = WindowsRegistryUtils.readString(WindowsRegistryUtils.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\Atria\\ClearCase\\CurrentVersion\\", "ClearCaseGroupName");
			logger.info("str2: " + str2);

			Map<String, String> map1 = WindowsRegistryUtils.readStringValues(
					WindowsRegistryUtils.HKEY_LOCAL_MACHINE, "SOFTWARE\\Atria\\ClearCase\\2.0\\");
			MapUtils.printMap(map1, Level.INFO);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
