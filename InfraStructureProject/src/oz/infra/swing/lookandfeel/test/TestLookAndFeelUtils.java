package oz.infra.swing.lookandfeel.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.lookandfeel.LookAndFeelUtils;

public class TestLookAndFeelUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		testGetLafInfoList();
		testGetLafInfoMap();
		testSetLoolAndFeel(LookAndFeelUtils.NIMBUS_LAF);
		testSetLoolAndFeel(LookAndFeelUtils.NIMBUS_LAF, "baba");

	}

	private static void testGetLafInfoList() {
		LookAndFeelUtils.getLafInfoList(Boolean.TRUE);
	}

	private static void testGetLafInfoMap() {
		LookAndFeelUtils.getLafInfoMap(Level.INFO);
	}

	private static void testSetLoolAndFeel(final String lafName, final String... lafClassNames) {
		LookAndFeelUtils.setLoolAndFeel(lafName, lafClassNames);
	}
}