package oz.jdir.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import oz.jdir.gui.listeners.DateJDialogActionListener;

/*
 * class DateJDialog
 */
public class DateJDialog extends JDialog {
	private static final Font headerFont = new Font("Arial", Font.BOLD, 14);
	private static final Font labelFont = new Font("Arial", Font.BOLD, 12);
	private DirPanel dirPanel;
	private static final String[] years = new String[9];
	private static final String[] monthes = new String[12];
	private static final String[] days = new String[31];
	private static final String[] hours = new String[24];
	private static final String[] minsec = new String[60];
	private JComboBox yearJCombo;
	private JComboBox monthJCombo;
	private JComboBox dayJCombo;
	private JComboBox hourJCombo;
	private JComboBox minuteJCombo;
	private JComboBox secondsJCombo;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private DateJDialogActionListener dateJDialogActionListener;

	public DateJDialog(final DirPanel dirPanelP) {
		logger.finest("Constructing DateJDialog");
		this.dirPanel = dirPanelP;
		setModal(true);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date date = cal.getTime();
		int currentYear = Integer.parseInt(formatter.format(date));
		formatter = new SimpleDateFormat("MM");
		int currentMonth = Integer.parseInt(formatter.format(date));
		formatter = new SimpleDateFormat("dd");
		int currentDay = Integer.parseInt(formatter.format(date));
		formatter = new SimpleDateFormat("HH");
		int currentHour = Integer.parseInt(formatter.format(date));
		formatter = new SimpleDateFormat("mm");
		int currentMinute = Integer.parseInt(formatter.format(date));
		formatter = new SimpleDateFormat("ss");
		int currentSeconds = Integer.parseInt(formatter.format(date));
		int yearIndex = 3;
		for (int i = 0; i < 8; i++)
			years[i] = String.valueOf(currentYear - yearIndex + i);
		for (int i = 0; i < 60; i++) {
			minsec[i] = String.valueOf(i);
			if (minsec[i].length() == 1)
				minsec[i] = "0" + minsec[i];
			if (i < 12) {
				monthes[i] = String.valueOf(i + 1);
				if (monthes[i].length() == 1)
					monthes[i] = "0" + monthes[i];
			}
			if (i < 24)
				hours[i] = minsec[i];
			if (i < 31) {
				days[i] = String.valueOf(i + 1);
				if (days[i].length() == 1)
					days[i] = "0" + days[i];
			}
		}
		yearJCombo = new JComboBox(years);
		monthJCombo = new JComboBox(monthes);
		dayJCombo = new JComboBox(days);
		hourJCombo = new JComboBox(hours);
		minuteJCombo = new JComboBox(minsec);
		secondsJCombo = new JComboBox(minsec);
		yearJCombo.setSelectedIndex(yearIndex);
		monthJCombo.setSelectedIndex(currentMonth - 1);
		dayJCombo.setSelectedIndex(currentDay - 1);
		hourJCombo.setSelectedIndex(currentHour);
		minuteJCombo.setSelectedIndex(currentMinute);
		secondsJCombo.setSelectedIndex(currentSeconds);
		yearJCombo.setFont(labelFont);
		monthJCombo.setFont(labelFont);
		dayJCombo.setFont(labelFont);
		hourJCombo.setFont(labelFont);
		minuteJCombo.setFont(labelFont);
		secondsJCombo.setFont(labelFont);
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(8, 2));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		cp.setLayout(gridbag);
		JLabel headerLabel = new JLabel("Select date and time");
		headerLabel.setFont(headerFont);
		headerLabel.setForeground(Color.blue);
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setFont(labelFont);
		yearLabel.setForeground(Color.blue);
		JLabel monthLabel = new JLabel("Month:");
		monthLabel.setFont(labelFont);
		monthLabel.setForeground(Color.blue);
		JLabel dayLabel = new JLabel("Day:");
		dayLabel.setFont(labelFont);
		dayLabel.setForeground(Color.blue);
		JLabel hourLabel = new JLabel("Hour:");
		hourLabel.setFont(labelFont);
		hourLabel.setForeground(Color.blue);
		JLabel minuteLabel = new JLabel("Minute:");
		minuteLabel.setFont(labelFont);
		minuteLabel.setForeground(Color.blue);
		JLabel secondLabel = new JLabel("Second:");
		secondLabel.setFont(labelFont);
		secondLabel.setForeground(Color.blue);
		JButton procButton = new JButton("Proceed");
		procButton.setFont(headerFont);
		procButton.setForeground(Color.blue);
		int y = 0;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.weighty = 0.3;
		c.insets = new Insets(0, 6, 10, 6);
		gridbag.setConstraints(headerLabel, c);
		cp.add(headerLabel);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		c.insets = new Insets(1, 1, 1, 1);
		gridbag.setConstraints(yearLabel, c);
		cp.add(yearLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(yearJCombo, c);
		cp.add(yearJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(monthLabel, c);
		cp.add(monthLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(monthJCombo, c);
		cp.add(monthJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(dayLabel, c);
		cp.add(dayLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(dayJCombo, c);
		cp.add(dayJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		c.insets = new Insets(12, 1, 1, 1);
		gridbag.setConstraints(hourLabel, c);
		cp.add(hourLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(hourJCombo, c);
		cp.add(hourJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		c.insets = new Insets(1, 1, 1, 1);
		gridbag.setConstraints(minuteLabel, c);
		cp.add(minuteLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(minuteJCombo, c);
		cp.add(minuteJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 0.3;
		gridbag.setConstraints(secondLabel, c);
		cp.add(secondLabel);
		c.gridx = 1;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 0.3;
		c.weighty = 0.3;
		gridbag.setConstraints(secondsJCombo, c);
		cp.add(secondsJCombo);
		c.gridx = 0;
		c.gridy = ++y;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.weighty = 0.3;
		c.insets = new Insets(12, 20, 10, 20);
		c.anchor = GridBagConstraints.SOUTH;
		gridbag.setConstraints(procButton, c);
		cp.add(procButton);
		this.pack();
		int i = dirPanel.getLocation().x + (dirPanel.getSize().width - getSize().width) / 2;
		int j = dirPanel.getLocation().y + (dirPanel.getSize().height - getSize().height) / 2;
		setLocation(i, j);
		dateJDialogActionListener = new DateJDialogActionListener(this);
		procButton.addActionListener(dateJDialogActionListener);
		return;
	}

	/**
	 * @return
	 */
	public JComboBox getDayJCombo() {
		return dayJCombo;
	}

	/**
	 * @return
	 */
	public String[] getDays() {
		return days;
	}

	/**
	 * @return
	 */
	public JComboBox getHourJCombo() {
		return hourJCombo;
	}

	/**
	 * @return
	 */
	public String[] getHours() {
		return hours;
	}

	/**
	 * @return
	 */
	public String[] getMinsec() {
		return minsec;
	}

	/**
	 * @return
	 */
	public JComboBox getMinuteJCombo() {
		return minuteJCombo;
	}

	/**
	 * @return
	 */
	public String[] getMonthes() {
		return monthes;
	}

	/**
	 * @return
	 */
	public JComboBox getMonthJCombo() {
		return monthJCombo;
	}

	/**
	 * @return
	 */
	public JComboBox getSecondsJCombo() {
		return secondsJCombo;
	}

	/**
	 * @return
	 */
	public JComboBox getYearJCombo() {
		return yearJCombo;
	}

	/**
	 * @return
	 */
	public String[] getYears() {
		return years;
	}

	/**
	 * @return
	 */
	public DirPanel getDirPanel() {
		return dirPanel;
	}
}