package oz.jdir.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.logging.Logger;

import oz.jdir.gui.DateJDialog;

public class DateJDialogActionListener implements ActionListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private DateJDialog dateJDialog;

	public DateJDialogActionListener(final DateJDialog dateJDialogP) {
		this.dateJDialog = dateJDialogP;
	}

	public final void actionPerformed(final ActionEvent e) {
		dateJDialog.setVisible(false);
		int myYear = dateJDialog.getYearJCombo().getSelectedIndex();
		int myMonth = dateJDialog.getMonthJCombo().getSelectedIndex();
		int myDay = dateJDialog.getDayJCombo().getSelectedIndex();
		int myHours = dateJDialog.getHourJCombo().getSelectedIndex();
		int myminutes = dateJDialog.getMinuteJCombo().getSelectedIndex();
		int mySeconds = dateJDialog.getSecondsJCombo().getSelectedIndex();
		String myYearString = dateJDialog.getYears()[myYear];
		String myMonthString = dateJDialog.getMonthes()[myMonth];
		String myDayString = dateJDialog.getDays()[myDay];
		String myHoursString = dateJDialog.getHours()[myHours];
		String myminutesString = dateJDialog.getMinsec()[myminutes];
		String mySecondsString = dateJDialog.getMinsec()[mySeconds];
		Calendar lastModifiedCalendar = Calendar.getInstance();
		lastModifiedCalendar.set(
				Integer.parseInt(myYearString),
				Integer.parseInt(myMonthString) - 1, // month is 0 based
				Integer.parseInt(myDayString), Integer.parseInt(myHoursString),
				Integer.parseInt(myminutesString), Integer.parseInt(mySecondsString));
		dateJDialog.getDirPanel().getJdirInfo().setLastModifiedCalendar(lastModifiedCalendar);
	}
}