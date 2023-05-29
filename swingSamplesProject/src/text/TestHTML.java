package text;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class TestHTML {

	public static void main(String[] args) {
		JTextPane editor = new JTextPane();
		editor.setContentType("text/html");
		String htmlText = "<html><head></head><body>" + "<div style='text-align: center;'>"
				+ "<code style='color:blue;'>   " + "Hello   SpacedWorld   !!!"
				+ "<br> hello baboon" + "</code>" + "</div>" + "</body></html>";
		editor.setText(htmlText);
		editor.setEditable(false);

		JFrame f = new JFrame("HTML Editor");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(editor);
		f.pack();
		f.setVisible(true);
	}
}