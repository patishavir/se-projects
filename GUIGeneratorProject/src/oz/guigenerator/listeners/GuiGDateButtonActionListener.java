package oz.guigenerator.listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import oz.infra.swing.jdialog.JDialogUtils;

import com.toedter.calendar.JCalendar;

public class GuiGDateButtonActionListener extends WindowAdapter implements ActionListener {
	// private JFrame jcalendarFrame = new JFrame("JCalendar");
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private JCalendar jCalendar = new JCalendar();
	private JTextField paramValueJTextField;
	private String dateFormat;

	public GuiGDateButtonActionListener(final JTextField paramValueJTextField,
			final String dateFormat) {
		this.paramValueJTextField = paramValueJTextField;
		this.dateFormat = dateFormat;
	}

	public final void actionPerformed(final ActionEvent e) {
		logger.finest(e.toString());
		int paramValueJTextFieldWidth = paramValueJTextField.getWidth();
		Component root = SwingUtilities.getRoot((JButton) e.getSource());
		JDialog jcalendarJDialog = JDialogUtils.getJDialog((Window) root);
		jcalendarJDialog.getContentPane().add(jCalendar);
		jcalendarJDialog.setResizable(false);
		jcalendarJDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		jcalendarJDialog.pack();
		Point paramJTextFieldLocation = new Point(0, 0);
		SwingUtilities.convertPointToScreen(paramJTextFieldLocation, paramValueJTextField);
		logger.finest("absoluteX: " + String.valueOf(paramJTextFieldLocation.x) + " absoluteY: "
				+ String.valueOf(paramJTextFieldLocation.y));
		jcalendarJDialog.setLocation(paramJTextFieldLocation.x + paramValueJTextFieldWidth + 20,
				paramJTextFieldLocation.y);
		jcalendarJDialog.setVisible(true);
		jcalendarJDialog.addWindowListener(this);
	}

	public void windowClosed(WindowEvent e) {
		Calendar calendar = jCalendar.getCalendar();
		if (dateFormat.equals("")) {
			dateFormat = "dd/MM/yyyy";
		}
		Locale enLocale = new Locale("en");
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, enLocale);
		paramValueJTextField.setText(dateFormatter.format(calendar.getTime()));
	}
}