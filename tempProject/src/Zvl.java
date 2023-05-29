import javax.swing.LookAndFeel;
import javax.swing.UIManager;

public class Zvl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Properties props = new Properties();
		// String version = "ccccccccccc";
		// String version = null;
		// props.setProperty("version", version);
		// System.out.println(props.getProperty("version"));
		// File file= new File (".");
		// file.lastModified();
		System.out.println(method1());
	}

	private static void lookAndFeel() {
		LookAndFeel lf = UIManager.getLookAndFeel();
	}

	private static String method1() {
		String xxx = "yyy" + "zzz";
		return xxx;
	}
}
