package oz.temp.swing;

import java.util.Locale;

import javax.swing.JTextArea;

public class JTextAreaHeb {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		jTextAreaHeb();
	}

	private static void jTextAreaHeb() {
		JTextArea jTextArea = new JTextArea();
		jTextArea.setLocale(new Locale("iw_IL"));
		jTextArea.setLocale(new Locale("ZZiw_IL"));
	}

}
