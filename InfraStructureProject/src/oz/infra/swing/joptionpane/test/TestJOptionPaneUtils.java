package oz.infra.swing.joptionpane.test;

import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.swing.joptionpane.JOptionPaneUtils;

public class TestJOptionPaneUtils {

	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// testShowMessageDialog();
		// testGetMultipleInputs();
		testShowListMessageDialog();
	}

	private static void testGetMultipleInputs() {
		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("x:"));
		myPanel.add(xField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("y:"));
		myPanel.add(yField);
		String title = "BBBBBZZZZZZZZZZZZZZZZZZ";
		int result = JOptionPaneUtils.getMultipleInputs(myPanel, title);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("x value: " + xField.getText());
			System.out.println("y value: " + yField.getText());
		}

	}

	private static void testShowMessageDialog() {
		// TODO Auto-generated method stub
		// String message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:center>׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!<br>׳³ג€�׳³ֲ§׳³ֲ© ׳³ֲ¢׳³ן¿½
		// ׳³ן¿½׳³ֲ§׳³ֲ© ׳³ג€÷׳³ן¿½׳³ֲ©׳³ג€�׳³ג€¢ ׳³ג€÷׳³ג€�׳³ג„¢
		// ׳³ן¿½׳³ג€�׳³ֳ—׳³ג€”׳³ג„¢׳³ן¿½ ׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€”׳³ג€�׳³ֲ©</div></body></html>";
		// message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:center>׳³ֲ ׳³ן¿½ ׳³ג€�׳³ג€“׳³ן¿½ ׳³ן¿½׳³ֲ¡׳³ג‚×׳³ֲ¨
		// ׳³ֲ ׳³ג€˜׳³ג€�׳³ֲ§</div></body></html>";
		// message =
		// "<html><body DIR=\"RTL\"><div align=right>׳³ֲ ׳³ן¿½ ׳³ג€�׳³ג€“׳³ן¿½
		// ׳³ן¿½׳³ֲ¡׳³ג‚×׳³ֲ¨ ׳³ֲ ׳³ג€˜׳³ג€�׳³ֲ§:</div></body></html>";
		// message =
		// "<html><body DIR=\"RTL\"><div align=\"right\">׳³ֲ ׳³ן¿½
		// ׳³ג€�׳³ג€“׳³ן¿½ ׳³ן¿½׳³ֲ¡׳³ג‚×׳³ֲ¨
		// ׳³ֲ ׳³ג€˜׳³ג€�׳³ֲ§:</div></body></html>";
		// message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:center>׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!<br>׳³ג€�׳³ֲ§׳³ֲ© ׳³ֲ¢׳³ן¿½
		// ׳³ן¿½׳³ֲ§׳³ֲ© ׳³ג€÷׳³ן¿½׳³ֲ©׳³ג€�׳³ג€¢ ׳³ג€÷׳³ג€�׳³ג„¢
		// ׳³ן¿½׳³ג€�׳³ֳ—׳³ג€”׳³ג„¢׳³ן¿½ ׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€”׳³ג€�׳³ֲ©</div></body></html>";
		// message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:right>׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!<br>׳³ג€�׳³ֲ§׳³ֲ© ׳³ֲ¢׳³ן¿½
		// ׳³ן¿½׳³ֲ§׳³ֲ© ׳³ג€÷׳³ן¿½׳³ֲ©׳³ג€�׳³ג€¢ ׳³ג€÷׳³ג€�׳³ג„¢
		// ׳³ן¿½׳³ג€�׳³ֳ—׳³ג€”׳³ג„¢׳³ן¿½ ׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€”׳³ג€�׳³ֲ©</div></body></html>";
		// message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:right>׳³ֲ ׳³ן¿½ ׳³ג€�׳³ג€“׳³ן¿½ ׳³ן¿½׳³ֲ¡׳³ג‚×׳³ֲ¨
		// ׳³ֲ ׳³ג€˜׳³ג€�׳³ֲ§</div></body></html>";
		// message =
		// "<html><body dir=\"rtl\"><div style=color:black
		// text-align:right>׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!<BR>׳³ן¿½׳³ֲ ׳³ן¿½ ׳³ֲ§׳³ֲ¨׳³ן¿½
		// ׳³ן¿½׳³ֲ ׳³ֲ¡׳³ג„¢׳³ג„¢׳³ֲ ׳³ג„¢׳³ֳ—</div></body></html>";
		// message = "׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§ ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!
		// ׳³ן¿½׳³ֲ ׳³ן¿½ ׳³ֲ§׳³ֲ¨׳³ן¿½ ׳³ן¿½׳³ֲ ׳³ֲ¡׳³ג„¢׳³ג„¢׳³ֲ ׳³ג„¢׳³ֳ—";
		// message = "׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§ ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!
		// ׳³ג€�׳³ֲ§׳³ֲ© ׳³ֲ¢׳³ן¿½ ׳³ן¿½׳³ֲ§׳³ֲ© ׳³ג€÷׳³ן¿½׳³ֲ©׳³ג€�׳³ג€¢
		// ׳³ג€÷׳³ג€�׳³ג„¢ ׳³ן¿½׳³ג€�׳³ֳ—׳³ג€”׳³ג„¢׳³ן¿½ ׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€”׳³ג€�׳³ֲ©";
		// message =
		// "<html><body><div style=\"float: right;\">׳³ג€�׳³ן¿½׳³ֲ©׳³ג€”׳³ֲ§
		// ׳³ג€�׳³ֲ¡׳³ֳ—׳³ג„¢׳³ג„¢׳³ן¿½!<BR>׳³ן¿½׳³ֲ ׳³ן¿½ ׳³ֲ§׳³ֲ¨׳³ן¿½
		// ׳³ן¿½׳³ֲ ׳³ֲ¡׳³ג„¢׳³ג„¢׳³ֲ ׳³ג„¢׳³ֳ—</div></body></html>";
		// JLabel message = new JLabel("sss\nyyy");
		// message.setHorizontalAlignment(JLabel.CENTER);
		// message.setHorizontalAlignment(JLabel.RIGHT);
		// int r = Label.RIGHT;
		// ComponentOrientation componentOrientation =
		// ComponentOrientation.getOrientation(Locale
		// .getDefault());
		// message.setComponentOrientation(componentOrientation);
		// JLabel message = new
		// JLabel("<html ><body><div>First Line<br>Second
		// Line</div></body></html>");
		// UIManager.put("OptionPane.messageFont",
		// new FontUIResource(new Font("Arial", Font.BOLD, 16)));
		// message.setHorizontalAlignment(JLabel.RIGHT);
		// JTextArea message = new JTextArea();
		// message.setText("line1\nline2");
		// message.setEditable(false);
		// message.setBackground(null);
		// message.setFont(FontUtils.ARIAL_BOLD_14);

		// JTextPane message = new JTextPane();
		// message.setText("line1\nline2");
		// message.setEditable(false);
		// message.setBackground(null);
		// message.setFont(FontUtils.ARIAL_BOLD_16);
		// message.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//
		// SimpleAttributeSet attribs = new SimpleAttributeSet();
		// StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
		// message.setParagraphAttributes(attribs, true);

		// DefaultStyledDocument doc;
		// JTextPane message;
		// StyleContext sc;
		//
		// sc = new StyleContext();
		// doc = new DefaultStyledDocument(sc);
		// message = new JTextPane(doc);
		// message.setPreferredSize(new Dimension(200, 80));
		// Style main = doc.addStyle("main", null);
		// StyleConstants.setAlignment(main, StyleConstants.ALIGN_RIGHT);
		// StyleConstants.setFontFamily(main, "QuickType Mono");
		// message.setText("line1\nline2");

		JTextPane message = new JTextPane();
		message.setContentType("text/html");
		String htmlText = "<html><head></head><body>" + "<div style='text-align: center;background-color:yellow;'>"
				+ "<code style='color:blue;'>" + "<font size='8' face='arial' color='red'>   "
				+ "Hello   SpacedWorld   !!!" + "<br> Hello baboon" + "</code>" + "</div>"
				+ "<div style='text-align: right;background-color:black;'>"
				+ "<font size='6' face='arial' color='white'>   " + "Hello   SpacedWorld6   !!!" + "<br> Hello baboon6"
				+ "</font>" + "</div>" + "</body></html>";
		message.setText(htmlText);
		message.setEditable(false);
		message.setBackground(null);

		String userInput = JOptionPaneUtils.showInputDialog(message);
		logger.info("userInput: " + userInput);
		JOptionPaneUtils.showMessageDialog(null, message, "tata title", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void testShowListMessageDialog() {
		// TODO Auto-generated method stub
		String[] items = generateArray(40);
		// showListMessageDialog(data);
		JOptionPaneUtils.showListMessageDialog(items, "My big big small small title", 15);
		JOptionPaneUtils.showListMessageDialog(items, "My big big small small title");
	}

	private static String[] generateArray(final int length) {
		String[] array = new String[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = NumberUtils.format(i + 1, NumberUtils.FORMATTER_0000000000);
		}
		return array;
	}
}
