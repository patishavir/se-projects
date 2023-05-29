package oz.temp.lookandfeel;

import java.util.logging.Logger;

import javax.swing.UIManager;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.lookandfeel.LookAndFeelUtils;

public class LookAndFeelMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getLafList();
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void getLafList() {
		LookAndFeelUtils.getLafInfoList(Boolean.TRUE);
	}
}
